package fourever.textile.mainclasses;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import fourever.textile.entity.CityEntity;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.FileUploader;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.ShowAlertDialog;
import fourever.textile.miscs.customtoast;

public class ProfileFragment extends Fragment {

    private String userid;
    private SharedPreferences Loginprefs;
    private TextView username, joiningdate, saveprofile;
    private NetworkImageView img;
    private String imageurl;
    Bitmap bitMap;

    ImageView editprofile;
    EditText edit_useraddress, edtphone;
    TextView useraddress, txtphone, txtStatus;

    LinearLayout editprofile_mainlayout, loadmore, profilestrip;
    ProgressBar updatefield;
    private int processUpdate;
    private Dialog dialog, ProfileDialog;
    private String cityid;
    private String res1;
    private Spinner spincity;
    private String getcityid;
    private String useremail, usercity_id;
    private String usernm;

    private boolean emailcheck;
    private boolean mobilecheck;
    private EditText mobile, edtemail;
    private int GET_PHOTO;
    private Uri selectedImageUri, thumbnailUri;
    private File myDir, profile_pic;
    private String selectedPath, thumbnailPath;
    private NetworkImageView img_profile_pic;
    private ImageLoader imageLoader;
    View view;
 //   private Toolbar Maintoolbar;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile_two, null);

