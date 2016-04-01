package fourever.textile.mainclasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fourever.textile.adapter.category_adapter;
import fourever.textile.adapter.followers_unfollow_adapter;
import fourever.textile.entity.CityEntity;
import fourever.textile.entity.all_people_entity;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.Utils;
import fourever.textile.miscs.customtoast;

public class Followers_Profile extends AppCompatActivity {

    // Start Follower and Following List

    LinearLayout linear_followers, linear_following;
    TextView following, followers;
    Context mContext;
    private FrameLayout frlyt;
    private Button txtrefresh;
    private TextView txtNoFollowers_dialog;
    private ProgressBar follower_dialog_progressBar;
    private RecyclerView followers_dialog_recycler;
    private followers_unfollow_adapter followers_following_adaptor;

    // End Follower and Following List
    private String userid;
    private SharedPreferences Loginprefs;
    private TextView username, joiningdate, saveprofile;
    private NetworkImageView img;
    // ImageView editprofile;
    EditText edit_useraddress, edtphone;
    TextView useraddress, txtphone, txtStatus;
    LinearLayout editprofile_mainlayout, loadmore, profilestrip;
    ProgressBar updatefield;

    private Dialog dialog, ProfileDialog;
    private String cityid;
    private Spinner spincity;
    private String useremail, usercity_id;
    private String usernm;

    private EditText mobile, edtemail;
    private NetworkImageView img_profile_pic;
    private ImageLoader imageLoader;
    private Button follow;
    private String people_user_id, people_is_follow;
    private String getcityid;
    private String profile_pic;
    private Dialog unfollow_dialog;

