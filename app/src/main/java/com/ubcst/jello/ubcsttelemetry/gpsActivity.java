package com.ubcst.jello.ubcsttelemetry;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class gpsActivity extends AppCompatActivity implements Runnable {

    Intent intent  = getIntent();

    private UsbManager manager = (UsbManager)getSystemService(Context.USB_SERVICE);
    private UsbAccessory linuxPC = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
    private ParcelFileDescriptor mFileDescriptor;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;

    private double latitude;
    private double longitude;
    private String timestamp;
    private String northsouth;
    private String eastwest;
    private String rawData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        latitude = 90.4500;
        longitude = 128.0900;
        timestamp = "12:00:01 - TEST ONLY";
        northsouth = "N - TEST ONLY";
        eastwest = "W - TEST ONLY";
        rawData = "$GPS,90.4500,N,128.0900,W,12:00:01,$END";

        TextView latitudeMsg = (TextView) findViewById(R.id.latitudeField);
        TextView longitudeMsg = (TextView) findViewById(R.id.longitudeField);
        TextView timeMsg = (TextView) findViewById(R.id.timeField);
        TextView rawMsg = (TextView) findViewById(R.id.rawDataField);

        String latitudeStr = latitude + northsouth;
        String longitudeStr = longitude + eastwest;

        latitudeMsg.setText(latitudeStr);
        longitudeMsg.setText(longitudeStr);
        timeMsg.setText(timestamp);
        rawMsg.setText(rawData);
    }

    private void openAccessory()
    {
        mFileDescriptor = manager.openAccessory(linuxPC);
        if(mFileDescriptor != null)
        {
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);
            Thread thread = new Thread(null, this, "AccessoryThread");
            thread.start();
        }
    }

    @Override
    public void run()
    {

    }

}
