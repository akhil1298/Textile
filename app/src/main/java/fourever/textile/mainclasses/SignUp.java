package fourever.textile.mainclasses;

import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.ShowAlertDialog;
import fourever.textile.miscs.customtoast;

public class SignUp extends AppCompatActivity {

    private EditText username;
    private EditText address;
    private Spinner spincity;
    private EditText phone;
    private EditText email;
    private EditText password;
    private Button btnregister;

    GoogleCloudMessaging gcm;
    String PROJECT_NUMBER = "693813425622";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    ShowAlertDialog salert;
    String regid, unm, add, cityid, cnno, emailid, pass, btitle, bdesc, res, res1, getcityid, Response, profile_pic, fb_login, date, device_id;
    private boolean emailcheck;
    private boolean mobilecheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.address);
        spincity = (Spinner) findViewById(R.id.spincity);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        //  businesstitle=(EditText)findViewById(R.id.businesstitle);
        // businessdesc=(EditText)findViewById(R.id.businessdesc);
        btnregister = (Button) findViewById(R.id.btnregister);

        salert = new ShowAlertDialog();
        fillcity();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (checkPlayServices()) {
            getRegId();
        } else {
        }

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(phone.getText().toString().trim()))
                        new CheckMobile().execute(phone.getText().toString().trim());
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(email.getText().toString()))
                        new CheckEmail().execute(email.getText().toString());
                }
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unm = username.getText().toString();
                add = address.getText().toString();
                cnno = phone.getText().toString();
                emailid = email.getText().toString();
                pass = password.getText().toString();
                 //btitle = businesstitle.getText().toString();
                //               bdesc = businessdesc.getText().toString();
                profile_pic = "";
                fb_login = "";

                getcityid = getselcityid();

                if (TextUtils.isEmpty(unm.trim())) {
                    username.setError("Please enter username.");
                }

                if (TextUtils.isEmpty(add.trim())) {
                    address.setError("Please enter address.");
                }

                if (TextUtils.isEmpty(cnno.trim())) {
                    phone.setError("Please enter phone number.");
                }

                if (TextUtils.isEmpty(emailid.trim())) {
                    email.setError("Please enter email address.");
                }

                if (TextUtils.isEmpty(pass.trim())) {
                    password.setError("Please enter password.");
                }

                /*if(TextUtils.isEmpty(btitle.trim())){
                    businesstitle.setError("Please enter mobile number.");
                }

                if(TextUtils.isEmpty(bdesc.trim())){
                    businessdesc.setError("Please enter mobile number.");
                }*/

                if (!TextUtils.isEmpty(unm.trim()) && !TextUtils.isEmpty(add.trim()) &&
                        !TextUtils.isEmpty(cnno.trim()) && !TextUtils.isEmpty(emailid.trim()) &&
                        !TextUtils.isEmpty(pass.trim())) {

                    if (emailcheck == false)
                        new CheckEmail().execute(emailid.trim());
                    if (mobilecheck == false)
                        new CheckMobile().execute(pass.trim());

                    if (emailcheck == true && mobilecheck == true && regid != "" && regid != null) {
                        if (!getcityid.equals("0")) {
                            new SaveServiceSync().execute(unm, emailid, cnno, pass, add, profile_pic, getcityid, fb_login, regid);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select City !!!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all detail",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getRegId() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging
                                .getInstance(SignUp.this);
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

    private boolean checkPlayServices() {
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    finish();
                }
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }

    public void fillcity() {
        new ServiceSync().execute();
    }

    public class ServiceSync extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SignUp.this,
                    "", "Please wait..");
        }

        @Override
        protected String doInBackground(Void... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/city_view.php");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                List<String> list1 = new ArrayList<String>();
                JSONArray jArray = new JSONArray(res.toString());
                list1.add(0, "Select City");

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    list1.add(json.get("name").toString());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                            SignUp.this, R.layout.spinneritem, list1);
                    dataAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spincity.setAdapter(dataAdapter);
                }
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public String getselcityid() {
        cityid = "0";
        if (spincity.getChildCount() == 0) {
            Toast.makeText(getApplicationContext(), "No City Added !!!",
                    Toast.LENGTH_LONG).show();
        } else {
            String cityname = spincity.getSelectedItem().toString();

            PutUtility objClient = new PutUtility();
            try {
                res1 = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/city_view.php");
                JSONArray jArray = new JSONArray(res1.toString());

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    if (json.getString("name").equals(cityname)) {
                        cityid = json.getString("city_id");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cityid;
    }

    public class SaveServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SignUp.this,
                    "", "");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                PutUtility objClient = new PutUtility();
                objClient.setParam("name", params[0]);
                objClient.setParam("email", params[1]);
                objClient.setParam("phone", params[2]);
                objClient.setParam("password", params[3]);
                objClient.setParam("address", params[4]);
                objClient.setParam("profile_pic", params[5]);
                objClient.setParam("city_id", params[6]);
                objClient.setParam("fb_login", params[7]);
                objClient.setParam("device_id", params[8]);
                objClient.setParam("flag", "normal_login");

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
                                customtoast.ShowToast(SignUp.this, "Registration successfully.!!!", R.layout.blue_toast);
                                mProgressDialog.dismiss();

                                /*
                                    Intent intent = new Intent(SignUp.this,Login.class);
                                    startActivity(intent);
                                    SignUp.this.finish();
                                */

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
                                alertDialog.setTitle("Registration");
                                alertDialog.setMessage("Registration successful...");
                                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent(SignUp.this,Login.class);
                                        startActivity(intent);
                                        SignUp.this.finish();
                                    }
                                });

                                alertDialog.show();

                            } else {
                                mProgressDialog.dismiss();
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(SignUp.this, json.getString("errormsg"), R.layout.red_toast);
                            }
                        }
                    }
                }
                setDefaultValues();
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public void setDefaultValues() {
        username.setText("");
        address.setText("");
        spincity.setSelection(0);
        phone.setText("");
        email.setText("");
        password.setText("");
//        businesstitle.setText("");
    //    businessdesc.setText("");
    }

    private class CheckEmail extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;
        private ShowAlertDialog salert;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(
                    SignUp.this, "",
                    "Checking email... Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();
            try {
                put.setParam("email", params[0]);
                //  put.setParam("id", params[1]);
                //res = put.postData("http://www.eazito.com/webservice/vendor_edit_email_check.php");
                res = put.postData("http://192.168.0.150:550/TextileApp/webservice/check_email_reg.php");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null || res.equals("")) {
                    mProgressDialog.dismiss();
                    salert.showAlertDialog(SignUp.this, "Registration",
                            "Error in Registration. Please try again.", true);
                } else {
                    JSONArray jArray = new JSONArray(res.toString());
                    if (jArray.length() == 0) {
                        mProgressDialog.dismiss();
                    } else {
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);

                            String errorcode = json.getString("errorcode");
                            if (errorcode.equals("0")) {
                                emailcheck = true;
                                mProgressDialog.dismiss();
                               /* FragmentManager frgManager = getActivity().getSupportFragmentManager();
                                frgManager.beginTransaction().replace(android.R.id.content, fragment)
                                        .commit();*/
                            } else {
                                emailcheck = false;
                                mProgressDialog.dismiss();
                                email.setError(json.getString("errormsg").toString());
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(SignUp.this, "This email is already registered.", R.layout.red_toast);
                            }
                        }
                    }
                }

                if (emailcheck == true) {

                    //   new ServiceEditSync().execute(txtname.getText().toString().trim(), txtemail.getText().toString().trim(), txtcnno.getText().toString().trim(), txtfblink.getText().toString().trim(), txtweblink.getText().toString().trim(), txtbussdesc.getText().toString().trim(),userid);
                }

                mProgressDialog.dismiss();

            } catch (Exception objEx) {
                mProgressDialog.dismiss();
                objEx.printStackTrace();
            }
        }
    }

    private class CheckMobile extends AsyncTask<String, Void, String> {

        private ProgressDialog mProgressDialog;
        private String res;
        // private ShowAlertDialog salert;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(
                    SignUp.this, "",
                    "Checking mobile... Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();
            try {
                put.setParam("phone", params[0]);
                //  put.setParam("id", params[1]);
                //res = put.postData("http://www.eazito.com/webservice/vendor_edit_email_check.php");
                res = put.postData("http://192.168.0.150:550/TextileApp/webservice/check_mobile_reg.php");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null || res.equals("")) {
                    mProgressDialog.dismiss();
                    /*salert.showAlertDialog(getApplicationContext(), "Registration",
                            "Error in Registration. Please try again.", true);*/
                } else {
                    JSONArray jArray = new JSONArray(res.toString());
                    if (jArray.length() == 0) {
                        mProgressDialog.dismiss();
                    } else {
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);

                            String errorcode = json.getString("errorcode");
                            if (errorcode.equals("0")) {
                                mobilecheck = true;
                                mProgressDialog.dismiss();
                               /* FragmentManager frgManager = getActivity().getSupportFragmentManager();
                                frgManager.beginTransaction().replace(android.R.id.content, fragment)
                                        .commit();*/
                            } else {
                                mobilecheck = false;
                                mProgressDialog.dismiss();
                                phone.setError(json.getString("errormsg").toString());
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(SignUp.this, json.getString("errormsg"), R.layout.red_toast);
                            }
                        }
                    }
                }

                if (mobilecheck == true) {

                    //   new ServiceEditSync().execute(txtname.getText().toString().trim(), txtemail.getText().toString().trim(), txtcnno.getText().toString().trim(), txtfblink.getText().toString().trim(), txtweblink.getText().toString().trim(), txtbussdesc.getText().toString().trim(),userid);
                }

                mProgressDialog.dismiss();

            } catch (Exception objEx) {
                mProgressDialog.dismiss();
                objEx.printStackTrace();
            }
        }
    }
}
