package com.example.crofo_app;

import android.os.AsyncTask;

import org.json.JSONArray;
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
import java.util.ArrayList;

public class CrossRequest extends AsyncTask<String, String, String> {
    private ArrayList<CrossInfo> crossList;
    public CrossRequest(ArrayList<CrossInfo> crossList) {
        this.crossList = crossList;
    }

    protected String doInBackground(String... urls) {

        try {

            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();


            for (CrossInfo item : crossList) {
//                JSONObject tempJson = new JSONObject();
//                double[] location = item.getCrossLocation();
//                double angle = item.getTrueBearing();
//                tempJson.accumulate("lat", doubleToString(location[0]));
//                tempJson.accumulate("lon", doubleToString(location[1]));
//                tempJson.accumulate("angle", doubleToString(angle));
//                jsonArr.put(tempJson);
            }

            jsonObj.accumulate("crossList", jsonArr);
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
                JSONObject resultJson = new JSONObject(json);
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
        System.out.println(result);
    }

    private String doubleToString(double dou) {
        return "" + dou;
    }
}