package com.ubcst.jello.ubcsttelemetry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class gpsActivity extends AppCompatActivity{

    Intent intent = getIntent();

    private double latitude;
    private double longitude;
    private String timestamp;
    private String northsouth;
    private String eastwest;
    private String rawData;

    TextView latitudeMsg;
    TextView longitudeMsg;
    TextView timeMsg;
    TextView rawMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        latitudeMsg = (TextView) findViewById(R.id.latitudeField);
        longitudeMsg = (TextView) findViewById(R.id.longitudeField);
        timeMsg = (TextView) findViewById(R.id.timeField);
        rawMsg = (TextView) findViewById(R.id.rawDataField);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        rawMsg.setText(UsbPhone.getString());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // TODO Add null check before closing
        UsbPhone.closeAccessory();
        unregisterReceiver(UsbPhone.mUsbReceiver);
    }
}
