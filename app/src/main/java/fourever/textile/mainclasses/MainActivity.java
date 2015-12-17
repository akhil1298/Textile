package fourever.textile.mainclasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import fourever.textile.fragment.newsfeed;
import fourever.textile.miscs.RoundedImageView;


public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    private TextView Name;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private SharedPreferences Loginprefs;
    private String userid;
    private RoundedImageView img;
    private String imageurl;
    private Bitmap bitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.newsfeed);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        Name = (TextView) mNavigationView.findViewById(R.id.user_name);
        img = (RoundedImageView) mNavigationView.findViewById(R.id.user_image);

        Name = (TextView) mNavigationView.findViewById(R.id.user_name);
        Name.setText(Loginprefs.getString("user_name", null));

        String userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {

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
                    imageurl = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null);
                    URL url = new URL(imageurl);
                    InputStream is = url.openConnection().getInputStream();
                    if(bitMap!=null)
                    {
                        bitMap.recycle();
                        bitMap=null;
                    }

                    bitMap = BitmapFactory.decodeStream(is);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img.setImageBitmap(bitMap);
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
        mFragmentTransaction.replace(R.id.containerView, new newsfeed()).commit();

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        mNavItemId = menuItem.getItemId();
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.nav_item_dashboard) {
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView, new newsfeed()).commit();
                            toolbar.setTitle(R.string.newsfeed);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_editprofile) {
                            Intent intent = new Intent(MainActivity.this, Profile.class);
                            startActivity(intent);
                            toolbar.setTitle(R.string.editprofile);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_chat) {
                            Intent intent = new Intent(MainActivity.this, Chat.class);
                            startActivity(intent);
                            toolbar.setTitle(R.string.chat);
                        }

                        if (menuItem.getItemId() == R.id.nav_item_rateus) {
                            toolbar.setTitle(R.string.setting);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        return super.onKeyUp(keyCode, objEvent);
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
}
