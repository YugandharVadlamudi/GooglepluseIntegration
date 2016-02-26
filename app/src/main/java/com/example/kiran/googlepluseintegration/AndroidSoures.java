package com.example.kiran.googlepluseintegration;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
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

/**
 * Created by Kiran on 10-12-2015.
 */
public class AndroidSoures extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient MgoogleApiClient;
    private ConnectionResult connectionResult;
    private boolean mIntentInProgress;
    private boolean mShouldResolve;
    private SignInButton GsingninButton;
    private Button Glogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GsingninButton=(SignInButton)findViewById(R.id.sign_in_button);
        Glogout=(Button)findViewById(R.id.sign_out_button);
        GsingninButton.setOnClickListener(onClickListener);
        Glogout.setOnClickListener(SignoutOnclick);
        MgoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //connect GoogleApiClient
        MgoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MgoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"onConnectedCreated",Toast.LENGTH_SHORT).show();
        if(Plus.PeopleApi.getCurrentPerson(MgoogleApiClient)!=null)
        {
//            person class This is the Java data model class that specifies how to parse/serialize into the JSON
            Person person=Plus.PeopleApi.getCurrentPerson(MgoogleApiClient);
            String PersonName=person.getDisplayName();
            String Email=Plus.AccountApi.getAccountName(MgoogleApiClient);
//            connectionResult=MgoogleApiClient.getConnectionResult();
            Toast.makeText(getApplicationContext(), "" + PersonName + "::" + Email, Toast.LENGTH_SHORT).show();
            Glogout.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"not data found",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(),"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"onConnectionFailed",Toast.LENGTH_SHORT).show();
        /*
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this,0).show();
            return;
        }
        if (!mIntentInProgress) {

            this.connectionResult = connectionResult;

            if (mShouldResolve) {

                resolveSignInError();
            }
        }*/


    }

    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                MgoogleApiClient.connect();
            }
        }
    }
    public View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!MgoogleApiClient.isConnecting()) {
                mShouldResolve = true;
                Glogout.setVisibility(View.VISIBLE);
                resolveSignInError();
            }

        }
    };
    public  View.OnClickListener SignoutOnclick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MgoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(MgoogleApiClient);
                MgoogleApiClient.disconnect();

            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        Toast.makeText(getApplicationContext(),"onActivityResult",Toast.LENGTH_SHORT).show();
        /*if (requestCode ==0) {
            if (responseCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIntentInProgress = false;

            if (!MgoogleApiClient.isConnecting()) {
                MgoogleApiClient.connect();
            }
        }*/
    }

}
