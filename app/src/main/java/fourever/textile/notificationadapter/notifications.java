package fourever.textile.notificationadapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.tonicartos.superslim.LayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.LinearSLM;
import com.tonicartos.superslim.SectionLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import fourever.textile.adapter.all_friend_request_adapter;
import fourever.textile.customsearch.widgets.MaterialSearchView;
import fourever.textile.entity.all_friend_request_entity;
import fourever.textile.mainclasses.Login;
import fourever.textile.mainclasses.MainActivity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

/**
 * Fragment that displays a list of country names.
 */
public class notifications extends Fragment {

    //Start sticky header config
    String lastHeader = "";
    int sectionManager = -1;
    int headerCount = 0;
    int sectionFirstPosition = 0;
    //End sticky header config

    private ArrayList<LineItem> mItems;
    private int skipItem = 0;
    private SharedPreferences Loginprefs;
    private String userid;


    FrameLayout frlyt;
    Button btnrefresh;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";

    private static final String TAG_COUNTRIES_FRAGMENT = "tag_countries_fragment";

    private ViewHolder mViews;

    private CountryNamesAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;

    private Random mRng = new Random();

    private Toast mToast = null;

    private GridSLM mGridSLM;

    private SectionLayoutManager mLinearSectionLayoutManager;
    private MenuItem refreshItem;

    public boolean areHeadersOverlaid() {
        return (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) != 0;
    }

    public boolean areHeadersSticky() {
        return (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY) != 0;
    }

    public boolean areMarginsFixed() {
        return mAreMarginsFixed;
    }

    public int getHeaderMode() {
        return mHeaderDisplay;
    }

