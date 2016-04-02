package fourever.textile.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fourever.textile.adapter.newsfeed_adapter;
import fourever.textile.entity.news_feed_entity;
import fourever.textile.entity.post_comments_entity;
import fourever.textile.entity.post_like_entity;
import fourever.textile.mainclasses.Login;
import fourever.textile.mainclasses.MainActivity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

public class newsfeed extends Fragment {

    private ImageLoader imageLoader;
    private SharedPreferences Loginprefs;
    String userid;
    RecyclerView newfeeds;
    newsfeed_adapter newsfeedAdapter;
    private TextView txtNoDataFound1;
    private Button txtrefresh;
    private FrameLayout frlyt;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<news_feed_entity> Services;
    int anInt;
    boolean isVisible = false, processVisible = false;
    private ProgressDialog mProgressDialog;
    private RelativeLayout nonewdata;
    private int skipItem = 0;
    //  private Toolbar Maintoolbar,toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfeed_design_main, null);

        // Maintoolbar = ((MainActivity) getActivity()).getToolBarLayout();
        // Maintoolbar.setTitle(R.string.newsfeed);
        // toolbar.setVisibility(View.GONE);

        imageLoader = AppController.getInstance().getImageLoader();
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        Loginprefs = getActivity().getSharedPreferences("logindetail", 0);

        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        Services = new ArrayList<news_feed_entity>();
        Services.clear();

        nonewdata = (RelativeLayout) view.findViewById(R.id.nonewdata);
        txtrefresh = (Button) view.findViewById(R.id.txtrefresh);
        frlyt = (FrameLayout) view.findViewById(R.id.frlyt);
        txtNoDataFound1 = (TextView) view.findViewById(R.id.txtNoDataFound);

