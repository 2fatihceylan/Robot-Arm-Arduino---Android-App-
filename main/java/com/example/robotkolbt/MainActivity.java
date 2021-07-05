package com.example.robotkolbt;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private OutputStream mmOutStream;
    private InputStream mmInStream;

    private BluetoothSocket mmSocket;
    private byte[] mmBuffer; // mmBuffer store for the stream

    private Handler mHandler;




    TextView textViewseekbar2,textViewseekbar3,textViewseekbar;
    SeekBar seekBar,seekBar2,seekBar3;
    Button buttontut,buttonsifirla,yap1,yap2,yap3,yap4,yap5;

    int a=10;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mmSocket = null;


        on();
        connector();
        th.start();


        textViewseekbar2 = (TextView) findViewById(R.id.textviewseekbar2);
        textViewseekbar3 = (TextView) findViewById(R.id.textviewseekbar3);

        seekBar = findViewById(R.id.seekbar);
        seekBar2 = findViewById(R.id.seekbar2);
        seekBar3 = findViewById(R.id.seekbar3);
        textViewseekbar = findViewById(R.id.textviewseekbar);
        buttonsifirla = findViewById(R.id.butonsifirla);
        buttontut = findViewById(R.id.butontut);
        yap1=findViewById(R.id.butonyap1);
        yap2=findViewById(R.id.butonyap2);
        yap3=findViewById(R.id.butonyap3);
        yap4=findViewById(R.id.butonyap4);
        yap5=findViewById(R.id.butonyap5);
        seekBar.setProgress(0);
        seekBar2.setProgress(0);
        seekBar3.setProgress(50);

        buttontut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(v, "tut");
            }
        });
        buttonsifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////



                seekBar.setProgress(0);
                seekBar2.setProgress(0);
                seekBar3.setProgress(50);

                write(v, "sifirla");

            }
        });
        yap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(v, "yap1");
            }
        });yap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(v, "yap2");
            }
        });yap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(v, "yap3");
            }
        });yap4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(v, "yap4");
            }
        });yap5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(v, "yap5");
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int a = (progress * 8 / 10)+100;
                String s = "C" + String.valueOf(a);

                textViewseekbar.setText("" + s + "%");

                write(seekBar, s);

                //////////////////////
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int a = (progress * 7 / 10)+110;
                String s = "Y" + String.valueOf(a);

                textViewseekbar2.setText("" + s + "%");

                write(seekBar, s);
                write(seekBar2,   s);

                //////////////////////
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int a = 180-(progress * 18 / 10);
                String s = "X" + String.valueOf(a);

                textViewseekbar3.setText("" + s + "%");


                write(seekBar3,  s);

                //////////////////////
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    // Bluetooth acık degilse acar
    public void on() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Bluetooth Açıldı...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth Açık...", Toast.LENGTH_LONG).show();
        }
    }

    // Bluetooth kapatma
    public void off(View v) {
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth Kapatıldı...", Toast.LENGTH_LONG).show();
    }

    // Eger bluetooth baglantisi kurulu degilse baglanır
    public void connect(View v) {


        try {
            String name = "CONNECTED";
            byte[] bytes = name.getBytes();
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Bağlanıyor...", Toast.LENGTH_LONG).show();
            connector();


        }
    }

    //Baglantı metodu
    public void connector() {

        OutputStream tmpOut = null;
        InputStream tmpIn = null;


        BluetoothSocket tmp = null;

        String dname;

        //eşlenmiş cihazların listeler
        pairedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice device = null;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                Log.d("TAG", bt.getName());
                dname = bt.getName();
                if (dname.equals("HC-05")) {
                    device = bt;
                    Log.d("TAG", "HC-05 Eşlendi!!!");
                    //Toast.makeText(getApplicationContext(), device.getName(), Toast.LENGTH_LONG).show();


                } else {
                    Log.d("TAG", "HC-05 degil");
                }

            }

            try {
                // ///
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                Log.d("TAG", "Socket's listen() hatası", e);
                Toast.makeText(getApplicationContext(), "Hata1...", Toast.LENGTH_LONG).show();
            }
            mmSocket = tmp;


            bluetoothAdapter.cancelDiscovery();


            try {
                //bluetooth cihaza baglanma: taki başarılı olana yada hata gönderene kadar
                mmSocket.connect();


                Log.d("TAG", "Socket bağlandı!!!!!");
                Toast.makeText(getApplicationContext(), "Bağlandı", Toast.LENGTH_LONG).show();
            } catch (IOException connectException) {
            }


            try {
                tmpIn = mmSocket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "input streamde hata var", e);
            }


            try {

                tmpOut = mmSocket.getOutputStream();


            } catch (IOException e) {
                Log.e(TAG, "output streamde hata var", e);
                Toast.makeText(getApplicationContext(), "Hata2...", Toast.LENGTH_LONG).show();
            }

            mmOutStream = tmpOut;
            mmInStream = tmpIn;


        } else {
            Log.d("TAG", "Cihaz Yok");
            Toast.makeText(getApplicationContext(), "HC-05 eşlenmedi", Toast.LENGTH_LONG).show();
        }


    }



    //hc05 modulunden gelen serial monitor verisini alır (iyi calışmıyor sonra bak)
    Thread th = new Thread(new Runnable() {
        public void run() {


            mmBuffer = new byte[4096];
            int numBytes; //

            // IOexception gelene kadar veri al

            while (true) {
                try {
                    if (mmInStream.available() > 2) {
                        Log.d("TAG", "mmInStream.available()>2");

                        // gelen veriyi okuma
                        numBytes = mmInStream.read(mmBuffer);


                        final String readMessage = new String(mmBuffer, 0, numBytes);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // textView.setText(readMessage);//textview da serial monitor yazdırma

                                //parse işlemi ile verilerin hangi sensorlerden geldiğini ayır ona göre ayrı textviewlara yazdır




                            }
                        });


                        Log.d("TAG", readMessage);
                    } else {
                        SystemClock.sleep(100);
                        Log.d("TAG", "Veri Yok");
                    }


                } catch (IOException e) {
                    Log.d("TAG", "Input stream  disconnected", e);
                    break;
                }
            }


            ////////////////////////////

        }
    });

    // hc05 üzerinden gönderilecek komutlar

    public void write(View v,String veri) {

        byte[] bytes = veri.getBytes(); //veri bytlarına ayrılır dizi olusturur
        Log.d("TAG", "Pressed: " + veri);
        try {
            mmOutStream.write(bytes); //byte dizisi gonderilir
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "write hatası", Toast.LENGTH_LONG).show();
        }

    }


}