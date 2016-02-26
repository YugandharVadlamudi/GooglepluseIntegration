package com.example.kiran.googlepluseintegration;

import android.app.Activity;
import android.content.Intent;
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
public class thirdclass extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient MgoogleApiClient;
    private ConnectionResult connectionResult;
    private boolean mIntentInProgress;
    private boolean mShouldResolve;
    SignInButton LoinButton;
    Button Logout_bv;
    String personName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoinButton = (SignInButton) findViewById(R.id.sign_in_button);
        Logout_bv = (Button) findViewById(R.id.sign_out_button);
        LoinButton.setOnClickListener(onClickListener);
        Logout_bv.setOnClickListener(SignoutOnclick);
        MgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

    }

    /*
    *  resolution value, indicating the maximum
    *  distance between data points from which the elevation was interpolated, in meters
    * */
    private void resolveErrors() {
        if (connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, 0);
            } catch (Exception e) {
                mIntentInProgress = false;
                Log.d("connection error", "");
            }
        }/*if*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        mShouldResolve = false;
        if (Plus.PeopleApi.getCurrentPerson(MgoogleApiClient) != null)
        {
            Person person = Plus.PeopleApi
                    .getCurrentPerson(MgoogleApiClient);

            personName = person.getDisplayName();
            String email = Plus.AccountApi.getAccountName(MgoogleApiClient);
            Toast.makeText(getApplicationContext(), "" + personName + "::" + email, Toast.LENGTH_SHORT).show();
            Logout_bv.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onConnectionSuspended(int i) {
        MgoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MgoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(MgoogleApiClient.isConnected())
        MgoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult Result) {
        if (!Result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProgress) {

            this.connectionResult = Result;

            if (mShouldResolve) {

                resolveErrors();
            }
        }
    }
    public View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!MgoogleApiClient.isConnecting()) {
                mShouldResolve = true;
//                Logout_bv.setVisibility(View.VISIBLE);
                resolveErrors();
            }

        }
    };
    public  View.OnClickListener SignoutOnclick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(),"didnt logout",Toast.LENGTH_SHORT).show();

            if (MgoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(MgoogleApiClient);
                MgoogleApiClient.disconnect();
                if(MgoogleApiClient.isConnected())
                {
                    Toast.makeText(getApplicationContext(),"didnt logout",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Toast.makeText(getApplicationContext(),personName+"logout",Toast.LENGTH_SHORT).show();
                    LoinButton.setVisibility(View.GONE);
                }
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == 0) {
            if (responseCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIntentInProgress = false;

            if (!MgoogleApiClient.isConnecting()) {
                MgoogleApiClient.connect();
            }
        }
    }

}
