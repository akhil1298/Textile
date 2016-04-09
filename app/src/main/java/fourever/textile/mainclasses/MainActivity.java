package fourever.textile.mainclasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import fourever.textile.fragment.ContactusFragment;
import fourever.textile.fragment.DashboardFragment;
import fourever.textile.fragment.Friend_Requests;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.Utils;
import fourever.textile.notificationadapter.notifications;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    public static Toolbar toolbar;
    private TextView Name;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private SharedPreferences Loginprefs;
    private String userid;
    private NetworkImageView img;
    private String imageurl;
    private Bitmap bitMap;
    private ImageLoader imageLoader;

    private static final int ANIM_DURATION_TOOLBAR = 300;

    GoogleCloudMessaging gcm;
    String PROJECT_NUMBER = "693813425622";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String regid;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.newsfeed);
        setSupportActionBar(toolbar);

        startIntroAnimation();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        prefs = getSharedPreferences("Pref_Count", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("count", 1);
        edit.commit();

        imageLoader = AppController.getInstance().getImageLoader();
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        if (userid == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {

            if (checkPlayServices()) {
                getRegId();
            }

            Name = (TextView) mNavigationView.findViewById(R.id.user_name);
            img = (NetworkImageView) mNavigationView.findViewById(R.id.user_image);
            Name = (TextView) mNavigationView.findViewById(R.id.user_name);

            Name.setText(Loginprefs.getString("user_name", null));
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    startActivity(intent);

                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                    }
                }
            });

            if (Loginprefs.getString("user_img", null) != null) {
                try {
                    img.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null), imageLoader);

                   /* imageurl = "http://192.168.0.150:550/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null);
                    URL url = new URL(imageurl);
                    InputStream is = url.openConnection().getInputStream();
                    if(bitMap!=null)
                    {
                        bitMap.recycle();
                        bitMap=null;
                    }

                    bitMap = BitmapFactory.decodeStream(is);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img.setImageBitmap(bitMap);*/
                } catch (Exception e) {
                    Log.d("Sutter image problem", "error - " + e.getMessage());
                }
            } else {
                img.setImageResource(R.drawable.maleuser);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (getIntent().getExtras() != null) {
            String role = getIntent().getStringExtra("role");
            if (role.equals("like_comment_noti")) {
                DashboardFragment dashboardFragment = new DashboardFragment();
                Bundle bundle = new Bundle();
                bundle.putString("role", "like_comment_noti");
                dashboardFragment.setArguments(bundle);
                mFragmentTransaction.replace(R.id.containerView, dashboardFragment).commit();
                toolbar.setTitle(R.string.newsfeed);
            }

            if (role.equals("sendRequest")) {
                Friend_Requests frd_request = new Friend_Requests();
                Bundle bundle = new Bundle();
                bundle.putString("role", "sendRequest");
                frd_request.setArguments(bundle);
                mFragmentTransaction.replace(R.id.containerView, frd_request).commit();
                toolbar.setTitle(R.string.frdrequest);
            }

        } else {
            mFragmentTransaction.replace(R.id.containerView, new DashboardFragment()).commit();
            toolbar.setTitle(R.string.newsfeed);
        }

       /* mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new DashboardFragment()).commit();*/

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        mNavItemId = menuItem.getItemId();
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.nav_item_dashboard) {
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new DashboardFragment()).commit();
                            toolbar.setTitle(R.string.newsfeed);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_editprofile) {
                            Intent intent = new Intent(MainActivity.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                            /*FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new ProfileFragment()).commit();
                            toolbar.setTitle(R.string.editprofile);*/
                        }

                        if (menuItem.getItemId() == R.id.nav_item_chat) {
                            Intent intent = new Intent(MainActivity.this, Chat.class);
                            startActivity(intent);
                            toolbar.setTitle(R.string.chat);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_friend_request) {
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new Friend_Requests()).commit();
                            toolbar.setTitle(R.string.frdrequest);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_invite_frds) {

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent
                                    .putExtra(
                                            android.content.Intent.EXTRA_TEXT,
                                            getResources().getString(R.string.invitefriends));
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                    "Take a look at our Textile Mobile App");
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));

                            /*FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new Invite_Friends()).commit();
                            toolbar.setTitle(R.string.frdrequest);*/
                        }

                        if (menuItem.getItemId() == R.id.nav_item_rateus) {
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new notifications()).commit();
                            toolbar.setTitle(R.string.newsfeed);
                            toolbar.setTitle(R.string.setting);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_contactus) {
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new ContactusFragment()).commit();
                            toolbar.setTitle(R.string.frdrequest);
                        }


                        if (menuItem.getItemId() == R.id.nav_item_logout) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            alertDialog.setTitle("Exit");
                            alertDialog.setMessage("Are you sure you want to Logout?");

                            alertDialog.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences.Editor edit = Loginprefs.edit();
                                            edit.clear();
                                            edit.commit();

                                            String userid = Loginprefs.getString("user_id", null);
                                            if (userid == null) {
                                                Intent intent = new Intent(MainActivity.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                                LoginManager.getInstance().logOut();
                                            }
                                        }
                                    });

                            alertDialog.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }

                        return true;
                    }
                });
    }


    private void startIntroAnimation() {
        //btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        toolbar.setTranslationY(-actionbarSize);
        // ivLogo.setTranslationY(-actionbarSize);
        // inboxMenuItem.getActionView().setTranslationY(-actionbarSize);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            toolbar.animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION_TOOLBAR)
                    .setStartDelay(1000);
        }
        /*ivLogo.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        inboxMenuItem.getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                       // startContentAnimation();
                    }
                })
                .start();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        //toolbar.setVisibility(View.GONE);
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    public static void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public static Toolbar getToolBarLayout() {
        return toolbar;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification_tab, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return false;
        /*if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);*/
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            goBack();
        }
    }

    public void goBack() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Exit");
        alertDialog.setMessage("Are you sure you want to Exit?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    private class ServiceUpdateID extends AsyncTask<Void, Void, String> {

        private String res;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            res = null;
            PutUtility put = new PutUtility();
            try {
                res = put.getData("http://192.168.0.150:550/TextileApp/webservice/update_deviceID.php?userid=" + userid + "&device_id=" + regid + "&flag=0");
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {
                }

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
                                .getInstance(MainActivity.this);
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
                    new ServiceUpdateID().execute();
                    //Toast.makeText(Login.this, regid, Toast.LENGTH_LONG).show();
                }
            }

        }.execute(null, null, null);
    }

    private boolean checkPlayServices() {
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(MainActivity.this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {

                }
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }
}
