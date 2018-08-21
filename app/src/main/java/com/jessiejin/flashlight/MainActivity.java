package com.jessiejin.flashlight;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private ImageButton powerButton, volumeButton;
    private Button sosButton, morseMenuButton;
    private SeekBar strobe_freq;
    private boolean isOn;
    private final String SOS = "1010102020201010100000";
    private Thread sosThread, strobeThread;
    private AtomicBoolean sosMode, strobeMode, volume;
    private AtomicInteger freq;
    private StrobeLightRunner strober;
    private Camera camera;
    private Camera.Parameters parameters;
    private ToneGenerator beepGen;
    public static final String VOLUME_ON_OFF = "com.example.Flashlight.VOLUME_ON_OFF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Boolean isFlashAvailable = getApplicationContext().getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        //epilepsy warning
        if (getIntent().getBooleanExtra(MorseActivity.DISPLAY_WARNING, true)) {
            AlertDialog warning = new AlertDialog.Builder(this).create();
            warning.setTitle("Warning");
            warning.setMessage("WARNING: The strobe light feature may potentially trigger seizures for people with photosensitive epilepsy. User discretion advised.");
            warning.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            warning.show();
        }

        if (!isFlashAvailable) {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error!!");
            alert.setMessage("You don't have flash, noob");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
            alert.show();
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Camera");
            alert.setMessage("This app requires permission to use the camera to access the flash.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int requestCode = 0;
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);
                }
            });
            alert.show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCamera();
        beepGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        volume = new AtomicBoolean(getIntent().getBooleanExtra(VOLUME_ON_OFF, true));

        powerButton = findViewById(R.id.power_button);
        volumeButton = findViewById(R.id.volumeButton);
        if (!volume.get()) {
            volumeButton.setImageResource(R.drawable.ic_volume_off);
        }
        sosButton = findViewById(R.id.sos_button);
        strobe_freq = findViewById(R.id.strobe_freq);
        morseMenuButton = findViewById(R.id.morse_menu);


        isOn = false;
        strobeMode = new AtomicBoolean();
        sosMode = new AtomicBoolean();
        freq = new AtomicInteger();

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOn) {
                    //turn off flash
                    turnOffLight();
                    if (sosMode.get()) {
                        sosMode.set(false);
                        sosButton.setTextColor(Color.RED);
                    }

                    if (strobeMode.get()) {
                        strobeMode.set(false);
                    }

                    powerButton.setImageResource(R.drawable.ic_power);
                } else {
                    //turn on flash
                    turnOnLight();
                    powerButton.setImageResource(R.drawable.ic_power_green);

                    // strobe light frequency
                    if (freq.get() != 0){
                        strober = new StrobeLightRunner();
                        strobeThread = new Thread(strober);
                        strobeMode.set(true);
                        strobeThread.start();
                    }
                }
            }
        });

        volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volume.get()) {
                    volumeButton.setImageResource(R.drawable.ic_volume_off);
                    volume.set(false);
                } else {
                    volumeButton.setImageResource(R.drawable.ic_volume_up);
                    volume.set(true);
                }
            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sosMode.get()) {
                    if (strobeMode.get()) {
                        freq.set(0);
                        strobe_freq.setProgress(0);
                        strobeMode.set(false);
                    }

                    sosMode.set(true);
                    sosButton.setTextColor(Color.GREEN);

                    if (!isOn) {
                        powerButton.setImageResource(R.drawable.ic_power_green);
                        turnOnLight();
                    }

                    sosThread = new Thread(){
                        public void run() {
                            turnOnSOSMode();
                        }
                    };
                    sosThread.start();
                } else {
                    sosMode.set(false);
                    sosButton.setTextColor(Color.RED);

                    if (isOn) {
                        turnOnLight();
                    }
                }
            }
        });

        strobe_freq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                freq.set(progress);

                if (sosMode.get()) {
                    sosMode.set(false);
                    sosButton.setTextColor(Color.RED);
                }

                if (isOn && !strobeMode.get()) {
                    strobeMode.set(true);
                    strober = new StrobeLightRunner();
                    strobeThread = new Thread(strober);
                    strobeThread.start();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void getCamera() {
        try {
            camera = Camera.open();
            parameters = camera.getParameters();
        } catch (RuntimeException e) {

        }
    }

    public void turnOffLight() {
        if (camera != null) {
            isOn = false;
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.startPreview();
        }
    }

    public void turnOnLight() {
        if (camera != null) {
            isOn = true;
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
        }
    }

    public void startMorseActivity(View v) {
        if (isOn) {
            powerButton.callOnClick();
        }
        Intent intent = new Intent(this, MorseActivity.class);
        intent.putExtra(VOLUME_ON_OFF, volume.get());
        startActivity(intent);
    }


    public void turnOnSOSMode() {
        boolean turnOffSound = false;
        int offDelay = 300;
        int onDelay = 700;
        while (true) {
            for (int i = 0; i < SOS.length(); i++) {
                if (!sosMode.get()) {
                    if (turnOffSound) {
                        beepGen.stopTone();
                    }
                    return;
                }
                if (SOS.charAt(i) == '1') {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    if (volume.get()) {
                        beepGen.startTone(ToneGenerator.TONE_DTMF_1);
                        turnOffSound = true;
                    }
                } else if (SOS.charAt(i) == '2') {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    if (volume.get()) {
                        beepGen.startTone(ToneGenerator.TONE_DTMF_1);
                        turnOffSound = true;
                    }
                    try {
                        Thread.sleep(onDelay);
                    } catch (InterruptedException e) {

                    }
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    if (turnOffSound) {
                        beepGen.stopTone();
                        turnOffSound = false;
                    }
                }
                try {
                    Thread.sleep(offDelay);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private class StrobeLightRunner implements Runnable {
        @Override
        public void run() {
            while (strobeMode.get()) {
                if (freq.get() == 0) {
                    turnOnLight();
                    strobeMode.set(false);
                    break;
                }
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                try {
                    Thread.sleep(1000 - freq.get());
                } catch (InterruptedException e) {

                }
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.startPreview();
                try {
                    Thread.sleep(1000 - freq.get()) ;
                } catch (InterruptedException e) {

                }
            }
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isOn){
            powerButton.callOnClick();
        }
        camera.release();
        beepGen.release();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCamera();
        beepGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCamera();
        beepGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }
}
