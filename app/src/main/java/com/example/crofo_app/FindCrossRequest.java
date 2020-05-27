package com.example.crofo_app;

import android.os.AsyncTask;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FindCrossRequest extends AsyncTask<String, String, String> {
    private double lat;
    private double lon;
    private SafetyDrive safetyDrive;

    public FindCrossRequest (double[] location, SafetyDrive SD) {
        lat = location[0];
        lon = location[1];
        safetyDrive = SD;
    }
    protected String doInBackground(String... urls) {

        try {

            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();

            jsonObj.accumulate("lat", doubleToString(lat));
            jsonObj.accumulate("lon", doubleToString(lon));

            HttpURLConnection con = null;
            BufferedReader reader = null;
            try{
                URL url = new URL(urls[0]);

                //연결을 함
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();

                //서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObj.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌

                //서버로 부터 데이터를 받음
                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String json = buffer.toString();//서버로 부터 받은 값

                return json;

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject resultJson = new JSONObject(result);
            boolean res = resultJson.getBoolean("result");
            if (res) {
                JSONArray resultArr = resultJson.getJSONArray("arr");
                int cnt = resultArr.length();

                for (int i = 0; i < cnt; i++) {
                    JSONObject jsonObj = resultArr.getJSONObject(i);
                    CrossInfo crossInfo = new CrossInfo();
                    crossInfo.setCrossid(jsonObj.getInt("id"));
                    double[] centerLocation = new double[2];
                    centerLocation[0] = jsonObj.getDouble("cent_x");
                    centerLocation[1] = jsonObj.getDouble("cent_y");
                    crossInfo.setCenterLocation(centerLocation);
                    jsonObj.getDouble("loc_x0");
                    jsonObj.getDouble("loc_y0");
                    jsonObj.getDouble("loc_x1");
                    jsonObj.getDouble("loc_y1");
                    jsonObj.getDouble("loc_x2");
                    jsonObj.getDouble("loc_y2");
                    jsonObj.getDouble("loc_x3");
                    jsonObj.getDouble("loc_y3");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    private String doubleToString(double dou) {
        return "" + dou;
    }
}
