package fourever.textile.mainclasses;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.LoginAuthorizationType;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.ShowAlertDialog;
import fourever.textile.miscs.customtoast;

public class Login extends FragmentActivity {

    ShowAlertDialog salert;
    GoogleCloudMessaging gcm;
    String PROJECT_NUMBER = "693813425622";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String regid;

    private SharedPreferences Loginprefs;
    private String userid;

    EditText username;
    private TextView txtsignup;
    private EditText password;
    private Button btnlogin, btnFacebookLogin;

//    <=============================================== Start Facebook API   ============================================================>

    private Button postStatusUpdateButton;
  //  private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackmanager;
    private ImageView backImage;

   /* final FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
            Toast.makeText(Login.this, profile.getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };*/

//    < =============================================== End Facebook API   ============================================================>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//    < =============================================== Start Facebook API   ============================================================>
        FacebookSdk.sdkInitialize(this.getApplicationContext());
       // callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(com.facebook.Profile oldProfile, com.facebook.Profile currentProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        /*LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                if (profile == null) {
                    Log.e("Profile", "null");
                } else {
                    // Toast.makeText(Login.this, profile.getProfilePictureUri(20,20).getLastPathSegment() , Toast.LENGTH_SHORT).show();
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.e("response: ", response + "");
                                try {

                                            *//*object.getString("id").toString();
                                            object.getString("email").toString();

                                            object.getString("gender").toString();*//*
                                    new SaveServiceSync().execute(object.getString("id").toString(), object.getString("name").toString(), object.getString("email").toString(), regid);
                                    //Toast.makeText(Login.this, object.getString("name").toString() + " Id - " + object.getString("id").toString() + " Email : - " + object.getString("email").toString(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                        *//*Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                                        startActivity(intent);
                                        finish();*//*

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

//    < ===============================================    End Facebook API   ============================================================>

        setContentView(R.layout.activity_login);

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        if (Loginprefs != null) {
            userid = Loginprefs.getString("user_id", null);
            if (userid == null) {
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        /*if (userid != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        salert = new ShowAlertDialog();
        backImage = (ImageView) findViewById(R.id.backImage);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        btnlogin = (Button) findViewById(R.id.btnLogin);
        // btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);

        txtsignup = (TextView) findViewById(R.id.txtsignup);
        Loginprefs = getSharedPreferences("logindetail", 0);



        if (checkPlayServices()) {
            getRegId();
        }

//    < =============================================== Start Facebook API   ============================================================>

    /*
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email","user_friends", "user_photos");*/

        postStatusUpdateButton = (Button) findViewById(R.id.btnFacebookLogin);
        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (AccessToken.getCurrentAccessToken() == null) {
                    postStatusUpdateButton.setText("Facebook");
                    Fblogin();
                   // LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email", "user_friends", "user_photos"));
                    return; // already logged out
                } else {
                    postStatusUpdateButton.setText("Logout");
                    LoginManager.getInstance().logOut();
                }

                //LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("user_friends"));
                //LoginManager.getInstance().registerCallback(callbackManager,callback);
            }
        });

//    <===============================================  End Facebook API   ============================================================>

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (TextUtils.isEmpty(username.getText().toString().trim())) {
                        username.setError("Please enter username.");
                    }

                    if (TextUtils.isEmpty(password.getText().toString().trim())) {
                        password.setError("Please enter Password.");
                    }

                    if (!TextUtils.isEmpty(username.getText().toString().trim()) && !TextUtils.isEmpty(password.getText().toString().trim())) {
                        new ServiceSync().execute(username.getText().toString(), password.getText().toString(), regid);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

        /*final float[] from = new float[3], to = new float[3];

        Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from);   // from white
        Color.colorToHSV(Color.parseColor("#FFFF0000"), to);     // to red

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
        anim.setDuration(300);                              // for 300 ms

        final float[] hsv  = new float[3];                  // transition color
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Transition along each axis of HSV (hue, saturation, value)
                hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                btnlogin.setBackgroundColor(Color.HSVToColor(hsv));
            }
        });

        anim.start();*/
    }

    private void Fblogin() {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends", "user_photos"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        AccessToken accessToken = loginResult.getAccessToken();
                        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                        if (profile == null) {
                            Log.e("Profile", "null");
                        } else {
                            // Toast.makeText(Login.this, profile.getProfilePictureUri(20,20).getLastPathSegment() , Toast.LENGTH_SHORT).show();
                        }

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.e("response: ", response + "");
                                        try {

                                            /*object.getString("id").toString();
                                            object.getString("email").toString();

                                            object.getString("gender").toString();*/
                                            new SaveServiceSync().execute(object.getString("id").toString(), object.getString("name").toString(), object.getString("email").toString(), regid);
                                            //Toast.makeText(Login.this, object.getString("name").toString() + " Id - " + object.getString("id").toString() + " Email : - " + object.getString("email").toString(), Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        /*Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                                        startActivity(intent);
                                        finish();*/

                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean checkPlayServices() {
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(Login.this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, Login.this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    salert.showAlertDialog(Login.this, "Error",
                            "Something went wrong please restart the app", true);
                }
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() == null) {
            postStatusUpdateButton.setText("Facebook");
            return; // already logged out
        } else {
            postStatusUpdateButton.setText("Logout");
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private class ServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Login.this,
                    "", "Login..");
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://192.168.0.150:550/TextileApp/webservice/user_login.php?username=" + params[0] + "&password=" + params[1] + "&device_id=" + params[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        protected void onPostExecute(String res) {
            try {
                JSONArray jArray = new JSONArray(res.toString());
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    String errorcode = json.getString("errorcode");
                    if (errorcode.equals("0") || errorcode.equals("2")) {

                        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
                        SharedPreferences.Editor edit = Loginprefs.edit();
                        edit.putString("user_id", json.getString("user_id"));
                        edit.putString("user_name", json.getString("user_name"));
                        edit.putString("user_img", json.getString("user_img"));
                        edit.putString("user_deviceId", json.getString("deviceID"));

                        edit.putString("flag", json.getString("flag"));
                        if(json.getString("flag").equals("1")) {
                            edit.putString("sub_user_id", json.getString("sub_user_id"));
                            edit.putString("subuser_name", json.getString("subuser_name"));
                        }

                        edit.commit();

                        customtoast.ShowToast(getApplicationContext(), "Login Successfully.", R.layout.blue_toast);

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        customtoast.ShowToast(getApplicationContext(), "Invalid Username and Password", R.layout.red_toast);
                        /*Toast.makeText(Login.this,
                                "Invalid Username and Password", Toast.LENGTH_LONG)
                                .show();*/
                    }
                }

                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public class SaveServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String Response;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Login.this,
                    "", "Login Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                PutUtility objClient = new PutUtility();
                objClient.setParam("fb_login", params[0]);
                objClient.setParam("name", params[1]);
                objClient.setParam("email", params[2]);
                objClient.setParam("device_id", params[3]);
                objClient.setParam("phone", "");
                objClient.setParam("password", "");
                objClient.setParam("address", "");
                objClient.setParam("profile_pic", "");
                objClient.setParam("city_id", "");

                objClient.setParam("flag", "facebook_login");

                Response = objClient.postData("http://192.168.0.150:550/TextileApp/webservice/user_registration_add.php");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return Response;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null || res.equals(null) || res.equals("null")) {
                    mProgressDialog.dismiss();
                } else {

                    JSONArray jArray = new JSONArray(res.toString());
                    if (jArray.length() == 0) {
                        mProgressDialog.dismiss();
                    } else {
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);

                            String errorcode = json.getString("errorcode");
                            if (errorcode.equals("0")) {
                                // customtoast.ShowToast(Login.this, "Registration successfully.!!!", R.layout.blue_toast);
                                mProgressDialog.dismiss();

                                Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
                                SharedPreferences.Editor edit = Loginprefs.edit();
                                edit.putString("user_id", json.getString("user_id"));
                                edit.putString("user_name", json.getString("user_name"));
                                edit.putString("user_img", json.getString("user_img"));
                                edit.putString("user_deviceId", json.getString("deviceID"));
                                edit.commit();

                                customtoast.ShowToast(getApplicationContext(), "Login Successfully.", R.layout.blue_toast);

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                               /* android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(Login.this);
                                alertDialog.setTitle("Registration");
                                alertDialog.setMessage("Registration successful...");
                                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });

                                alertDialog.show();*/

                            } else {
                                mProgressDialog.dismiss();
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(Login.this, json.getString("errormsg"), R.layout.red_toast);
                            }
                        }
                    }
                }

                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    private void getRegId() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging
                                .getInstance(Login.this);
                    }

                    regid = gcm.register(PROJECT_NUMBER);
                    if (regid == null) {
                        regid = gcm.register(PROJECT_NUMBER);
                    }
                } catch (Exception e) {
                }
                return regid;
            }

            @Override
            protected void onPostExecute(String result) {
                regid = result;
                if (regid == null || regid.equals(null) || regid.equals("null")
                        || regid.equals("")) {
                   /* salert.showAlertDialog(Login.this, "Error",
                            "Something went wrong please restart the app", true);*/
                } else {
                    //Toast.makeText(Login.this, regid, Toast.LENGTH_LONG).show();
                }
            }

        }.execute(null, null, null);
    }

    public void clicked(View v) {
        /*Intent intent = new Intent(Login.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    public void dashboard(View v) {
        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rounddialog);
        /*RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);*/
        // Grab the window of the dialog, and change the width
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        /*
        final Dialog dialog = new Dialog(Login.this);
        //dialog.setTitle("Choose Your Option");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.dialog);
        dialog.getWindow().getAttributes().windowAnimations  = R.style.DialogAnimation;
        dialog.show();
        */
       /* Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }
}
