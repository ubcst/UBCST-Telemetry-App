package com.ubcst.jello.ubcsttelemetry;

import android.content.BroadcastReceiver;
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
import java.io.IOException;
import java.nio.ByteBuffer;

public class gpsActivity extends AppCompatActivity implements Runnable {

    Intent intent;

    private UsbManager manager;
    private UsbAccessory linuxPC;
    private ParcelFileDescriptor mFileDescriptor;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;

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

        intent = getIntent();
        manager = (UsbManager)getSystemService(Context.USB_SERVICE);
        linuxPC = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

        latitudeMsg = (TextView) findViewById(R.id.latitudeField);
        longitudeMsg = (TextView) findViewById(R.id.longitudeField);
        timeMsg = (TextView) findViewById(R.id.timeField);
        rawMsg = (TextView) findViewById(R.id.rawDataField);

        setContentView(R.layout.activity_gps);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        openAccessory();
    }

    @Override
    public void onDestroy()
    {
        closeAccessory();
        unregisterReceiver(mUsbReceiver);
        super.onDestroy();
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

    private void closeAccessory()
    {
        try
        {
            if(mFileDescriptor != null)
            {
                mFileDescriptor.close();
            }
        }
        catch (IOException e)
        {
        }
        finally
        {
            mFileDescriptor = null;
            linuxPC = null;
        }
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action))
            {
                UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null) {
                    closeAccessory();
                }
            }
        }
    };

    @Override
    public void run()
    {
        int response = 0;
        byte[] message = new byte[16384];

        try
        {
            response = mInputStream.read(message);
        }
        catch( IOException e)
        {
            // Exception
        }

        if(response < 0)
        {
            rawMsg.setText("ERROR READING STREAM");
        }
        else
        {
            String str = new String(message);
            rawMsg.setText(str);
        }
    }

}
