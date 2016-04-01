package fourever.textile.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fourever.textile.adapter.all_friend_request_adapter;
import fourever.textile.entity.all_friend_request_entity;
import fourever.textile.mainclasses.Login;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.PutUtility;

public class Friend_Requests extends Fragment {

    RecyclerView all_friends_request;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String userid;
    private SharedPreferences Loginprefs;
    private Button txtrefresh;
    private FrameLayout frlyt;
    private TextView txtNoDataFound1;
    private ArrayList<all_friend_request_entity> Services;
    private all_friend_request_adapter friend_request_adaptor;
    private int skipItem = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_request, container, false);

        txtrefresh = (Button) view.findViewById(R.id.txtrefresh);
        frlyt = (FrameLayout) view.findViewById(R.id.frlyt);
        txtNoDataFound1 = (TextView) view.findViewById(R.id.txtNoDataFound);

        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.all_friendrequest_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColorDark, R.color.ColorPrimaryDark, R.color.green);
        Services = new ArrayList<all_friend_request_entity>();
        Services.clear();

        //new ServiceSync().execute(userid,String.valueOf(0));
        new ServiceSync().execute(userid, String.valueOf(skipItem));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                try {
                    //  new ServiceSync().execute(userid,String.valueOf(0));
                    skipItem = 0;
                    new ServiceSync().execute(userid, String.valueOf(skipItem));
                } catch (Exception e) {
                }
            }
        });

        all_friends_request = (RecyclerView) view.findViewById(R.id.all_friends_request);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        all_friends_request.setLayoutManager(gridLayoutManager);

        all_friends_request.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

        } else {

        }
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
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/new_friend_request.php?user_id=" + params[0] + "&skipitem=" + params[1]);
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
                    // customtoast.ShowToast(getActivity(), "Please Check your internet connectivity.", R.layout.red_toast);
                    txtNoDataFound1.setVisibility(View.VISIBLE);
                    txtNoDataFound1.setText("No new friend request Found !");
                    txtNoDataFound1.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {

                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    txtrefresh.setVisibility(View.VISIBLE);

                    all_friends_request.setVisibility(View.GONE);
                    all_friends_request.setEnabled(false);

                    txtNoDataFound1.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);

                    txtNoDataFound1.setText("No new friend request Found !");
                    txtNoDataFound1.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                } else {

                    all_friends_request.setVisibility(View.VISIBLE);
                    all_friends_request.setEnabled(true);

                    frlyt.setVisibility(View.GONE);
                    frlyt.setSaveEnabled(false);

                    // Services.clear();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        all_friend_request_entity service = new all_friend_request_entity();
                        service.setUser_id(json.getString("user_id"));
                        service.setUser_name(json.getString("user_name"));
                        service.setUser_pic(json.getString("user_pic"));
                        Services.add(service);
                    }
                    if (Services.size() > 0) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        friend_request_adaptor = new all_friend_request_adapter(getActivity(), Services);
                        all_friends_request.setAdapter(friend_request_adaptor);
                        friend_request_adaptor.notifyDataSetChanged();
                    } else {

                        frlyt.setVisibility(View.VISIBLE);
                        frlyt.setSaveEnabled(true);
                        txtrefresh.setVisibility(View.VISIBLE);

                        all_friends_request.setVisibility(View.GONE);
                        all_friends_request.setEnabled(false);
                        mSwipeRefreshLayout.setRefreshing(false);

                        txtNoDataFound1.setVisibility(View.VISIBLE);
                        txtNoDataFound1.setText("No new friend request Found !");
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
