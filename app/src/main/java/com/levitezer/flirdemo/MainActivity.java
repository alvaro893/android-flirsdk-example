package com.levitezer.flirdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.flir.flironesdk.Device;

public class MainActivity extends AppCompatActivity {

    private FlirCam flircam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flircam = new FlirCam(this, (ImageView)findViewById(R.id.imageView));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Device.startDiscovery(this, flircam);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Device.stopDiscovery();
    }
}
