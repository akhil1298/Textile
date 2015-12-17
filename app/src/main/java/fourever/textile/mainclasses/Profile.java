package fourever.textile.mainclasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.RoundedImageView;
import fourever.textile.miscs.customtoast;

public class Profile extends AppCompatActivity {

    private String userid;
    private SharedPreferences Loginprefs;
    private TextView username, joiningdate, saveprofile;
    private RoundedImageView img;
    private String imageurl;
    Bitmap bitMap;

    ImageView editprofile, editbusinessprofile;
    EditText edit_useraddress, edtphone, edtbusishort, edtbusslong;
    TextView useraddress, txtphone, savebusinessprofile, txtbusishort, txtbussislong;

    LinearLayout editprofile_mainlayout;
    ProgressBar updatefield;
    private int processUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        processUpdate = 1;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editprofile_mainlayout=(LinearLayout)findViewById(R.id.editprofile_mainlayout);

        updatefield = (ProgressBar) findViewById(R.id.updatefield);

        editprofile = (ImageView) findViewById(R.id.editprofile);
        edit_useraddress = (EditText) findViewById(R.id.edit_useraddress);
        edtphone = (EditText) findViewById(R.id.edtphone);
        saveprofile = (TextView) findViewById(R.id.saveprofile);
        username = (TextView) findViewById(R.id.uname);
        joiningdate = (TextView) findViewById(R.id.joingingadte);
        useraddress = (TextView) findViewById(R.id.useraddress);

        editbusinessprofile = (ImageView) findViewById(R.id.editbusinessprofile);
        savebusinessprofile = (TextView) findViewById(R.id.savebusinessprofile);

        edtbusishort = (EditText) findViewById(R.id.edtbusishort);
        edtbusslong = (EditText) findViewById(R.id.edtbusslong);

        txtbusishort = (TextView) findViewById(R.id.txtbusishort);
        txtbussislong = (TextView) findViewById(R.id.txtbussislong);

        txtphone = (TextView) findViewById(R.id.txtphone);

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(Profile.this, Login.class);
            startActivity(intent);
            finish();
        }

        username.setText(Loginprefs.getString("user_name", null));
        img = (RoundedImageView) findViewById(R.id.rImage);

        if (Loginprefs.getString("user_img", null) != null) {

            /*try {
                imageurl = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null);
                URL url = new URL(imageurl);
                InputStream is = url.openConnection().getInputStream();
                if (bitMap != null) {
                    bitMap.recycle();
                    bitMap = null;
                }

                bitMap = BitmapFactory.decodeStream(is);

                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setImageBitmap(bitMap);

            } catch (Exception e) {
                Log.d("Sutter image problem", "error - " + e.getMessage());
            }*/
        }

        saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editprofile.setVisibility(View.VISIBLE);
                saveprofile.setVisibility(View.GONE);
                edit_useraddress.setVisibility(View.GONE);
                useraddress.setVisibility(View.VISIBLE);
                edtphone.setVisibility(View.GONE);
                txtphone.setVisibility(View.VISIBLE);
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveprofile.setVisibility(View.VISIBLE);
                editprofile.setVisibility(View.GONE);
                edit_useraddress.setVisibility(View.VISIBLE);
                useraddress.setVisibility(View.GONE);
                edtphone.setVisibility(View.VISIBLE);
                txtphone.setVisibility(View.GONE);
            }
        });

        editbusinessprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savebusinessprofile.setVisibility(View.VISIBLE);
                editbusinessprofile.setVisibility(View.GONE);

                edtbusishort.setVisibility(View.VISIBLE);
                edtbusslong.setVisibility(View.VISIBLE);

                txtbusishort.setVisibility(View.GONE);
                txtbussislong.setVisibility(View.GONE);
            }
        });


        savebusinessprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editbusinessprofile.setVisibility(View.VISIBLE);
                savebusinessprofile.setVisibility(View.GONE);

                edtbusishort.setVisibility(View.GONE);
                edtbusslong.setVisibility(View.GONE);

                txtbusishort.setVisibility(View.VISIBLE);
                txtbussislong.setVisibility(View.VISIBLE);
            }
        });
