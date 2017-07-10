package ru.digios.sms2net.core;


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
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MessageNetwork {
    private static final Logger logger = Log.getLogger(SmsService.class);

    public boolean postRequest(String requestURL, HashMap<String, String> postDataParams) {
        boolean result = false;
        try {
            URL url = new URL(requestURL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                result = responseCode == HttpsURLConnection.HTTP_OK;
            } finally {
                urlConnection.disconnect();
            }
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

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
