package fourever.textile.mainclasses;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fourever.textile.notificationadapter.notifications;

public class Chat extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    private int int_items = 3;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Chats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton newchat = (FloatingActionButton) findViewById(R.id.newchat);
        newchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "new Chat.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final MyAdapter mad = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mad);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    
   class MyAdapter extends FragmentPagerAdapter {

       public MyAdapter(FragmentManager fm) {
           super(fm);
       }

       public void setTab(int pos) {
           getItem(pos);
       }

       @Override
       public Fragment getItem(int position) {
           switch (position) {
               case 0:
                   return new notifications();
               case 1:
                   return new notifications();
               case 2:
                   return new notifications();

           }
           return null;
       }

       @Override
       public int getCount() {
           return int_items;
       }


       @Override
       public CharSequence getPageTitle(int position) {

           switch (position) {
               case 0:
                   return "Chats";
               case 1:
                   return "Groups";
               case 2:
                   return "Request";
           }
           return null;
       }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