        newfeeds = (RecyclerView) view.findViewById(R.id.newfeeds);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        newfeeds.setLayoutManager(gridLayoutManager);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        newfeeds.setItemAnimator(itemAnimator);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_newsfeed_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColorDark, R.color.ColorPrimaryDark, R.color.green);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                try {
                    new ServiceSync().execute(userid, String.valueOf(skipItem));
                } catch (Exception e) {
                }
            }
        });

        txtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new ServiceSync().execute(userid,String.valueOf(skipItem) );
                } catch (Exception e) {
                }
            }
        });

        mProgressDialog = new ProgressDialog(getActivity());

        if (isVisible) {
            try {
                processVisible = true;
                new ServiceSync().execute(userid, String.valueOf(skipItem));
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (anInt == 1) {
                try {
                    new ServiceSync().execute(userid, String.valueOf(skipItem));
                } catch (Exception e) {
                    //  Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        }

        /*newfeeds.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        skipItem = skipItem + 1;
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
        });*/

        /*news_feed_entity en0 = new news_feed_entity();
        en0.setTxtmessage("Create Post");
        Services.add(en0);

        news_feed_entity en = new news_feed_entity();
        en.setTxtmessage("This is best best best collection of our comapny.");
        Services.add(en);

        news_feed_entity en1 = new news_feed_entity();
        en1.setTxtmessage("This is best best best collection of our comapny. This is best best best collection of our comapny.");
        Services.add(en1);*/

       /* Snackbar snackbar = Snackbar
                .make(view.findViewById(R.id.coordinatorLayout), R.string.app_name, Snackbar.LENGTH_LONG)
                .setAction("Done", mOnClickListener);
        snackbar.setActionTextColor(Color.CYAN);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#2196F3"));//change Snackbar's background color;

        TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLUE);//change Snackbar's text color;
        snackbar.show();*/
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            setHasOptionsMenu(true);
            MainActivity.setToolbarTitle("News feed");
            MainActivity.getToolBarLayout().setVisibility(View.VISIBLE);

            processVisible = false;
            anInt = 1;
            isVisible = true;
            try {
                //  new ServiceSync().execute(userid);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
        } else {
            processVisible = false;
            anInt = 0;
            isVisible = false;
        }
    }

    public class ServiceSync extends AsyncTask<String, Void, String> {
        private String res;

        //ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            if (processVisible == true) {
                mProgressDialog.setMessage("Loading..");
                mProgressDialog.show();
            }
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
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/newsfeed.php?user_id=" + params[0] + "&skipitem=" + params[1]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null || res == " " || res.equals(" ")) {
                    if (processVisible == true) {
                        mProgressDialog.dismiss();
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
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

                    Services = new ArrayList<news_feed_entity>();
                    //  Services.clear();

                    news_feed_entity service1 = new news_feed_entity();
                    Services.add(service1);
                    newsfeedAdapter = new newsfeed_adapter(getActivity(), Services);
                    newfeeds.setAdapter(newsfeedAdapter);
                    newsfeedAdapter.notifyDataSetChanged();
                } else {
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {

                    if (processVisible == true) {
                        mProgressDialog.dismiss();
                    }

                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);
                    txtrefresh.setVisibility(View.VISIBLE);

                    //  newfeeds.setVisibility(View.GONE);
                    newfeeds.setEnabled(false);

                    txtNoDataFound1.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.GONE);

                    txtNoDataFound1.setText("No Newsfeed Found !");
                    txtNoDataFound1.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);


                } else if (jArray.length() == 1) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    frlyt.setVisibility(View.VISIBLE);
                    frlyt.setSaveEnabled(true);

                    nonewdata.setVisibility(View.VISIBLE);

                    Services = new ArrayList<news_feed_entity>();
                    Services.clear();

                    news_feed_entity service1 = new news_feed_entity();
                    Services.add(service1);

                    newsfeedAdapter = new newsfeed_adapter(getActivity(), Services);
                    newfeeds.setAdapter(newsfeedAdapter);
                    newsfeedAdapter.notifyDataSetChanged();

                } else {

                   /* if(skipItem == 0){
                        news_feed_entity service1 = new news_feed_entity();
                        Services.add(service1);
                    }*/

                    newfeeds.setVisibility(View.VISIBLE);
                    newfeeds.setEnabled(true);

                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    frlyt.setVisibility(View.GONE);
                    frlyt.setSaveEnabled(false);

                    Services = new ArrayList<news_feed_entity>();
                    Services.clear();

                    news_feed_entity service1 = new news_feed_entity();
                    Services.add(service1);

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject json = jArray.getJSONObject(i);
                        news_feed_entity service = new news_feed_entity();
                        service.setPost_id(json.getString("post_id"));
                        service.setPost_user_id(json.getString("post_userid"));
                        service.setPost_username(json.getString("post_username"));
                        service.setPost_userpic(json.getString("post_userpic"));
                        service.setPost_category(json.getString("post_category"));
                        service.setPost_description(json.getString("post_description"));
                        service.setPost_date(json.getString("post_date"));
                        service.setPost_time(json.getString("post_time"));

                        // Add post pictures
                        ArrayList<String> post_picture = new ArrayList<String>();
                        post_picture.clear();
                        JSONArray post_pictures = json.getJSONArray("post_picture");
                        for (int j = 0; j < post_pictures.length(); j++) {
                            JSONObject post_pictures_json = post_pictures.getJSONObject(j);
                            post_picture.add(post_pictures_json.getString("post_picture"));
                        }
                        service.setPost_picture(post_picture);
                        //End Add post pictures

                        // Add post Likes
                        ArrayList<post_like_entity> post_likes = new ArrayList<post_like_entity>();
                        post_likes.clear();
                        JSONArray post_like = json.getJSONArray("post_likes");
                        for (int j = 0; j < post_like.length(); j++) {
                            JSONObject post_like_json = post_like.getJSONObject(j);
                            post_like_entity post_like_entity = new post_like_entity();

                            if (!post_like_json.getString("total_likes").toString().trim().equals("0")) {
                                post_like_entity.setLikes_id(post_like_json.getString("likes_id"));
                                post_like_entity.setLikes_name(post_like_json.getString("likes_name"));
                                post_like_entity.setLikes_pic(post_like_json.getString("likes_pic"));
                                post_like_entity.setTotal_likes(post_like_json.getString("total_likes"));
                                post_likes.add(post_like_entity);
                            }
                        }
                        service.setPost_likes(post_likes);
                        //End Add post Likes

                        // Add post Comments
                        ArrayList<post_comments_entity> post_comments = new ArrayList<post_comments_entity>();
                        post_comments.clear();
                        JSONArray post_comment = json.getJSONArray("post_comments");
                        for (int j = 0; j < post_comment.length(); j++) {

                            JSONObject post_comment_json = post_comment.getJSONObject(j);
                            post_comments_entity post_comments_entity = new post_comments_entity();
                            if (!post_comment_json.getString("total_comment").toString().trim().equals("0")) {

                                post_comments_entity.setCommenter_id(post_comment_json.getString("commenter_id"));
                                post_comments_entity.setCommenter_name(post_comment_json.getString("commenter_name"));
                                post_comments_entity.setComment(post_comment_json.getString("comment"));
                                post_comments_entity.setCommenter_pic(post_comment_json.getString("commenter_pic"));
                                post_comments_entity.setCommente_date(post_comment_json.getString("commente_date"));
                                post_comments_entity.setCommente_time(post_comment_json.getString("commente_time"));
                                post_comments_entity.setTotal_comments(post_comment_json.getString("total_comment"));
                                post_comments.add(post_comments_entity);
                            }
                        }
                        service.setPost_comments(post_comments);
                        //End Add post Comments
                        Services.add(service);
                    }

                    if (Services.size() > 0) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        newsfeedAdapter = new newsfeed_adapter(getActivity(), Services);
                        newfeeds.setAdapter(newsfeedAdapter);
                        newsfeedAdapter.notifyDataSetChanged();
                    } else {

                        frlyt.setVisibility(View.VISIBLE);
                        frlyt.setSaveEnabled(true);
                        txtrefresh.setVisibility(View.VISIBLE);

                        newfeeds.setVisibility(View.GONE);
                        newfeeds.setEnabled(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setVisibility(View.GONE);

                        txtNoDataFound1.setVisibility(View.VISIBLE);
                        txtNoDataFound1.setText("No Data Found !");
                        txtNoDataFound1.setGravity(Gravity.CENTER
                                | Gravity.CENTER_HORIZONTAL);
                    }
                }
                if (processVisible == true) {
                    mProgressDialog.dismiss();
                }
                // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                if (processVisible == true) {
                    mProgressDialog.dismiss();
                }
                objEx.printStackTrace();
            }
        }
    }
}
