package fourever.textile.customsearch.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fourever.textile.adapter.all_people_adapter;
import fourever.textile.adapter.all_people_search_popup_adapter;
import fourever.textile.adapter.all_people_search_popup_recycler_adapter;
import fourever.textile.customsearch.interfaces.onSearchActionsListener;
import fourever.textile.entity.all_people_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

/**
 * Created by shahroz on 1/8/2016.
 */
public class SearchViewResults extends RecyclerView.OnScrollListener implements AdapterView.OnItemClickListener {
    private static final int TRIGGER_SEARCH = 1;
    private static final long SEARCH_TRIGGER_DELAY_IN_MS = 400;
    private final SharedPreferences Loginprefs;
    private final String userid;
    private RecyclerView mListView;
    private String sequence;
    private int mPage;
    private SearchTask mSearch;
    private Handler mHandler;
    private boolean isLoadMore;
    private Context mContext;
    private int skipItem = 0;

    //  private ArrayAdapter mAdapter;
    private all_people_search_popup_recycler_adapter all_people_search_popup_adapter;

    private onSearchActionsListener mListener;
    private ArrayList<all_people_entity> searchList;
    private boolean FirstTimeListFill;
    private String trim;

    public SearchViewResults(Context context, String searchQuery, RecyclerView listView) {

        Loginprefs = context.getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        mContext = context;
        sequence = searchQuery;
        searchList = new ArrayList<all_people_entity>();

      /*  mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_SEARCH) {
                    clearAdapter();
                    String sequence = (String) msg.obj;
                    mSearch = new SearchTask();
                    mSearch.execute(sequence);
                }
                return false;
            }
        });*/
    }

    public void setListView(RecyclerView listView, final String trim, final String searchQuery) {

        //  mListView.addOnScrollListener(this);
        mListView = listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(linearLayoutManager);
        this.skipItem = 0;
        this.trim = trim;
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        //  Toast.makeText(mContext, String.valueOf(skipItem), Toast.LENGTH_SHORT).show();
                        new ServiceSync().execute(userid, trim, String.valueOf(skipItem), "abc");

                        // loadMore.setVisibility(View.VISIBLE);
                        //mSwipeRefreshLayout.setRefreshing(true);
                        if (totalLoadedData == totalItemCount) {

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    //  loadMore.setVisibility(View.GONE);
                                    // mSwipeRefreshLayout.setRefreshing(false);
                                    // txtNoDataFound1.setVisibility(View.GONE);
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

        // all_people_search_popup_adapter = new all_people_search_popup_adapter(mContext,searchList, R.layout.search_people_popup_item);
        new ServiceSync().execute(userid, this.trim, String.valueOf("0"), searchQuery);
        //mListView.setAdapter(all_people_search_popup_adapter);
        //updateSequence();
    }

    public void updateSequence(String s) {
       /* sequence = s;
        updateSequence();*/
    }

    private void updateSequence() {
        mPage = 0;
        isLoadMore = true;

        if (mSearch != null) {
            mSearch.cancel(false);
        }
        if (mHandler != null) {
            mHandler.removeMessages(TRIGGER_SEARCH);
        }
        if (!sequence.isEmpty()) {
            Message searchMessage = new Message();
            searchMessage.what = TRIGGER_SEARCH;
            searchMessage.obj = sequence;
            mHandler.sendMessageDelayed(searchMessage, SEARCH_TRIGGER_DELAY_IN_MS);
        } else {
            isLoadMore = false;
            clearAdapter();
        }
    }

    private void clearAdapter() {
        all_people_search_popup_adapter.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mListener.onItemClicked((String) all_people_search_popup_adapter.getItemId(position));
    }

    /*
    * Implement the Core search functionality here
    * Could be any local or remote
    */

    private ArrayList<String> findItem(String query, int page) {
        ArrayList<String> result = new ArrayList<>();
        result.add(query);
        return result;
    }

    public void setSearchProvidersListener(onSearchActionsListener listener) {
        this.mListener = listener;
    }

    /*
    * Search for the item asynchronously
    */
    private class SearchTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListener.showProgress(true);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String query = params[0];
            ArrayList<String> result = findItem(query, mPage);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            if (!isCancelled()) {
                mListener.showProgress(false);
                if (mPage == 0 && result.isEmpty()) {
                    mListener.listEmpty();
                } else {
                    all_people_search_popup_adapter.notifyDataSetChanged();
                    //  all_people_search_popup_adapter.addAll(result);
                    all_people_search_popup_adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public class ServiceSync extends AsyncTask<String, Void, String> {
        private String res;
       // ProgressDialog mProgressDialog;
        String searchQuery;

        @Override
        protected void onPreExecute() {
            res = null;
            mListener.Dialog_progressBar(true);
            /*mProgressDialog = ProgressDialog.show(mContext
                    , "",
                    "Loading...");*/
        }

        @Override
        protected String doInBackground(String... params) {
            searchQuery = params[3];
            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/all_people_search.php?user_id=" + params[0] + "&query=" + params[1] + "&skipitem=" + params[2]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {
                    mListener.Dialog_progressBar(false);
                   // mProgressDialog.dismiss();
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    // searchList.clear();
                    mListener.Dialog_progressBar(false);
                    //mProgressDialog.dismiss();
                } else {

                    //  if(FirstTimeListFill)
                    if (searchQuery.equalsIgnoreCase("searchQuery"))
                        searchList.clear();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);

                        if (json.getString("error_code").equals("0")) {
                            all_people_entity service = new all_people_entity();
                            service.setUser_id(json.getString("user_id"));
                            service.setUser_name(json.getString("user_name"));
                            service.setUser_pic(json.getString("user_pic"));
                            service.setIs_follow(json.getString("is_follow"));
                            searchList.add(service);
                        } else {
                           // Toast.makeText(mContext, json.getString("errormsg"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    all_people_search_popup_adapter = new all_people_search_popup_recycler_adapter(mContext, searchList);
                    mListView.setAdapter(all_people_search_popup_adapter);
                    all_people_search_popup_adapter.notifyDataSetChanged();
                    //  mListView.setVisibility(View.VISIBLE);
                    //  mListView.clearOnScrollListeners();
                }
                mListener.Dialog_progressBar(false);
               // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mListener.Dialog_progressBar(false);
                //mProgressDialog.dismiss();
                objEx.printStackTrace();
            }
        }
    }
}


