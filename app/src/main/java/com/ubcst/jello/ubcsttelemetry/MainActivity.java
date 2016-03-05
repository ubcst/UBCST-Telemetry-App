package com.ubcst.jello.ubcsttelemetry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ubcst.jello.ubcsttelemetry.http.VolleyRequests;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Move somewhere else. Just placing here for testing.
        // TODO: Check what context to pass into the the constructor.
        // Commenting out for now.
        /* VolleyRequests volleyRequests = new VolleyRequests(this.getApplicationContext());
        volleyRequests.mainLoop(); */
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
}
