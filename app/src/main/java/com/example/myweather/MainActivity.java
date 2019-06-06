package com.example.myweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resulttextView);

    }
    public void getWeather(View v){
        DownloadTask task = new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url =  new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1)
                {
                    char current = (char) data;
                    result+=current;
                    data= reader.read();
                }
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();
                return  null;
            }
        }
        @Override
        protected void onPostExecute(String s)
        {
                     String msg ="";
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonpart = jsonObject.getJSONObject("main");
                String temp="Temp: \t"+jsonpart.getString("temp")+"°C\n";
                String humidity="Humidity: \t"+jsonpart.getString("humidity")+"%\n";

                String weatherinfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherinfo);

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonpart2 = arr.getJSONObject(i);
                    String desc=jsonpart2.getString("description")+"\n";
                    msg+=desc;
                }
                msg+=temp+humidity;
                if(!msg.equals("")){
                    resultTextView.setText(msg);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();
            }
        }
    }
}