    private TextView txt_nocategory;
    private RecyclerView categoryRecyclerView;
    private category_adapter category_list_adapter;
    private RelativeLayout lblrelcategory;
    private ProgressBar category_list_progressBar;
    private ImageView category_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_profile);
        mContext = Followers_Profile.this;

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(Followers_Profile.this, Login.class);
            startActivity(intent);
            finish();
        }

        if (getIntent().getExtras() != null) {
            people_user_id = getIntent().getStringExtra("people_user_id");
            people_is_follow = getIntent().getStringExtra("people_is_follow");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        GridLayoutManager catgridLayoutManager = new GridLayoutManager(mContext, 1);
        categoryRecyclerView.setLayoutManager(catgridLayoutManager);

        category_list_progressBar = (ProgressBar) findViewById(R.id.category_list_progressBar);
        txt_nocategory = (TextView) findViewById(R.id.txt_nocategory);
        category_refresh = (ImageView) findViewById(R.id.category_refresh);
        lblrelcategory = (RelativeLayout) findViewById(R.id.lblrelcategory);
        category_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Category_list().execute(people_user_id);
            }
        });
        new Category_list().execute(people_user_id);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
            float heightDp = getResources().getDimension(R.dimen.user_profile_height21);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
            lp.height = (int) heightDp;
        } else {
            AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
            float heightDp = getResources().getDimension(R.dimen.user_profile_height);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
            lp.height = (int) heightDp;
        }

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        imageLoader = AppController.getInstance().getImageLoader();
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editprofile_mainlayout = (LinearLayout) findViewById(R.id.editprofile_mainlayout);
        loadmore = (LinearLayout) findViewById(R.id.loadmore);

        followers = (TextView) findViewById(R.id.followers);
        following = (TextView) findViewById(R.id.following);
        linear_followers = (LinearLayout) findViewById(R.id.linear_followers);
        linear_following = (LinearLayout) findViewById(R.id.linear_following);

        linear_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (Followers_dialog == null) {
                    Followers_dialog = new Dialog(mContext, android.R.style.Theme_Light);
                }*/

                final Dialog Followers_dialog = new Dialog(mContext, android.R.style.Theme_Light);

                Followers_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                final View vw = LayoutInflater.from(mContext).inflate(R.layout.followers_following_dialog, null, false);
                Followers_dialog.setContentView(vw);
                Followers_dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                frlyt = (FrameLayout) Followers_dialog.findViewById(R.id.frlyt);
                txtrefresh = (Button) Followers_dialog.findViewById(R.id.txtrefresh);
                txtNoFollowers_dialog = (TextView) Followers_dialog.findViewById(R.id.txtNoFollowers_dialog);
                follower_dialog_progressBar = (ProgressBar) Followers_dialog.findViewById(R.id.follower_dialog_progressBar);
                ImageView Imgclose = (ImageView) Followers_dialog.findViewById(R.id.closedialog);
                TextView title = (TextView) Followers_dialog.findViewById(R.id.followersdialog);
                title.setText("Followers");

                Imgclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                vw.animate()
                                        .translationY(Utils.getScreenHeight(mContext))
                                        .setDuration(500)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                               /* CommentsActivity.super.onBackPressed();
                                                overridePendingTransition(0, 0);*/
                                                Followers_dialog.dismiss();
                                            }
                                        })
                                        .start();
                            }
                        }
                    }
                });

                final TextView txtNoDataFound_comment = (TextView) Followers_dialog.findViewById(R.id.txtNoDataFound_comment);
                followers_dialog_recycler = (RecyclerView) Followers_dialog.findViewById(R.id.followers_dialog_recycler);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
                followers_dialog_recycler.setLayoutManager(gridLayoutManager);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  0 = followers , 1 = following
                        new Followers_following_service().execute(people_user_id, "0", "other_user_profile");
                    }
                });

                // if (!Followers_dialog.isShowing()) {
                Followers_dialog.show();
                // }
            }
        });

        linear_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*
                if (Following_dialog == null) {
                    Following_dialog = new Dialog(mContext, android.R.style.Theme_Light);
                }
                */

                final Dialog Following_dialog = new Dialog(mContext, android.R.style.Theme_Light);

                Following_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                final View vw = LayoutInflater.from(mContext).inflate(R.layout.followers_following_dialog, null, false);
                Following_dialog.setContentView(vw);
                Following_dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                frlyt = (FrameLayout) Following_dialog.findViewById(R.id.frlyt);
                txtrefresh = (Button) Following_dialog.findViewById(R.id.txtrefresh);
                txtNoFollowers_dialog = (TextView) Following_dialog.findViewById(R.id.txtNoFollowers_dialog);
                follower_dialog_progressBar = (ProgressBar) Following_dialog.findViewById(R.id.follower_dialog_progressBar);
                ImageView Imgclose = (ImageView) Following_dialog.findViewById(R.id.closedialog);
                TextView title = (TextView) Following_dialog.findViewById(R.id.followersdialog);
                title.setText("Following");

                Imgclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                vw.animate()
                                        .translationY(Utils.getScreenHeight(mContext))
                                        .setDuration(500)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                       /* CommentsActivity.super.onBackPressed();
                                        overridePendingTransition(0, 0);*/
                                                Following_dialog.dismiss();
                                            }
                                        }).start();
                            }
                        }
                    }
                });

                final TextView txtNoDataFound_comment = (TextView) Following_dialog.findViewById(R.id.txtNoDataFound_comment);
                followers_dialog_recycler = (RecyclerView) Following_dialog.findViewById(R.id.followers_dialog_recycler);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
                followers_dialog_recycler.setLayoutManager(gridLayoutManager);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 0 = followers , 1 = following
                        new Followers_following_service().execute(people_user_id, "1", "other_user_profile");
                    }
                });


                // if (!Following_dialog.isShowing()) {
                Following_dialog.show();
                //  }
            }
        });


        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProfileDialog = new Dialog(Followers_Profile.this, android.R.style.Theme_Light);
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
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                final String add = useraddress.getText().toString().trim();
                TextView edit = (TextView) ProfileDialog.findViewById(R.id.edit);
                edit.setVisibility(View.GONE);

                final EditText edtadd = (EditText) ProfileDialog.findViewById(R.id.edtadd);
                edtadd.setEnabled(false);
                if (add == null || add.equals("") || add.equals("null") || add.equals(null)) {
                    edtadd.setHint("Enter address here.");
                } else {
                    edtadd.setText(useraddress.getText().toString());
                }

                final String mob = edtphone.getText().toString();
                mobile = (EditText) ProfileDialog.findViewById(R.id.edtmobile);
                mobile.setEnabled(false);
                if (mob == null || mob.equals("") || mob.equals("null") || mob.equals(null)) {
                    mobile.setHint("Enter Mobile Number.");
                } else {
                    mobile.setText(edtphone.getText().toString());
                }

                edtemail = (EditText) ProfileDialog.findViewById(R.id.edtemail);
                edtemail.setEnabled(false);
                if (useremail == null || useremail.equals("") || useremail.equals("null") || useremail.equals(null)) {
                    edtemail.setHint("Enter Email Address.");
                } else {
                    edtemail.setText(useremail);
                }

                final EditText edtname = (EditText) ProfileDialog.findViewById(R.id.name);
                edtname.setEnabled(false);
                if (usernm == null || usernm.equals("") || usernm.equals("null") || usernm.equals(null)) {
                    edtname.setHint("Enter name.");
                } else {
                    edtname.setText(usernm);
                }

                //Button button = (Button) ProfileDialog.findViewById(R.id.saveprofiles);
                TextView button = (TextView) ProfileDialog.findViewById(R.id.saveprofile_actionbar);
                button.setVisibility(View.GONE);

                TextView updateprofile = (TextView) ProfileDialog.findViewById(R.id.updateprofile);
                updateprofile.setText("Profile");

                //User profile pic update

                img_profile_pic = (NetworkImageView) ProfileDialog.findViewById(R.id.img_profile_pic);
                if (img_profile_pic != null) {
                    imageLoader.get(profile_pic, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "Image Load Error: " + error.getMessage());
                            img_profile_pic.setBackground(ContextCompat.getDrawable(Followers_Profile.this, R.drawable.maleuser));
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null) {
                                // load image into imageview
                                img_profile_pic.setImageUrl(response.getRequestUrl(), imageLoader);
                            }
                        }
                    });
                }
                profilestrip = (LinearLayout) ProfileDialog.findViewById(R.id.profilestrip);
                //End profile pic

                spincity = (Spinner) ProfileDialog.findViewById(R.id.cty);
                spincity.setEnabled(false);
                fillcity();

                ProfileDialog.show();
            }
        });

        updatefield = (ProgressBar) findViewById(R.id.updatefield);
        //  editprofile = (ImageView) findViewById(R.id.editprofile);
        edit_useraddress = (EditText) findViewById(R.id.edit_useraddress);
        edtphone = (EditText) findViewById(R.id.edtphone);
        saveprofile = (TextView) findViewById(R.id.saveprofile);
        username = (TextView) findViewById(R.id.uname);

        joiningdate = (TextView) findViewById(R.id.joingingadte);
        useraddress = (TextView) findViewById(R.id.useraddress);
        txtphone = (TextView) findViewById(R.id.txtphone);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        follow = (Button) findViewById(R.id.follow);
        if (people_is_follow.equals("1") || people_is_follow.equals("3")) {
            //followBtnString = "follow";
            follow.setText("Following");
            follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
            follow.setBackgroundResource(R.drawable.following_button);
            follow.setTextColor(ContextCompat.getColor(Followers_Profile.this, R.color.accentcolor));
        }

        if (people_is_follow.equals("0")) {
            // followBtnString = "unfollow";
            follow.setText("Requested");
            follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.requested, 0, 0, 0);
            follow.setBackgroundResource(R.drawable.requested_button);
            follow.setTextColor(ContextCompat.getColor(Followers_Profile.this, R.color.accentcolor));
        }

        //if (people_is_follow.equals("2") || people_is_follow.equals("3")) {
        if (people_is_follow.equals("2")) {
            Drawable img = ContextCompat.getDrawable(Followers_Profile.this, R.drawable.follow_add);
            follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            follow.setBackgroundResource(R.drawable.follow_button);
            follow.setText("Follow");
            follow.setTextColor(ContextCompat.getColor(Followers_Profile.this, R.color.primaryColor));
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().toString().equalsIgnoreCase("Requested")) {
                    Drawable img = ContextCompat.getDrawable(Followers_Profile.this, R.drawable.follow_add);
                    follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    follow.setBackgroundResource(R.drawable.follow_button);
                    follow.setText("Follow");
                    follow.setTextColor(ContextCompat.getColor(Followers_Profile.this, R.color.primaryColor));

                    new follow_unfollow().execute(userid, people_is_follow, "2");
                    return;
                }

                if (follow.getText().toString().equalsIgnoreCase("Follow")) {
                    follow.setText("Requested");
                    follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.requested, 0, 0, 0);
                    follow.setBackgroundResource(R.drawable.requested_button);
                    follow.setTextColor(ContextCompat.getColor(Followers_Profile.this, R.color.accentcolor));
                    new follow_unfollow().execute(userid, people_is_follow, "0");
                    return;
                }

                if (follow.getText().toString().equalsIgnoreCase("Following")) {

                    unfollow_dialog = new Dialog(Followers_Profile.this, android.R.style.Theme_Light);
                    unfollow_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    unfollow_dialog.setContentView(R.layout.unfollow_dialog);
                    unfollow_dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    unfollow_dialog.getWindow().getAttributes().windowAnimations = R.style.ProfileDialogAnimation;
                    unfollow_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    unfollow_dialog.show();

                    final TextView people_name = (TextView) unfollow_dialog.findViewById(R.id.people_name);
                    people_name.setText(usernm + " ?");
                    final NetworkImageView peopleimg = (NetworkImageView) unfollow_dialog.findViewById(R.id.peopleimg);

                    imageLoader.get(profile_pic, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "Image Load Error: " + error.getMessage());
                            peopleimg.setBackground(ContextCompat.getDrawable(Followers_Profile.this, R.drawable.maleuser));
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null) {
                                // load image into imageview
                                peopleimg.setImageUrl(response.getRequestUrl(), imageLoader);
                            }
                        }
                    });

                    Button cancel = (Button) unfollow_dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unfollow_dialog.dismiss();
                        }
                    });

                    final Button unfollow = (Button) unfollow_dialog.findViewById(R.id.unfollow);
                    unfollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new follow_unfollow().execute(userid, people_user_id, "2");

                            Drawable img = ContextCompat.getDrawable(Followers_Profile.this, R.drawable.follow_add);
                            follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                            follow.setBackgroundResource(R.drawable.follow_button);
                            follow.setText("Follow");
                            follow.setTextColor(ContextCompat.getColor(Followers_Profile.this, R.color.primaryColor));
                            unfollow_dialog.dismiss();
                        }
                    });
                }
            }
        });

        img = (NetworkImageView) findViewById(R.id.rImage);
        new ServiceSync().execute(people_user_id, userid);
    }

    public class Followers_following_service extends AsyncTask<String, Void, String> {

        private String res;
        private ArrayList<all_people_entity> Services;
        private String flag;
        private String userType;

        //  ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            //mSwipeRefreshLayout.setRefreshing(true);
            follower_dialog_progressBar.setVisibility(View.VISIBLE);

           /* mProgressDialog = ProgressDialog.show(
                    getActivity(), "Loading...",
                    "Loading Ongoing cases.");*/
        }

        @Override
        protected String doInBackground(String... params) {
            flag = params[1];
            PutUtility objClient = new PutUtility();
            try {

                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/followers_following_list.php?userid=" + params[0] + "&flag=" + params[1] + "&loged_user_id=" + userid);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {

                    follower_dialog_progressBar.setVisibility(View.GONE);
                    //mSwipeRefreshLayout.setRefreshing(false);
                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    txtrefresh.setVisibility(View.VISIBLE);

                    // mProgressDialog.dismiss();
                    // Toast.makeText(getActivity(), "Please Check your internet connectivity.", Toast.LENGTH_LONG).show();
                    customtoast.ShowToast(mContext, "Please Check your internet connectivity.", R.layout.red_toast);
                    txtNoFollowers_dialog.setVisibility(View.VISIBLE);
                    if (flag.equals("1")) {
                        txtNoFollowers_dialog.setText("No Following found");
                    } else {
                        txtNoFollowers_dialog.setText("No Followers found");
                    }

                    txtNoFollowers_dialog.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {

                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    txtrefresh.setVisibility(View.VISIBLE);

                    followers_dialog_recycler.setVisibility(View.GONE);
                    followers_dialog_recycler.setEnabled(false);

                    if (flag.equals("1")) {
                        txtNoFollowers_dialog.setText("No Following found");
                    } else {
                        txtNoFollowers_dialog.setText("No Followers found");
                    }
                    txtNoFollowers_dialog.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                } else {

                    followers_dialog_recycler.setVisibility(View.VISIBLE);
                    followers_dialog_recycler.setEnabled(true);

                    frlyt.setVisibility(View.GONE);
                    frlyt.setSaveEnabled(false);

                    Services = new ArrayList<all_people_entity>();
                    Services.clear();

                    JSONObject js = jArray.getJSONObject(0);
                    if (js.getString("total_follower") == "0" || js.getString("total_follower").equals("0")) {
                        frlyt.setVisibility(View.VISIBLE);
                        frlyt.setSaveEnabled(true);
                        txtrefresh.setVisibility(View.VISIBLE);

                        followers_dialog_recycler.setVisibility(View.GONE);
                        followers_dialog_recycler.setEnabled(false);

                        txtNoFollowers_dialog.setVisibility(View.VISIBLE);
                        if (flag.equals("1")) {
                            txtNoFollowers_dialog.setText("No Following found");
                        } else {
                            txtNoFollowers_dialog.setText("No Followers found");
                        }
                        txtNoFollowers_dialog.setGravity(Gravity.CENTER
                                | Gravity.CENTER_HORIZONTAL);
                    } else {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            all_people_entity service = new all_people_entity();
                            service.setUser_id(json.getString("user_id"));
                            service.setUser_name(json.getString("user_name"));
                            service.setUser_pic(json.getString("user_pic"));
                            service.setIs_follow(json.getString("is_follow"));
                            service.setTotal(json.getString("total_follower"));
                            Services.add(service);
                        }
                    }

                    JSONObject json = jArray.getJSONObject(0);
                    if (flag.equals("1")) {
                        following.setText(json.getString("total_follower"));
                    } else {
                        followers.setText(json.getString("total_follower"));
                    }

                    if (Services.size() > 0) {
                        followers_following_adaptor = new followers_unfollow_adapter(mContext, Services, people_user_id, "other_user_profile");
                        followers_dialog_recycler.setAdapter(followers_following_adaptor);
                        followers_following_adaptor.notifyDataSetChanged();
                    } else {

                        frlyt.setVisibility(View.VISIBLE);
                        frlyt.setSaveEnabled(true);
                        txtrefresh.setVisibility(View.VISIBLE);

                        followers_dialog_recycler.setVisibility(View.GONE);
                        followers_dialog_recycler.setEnabled(false);

                        txtNoFollowers_dialog.setVisibility(View.VISIBLE);
                        if (flag.equals("1")) {
                            txtNoFollowers_dialog.setText("No Following found");
                        } else {
                            txtNoFollowers_dialog.setText("No Followers found");
                        }
                        txtNoFollowers_dialog.setGravity(Gravity.CENTER
                                | Gravity.CENTER_HORIZONTAL);
                    }
                }

                follower_dialog_progressBar.setVisibility(View.GONE);
                // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public void fillcity() {
        new FillCity().execute();
    }

    public class FillCity extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Followers_Profile.this,
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
                spincity.setAdapter(new MyCustomAdapter(getApplicationContext(), R.layout.spinner_items, list1));

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

    private class ServiceSync extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        private String res;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Followers_Profile.this,
                    "", "Loading..");
            // mProgressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://192.168.0.150:550/TextileApp/webservice/user_detail.php?userid=" + params[0] + "&logged_in_user=" + params[1]);
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

                       /* Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
                        SharedPreferences.Editor edit = Loginprefs.edit();
                        edit.putString("user_id", json.getString("userid"));
                        edit.putString("user_name", json.getString("name"));
                        edit.putString("user_img", json.getString("profile_pic"));
                        edit.putString("user_deviceId", json.getString("device_id"));
                        edit.commit();*/

                        following.setText(json.getString("total_following"));
                        followers.setText(json.getString("total_followers"));

                        profile_pic = "http://192.168.0.150:550/TextileApp/profile_pictures/" + json.getString("profile_pic");
                        joiningdate.setText("Joining at " + json.getString("date"));
                        try {

                            imageLoader.get(profile_pic, new ImageLoader.ImageListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error", "Image Load Error: " + error.getMessage());
                                    img.setBackground(ContextCompat.getDrawable(Followers_Profile.this, R.drawable.maleuser));
                                }

                                @Override
                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                    if (response.getBitmap() != null) {
                                        // load image into imageview
                                        img.setImageUrl(response.getRequestUrl(), imageLoader);
                                    }
                                }
                            });

                        } catch (Exception e) {
                            Log.d("Sutter image problem", "error - " + e.getMessage());
                        }
                       /* try {
                            img_profile_pic.setImageUrl(, imageLoader);
                            img.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + json.getString("profile_pic"), imageLoader);

                        } catch (Exception e) {
                            Log.d("Sutter image problem", "error - " + e.getMessage());
                        }*/

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

                        username.setText(usernm);

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

    public class follow_unfollow extends AsyncTask<String, Void, String> {
        private String res;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;

            mProgressDialog = ProgressDialog.show(
                    Followers_Profile.this, "",
                    "Loading...");
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/follow_unfollow.php?user_id=" + params[0] + "&follower_id=" + params[1] + "&follow=" + params[2]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {
                    mProgressDialog.dismiss();
                    customtoast.ShowToast(Followers_Profile.this, "Something went Wrong.", R.layout.red_toast);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    mProgressDialog.dismiss();
                    customtoast.ShowToast(Followers_Profile.this, "Something went Wrong.", R.layout.red_toast);
                } else {

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        String errorcode = json.getString("errorcode");
                        if (errorcode.equals("0")) {
                            // customtoast.ShowToast(mContext,"Following.",R.layout.red_toast);
                        } else {
                            customtoast.ShowToast(Followers_Profile.this, json.getString("errormsg"), R.layout.red_toast);
                        }
                    }
                }
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mProgressDialog.dismiss();
                customtoast.ShowToast(Followers_Profile.this, "Something went Wrong.", R.layout.red_toast);
                objEx.printStackTrace();
            }
        }
    }

    public class Category_list extends AsyncTask<String, Void, String> {

        private String Response;
        ArrayList<CityEntity> Services;

        @Override
        protected void onPreExecute() {
            category_list_progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                PutUtility objClient = new PutUtility();
                Response = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/category_list.php?userid=" + params[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return Response;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null || res.equals(null) || res.equals("null")) {
                    category_list_progressBar.setVisibility(View.GONE);
                } else {

                    JSONArray jArray = new JSONArray(res.toString());
                    if (jArray.length() == 0) {
                        category_list_progressBar.setVisibility(View.GONE);
                        txt_nocategory.setVisibility(View.VISIBLE);
                    } else {

                        Services = new ArrayList<CityEntity>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            String errorcode = json.getString("errorcode");

                            if (errorcode.equals("0")) {
                                CityEntity service = new CityEntity();
                                service.setCity_id(json.getString("Category_id"));
                                service.setCity_name(json.getString("Category_name"));
                                Services.add(service);
                            } else {
                                customtoast.ShowToast(Followers_Profile.this, json.getString("errormsg"), R.layout.red_toast);
                                category_list_progressBar.setVisibility(View.GONE);
                                txt_nocategory.setVisibility(View.VISIBLE);
                            }
                        }

                        if (Services.size() <= 0) {
                            txt_nocategory.setVisibility(View.VISIBLE);
                            category_list_progressBar.setVisibility(View.GONE);
                        } else {
                            txt_nocategory.setVisibility(View.GONE);
                        }

                        if (Services.size() == 1) {
                            lblrelcategory.setMinimumHeight(160);
                            lblrelcategory.getLayoutParams().height = 160;
                        }

                        if (Services.size() == 2) {
                            lblrelcategory.setMinimumHeight(250);
                            lblrelcategory.getLayoutParams().height = 250;
                        }

                        if (Services.size() == 3) {
                            lblrelcategory.setMinimumHeight(350);
                            lblrelcategory.getLayoutParams().height = 350;
                        }

                        if (Services.size() >= 4) {
                            lblrelcategory.setMinimumHeight(420);
                            lblrelcategory.getLayoutParams().height = 420;
                        }

                        category_list_adapter = new category_adapter(Followers_Profile.this, Services, "other_user_profile");
                        categoryRecyclerView.setAdapter(category_list_adapter);
                        category_list_adapter.notifyDataSetChanged();
                    }
                }
                category_list_progressBar.setVisibility(View.GONE);
            } catch (Exception objEx) {
                txt_nocategory.setVisibility(View.VISIBLE);
                category_list_progressBar.setVisibility(View.GONE);
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

        }


        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            //return super.getView(position, convertView, parent);

            CityEntity tempValues = (CityEntity) data.get(position);

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_items, parent, false);
            TextView label = (TextView) row.findViewById(R.id.servicename);
            label.setText(tempValues.getCity_name());

            return row;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return false;
    }
}
