package com.example.dts12_resfull_api.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.dts12_resfull_api.MainActivity;
import com.example.dts12_resfull_api.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBHelperBackend {


    private static final String URL_ADD = "http://192.168.100.115/pegawai/tambahpgw.php";
    private static final String URL_GETALL = "http://192.168.100.115/pegawai/tampilsemuapgw.php";
    private static final String URL_UPDATE = "http://192.168.100.115/pegawai/updatepgw.php";
    private static final String URL_DELETE = "http://192.168.100.115/pegawai/hapuspgw.php?id=";

//    private static final String URL_ADD = "http://10.0.1.92/pegawai/tambahpgw.php";
//    private static final String URL_GETALL = "http://10.0.1.92/pegawai/tampilsemuapgw.php";

    private List<Data> items;
    private Activity activity;

    public DBHelperBackend(List<Data> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    public void addPegawai(String name, String position, String salary){

//        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
//        String ip = sharedPreferences.getString("logintrue","");

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<String, String>("name", name));
        params.add(new Pair<String, String>("position", position));
        params.add(new Pair<String, String>("salary", salary));

        new PostAsync().execute(URL_ADD, params);

    }

    public void getAllPegawai(){
        new GetAsync().execute(URL_GETALL);
    }

    public void updatePegawai(String id, String name, String position, String salary){

//        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
//        String ip = sharedPreferences.getString("logintrue","");

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<String, String>("id", id));
        params.add(new Pair<String, String>("name", name));
        params.add(new Pair<String, String>("position", position));
        params.add(new Pair<String, String>("salary", salary));

        new PostAsync().execute(URL_UPDATE, params);

    }

    public void hapusPegawai(String id){

//        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
//        String ip = sharedPreferences.getString("logintrue","");

        new GetAsync().execute(URL_DELETE+id);

    }

    public void ParseJSON(String json)
    {
        items.clear();
        try {
            JSONObject objJSON = new JSONObject(json);
            JSONArray arr = objJSON.getJSONArray("result");

            for (int i = 0; i < arr.length(); i++)
            {
                String name = arr.getJSONObject(i).getString("name");
                String id = arr.getJSONObject(i).getString("id");
                String position = arr.getJSONObject(i).getString("position");
                String salary = arr.getJSONObject(i).getString("salary");

                Data item = new Data(id,name,position,salary);

                items.add(item);

            }

        }catch(JSONException e) {
            e.printStackTrace();
        }

        ((MainActivity)activity).ReloadList();

    }

    /*
        ini class async untuk get from backend
     */

    private class GetAsync extends AsyncTask
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(objects[0].toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }

                return buffer.toString();


            } catch ( MalformedURLException e) {
                e.printStackTrace();
            } catch ( IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ParseJSON(o.toString());
        }
    }



    private class PostAsync extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        private String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (Pair<String, String> pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.first, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.second, "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            int timeout=5000;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(objects[0].toString());
                List<Pair<String, String>>  params = (List<Pair<String, String>> )objects[1];
                connection = (HttpURLConnection) url.openConnection();

                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                // Send request
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");

                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch ( MalformedURLException e) {
                e.printStackTrace();
            } catch ( IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


}
