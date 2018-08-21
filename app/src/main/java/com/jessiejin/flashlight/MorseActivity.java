package com.jessiejin.flashlight;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MorseActivity extends AppCompatActivity {
    Button a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s,t, u, v, w, x, y, z,
        button1, button2, button3, button4, button5, button6, button7, button8, button9, button0,
        cancelButton;
    ImageButton volumeButton;
    private Camera camera;
    private Camera.Parameters params;

    private ExecutorService executor;
    private PopupWindow textPopupWindow;
    private EditText enterText;
    private String text;
    private ToneGenerator beepGen;

    private AtomicBoolean volume;

    public static final Object LOCK = new Object();

    public static final String DISPLAY_WARNING = "com.example.Flashlight.DISPLAY_WARNING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MorseActivity.this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }
        if (camera == null) {
            getCamera();
        }

        beepGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        volume = new AtomicBoolean(getIntent().getBooleanExtra(MainActivity.VOLUME_ON_OFF, true));

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        f = findViewById(R.id.f);
        g = findViewById(R.id.g);
        h = findViewById(R.id.h);
        i = findViewById(R.id.i);
        j = findViewById(R.id.j);
        k = findViewById(R.id.k);
        l = findViewById(R.id.l);
        m = findViewById(R.id.m);
        n = findViewById(R.id.n);
        o = findViewById(R.id.o);
        p = findViewById(R.id.p);
        q = findViewById(R.id.q);
        r = findViewById(R.id.r);
        s = findViewById(R.id.s);
        t = findViewById(R.id.t);
        u = findViewById(R.id.u);
        v = findViewById(R.id.v);
        w = findViewById(R.id.w);
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2= findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        volumeButton = findViewById(R.id.volumeOn);

        if (!volume.get()) {
            volumeButton.setImageResource(R.drawable.ic_volume_off);
        }

        executor = Executors.newSingleThreadExecutor();

        findViewById(R.id.exitPopUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.shutdownNow();
                exitMorse(view);
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

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute((new DotRunner()));
            }
        });

        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
            }
        });

        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
            }
        });

        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DashRunner());
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
                executor.execute(new DotRunner());
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DotRunner());
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
                executor.execute(new DashRunner());
            }
        });

        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.text_popup, null);
                textPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                if (Build.VERSION.SDK_INT >= 21) {
                    textPopupWindow.setElevation(5.0f);
                }

                textPopupWindow.showAtLocation(findViewById(R.id.morse_popup), Gravity.CENTER, 0, 0);
                textPopupWindow.setFocusable(true);
                textPopupWindow.update();

                enterText = customView.findViewById(R.id.enterText);
                cancelButton = customView.findViewById(R.id.cancelButton);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textPopupWindow.dismiss();
                    }
                });

                customView.findViewById(R.id.enterButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        text = enterText.getText().toString();
                        messageToLight(text);
                        synchronized (LOCK) {
                            try {
                                LOCK.wait();
                                enterText.setText("");
                            } catch (InterruptedException e) {

                            }
                        }
                    }
                });
            }
        });
    }

    private void turnOnLight() {
        if (camera != null) {
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
        }
    }

    private void turnOffLight() {
        if (camera != null) {
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
        }
    }

    private void messageToLight(String message) {
        char currChar;
        for (int placeholder = 0; placeholder < message.length(); placeholder++) {
            currChar = message.charAt(placeholder);
            switch(currChar) {
                case 'a':
                    a.performClick();
                    break;
                case 'b':
                    b.performClick();
                    break;
                case 'c':
                    c.performClick();
                    break;
                case 'd':
                    d.performClick();
                    break;
                case 'e':
                    e.performClick();
                    break;
                case 'f':
                    f.performClick();
                    break;
                case 'g':
                    g.performClick();
                    break;
                case 'h':
                    h.performClick();
                    break;
                case 'i':
                    i.performClick();
                    break;
                case 'j':
                    j.performClick();
                    break;
                case 'k':
                    k.performClick();
                    break;
                case 'l':
                    l.performClick();
                    break;
                case 'm':
                    m.performClick();
                    break;
                case 'n':
                    n.performClick();
                    break;
                case 'o':
                    o.performClick();
                    break;
                case 'p':
                    p.performClick();
                    break;
                case 'q':
                    q.performClick();
                    break;
                case 'r':
                    r.performClick();
                    break;
                case 's':
                    s.performClick();
                    break;
                case 't':
                    t.performClick();
                    break;
                case 'u':
                    u.performClick();
                    break;
                case 'v':
                    v.performClick();
                    break;
                case 'w':
                    w.performClick();
                    break;
                case 'x':
                    x.performClick();
                    break;
                case 'y':
                    y.performClick();
                    break;
                case 'z':
                    z.performClick();
                    break;
                case '1':
                    button1.performClick();
                    break;
                case '2':
                    button2.performClick();
                    break;
                case '3':
                    button3.performClick();
                    break;
                case '4':
                    button4.performClick();
                    break;
                case '5':
                    button5.performClick();
                    break;
                case '6':
                    button6.performClick();
                    break;
                case '7':
                    button7.performClick();
                    break;
                case '8':
                    button8.performClick();
                    break;
                case '9':
                    button9.performClick();
                    break;
                case '0':
                    button0.performClick();
                    break;
                case ' ':
                    executor.execute(new WordSpaceRunner());
                    break;
            }
            executor.execute(new LetterSpaceRunner());
        }
        executor.execute(new updateDisplay());
    }

    private void exitMorse(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DISPLAY_WARNING, false);
        intent.putExtra(MainActivity.VOLUME_ON_OFF, volume.get());
        startActivity(intent);
    }

    private class DotRunner implements Runnable {
        @Override
        public void run() {
            boolean turnOffSound = false;

            turnOnLight();
            if (volume.get()) {
                beepGen.startTone(ToneGenerator.TONE_DTMF_1);
                turnOffSound = true;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }

            turnOffLight();

            if (turnOffSound) {
                beepGen.stopTone();
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }

        }
    }

    private class DashRunner implements Runnable {
        @Override
        public void run() {
            boolean turnOffSound = false;

            turnOnLight();
            if (volume.get()) {
                beepGen.startTone(ToneGenerator.TONE_DTMF_1);
                turnOffSound = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            turnOffLight();
            if (turnOffSound) {
                beepGen.stopTone();
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }
            return;
        }
    }

    private class LetterSpaceRunner implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            return;
        }
    }

    private class WordSpaceRunner implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {

            }
            return;
        }
    }

    private class updateDisplay implements Runnable {
        @Override
        public void run() {
            synchronized (LOCK) {
                LOCK.notify();
            }
        }
    }

    private void getCamera() {
        try {
            camera = Camera.open();
            params = camera.getParameters();
        } catch (RuntimeException e) {
        }
    }

    @Override
    protected void onStop() {
        //releaseCameraAndPreview();
        executor.shutdownNow();
        camera.release();
        beepGen.release();
        super.onStop();
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
        if (camera == null) {
            getCamera();
        }
        executor = Executors.newSingleThreadExecutor();
        beepGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }
}
