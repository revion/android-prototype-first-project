package com.example.ryan.myfirsttesting;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    private WeakReference<Context> someContext;

    BackgroundTask(Context anotherContext) {
        someContext = new WeakReference<>(anotherContext);
    }

    @Override
    protected void onPreExecute() {
        final Context anotherContext = someContext.get();
        if(anotherContext != null) {
            super.onPreExecute();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        //PHP file that used as storing data
        String submit_form = "http://192.168.1.69/project/test/submit.php";
        //Director to do something
        String method = params[0];
        //Send report
        String text = "";
        //Do something based on method values
        if(method.equals("submitForm")) {
            //Form values that will be send to database
            String name = params[1];
            String email = params[2];
            String phone = params[3];
            String product = params[4];
            String quantity = params[5];
            try {
                URL source_url = new URL(submit_form);
                HttpURLConnection httpDoOpen = (HttpURLConnection) source_url.openConnection();
                httpDoOpen.setRequestMethod("POST");
                httpDoOpen.setDoOutput(true);
                //httpDoOpen.setDoInput(true);
                OutputStream output = httpDoOpen.getOutputStream();
                String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" + URLEncoder.encode("productSelect", "UTF-8") + "=" + URLEncoder.encode(product, "UTF-8") + "&" + URLEncoder.encode("productQty", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");
                BufferedWriter input = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
                input.write(post_data);
                input.flush();
                int status = httpDoOpen.getResponseCode();
                //If the server is connected, send data
                if(status == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpDoOpen.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    text = builder.toString();
                    input.close();
                }
                return text;
            }
            //Invalid URL
            catch (MalformedURLException exception) {
                exception.printStackTrace();
                return "URL is invalid";
                //Log.e("Error: ", "URL is not valid or offline");
            }
            //Invalid input
            catch (IOException exception) {
                exception.printStackTrace();
                return "Connection refused";
                //Log.e("Error: ", "Your input is invalid");
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        final Context anotherContext = someContext.get();
        if(anotherContext != null) {
            Toast.makeText(anotherContext, result, Toast.LENGTH_LONG).show();
        }
    }
}