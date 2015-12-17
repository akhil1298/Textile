package fourever.textile.mainclasses;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.ShowAlertDialog;
import fourever.textile.miscs.customtoast;

public class Login extends AppCompatActivity {

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
    private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        salert = new ShowAlertDialog();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        btnlogin = (Button) findViewById(R.id.btnLogin);
        
        txtsignup=(TextView)findViewById(R.id.txtsignup);

        Loginprefs = getSharedPreferences("logindetail", 0);
        if (Loginprefs != null) {
            userid = Loginprefs.getString("user_id", null);
            if (userid == null) {
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        if (checkPlayServices()) {
            getRegId();
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(TextUtils.isEmpty(username.getText().toString().trim())){
                        username.setError("Please enter username.");
                    }

                    if(TextUtils.isEmpty(password.getText().toString().trim())){
                        password.setError("Please enter Password.");
                    }

                    if (!TextUtils.isEmpty(username.getText().toString().trim()) && !TextUtils.isEmpty(password.getText().toString().trim()) ) {
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
                Intent i= new Intent(Login.this,SignUpActivity.class);
                startActivity(i);
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
                res = put.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/user_login.php?username=" + params[0] + "&password=" + params[1] + "&device_id=" + params[2]);
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
        Intent intent = new Intent(Login.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
        dialog.getWindow().getAttributes().windowAnimations  = R.style.DialogAnimation;
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
