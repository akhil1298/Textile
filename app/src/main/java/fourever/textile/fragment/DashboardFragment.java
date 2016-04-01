package fourever.textile.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.List;

import fourever.textile.mainclasses.R;
import fourever.textile.miscs.PutUtility;
import fourever.textile.notificationadapter.notifications;

public class DashboardFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;

    private static final String TAG_COUNTRIES_FRAGMENT = "tag_countries_fragment";

    private int[] tabIcons = {
            R.drawable.homeunselect,
            R.drawable.searchunselect,
            R.drawable.activityunselect
    };
    private ViewPagerAdapter adapter;

    GoogleCloudMessaging gcm;
    String PROJECT_NUMBER = "693813425622";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String regid;
    private SharedPreferences Loginprefs;
    private String userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.dashboard_fragement_layout, null);

        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("vendor_id", null);

        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        if(getArguments() != null){
            String role = getArguments().getString("role");
            if(role.equals("like_comment_noti")){
                tabLayout.getTabAt(2).select();
            }
        }

       /* tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        final MyAdapter mad = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(mad);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });*/

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    updateHeaderMode(LayoutManager.LayoutParams.HEADER_INLINE);
                    notifications f = getCountriesFragment();
                    f.setHeadersSticky(true);
                    f.setMarginsFixed(false);

                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setHasOptionsMenu(true);
        viewPager.setOffscreenPageLimit(int_items);

        return x;
    }

    private void updateHeaderMode(int mode) {
        notifications fragment = getCountriesFragment();
        fragment.setHeaderMode(mode);
    }

    private notifications getCountriesFragment() {
        return (notifications) adapter.getItem(2);
        /*return (notifications) getActivity().getSupportFragmentManager()
                .findFragmentByTag(TAG_COUNTRIES_FRAGMENT);*/
    }

    private void setupTabIcons() {
        //   View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

        /*TextView tabOne = (TextView)v.findViewById(R.id.tab);
        tabOne.setText("1");
        ImageView tabicon = (ImageView)v.findViewById(R.id.tabicon);
        tabicon.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.tab_selection_one));
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_selection_one, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);*/

       /*  tabLayout.getTabAt(1).setIcon(R.drawable.tab_selection_two);
        tabLayout.getTabAt(2).setIcon(R.drawable.tab_selection_three);*/

        tabLayout.getTabAt(0).setCustomView(adapter.getTabView(0, ""));
        tabLayout.getTabAt(1).setCustomView(adapter.getTabView(1, ""));
        tabLayout.getTabAt(2).setCustomView(adapter.getTabView(2, "100"));

        tabLayout.getTabAt(0).getCustomView().setSelected(true);

        //tabLayout.setSelected(false);

       /* tabLayout.getTabAt(1).setIcon(R.drawable.tab_selection_two);
        tabLayout.getTabAt(2).setIcon(R.drawable.tab_selection_three);*/
        //  updateTabCount(0,"6");
    }

    public void updateTabCount(int tabPosition, String count) {
        View v = tabLayout.getTabAt(0).getCustomView();
        TextView tx = (TextView) v.findViewById(R.id.tab);
        if (count.equals(""))
            tx.setVisibility(View.GONE);
        else
            tx.setVisibility(View.VISIBLE);
        tx.setText(count);
        tabLayout.getTabAt(0).setCustomView(v);

        adapter.notifyDataSetChanged();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(TAG_COUNTRIES_FRAGMENT);

        adapter.addFrag(new newsfeed(), "News Feed");
        adapter.addFrag(new PeopleSearch(), "Search People");
        adapter.addFrag(new notifications(), TAG_COUNTRIES_FRAGMENT);

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private String tabTitles[] = new String[]{"1", "", "1000"};
        private int[] imageResId = {R.drawable.tab_selection_one, R.drawable.tab_selection_two, R.drawable.tab_selection_three};

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public View getTabView(int position, String count) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView

            View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

            TextView tv = (TextView) v.findViewById(R.id.tab);
            if (count.equals(""))
                tv.setVisibility(View.GONE);
            tv.setText(count);
            ImageView img = (ImageView) v.findViewById(R.id.tabicon);
            img.setImageResource(imageResId[position]);

            return v;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

   /* class MyAdapter extends FragmentPagerAdapter {

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
                    return new newsfeed();
                case 1:
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
                    return "New Leads";
                case 1:
                    return "Ongoing";
            }
            return null;
        }
    }*/



}
