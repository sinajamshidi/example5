package com.example.asus.example5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver battry;
Context mcontext;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(battry);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            mcontext=this;
        battry=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(intent.ACTION_POWER_CONNECTED))

                {

                    BroadcastReceiver darsadbatry=new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            int batteryLevel = intent.getIntExtra("level", 0);
                            Intent go = new Intent(MainActivity.this, Main2Activity.class);
                            go.putExtra("battery", String.valueOf(batteryLevel));
                            startActivity(go);

                        }
                    };

                    IntentFilter filterb=new IntentFilter("android.intent.action.BATTERY_CHANGED");
                    registerReceiver(darsadbatry,filterb);




                }




            }
        };

        IntentFilter filter=new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        registerReceiver(battry,filter);

    }
}
