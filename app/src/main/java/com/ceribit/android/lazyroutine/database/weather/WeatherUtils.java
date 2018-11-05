package com.ceribit.android.lazyroutine.database.weather;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class WeatherUtils {

    // FOR DEBUGGING PURPOSES ONLY
    private static final String LOG_TAG = WeatherUtils.class.getSimpleName();
    private static final String WEATHER_API_URL =
            "http://api.openweathermap.org/data/2.5/weather?";


    /**
     * Updates temperature stored in SharedPreferences using the parameters found in it
     */
    public static void updateTemperature(Activity activity){
        WeatherPreferences.init(activity);

        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "APPID";

        String locationQuery = WeatherPreferences.getCity();
        String format = "json";
        String units = WeatherPreferences.getUnits();
        int numDays = 1;

        Uri builtUri =
                Uri.parse(WEATHER_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, "4c40483430aae0b79f2124b349806d19") //TODO: Create file to hold api key
                .build();
        Log.e(LOG_TAG, "TAG IS " + builtUri.toString());

        URL url = createUrl(builtUri.toString());

        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
            getWeatherDataFromJson(jsonResponse, locationQuery);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem building the HTTP request.", e);
        } catch (JSONException e){
            Log.e(LOG_TAG, "Issue when extracting data from the JSON file", e);
        }
    }

    /**
     * Creates URL while checking for exceptions
     * */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error in building URL");
        }
        return url;
    }

    /**
     * Returns the JSON response of the HTTP request after establishing a connection
     */
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            // Establish and execute a URL Connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(2000);
            urlConnection.connect();

            // Retrieve data from the URL Connection
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Issue when attempting to retrieve JSON results.");
        } finally {
            // Close used connections and streams
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converts InputStream retrieved from the HTTP request into a JSON string
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Stores the weather data in SharedPreferences
     */
    private static void getWeatherDataFromJson(String forecastJsonStr,
                                               String locationSettings)
        throws JSONException {

        // Location information
        final String OWM_CITY_NAME = "name";

        // All temperatures are children of the "temp" object.
        final String OWM_TEMPERATURE_OBJECT = "main";
        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";

        try {
            // Get stored objects from JSON string
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONObject temperatureObject = forecastJson.getJSONObject(OWM_TEMPERATURE_OBJECT);

            // Retrieve data from JSONObjects
            double low = temperatureObject.getDouble(OWM_MIN);
            double high = temperatureObject.getDouble(OWM_MAX);
            String cityName = forecastJson.getString(OWM_CITY_NAME);

            // Store retrieved data into SharedPreferences
            WeatherPreferences.setLowTemperature(((float) low));
            WeatherPreferences.storeHighTemperature(((float) high));


            Log.e(LOG_TAG, "Sync Complete. 1 value inserted");
            Log.e(LOG_TAG, "Min Temp: " + low);
            Log.e(LOG_TAG, "Max Temp: " + high);
            Log.e(LOG_TAG, "City is " + cityName);


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