    public void setHeaderMode(int mode) {
        mHeaderDisplay = mode | (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (
                mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notifications, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mItems = new ArrayList<>();

        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        btnrefresh = (Button) view.findViewById(R.id.btnrefresh);
        frlyt = (FrameLayout) view.findViewById(R.id.frlyt);
       /* mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.activity_noti_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColorDark, R.color.ColorPrimaryDark, R.color.green);*/

        if (savedInstanceState != null) {
            mHeaderDisplay = savedInstanceState
                    .getInt(KEY_HEADER_POSITIONING,
                            getResources().getInteger(R.integer.default_header_display));
            mAreMarginsFixed = savedInstanceState
                    .getBoolean(KEY_MARGINS_FIXED,
                            getResources().getBoolean(R.bool.default_margins_fixed));
        } else {
            mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }

        mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));
        //  mViews.initViewsLinear(new LinearLayoutManager(getActivity()));
        mAdapter = new CountryNamesAdapter(getActivity(), mHeaderDisplay);

       /* mViews.mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(
                new LinearLayoutManager(getActivity())) {
            @Override
            public void onLoadMore(int currentPage) {

            }
        });*/

       /* mViews.mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(new LayoutManager(getActivity())) {
            @Override
            public void onLoadMore(int current_page) {
                if (skipItem == 0)
                    skipItem = 1 * current_page;
                else
                    skipItem = skipItem * current_page;

                new ServiceSync().execute("26", String.valueOf(skipItem));
            }
        });*/

        /*mViews.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int currentFirstVisibleItem = 0;
            private int currentVisibleItemCount = 0;
            private int currentScrollState;
            private boolean isLoading = false;
            private int totalLoadedData = 0;
            private int totalItemCount = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0
                        && this.currentScrollState == 0) {

                    if (totalLoadedData < (currentFirstVisibleItem + currentVisibleItemCount)) {
                        totalLoadedData = currentFirstVisibleItem
                                + currentVisibleItemCount;

                        skipItem = skipItem + 4;
                        //   Toast.makeText(getActivity(), String.valueOf(skipItem), Toast.LENGTH_SHORT).show();
                        new ServiceSync().execute(userid, String.valueOf(skipItem));
                        // loadMore.setVisibility(View.VISIBLE);
                        // mSwipeRefreshLayout.setRefreshing(true);
                        if (totalLoadedData == totalItemCount) {

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    //  loadMore.setVisibility(View.GONE);
                                    //  mSwipeRefreshLayout.setRefreshing(false);
                                    //  txtNoDataFound1.setVisibility(View.GONE);
                                }
                            }, 2000);
                        }
                    }

                    if (!isLoading) {
                        isLoading = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {

                LayoutManager lm =(LayoutManager) mRecyclerView.getLayoutManager();
                this.currentFirstVisibleItem = lm.findFirstVisibleItemPosition();
                this.currentVisibleItemCount = mRecyclerView.getChildCount();
                this.totalItemCount = lm.getItemCount();
                //onScroll(currentFirstVisibleItem, currentVisibleItemCount, totalItemCount);
            }
        });*/

       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                try {
                    new ServiceSync().execute(userid, String.valueOf(skipItem));
                } catch (Exception e) {
                }
            }
        });*/

        new ServiceSync().execute(userid, String.valueOf(skipItem));

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServiceSync().execute(userid, String.valueOf(skipItem));
            }
        });
      /*  updateHeaderMode(LayoutManager.LayoutParams.HEADER_INLINE);
        notifications f = getCountriesFragment();
        f.setHeadersSticky(true);*/
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_notification_tab, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();    // Remove all items
        getActivity().getMenuInflater().inflate(R.menu.menu_notification_tab, menu);
        refreshItem = menu.findItem(R.id.action_refresh);
        refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                lastHeader = "";
                sectionManager = -1;
                headerCount = 0;
                sectionFirstPosition = 0;

                new ServiceSync().execute(userid, String.valueOf(skipItem));
                return true;
            }
        });
    }


  /*  public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        private int previousTotal = 0; // The total number of items in the dataset after the last load
        private boolean loading = true; // True if we are still waiting for the last set of data to load.
        private int visibleThreshold = 4; // The minimum amount of items to have below your current scroll position before loading more.
        int firstVisibleItem, visibleItemCount, totalItemCount;

        private int current_page = 1;

        private LayoutManager mlayoutManager;

        public EndlessRecyclerOnScrollListener(LayoutManager linearLayoutManager) {
            this.mlayoutManager =(LayoutManager) linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mlayoutManager.getItemCount();
            firstVisibleItem = mlayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                // Do something here
                current_page++;
                loading = true;
            }
        }

        public abstract void onLoadMore(int current_page);
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setHasOptionsMenu(true);
            MainActivity.setToolbarTitle(getResources().getString(R.string.notifications));
        } else {
            setHasOptionsMenu(false);
        }
    }

    /* private void updateHeaderMode(int mode) {
        notifications fragment = getCountriesFragment();
        fragment.setHeaderMode(mode);
    }

    private notifications getCountriesFragment() {
        return (notifications) getTargetFragment();
        *//*return (notifications) getActivity().getSupportFragmentManager()
                .findFragmentByTag(TAG_COUNTRIES_FRAGMENT);*//*
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_HEADER_POSITIONING, mHeaderDisplay);
        outState.putBoolean(KEY_MARGINS_FIXED, mAreMarginsFixed);
    }

    public void scrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        String s = "Scroll to position " + position
                + (mAdapter.isItemHeader(position) ? ", header " : ", item ")
                + mAdapter.itemToString(position) + ".";
        if (mToast != null) {
            mToast.setText(s);
        } else {
            mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        }
        mToast.show();
        mViews.scrollToPosition(position);
    }

    public void setHeadersOverlaid(boolean areHeadersOverlaid) {
        mHeaderDisplay = areHeadersOverlaid ? mHeaderDisplay
                | LayoutManager.LayoutParams.HEADER_OVERLAY
                : mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_OVERLAY;
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setHeadersSticky(boolean areHeadersSticky) {
        mHeaderDisplay = areHeadersSticky ? mHeaderDisplay
                | LayoutManager.LayoutParams.HEADER_STICKY
                : mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_STICKY;
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setMarginsFixed(boolean areMarginsFixed) {
        mAreMarginsFixed = areMarginsFixed;
        mAdapter.setMarginsFixed(areMarginsFixed);
    }

    public void smoothScrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        String s = "Smooth scroll to position " + position
                + (mAdapter.isItemHeader(position) ? ", header " : ", item ")
                + mAdapter.itemToString(position) + ".";
        if (mToast != null) {
            mToast.setText(s);
        } else {
            mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        }
        mToast.show();
        mViews.smoothScrollToPosition(position);
    }

    private static class ViewHolder {

        private final RecyclerView mRecyclerView;

        public ViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        }

        public void initViews(LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        public void initViewsLinear(LinearLayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        public void scrollToPosition(int position) {
            mRecyclerView.scrollToPosition(position);
        }

        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }

        public void smoothScrollToPosition(int position) {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    public class ServiceSync extends AsyncTask<String, Void, String> {
        private String res;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            //   mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            //   mSwipeRefreshLayout.setRefreshing(true);

            mProgressDialog = ProgressDialog.show(
                    getActivity(), "",
                    "Loading...");
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/notifications.php?user_id=" + params[0] + "&skipitem=" + params[1]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {

                if (res == null || res == " " || res.equals(" ")) {

                    //  mSwipeRefreshLayout.setRefreshing(false);
                    //  mSwipeRefreshLayout.setVisibility(View.GONE);
                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    mProgressDialog.dismiss();
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    mProgressDialog.dismiss();

                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);

                    //  mSwipeRefreshLayout.setRefreshing(false);
                    //  mSwipeRefreshLayout.setVisibility(View.GONE);

                } else {

                    mItems.clear();
                    JSONObject jsn = jArray.getJSONObject(0);
                    String errorcode = jsn.getString("errorcode");

                    if (errorcode.equals("0")) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);

                            String header = json.getString("notidate").substring(0, 10);
                            if (!TextUtils.equals(lastHeader, header)) {
                                // Insert new header view and update section data.
                                //sectionManager = (sectionManager + 1) % 2;
                                sectionManager = (sectionManager + 1);
                                sectionFirstPosition = i + headerCount;
                                lastHeader = header;
                                headerCount += 1;
                                mItems.add(new LineItem(header, true, sectionManager, sectionFirstPosition));
                            }

                            LineItem lineItem = new LineItem(header, false, sectionManager, sectionFirstPosition);
                            lineItem.setNoti_user_id(json.getString("user_id"));
                            lineItem.setNoti_post_id(json.getString("post_id"));
                            lineItem.setNoti_user_name(json.getString("user_name"));
                            lineItem.setNoti_user_pic(json.getString("user_pic"));
                            lineItem.setNoti_message(json.getString("message"));
                            lineItem.setNoti_time(json.getString("notitime"));
                            mItems.add(lineItem);
                        }

                        if (mItems.size() > 0) {
                            //mSwipeRefreshLayout.setRefreshing(false);

                            mAdapter.addItems(mItems);
                            mAdapter.setMarginsFixed(mAreMarginsFixed);
                            mAdapter.setHeaderDisplay(mHeaderDisplay);
                            mViews.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } else {
                            //  mProgressDialog.dismiss();
                        }

                        //   mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        //   mSwipeRefreshLayout.setRefreshing(false);
                        frlyt.setVisibility(View.GONE);
                        frlyt.setSaveEnabled(false);

                    } else {
                        mAdapter = new CountryNamesAdapter(getActivity(), mHeaderDisplay);
                        mAdapter.setMarginsFixed(mAreMarginsFixed);
                        mAdapter.setHeaderDisplay(mHeaderDisplay);
                        mViews.setAdapter(mAdapter);

                        //   mSwipeRefreshLayout.setVisibility(View.GONE);
                        //  mSwipeRefreshLayout.setRefreshing(false);
                        frlyt.setVisibility(View.VISIBLE);
                        frlyt.setSaveEnabled(true);
                        mProgressDialog.dismiss();
                    }

                }

                // mSwipeRefreshLayout.setRefreshing(false);
                mProgressDialog.dismiss();

            } catch (Exception objEx) {

                //  mSwipeRefreshLayout.setVisibility(View.GONE);
                //  mSwipeRefreshLayout.setRefreshing(false);
                frlyt.setVisibility(View.VISIBLE);
                frlyt.setSaveEnabled(true);

                mProgressDialog.dismiss();
                objEx.printStackTrace();
            }
        }
    }
}
