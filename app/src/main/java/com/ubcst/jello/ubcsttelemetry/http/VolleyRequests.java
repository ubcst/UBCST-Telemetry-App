package com.ubcst.jello.ubcsttelemetry.http;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Class to handle sending HTTP requests to a server by using Volley.
 * Created by Trevor on 27/02/2016.
 */
public class VolleyRequests {
    // TODO: Update to actual URL that will be used
    public static final String URL = "http://httpbin.org/post";
    public RequestQueue requestQueue;

    public VolleyRequests(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Creates a JsonObjectRequest to send.
     * @param jsonObject The JSON object represented as a JSONObject
     */
    public JsonObjectRequest createJsonRequest(JSONObject jsonObject) {
        // Creates a POST request to the URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
            jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("Response", jsonObject.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Log.d("Error.Response", error.getMessage());
                }
            }
        );

        return jsonObjectRequest;
    }

    /**
     * Adds a JsonObjectRequest to a RequestQueue to be sent.
     * @param jsonObjectRequest The JsonObjectRequest to send.
     */
    public void sendJsonRequest(JsonObjectRequest jsonObjectRequest) {
        this.requestQueue.add(jsonObjectRequest);
    }

    /**
     * Essentially an infinite loop to continually send JSON POST requests.
     */
    public void mainLoop() {
        while(true) {
            // TODO: Get the new latitude and longitude values.
            double latitude = 0;
            double longitude = 0;

            // Create a new GpsData object
            GpsData gpsData = new GpsData(latitude, longitude);

            // Transform the gpsData object to a JsonRequestObject and send it.
            JSONObject jsonObject = gpsData.toJsonObject();
            JsonObjectRequest jsonObjectRequest = this.createJsonRequest(jsonObject);
            this.sendJsonRequest(jsonObjectRequest);
        }
    }
}
