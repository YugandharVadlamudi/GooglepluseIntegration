package com.example.kiran.googlepluseintegration;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.sql.Connection;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
private SignInButton GsingninButton;
    private Button Glogout;
    private GoogleApiClient MgoogleApiClient;
    private ConnectionResult connectionResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GsingninButton=(SignInButton)findViewById(R.id.sign_in_button);
        Glogout=(Button)findViewById(R.id.sign_out_button);
        GsingninButton.setOnClickListener(onClickListener);
        Glogout.setOnClickListener(onClickListener);
        MgoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        Log.d("MgoogleApiClient", MgoogleApiClient.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        MgoogleApiClient.connect();
    }


    /*
    * connection callbackmethods
    * */
    @Override
    public void onConnected(Bundle bundle) {
        /*
        * Returns profile information for the current user getCurrentPerson
        * */
        if(Plus.PeopleApi.getCurrentPerson(MgoogleApiClient)!=null)
        {
            /*person class This is the Java data model class that specifies how to parse/serialize into the JSON*/
            Person person=Plus.PeopleApi.getCurrentPerson(MgoogleApiClient);
            String PersonName=person.getDisplayName();
            String Email=Plus.AccountApi.getAccountName(MgoogleApiClient);
//            connectionResult=MgoogleApiClient.getConnectionResult();
            Toast.makeText(getApplicationContext(),""+PersonName+"::"+Email,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"not data found",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    /*connection call back*/
    /*-------------------------------------*/
    /*
    * Connection fail method
    * */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Returns true if calling startResolutionForResult(Activity, int)
// will start any intents requiring user interaction.
        if(!connectionResult.hasResolution())
        {   /*show the error codes if connection fail*/
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this,0).show();
            this.connectionResult=connectionResult;
        }/*if*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*check the connection status*/
        if(MgoogleApiClient.isConnected())
            MgoogleApiClient.disconnect();
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.sign_in_button)
            {
                if(!MgoogleApiClient.isConnected())
                {
                    Log.d("helloMgoogleClient","hello");
                        if(connectionResult.hasResolution())
                        {
                            try {
                                connectionResult.startResolutionForResult(MainActivity.this,0);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                                MgoogleApiClient.connect();
                            }
                            Glogout.setVisibility(View.VISIBLE);
                        }

                }

            }
            else
            {
                Plus.AccountApi.clearDefaultAccount(MgoogleApiClient);
                MgoogleApiClient.disconnect();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
        {
            if(resultCode!=RESULT_OK)
            {
                Toast.makeText(getApplicationContext(),"resultCode",Toast.LENGTH_SHORT).show();
            }
            if(MgoogleApiClient.isConnected())
            {
                MgoogleApiClient.connect();
            }
        }
    }
}


/*
*
* 562790085008-4i09cnvgpdf6f8jovklrlad448u5ut67.apps.googleusercontent.com
*
* */