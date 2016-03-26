package com.ubcst.jello.ubcsttelemetry;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ubcst.jello.ubcsttelemetry.http.VolleyRequests;

public class MainActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsbPhone.manager = (UsbManager)getSystemService(Context.USB_SERVICE);
        UsbPhone.linuxPC = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

        // TODO: Move somewhere else. Just placing here for testing.
        // TODO: Check what context to pass into the the constructor.
        // Commenting out for now.
        /* VolleyRequests volleyRequests = new VolleyRequests(this.getApplicationContext());
        volleyRequests.mainLoop(); */
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        UsbPhone.closeAccessory();

        super.onDestroy();
    }

    public void gotoGPS(View view)
    {
        Intent intent = new Intent(this, gpsActivity.class);
        startActivity(intent);
    }

    public void gotoSensor(View view)
    {
        Intent intent = new Intent(this, sensorActivity.class);
        startActivity(intent);
    }

    public void gotoConnect(View view)
    {
        // Connect to the USB device
        UsbPhone.openAccessory();
    }
}
