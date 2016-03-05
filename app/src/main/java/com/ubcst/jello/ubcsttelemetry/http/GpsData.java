package com.ubcst.jello.ubcsttelemetry.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class to hold GPS data received from the Raspberry Pi.
 * Created by Trevor on 28/02/2016.
 */
public class GpsData {
    public double latitude;
    public double longitude;

    /**
     * Constructor that sets the latitude and longitude parameters.
     * @param latitude The latitude represented as a double.
     * @param longitude The longitude represented as a double.
     */
    public GpsData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor that sets the latitude and longitude parameters based on a GPS string.
     * @param gpsCoords A string containing a latitude and longitude value. Assumes
     *                  the latitude and longitude values can be parsed into a
     *                  double.
     * @param delim A delimiter value to split the latitude and longitude values.
     */
    public GpsData(String gpsCoords, String delim) {
        String[] latLong = gpsCoords.split(delim);

        // Checks if there is at least 2 elements in the gpsCoords
        if( latLong.length < 2 ) {
            return;
        }

        try {
            this.latitude = Double.parseDouble(latLong[0]);
            this.longitude = Double.parseDouble(latLong[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforms the data in this object into a JSON object.
     * @return A JSON representation of this object's fields.
     */
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", this.latitude);
            jsonObject.put("longitude", this.longitude);
        } catch( JSONException e ) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
