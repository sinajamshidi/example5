package com.example.asus.example5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.example5.yahoomodel.Image;
import com.example.asus.example5.yahoomodel.Yahoomodel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class Main2Activity extends AppCompatActivity {
    Context mcontext;

    BroadcastReceiver disconnect;
    TextView result;
    TextView tehran;
    TextView maxmin;
    ImageView image;
String url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22tehran%2C%20ir%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(disconnect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mcontext=this;
        tehran=(TextView) findViewById(R.id.tehran);
        maxmin=(TextView) findViewById(R.id.max);
        result=(TextView) findViewById(R.id.result);
        Intent get=getIntent();
        String battry=get.getStringExtra("battery");
       result.setText("Battery percentage = " + battry +  "%");

        getdata();
        disconnect=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(intent.ACTION_POWER_DISCONNECTED))
                {
                    Intent back=new Intent(mcontext,MainActivity.class);
                    startActivity(back);
                }

            }
        };


        IntentFilter disfilter=new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerReceiver(disconnect,disfilter);


    }

    private void getdata() {
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(Main2Activity.this, "connect to internet pls", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    parsdata(responseString);
            }
        });
    }

    private void parsdata(String responseString) {
        Gson gson=new Gson();
        Yahoomodel yahoo=gson.fromJson(responseString,Yahoomodel.class);
        String temp=yahoo.getQuery().getResults().getChannel().getItem().getCondition().getTemp();

        Double c=Double.parseDouble(temp);
        c=(c-32)/1.8;

        int N = -1;
        double d = c*Math.pow(10,N);
        int i = (int) d;
        double f2 = i/Math.pow(10,N);
        tehran.setText( f2+"°C");

        String max=yahoo.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh();
        String min=yahoo.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow();

        Double cc=Double.parseDouble(max);
        cc=(cc-32)/1.8;

        int Nn = -1;
        double dd = cc*Math.pow(10,Nn);
        int ii = (int) dd;
        double f22 = ii/Math.pow(10,Nn);

        Double ccc=Double.parseDouble(min);
        ccc=(ccc-32)/1.8;

        int Nnn = -1;
        double ddd = ccc*Math.pow(10,Nnn);
        int iii = (int) ddd;
        double f222 = iii/Math.pow(10,Nnn);


        maxmin.setText( f22+""+"↑"+" "+f222+" "+ "↓");

        String vaziat=yahoo.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText();


    }

}