      //  Maintoolbar = ((MainActivity) getActivity()).getToolBarLayout();
      //  Maintoolbar.setVisibility(View.GONE);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");

        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }

      /*  ((MainActivity) getActivity()).setSupportActionBar(Maintoolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

       /* ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppBarLayout appbar = (AppBarLayout) view.findViewById(R.id.appbar);
            float heightDp = getResources().getDimension(R.dimen.user_profile_height21);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
            lp.height = (int) heightDp;
        } else {
            AppBarLayout appbar = (AppBarLayout) view.findViewById(R.id.appbar);
            float heightDp = getResources().getDimension(R.dimen.user_profile_height);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
            lp.height = (int) heightDp;
        }

        imageLoader = AppController.getInstance().getImageLoader();
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        processUpdate = 1;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editprofile_mainlayout = (LinearLayout) view.findViewById(R.id.editprofile_mainlayout);
        loadmore = (LinearLayout) view.findViewById(R.id.loadmore);

        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileDialog = new Dialog(getActivity(), android.R.style.Theme_Light);
                ProfileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ProfileDialog.setContentView(R.layout.systemnotification_dialog);
                ProfileDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ProfileDialog.getWindow().getAttributes().windowAnimations = R.style.ProfileDialogAnimation;
                ProfileDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                ImageView imageView_close = (ImageView) ProfileDialog.findViewById(R.id.imageView_close);
                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProfileDialog.dismiss();
                    }
                });

                /*RelativeLayout profileupdates = (RelativeLayout) ProfileDialog.findViewById(R.id.profileupdates);
                profileupdates.getBackground().setAlpha(80);*/
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                final String add = useraddress.getText().toString().trim();
                final EditText edtadd = (EditText) ProfileDialog.findViewById(R.id.edtadd);
                if (add == null || add.equals("") || add.equals("null") || add.equals(null)) {
                    edtadd.setHint("Enter address here.");
                } else {
                    edtadd.setText(useraddress.getText().toString());
                }

                final String mob = edtphone.getText().toString();
                mobile = (EditText) ProfileDialog.findViewById(R.id.edtmobile);
                if (mob == null || mob.equals("") || mob.equals("null") || mob.equals(null)) {
                    mobile.setHint("Enter Mobile Number.");
                } else {
                    mobile.setText(edtphone.getText().toString());
                }

                edtemail = (EditText) ProfileDialog.findViewById(R.id.edtemail);
                if (useremail == null || useremail.equals("") || useremail.equals("null") || useremail.equals(null)) {
                    edtemail.setHint("Enter Email Address.");
                } else {
                    edtemail.setText(useremail);
                }

                final EditText edtname = (EditText) ProfileDialog.findViewById(R.id.name);
                if (usernm == null || usernm.equals("") || usernm.equals("null") || usernm.equals(null)) {
                    edtname.setHint("Enter name.");
                } else {
                    edtname.setText(usernm);
                }

                mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if (!TextUtils.isEmpty(mobile.getText().toString().trim()))
                                new CheckMobile().execute(userid, mobile.getText().toString().trim());
                        }
                    }
                });

                edtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if (!TextUtils.isEmpty(edtemail.getText().toString()))
                                new CheckEmail().execute(userid, edtemail.getText().toString().trim());
                        }
                    }
                });

                Button button = (Button) ProfileDialog.findViewById(R.id.saveprofiles);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getcityid = getselcityid();

                        if (TextUtils.isEmpty(edtname.getText().toString().trim())) {
                            edtname.setError("Please enter name.");
                        }

                        if (TextUtils.isEmpty(edtemail.getText().toString().trim())) {
                            edtemail.setError(Html.fromHtml("<font color='red'>Please enter Email.</font>"));
                        }

                        if (TextUtils.isEmpty(mobile.getText().toString().trim())) {
                            mobile.setError(Html.fromHtml("<font color='red'>Please enter phone number.</font>"));
                        }

                        if (!TextUtils.isEmpty(edtname.getText().toString()) && !TextUtils.isEmpty(edtemail.getText().toString()) &&
                                !TextUtils.isEmpty(mobile.getText().toString())) {

                           /* if (emailcheck == false)
                                new CheckEmail().execute(userid, edtemail.getText().toString().trim());
                            if (mobilecheck == false)
                                new CheckMobile().execute(userid, mobile.getText().toString().trim());

                            if (emailcheck == true && mobilecheck == true) {*/

                            if (!getcityid.equals("0")) {
                                new SaveServiceSync().execute(userid,
                                        edtname.getText().toString().trim(),
                                        edtemail.getText().toString().trim(),
                                        mobile.getText().toString().trim(),
                                        edtadd.getText().toString().trim(), getcityid);
                            } else {
                                Toast.makeText(getActivity(), "Please Select City !!!",
                                        Toast.LENGTH_LONG).show();
                            }

                            //  }
                        }
                    }
                });

                //User profile pic update

                img_profile_pic = (NetworkImageView) ProfileDialog.findViewById(R.id.img_profile_pic);
                if (Loginprefs.getString("user_img", null) != null) {
                    img_profile_pic.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null), imageLoader);
                }

                profilestrip = (LinearLayout) ProfileDialog.findViewById(R.id.profilestrip);
                profilestrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            GET_PHOTO = 2;
                            startActivityForResult(Intent.createChooser(intent, "Profile picture "), 2);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            GET_PHOTO = 2;
                            startActivityForResult(Intent.createChooser(intent, "Profile picture "), 2);
                        }
                    }
                });

                //End profile pic

                spincity = (Spinner) ProfileDialog.findViewById(R.id.cty);
                fillcity();

                ProfileDialog.show();
            }
        });

        updatefield = (ProgressBar) view.findViewById(R.id.updatefield);

        editprofile = (ImageView) view.findViewById(R.id.editprofile);
        edit_useraddress = (EditText) view.findViewById(R.id.edit_useraddress);
        edtphone = (EditText) view.findViewById(R.id.edtphone);
        saveprofile = (TextView) view.findViewById(R.id.saveprofile);
        username = (TextView) view.findViewById(R.id.uname);
        joiningdate = (TextView) view.findViewById(R.id.joingingadte);
        useraddress = (TextView) view.findViewById(R.id.useraddress);

        txtphone = (TextView) view.findViewById(R.id.txtphone);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.review_dialog);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                    final EditText edtStatus = (EditText) dialog.findViewById(R.id.edtStatus);
                    String txtst = txtStatus.getText().toString();
                    if (txtst == null || txtst.equals("")
                            || txtst.equals("null") || txtst.equals(null)) {
                        edtStatus.setHint("Write something about your business.");
                    } else {
                        edtStatus.setText(txtStatus.getText().toString());
                    }

                    Button saveStatus = (Button) dialog.findViewById(R.id.savestatus);
                    saveStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new businessstatus().execute(userid, edtStatus.getText().toString(), "Status");
                        }
                    });

                    ImageView closedialog = (ImageView) dialog.findViewById(R.id.closedialog);
                    closedialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                }
            }
        });


        username.setText(Loginprefs.getString("user_name", null));
        img = (NetworkImageView) view.findViewById(R.id.rImage);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    GET_PHOTO = 2;
                    startActivityForResult(Intent.createChooser(intent, "Profile picture "), 2);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    GET_PHOTO = 2;
                    startActivityForResult(Intent.createChooser(intent, "Profile picture "), 2);
                }
            }
        });

        if (Loginprefs.getString("user_img", null) != null) {

            try {
                img.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null), imageLoader);
                /*imageurl = "http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null);
                URL url = new URL(imageurl);
                InputStream is = url.openConnection().getInputStream();
                if (bitMap != null) {
                    bitMap.recycle();
                    bitMap = null;
                }

                bitMap = BitmapFactory.decodeStream(is);

                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setImageBitmap(bitMap);*/

            } catch (Exception e) {
                Log.d("Sutter image problem", "error - " + e.getMessage());
            }
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

        //------------------------------------------------------------------------------------------------
        //    1 = useraddress , 2 = phonenumber , 3 = business short , 4 = business long

        useraddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditField(useraddress, edit_useraddress, "address");
            }
        });

        txtphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditField(txtphone, edtphone, "Mobile_no");
            }
        });

        try {
            new ServiceSync().execute(userid);
        } catch (Exception e) {
        }

        return view;
    }

    public void fillcity() {
        new FillCity().execute();
    }

    public class FillCity extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(),
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
                ArrayList<CityEntity> list1 = new ArrayList<CityEntity>();
                list1.clear();
                JSONArray jArray = new JSONArray(res.toString());
                list1.add(new CityEntity("0", "Select City"));

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    list1.add(new CityEntity(json.get("city_id").toString(), json.get("name").toString()));

                    // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Profile.this, R.layout.spinneritem, list1);
                    //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                spincity.setAdapter(new MyCustomAdapter(getActivity(), R.layout.spinner_items, list1));

                for (int i = 0; i < spincity.getAdapter().getCount(); i++) {
                    CityEntity entity = (CityEntity) spincity.getAdapter().getItem(i);
                    if (usercity_id.equals(entity.getCity_id())) {
                        spincity.setSelection(i);
                    }
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
            Toast.makeText(getActivity(), "No City Added !!!",
                    Toast.LENGTH_LONG).show();
        } else {
            //String cityname = spincity.getSelectedItem().toString();
            CityEntity cityEntity = (CityEntity) spincity.getSelectedItem();
            try {
                cityid = cityEntity.getCity_id();

                /*PutUtility objClient = new PutUtility();
                res1 = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/city_view.php");
                JSONArray jArray = new JSONArray(res1.toString());

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    if (json.getString("name").equals(cityEntity.getCity_name())) {
                        cityid = json.getString("city_id");
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cityid;
    }

    private void hideKeyboard(EditText edt) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    private void showKeyboard(EditText edt) {
        /*InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(edit_useraddress.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);*/
        edt.setSelection(edt.getText().length());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);

    }

    private void EditField(final TextView txt, final EditText edt, final String field) {
        if (processUpdate == 1 || processUpdate == 2) {
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

                    if (processUpdate == 1 || processUpdate == 2) {
                        hideKeyboard(edt);
                        txt.setVisibility(View.VISIBLE);
                        edt.setVisibility(View.GONE);

                        if (field.equals("Mobile_no")) {
                            new MainCheckMobile().execute(userid, edt.getText().toString().trim(), field);
                        } else {
                            new UpdateSingleField().execute(userid, edt.getText().toString(), field);
                        }
                    } else {
                        Toast.makeText(getActivity(), "updating in progress.. please wait..", Toast.LENGTH_SHORT).show();
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

        ScrollView scroll = (ScrollView) view.findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edt.hasFocus()) {
                    edt.clearFocus();
                }
                return false;
            }
        });

        /*txtStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(Profile.this, "Touch", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });*/

    }

   /* @Override
    public void onResume() {
        super.onResume();
        if (Maintoolbar != null) {
            Maintoolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Maintoolbar.setVisibility(View.VISIBLE);
    }*/

    public void changeStatus(View v) {

        try {
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.review_dialog);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            final EditText edtStatus = (EditText) dialog.findViewById(R.id.edtStatus);
            String txtst = txtStatus.getText().toString();
            if (txtst == null || txtst.equals("")
                    || txtst.equals("null") || txtst.equals(null)) {
                edtStatus.setHint("Write something about your business.");
            } else {
                edtStatus.setText(txtStatus.getText().toString());
            }

            Button saveStatus = (Button) dialog.findViewById(R.id.savestatus);
            saveStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new businessstatus().execute(userid, edtStatus.getText().toString(), "Status");
                }
            });

            ImageView closedialog = (ImageView) dialog.findViewById(R.id.closedialog);
            closedialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {
        }
    }

    private class businessstatus extends AsyncTask<String, Void, String> {

        private String res;
        private String fieldname;
        private String fieldvalue;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(),
                    "", "Loading..");
            mProgressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                fieldvalue = params[1];
                fieldname = params[2];
                res = null;
                PutUtility put = new PutUtility();
                res = put.getData("http://192.168.0.150:550/TextileApp/webservice/profile_update/singlefield_update.php?id=" + params[0] + "&fieldname=" + params[2] + "&fieldvalue=" + URLEncoder.encode(params[1], "UTF-8"));

            } catch (Exception e) {
                mProgressDialog.dismiss();
                e.printStackTrace();
                return null;
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
                        txtStatus.setText(fieldvalue);
                        dialog.dismiss();
                    } else {
                        customtoast.ShowToast(getActivity(), "Error in update " + fieldname, R.layout.red_toast);
                    }
                }
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mProgressDialog.dismiss();
                objEx.printStackTrace();
            }
        }
    }

    private class ServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(),
                    "", "Loading..");
            mProgressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://192.168.0.150:550/TextileApp/webservice/user_detail.php?userid=" + params[0]);
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

                        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
                        SharedPreferences.Editor edit = Loginprefs.edit();
                        edit.putString("user_id", json.getString("userid"));
                        edit.putString("user_name", json.getString("name"));
                        edit.putString("user_img", json.getString("profile_pic"));
                        edit.putString("user_deviceId", json.getString("device_id"));
                        edit.commit();

                        joiningdate.setText("Joining at " + json.getString("date"));

                        if (json.getString("address") == null || json.getString("address").equals("") || json.getString("address").equals("null") || json.getString("address").equals(null)) {
                            useraddress.setHint("Enter address here");
                        } else {
                            useraddress.setText(json.getString("address"));
                        }

                        if (json.getString("phone") == null || json.getString("phone").equals("") || json.getString("phone").equals("null") || json.getString("phone").equals(null)) {
                            txtphone.setText("Enter phone number here");
                        } else {
                            txtphone.setText(json.getString("phone"));
                        }

                        useremail = json.getString("email");
                        usercity_id = json.getString("city_id");
                        usernm = json.getString("name");

                        if (json.getString("Status") == null || json.getString("Status").equals("")
                                || json.getString("Status").equals("null") || json.getString("Status").equals(null)) {
                            //txtStatus.setText("Write something about your business.");
                            txtStatus.setHint("Write something about your business.");
                        } else {
                            txtStatus.setText(json.getString("Status"));
                        }

                        /*txtbusishort.setText(URLDecoder.decode(json.getString("short_desc"),"UTF-8"));
                        txtbussislong.setText(json.getString("long_desc"));*/
                        if (json.getString("address") == null || json.getString("address").equals("") || json.getString("address").equals("null") || json.getString("address").equals(null)) {
                            edit_useraddress.setText("");
                        } else {
                            edit_useraddress.setText(json.getString("address"));
                        }

                        if (json.getString("phone") == null || json.getString("phone").equals("") || json.getString("phone").equals("null") || json.getString("phone").equals(null)) {
                            edtphone.setText("");
                        } else {
                            edtphone.setText(json.getString("phone"));
                        }

                        /*edtbusishort.setText(json.getString("short_desc"));
                        edtbusslong.setText(json.getString("long_desc"));*/
                    } else {
                        customtoast.ShowToast(getActivity(), "Error in getting data.", R.layout.red_toast);
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
        private String fieldname;

        // 1 = update completed , 2 = error in update , 3 = ongoing.
        @Override
        protected void onPreExecute() {
            updatefield.setVisibility(View.VISIBLE);
            processUpdate = 3;
        }

        @Override
        protected String doInBackground(String... params) {
            fieldname = params[2];
            res = null;
            PutUtility put = new PutUtility();
            processUpdate = 3;
            try {
                res = put.getData("http://192.168.0.150:550/TextileApp/webservice/profile_update/singlefield_update.php?id=" + params[0] + "&fieldname=" + params[2] + "&fieldvalue=" + URLEncoder.encode(params[1], "UTF-8"));
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

                        processUpdate = 1;
                        new ServiceSync().execute(userid);
                    } else {
                        processUpdate = 2;
                        customtoast.ShowToast(getActivity(), "Error in update " + fieldname, R.layout.red_toast);
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

    private class CheckEmail extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;
        private ShowAlertDialog salert;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(
                    getActivity(), "",
                    "Checking email... Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();
            try {
                put.setParam("id", params[0]);
                put.setParam("user_email", params[1]);
                //  put.setParam("id", params[1]);
                //res = put.postData("http://www.eazito.com/webservice/vendor_edit_email_check.php");
                res = put.postData("http://192.168.0.150:550/TextileApp/webservice/user_edit_email.php");

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
                    salert.showAlertDialog(getActivity(), "Registration",
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

                                edtemail.setError(Html.fromHtml("<font color='red'>" + json.getString("errormsg").toString() + "</font>"));
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(getActivity(), "This email is already registered.", R.layout.red_toast);
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
                    getActivity(), "",
                    "Checking mobile... Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();
            try {
                put.setParam("id", params[0]);
                put.setParam("phone", params[1]);
                //  put.setParam("id", params[1]);
                //res = put.postData("http://www.eazito.com/webservice/vendor_edit_email_check.php");
                res = put.postData("http://192.168.0.150:550/TextileApp/webservice/user_edit_mobile.php");

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
                    /*salert.showAlertDialog(getActivity(), "Registration",
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
                                mobile.setError(Html.fromHtml("<font color='red'>" + json.getString("errormsg").toString() + "</font>"));
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(getActivity(), json.getString("errormsg"), R.layout.red_toast);
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

    private class MainCheckMobile extends AsyncTask<String, Void, String> {

        private ProgressDialog mProgressDialog;
        private String res;
        private String uid;
        private String phone;
        private String field;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(
                    getActivity(), "",
                    "Checking mobile... Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {

            uid = params[0];
            phone = params[1];
            field = params[2];

            res = null;
            PutUtility put = new PutUtility();
            try {
                put.setParam("id", params[0]);
                put.setParam("phone", params[1]);
                res = put.postData("http://192.168.0.150:550/TextileApp/webservice/user_edit_mobile.php");

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
                                new UpdateSingleField().execute(userid, phone, field);
                            } else {
                                mobilecheck = false;
                                mProgressDialog.dismiss();
                                edtphone.requestFocus();
                                edtphone.setError(Html.fromHtml("<font color='red'>" + json.getString("errormsg").toString() + "</font>"));
                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                customtoast.ShowToast(getActivity(), json.getString("errormsg"), R.layout.red_toast);
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

    public class SaveServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String Response;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(),
                    "", "");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                PutUtility objClient = new PutUtility();
                objClient.setParam("id", params[0]);
                objClient.setParam("name", Uri.encode(params[1]));
                objClient.setParam("email", Uri.encode(params[2]));
                objClient.setParam("phone", Uri.encode(params[3]));
                objClient.setParam("address", Uri.encode(params[4]));
                objClient.setParam("city_id", params[5]);

                Response = objClient.postData("http://192.168.0.150:550/TextileApp/webservice/profile_update/update_all.php");
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
                                customtoast.ShowToast(getActivity(), "update successfully.!!!", R.layout.blue_toast);
                                try {
                                    new ServiceSync().execute(userid);
                                } catch (Exception e) {
                                }
                                mProgressDialog.dismiss();
                                ProfileDialog.dismiss();

                            } else if (errorcode.equals("1")) {
                                customtoast.ShowToast(getActivity(), json.getString("errormsg"), R.layout.red_toast);
                                mProgressDialog.dismiss();
                                ProfileDialog.dismiss();

                            } else {
                                if (json.getString("erroremail").equals("1")) {
                                    edtemail.setError(Html.fromHtml("<font color='red'>" + json.getString("erroremailmsg") + "</font>"));
                                }
                                if (json.getString("errormob").equals("1")) {
                                    mobile.setError(Html.fromHtml("<font color='red'>" + json.getString("errormobmsg") + "</font>"));
                                }

                                mProgressDialog.dismiss();

                                // Toast.makeText(getActivity(), json.getString("errormsg"), Toast.LENGTH_LONG).show();
                                // customtoast.ShowToast(Profile.this, json.getString("errormsg"), R.layout.red_toast);
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

    public class MyCustomAdapter extends ArrayAdapter<CityEntity> {

        private ArrayList<CityEntity> data;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<CityEntity> objects) {
            super(context, textViewResourceId, objects);
            data = objects;
            // TODO Auto-generated constructor stub
        }


        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            CityEntity tempValues = (CityEntity) data.get(position);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_items, parent, false);
            TextView label = (TextView) row.findViewById(R.id.servicename);
            label.setText(tempValues.getCity_name());

            return row;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (GET_PHOTO == 2) {
                //Original Image URI
                selectedImageUri = data.getData();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        performCrop(selectedImageUri);
                    }
                }).start();
            }
            //user is returning from cropping the image
            else if (GET_PHOTO == 1) {
                //get the returned data
                thumbnailUri = data.getData();
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");

                try {
                    /*myDir = new File(this.getFilesDir(), "Avatars");
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }*/

                    FileOutputStream foutMain = getActivity().openFileOutput("me.jpg", getActivity().MODE_PRIVATE);
                    thePic.compress(Bitmap.CompressFormat.JPEG, 80, foutMain);

                   /* int bytes = byteSizeOf(thePic);
                    ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                    thePic.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                    byte[] array = buffer.array();
                    foutMain.write(array);*/

                    //Toast.makeText(Profile.this, thumbnailUri.getPath().toString(), Toast.LENGTH_SHORT).show();

                    //  profile_pic = new File(myDir, "me.jpg");
                    //  FileOutputStream fout = new FileOutputStream(profile_pic);
                    //  selectedPath = getPath(selectedImageUri);

                    thumbnailPath = getActivity().getFilesDir().getAbsolutePath() + "/me.jpg";
                    dialog = ProgressDialog.show(getActivity(), "", "Uploading file...", true);

                    new Thread(new Runnable() {
                        public void run() {

                            try {
                                String url = "http://192.168.0.150:550/TextileApp/webservice/user_profilepic_upload.php";
                                FileUploader FU = new FileUploader(url, "UTF-8", getActivity());
                                FU.addFormField("user_id", userid);
                                FU.addLowFilePart("user_img", new File(thumbnailPath), userid);
                                String datas = FU.finishString();

                                JSONArray jArray = new JSONArray(datas);
                                if (jArray.length() == 0) {

                                } else {

                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject json = jArray.getJSONObject(i);
                                        final String errorcode = json.getString("errorcode");
                                        try {
                                            if (errorcode.equals("0")) {
                                                Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
                                                SharedPreferences.Editor edit = Loginprefs.edit();
                                                edit.putString("user_name", json.getString("user_name"));
                                                edit.putString("user_img", json.getString("user_img"));
                                                edit.commit();

                                                if (Loginprefs.getString("user_img", null) != null) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            img.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null), imageLoader);
                                                            if (img_profile_pic != null) {
                                                                img_profile_pic.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null), imageLoader);
                                                            }
                                                            //Dashboard.img.setImageUrl("http://www.eazito.com/media/vender/" + Loginprefs.getString("vendor_img", null), imageLoader);
                                                            customtoast.ShowToast(getActivity(), "Updated successfully.", R.layout.blue_toast);
                                                        }
                                                    });
                                                } else {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            img.setImageResource(R.drawable.maleuser);
                                                            if (img_profile_pic != null) {
                                                                img_profile_pic.setImageResource(R.drawable.maleuser);
                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                final String errormsg = json.getString("errormsg");
                                                /*if (serverResponseCode == 200) {
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            //Toast.makeText(Dashboard.this, errormsg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }*/
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            String er = e.getMessage();
                                        }
                                    }
                                }

                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (Exception e) {
                }

                if (img_profile_pic != null) {
                    img_profile_pic.setImageBitmap(thePic);
                }

            }
        }
    }

    protected int byteSizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount();
        } else {
            return data.getAllocationByteCount();
        }
    }

    private void performCrop(Uri selectedImageUri1) {
        //take care of exceptions
        // String tmp  = getPath(selectedImageUri);
        //tmpUri = Uri.parse(selectedImageUri1.getPath());

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImageUri1, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 500);
            cropIntent.putExtra("aspectY", 500);
            //indicate output X and Y

            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);

            // cropIntent.putExtra("scaleUpIfNeeded", true);

            cropIntent.putExtra("scale", true);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            // cropIntent.putExtra(MediaStore.EXTRA_OUTPUT,tmpUri);
            //start the activity - we handle returning in onActivityResult

           /* if (cropIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cropIntent, 1);
            } else {
                Toast.makeText(getActivity(), "No Crop App Available", Toast.LENGTH_SHORT).show();
            }*/

            GET_PHOTO = 1;
            startActivityForResult(cropIntent, 1);
        }
        //respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            customtoast.ShowToast(getActivity(), errorMessage, R.layout.red_toast);
            /*Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();*/
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}
