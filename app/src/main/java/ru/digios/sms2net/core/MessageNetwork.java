package ru.digios.sms2net.core;


import android.os.NetworkOnMainThreadException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MessageNetwork {
    private static final Logger logger = Log.getLogger(SmsService.class);

    public boolean send(String requestURL, List<Message> list) {
        if (list == null)
            return false;

        if (list.isEmpty())
            return false;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(list);

        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("json", json);

        boolean result = postRequest(/*"https://arsenal.army/index.php?option=com_jshopping&controller=modem&task=test"*/requestURL, postDataParams);

        return result;
    }

    public boolean postRequest(String requestURL, HashMap<String, String> postDataParams) {
        boolean result = false;
        try {
            URL url = new URL(requestURL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(false);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            result = responseCode == HttpsURLConnection.HTTP_OK;

            urlConnection.disconnect();
        }
        catch (NetworkOnMainThreadException nex) {
            result = false;
            logger.error(nex);
            Utils.sleep(2000);
        }
        catch (Exception ex) {
            result = false;
            logger.error(ex);
        }

        return result;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            //result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
            //result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
