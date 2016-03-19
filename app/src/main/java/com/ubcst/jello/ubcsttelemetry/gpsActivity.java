package com.ubcst.jello.ubcsttelemetry;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class gpsActivity extends AppCompatActivity implements Runnable {

    Intent intent;

    private static final String TAG = "gpsActivity";
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    private UsbManager manager;
    private UsbAccessory linuxPC;
    private ParcelFileDescriptor mFileDescriptor;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;
    private PendingIntent mPermissionIntent;

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
        linuxPC = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

        setContentView(R.layout.activity_gps);
        setupAccessory();

        latitudeMsg = (TextView) findViewById(R.id.latitudeField);
        longitudeMsg = (TextView) findViewById(R.id.longitudeField);
        timeMsg = (TextView) findViewById(R.id.timeField);
        rawMsg = (TextView) findViewById(R.id.rawDataField);

    }

    @Override
    public void onResume()
    {
        super.onResume();

        openAccessory(linuxPC);
    }

    @Override
    public void onDestroy()
    {
        closeAccessory();
        unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    private void openAccessory(UsbAccessory accessory)
    {
        if(accessory == null) {
            Log.d(TAG, "Accessory is null");
            return;
        }
        UsbAccessory[] accssories = manager.getAccessoryList();
        for( UsbAccessory usbAccessory : accssories ) {
            Log.d(TAG, usbAccessory.getDescription());
            Log.d(TAG, usbAccessory.getManufacturer());
            Log.d(TAG, usbAccessory.getModel());
            Log.d(TAG, usbAccessory.getSerial());
            Log.d(TAG, usbAccessory.getUri());
            Log.d(TAG, usbAccessory.getVersion());
            accessory = usbAccessory;
        }
        HashMap<String, UsbDevice> usbDevices = manager.getDeviceList();
        Iterator iterator = usbDevices.keySet().iterator();
        while(iterator.hasNext()) {
            String usb = (String)iterator.next();
            Log.d(TAG, "usb device: " + usb);
        }
        try {
            mFileDescriptor = manager.openAccessory(accessory);
        } catch(IllegalArgumentException e) {
            Log.d(TAG, "Could not open accessory: " + accessory.getDescription());
            return;
        }

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

    private void setupAccessory(){
        //Reference to USB System Service
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        //Ask for USB Permission when an Accessory connects
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        // Register the Receiver
        registerReceiver(mUsbReceiver, filter);
        openAccessory(linuxPC);
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        openAccessory(accessory);
                    } else {
                        Log.d(TAG, "permission denied for accessory "
                                + accessory);
                    }
                    // mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action))
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