//------------------------------------------------------------------------------------------------
    //    1 = useraddress , 2 = phonenumber , 3 = business short , 4 = business long

        useraddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditField(useraddress, edit_useraddress,"address");
            }
        });

        txtphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 EditField(txtphone, edtphone, "phone");
            }
        });

        txtbusishort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditField(txtbusishort, edtbusishort, "short_desc");
            }
        });

        txtbussislong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditField(txtbussislong, edtbusslong, "long_desc");
            }
        });

        try {
             new ServiceSync().execute(userid);
        } catch (Exception e) {
        }
    }

    private void hideKeyboard(EditText edt) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    private void showKeyboard(EditText edt) {
        /*InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(edit_useraddress.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);*/
        edt.setSelection(edt.getText().length());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);

    }

    private void EditField(final TextView txt, final EditText edt, final String field){
        if(processUpdate == 1 || processUpdate == 2) {
            txt.setVisibility(View.GONE);
            edt.setVisibility(View.VISIBLE);
            edt.requestFocus();
            showKeyboard(edt);
        }
        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == true) {

                } else {

                    if(processUpdate == 1 || processUpdate == 2) {
                        hideKeyboard(edt);
                        txt.setVisibility(View.VISIBLE);
                        edt.setVisibility(View.GONE);
                        new UpdateSingleField().execute(userid,edt.getText().toString(),field);
                    }else{
                        Toast.makeText(getApplicationContext(),"updating in progress.. please wait..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

       /* editprofile_mainlayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    useraddress.setVisibility(View.GONE);
                    edit_useraddress.setVisibility(View.VISIBLE);
                }
            }
        });*/

        ScrollView scroll = (ScrollView)findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edt.hasFocus()) {
                    edt.clearFocus();
                }
                return false;
            }
        });
    }

/*

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

*/

    private class ServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Profile.this,
                    "", "Loading..");
            mProgressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/user_detail.php?userid=" + params[0]);
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
                    if (errorcode.equals("0")) {

                        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
                        SharedPreferences.Editor edit = Loginprefs.edit();
                        edit.putString("user_id", json.getString("userid"));
                        edit.putString("user_name", json.getString("name"));
                        edit.putString("user_img", json.getString("profile_pic"));
                        edit.putString("user_deviceId", json.getString("device_id"));
                        edit.commit();

                        joiningdate.setText("Joining at " + json.getString("date"));
                        useraddress.setText(json.getString("address"));
                        txtphone.setText(json.getString("phone"));
                        txtbusishort.setText(json.getString("short_desc"));
                        txtbussislong.setText(json.getString("long_desc"));

                        edit_useraddress.setText(json.getString("address"));
                        edtphone.setText(json.getString("phone"));
                        edtbusishort.setText(json.getString("short_desc"));
                        edtbusslong.setText(json.getString("long_desc"));

                    } else {
                        customtoast.ShowToast(getApplicationContext(), "Error in getting data.", R.layout.red_toast);
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

    private class UpdateSingleField extends AsyncTask<String, Void, String> {
        private String res;
        // 1 = update completed , 2 = error in update , 3 = ongoing.
        @Override
        protected void onPreExecute() {
            updatefield.setVisibility(View.VISIBLE);
            processUpdate = 3;
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();
            processUpdate = 3;
            try {
                res = put.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/profile_update/singlefield_update.php?id=" + params[0] + "&fieldvalue = " + params[1] + "&fieldname=" + params[2] );
            } catch (Exception e) {
                processUpdate = 2;
                e.printStackTrace();
                return null;
            }
            return res;
        }

        protected void onPostExecute(String res) {
            try {
                processUpdate = 3;
                JSONArray jArray = new JSONArray(res.toString());
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    String errorcode = json.getString("errorcode");
                    if (errorcode.equals("0")) {

                        /*Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
                        SharedPreferences.Editor edit = Loginprefs.edit();
                        edit.putString("user_id", json.getString("userid"));
                        edit.putString("user_name", json.getString("name"));
                        edit.putString("user_img", json.getString("profile_pic"));
                        edit.putString("user_deviceId", json.getString("device_id"));
                        edit.commit();

                        joiningdate.setText("Joining at " + json.getString("date"));
                        useraddress.setText(json.getString("address"));
                        txtphone.setText(json.getString("phone"));
                        txtbusishort.setText(json.getString("short_desc"));
                        txtbussislong.setText(json.getString("long_desc"));*/

                        /*edit_useraddress.setText(json.getString("address"));
                        edtphone.setText(json.getString("phone"));
                        edtbusishort.setText(json.getString("short_desc"));
                        edtbusslong.setText(json.getString("long_desc"));*/
                        processUpdate = 1;
                    } else {
                        processUpdate = 2;
                        customtoast.ShowToast(getApplicationContext(), "Error in getting data.", R.layout.red_toast);
                    }
                }
                updatefield.setVisibility(View.GONE);
            } catch (Exception objEx) {
                processUpdate = 2;
                updatefield.setVisibility(View.GONE);
                objEx.printStackTrace();
            }
        }
    }
}
