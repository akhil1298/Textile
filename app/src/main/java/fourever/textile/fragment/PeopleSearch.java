package fourever.textile.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fourever.textile.adapter.all_people_adapter;
import fourever.textile.customsearch.interfaces.onSearchListener;
import fourever.textile.customsearch.interfaces.onSimpleSearchActionsListener;
import fourever.textile.customsearch.widgets.MaterialSearchView;
import fourever.textile.entity.all_people_entity;
import fourever.textile.mainclasses.Login;
import fourever.textile.mainclasses.MainActivity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

public class PeopleSearch extends Fragment implements onSimpleSearchActionsListener, onSearchListener {

    private boolean mSearchViewAdded = false;
    private MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private Toolbar mToolbar;
    private MenuItem searchItem;
    private boolean searchActive = false;
    RecyclerView recycler_all_people;
    private Button follow, following;
    String followBtnString = "follow";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String userid;
    private SharedPreferences Loginprefs;
    private Button txtrefresh;
    private FrameLayout frlyt;
    private TextView txtNoDataFound1;
    private ArrayList<all_people_entity> Services;
    private all_people_adapter newsfeedAdapter;
    private int skipItem = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.peoplesearch, container, false);

        txtrefresh = (Button) view.findViewById(R.id.txtrefresh);
        frlyt = (FrameLayout) view.findViewById(R.id.frlyt);
        txtNoDataFound1 = (TextView) view.findViewById(R.id.txtNoDataFound);

        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.all_people_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColorDark, R.color.ColorPrimaryDark, R.color.green);
        Services = new ArrayList<all_people_entity>();
        Services.clear();

        //new ServiceSync().execute(userid,String.valueOf(0));
        new ServiceSync().execute(userid, String.valueOf(skipItem));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                try {
                    //new ServiceSync().execute(userid,String.valueOf(0));
                    new ServiceSync().execute(userid, String.valueOf(skipItem));
                } catch (Exception e) {
                }
            }
        });

        recycler_all_people = (RecyclerView) view.findViewById(R.id.all_people);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recycler_all_people.setLayoutManager(gridLayoutManager);

        recycler_all_people.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        skipItem = skipItem + 12;
                        //   Toast.makeText(getActivity(), String.valueOf(skipItem), Toast.LENGTH_SHORT).show();
                        new ServiceSync().execute(userid, String.valueOf(skipItem));
                        // loadMore.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        if (totalLoadedData == totalItemCount) {

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    //  loadMore.setVisibility(View.GONE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    txtNoDataFound1.setVisibility(View.GONE);
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
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView
                        .getLayoutManager();
                this.currentFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                this.currentVisibleItemCount = mRecyclerView.getChildCount();
                this.totalItemCount = mLayoutManager.getItemCount();
                //onScroll(currentFirstVisibleItem, currentVisibleItemCount, totalItemCount);
            }
        });

        /*recycler_all_people.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                new ServiceSync().execute(userid,String.valueOf(current_page));
            }
        });*/

        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        mToolbar = MainActivity.getToolBarLayout();
        mSearchView = new MaterialSearchView(getActivity());
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setHintText("Search");


        follow = (Button) view.findViewById(R.id.follow);
        // following = (Button) view.findViewById(R.id.following);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (followBtnString.equals("follow")) {
                    followBtnString = "unfollow";
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.follow_add);
                    follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    follow.setBackgroundResource(R.drawable.follow_button);
                    follow.setText("Follow");
                    follow.setTextColor(ContextCompat.getColor(getActivity(), R.color.primaryColor));
                } else {
                    followBtnString = "follow";
                    follow.setText("Following");
                    follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
                    follow.setBackgroundResource(R.drawable.following_button);
                    follow.setTextColor(ContextCompat.getColor(getActivity(), R.color.accentcolor));
                }
            }
        });

        setHasOptionsMenu(true);
        return view;
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

            if (mToolbar != null) {
                // Delay adding SearchView until Toolbar has finished loading
                mToolbar.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mSearchViewAdded && mWindowManager != null) {
                            mWindowManager.addView(mSearchView,
                                    MaterialSearchView.getSearchViewLayoutParams(getActivity()));
                            mSearchViewAdded = true;
                        }
                    }
                });
            }

            setHasOptionsMenu(true);
            MainActivity.setToolbarTitle("Search People");
        } else {
            setHasOptionsMenu(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_perople_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
       /* if (Build.VERSION.SDK_INT >= 11) {
            // selectMenu(menu);
            getActivity().getParent().getMenuInflater().inflate(R.menu.menu_perople_search, menu);
        }*/
        //getActivity().getMenuInflater().inflate(R.menu.menu_perople_search, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();    // Remove all items
        getActivity().getMenuInflater().inflate(R.menu.menu_perople_search, menu);
        searchItem = menu.findItem(R.id.search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSearchView.display();
                openKeyboard();
                return true;
            }
        });
        if (searchActive)
            mSearchView.display();
    }

    private void openKeyboard() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }

    @Override
    public void onSearch(String query) {

    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {
        searchActive = false;
        mSearchView.hide();
    }

    @Override
    public void onItemClicked(String item) {

    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }

    public class ServiceSync extends AsyncTask<String, Void, String> {
        private String res;

        //ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            mSwipeRefreshLayout.setRefreshing(true);

           /* mProgressDialog = ProgressDialog.show(
                    getActivity(), "Loading...",
                    "Loading Ongoing cases.");*/
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/all_people.php?user_id=" + params[0] + "&skipitem=" + params[1]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    txtrefresh.setVisibility(View.VISIBLE);

                    // mProgressDialog.dismiss();
                    // Toast.makeText(getActivity(), "Please Check your internet connectivity.", Toast.LENGTH_LONG).show();
                    customtoast.ShowToast(getActivity(), "Please Check your internet connectivity.", R.layout.red_toast);
                    txtNoDataFound1.setVisibility(View.VISIBLE);
                    txtNoDataFound1.setText("No news feed found");
                    txtNoDataFound1.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {

                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    txtrefresh.setVisibility(View.VISIBLE);

                    recycler_all_people.setVisibility(View.GONE);
                    recycler_all_people.setEnabled(false);

                    txtNoDataFound1.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);

                    txtNoDataFound1.setText("No Newsfeed Found !");
                    txtNoDataFound1.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                } else {

                    recycler_all_people.setVisibility(View.VISIBLE);
                    recycler_all_people.setEnabled(true);

                    frlyt.setVisibility(View.GONE);
                    frlyt.setSaveEnabled(false);

                    Services.clear();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        all_people_entity service = new all_people_entity();
                        service.setUser_id(json.getString("user_id"));
                        service.setUser_name(json.getString("user_name"));
                        service.setUser_pic(json.getString("user_pic"));
                        service.setIs_follow(json.getString("is_follow"));
                        Services.add(service);
                    }
                    if (Services.size() > 0) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        newsfeedAdapter = new all_people_adapter(getActivity(), Services);
                        recycler_all_people.setAdapter(newsfeedAdapter);
                        newsfeedAdapter.notifyDataSetChanged();
                    } else {

                        frlyt.setVisibility(View.VISIBLE);
                        frlyt.setSaveEnabled(true);
                        txtrefresh.setVisibility(View.VISIBLE);

                        recycler_all_people.setVisibility(View.GONE);
                        recycler_all_people.setEnabled(false);
                        mSwipeRefreshLayout.setRefreshing(false);

                        txtNoDataFound1.setVisibility(View.VISIBLE);
                        txtNoDataFound1.setText("No Data Found !");
                        txtNoDataFound1.setGravity(Gravity.CENTER
                                | Gravity.CENTER_HORIZONTAL);
                    }
                }

                // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mSwipeRefreshLayout.setRefreshing(false);
                objEx.printStackTrace();
            }
        }
    }

}
