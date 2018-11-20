package com.develop.daniil.sms_v01.Send;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.develop.daniil.sms_v01.R;
import com.develop.daniil.sms_v01.Receive.ReceiveFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    SendFragment sendFragment;
    ReceiveFragment receiveFragment;

    IntentFilter intentFilter;

    private BroadcastReceiver intentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String bodyMsg = intent.getExtras().getString("bodyMsg");
            String fromAddress = intent.getExtras().getString("fromAddress");

//            Todo: send to fragment
            Bundle dataToFragment = new Bundle();
            dataToFragment.putString("bodyMsg", bodyMsg);
            dataToFragment.putString("fromAddress", fromAddress);
            receiveFragment.setArguments(dataToFragment);
        }
    };

    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener); //переходы

        sendFragment = new SendFragment();
        receiveFragment = new ReceiveFragment();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE); //add permissions to receive

        showSendFragment(); // default fragment
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReciever,intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReciever);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  //for Receive SMS
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            // YES!!
            Log.i("TAG", "MY_PERMISSIONS_REQUEST_SMS_RECEIVE --> YES");
        }
    }

    public void showSendFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(sendFragment.isAdded()){
            ft.show(sendFragment);
        }
        else {
          ft.add(R.id.FrameContainer, sendFragment, "first");
        }
        if(receiveFragment.isAdded()){
            ft.hide(receiveFragment);
        }
        ft.commit();
    }

    public void showReceiveFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(receiveFragment.isAdded()){
            ft.show(receiveFragment);
        }
        else {
            ft.add(R.id.FrameContainer, receiveFragment, "second");
        }
        if(sendFragment.isAdded()){
            ft.hide(sendFragment);
        }
        ft.commit();
    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener //переход
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener(){                        // между фрёмами
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.ic_send:
                    showSendFragment();
                    break;
                case R.id.ic_reciew:
                    showReceiveFragment();
                    break;
            }
            return true;
        }
    };

}
