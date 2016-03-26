package com.ubcst.jello.ubcsttelemetry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jello on 05/03/16.
 */
public final class UsbPhone implements Runnable{

    // Fields
    public static UsbManager manager;
    public static UsbAccessory linuxPC;
    private static ParcelFileDescriptor mFileDescriptor;
    private static FileInputStream mInputStream;
    private static FileOutputStream mOutputStream;

    private static String str;

    /*public static BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
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
    };*/

    // Methods

    public UsbPhone(UsbManager usbManager, UsbAccessory usbAccessory)
    {
        // TODO Fill out the constructor, initialize the USB stuff
    }

    public static void openAccessory()
    {
        try {
            mFileDescriptor = manager.openAccessory(linuxPC);
        } catch(IllegalArgumentException e) {
            // IllegalArgumentException when no accessory is attached
            return;
        }

        if(mFileDescriptor != null)
        {
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);
            Thread thread = new Thread(null, null, "AccessoryThread");
            thread.start();
        }
    }

    public static void closeAccessory()
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

    @Override
    public void run() {
        int response = 0;
        byte[] message = new byte[16834];

        try
        {
            response = mInputStream.read(message);
        }
        catch( IOException e )
        {
            // TODO Handle the exception
        }

        if( response < 0 )
        {
            str = new String("No message");
        }
        else
        {
            str = new String(message);
        }
    }

    /**
     * toString()
     * Returns the USB message
     * @return str - the message from the USB accessory
     */
    public static String getString()
    {
        return str;
    }

}
