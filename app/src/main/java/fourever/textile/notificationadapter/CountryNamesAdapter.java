package fourever.textile.notificationadapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fourever.textile.adapter.comment_popup_adater;
import fourever.textile.adapter.newsfeed_adapter;
import fourever.textile.adapter.post_images_gallary_adapter;
import fourever.textile.entity.news_feed_entity;
import fourever.textile.entity.post_comments_entity;
import fourever.textile.entity.post_like_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.Utils;
import fourever.textile.miscs.customtoast;

public class CountryNamesAdapter extends RecyclerView.Adapter<CountryViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT = 0x00;

    private static final int LINEAR = 0;
    private final SharedPreferences Loginprefs;
    private final String userid;

    private ArrayList<LineItem> mItems;
    private ImageLoader imageLoader;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    private final Context mContext;
    private NetworkImageView postprofileimg;
    private TextView post_id;
    private TextView post_user_id;
    private TextView post_time;
    private TextView post_type;
    private TextView post_text;
    private TextView post_like_count;
    private TextView post_comment_count;
    private TextView post_like_view;
    private TextView post_comment_view;
    private TextView post_comment_view_all;
    private RecyclerView post_gallry;
    private ImageView post_like_button;
    private ImageView post_comment_button;
    private LinearLayout view_likes;
    private LinearLayout view_comments;
    private ProgressBar noti_dialog_progressBar;
    private LinearLayout mainLayout_noti;
    private TextView txt_likeunlike_tag;
    private TextView post_user_name;

    private ArrayList<post_like_entity> post_likes;
    private ArrayList<post_comments_entity> post_comments;

    public CountryNamesAdapter(Context context, int headerMode) {
        mContext = context;

        final String[] countryNames = context.getResources().getStringArray(R.array.animals);
        mHeaderDisplay = headerMode;

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        Loginprefs = mContext.getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        //Insert headers into list of items.
        /*String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < countryNames.length; i++) {

            String header = countryNames[i].substring(0, 10);
            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                //sectionManager = (sectionManager + 1) % 2;
                sectionManager = (sectionManager + 1);
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                mItems.add(new LineItem(header, true, sectionManager, sectionFirstPosition));
            }

            LineItem lineItem = new LineItem(countryNames[i], false, sectionManager, sectionFirstPosition);
            lineItem.setNoti_time("vishal bhatt" + i);
            lineItem.setNoti_user_name("Lapil raj bhandariiii..." + i);
            mItems.add(lineItem);
        }*/
    }

    public void addItems(ArrayList<LineItem> mItems) {
        this.mItems = mItems;
    }

    public boolean isItemHeader(int position) {
        return mItems.get(position).isHeader;
    }

    public String itemToString(int position) {
        return mItems.get(position).date;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.text_line_item, parent, false);
        }
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;

        /*
        if(holder != null  && holder.equals("")) {

            if (position == VIEW_TYPE_CONTENT) {
                holder.noti_user_name.setText("rocket");
                holder.noti_time.setText("testing ");
            }
        }
        */

        holder.bindItem(item.date);

        if (!item.isHeader) {
            holder.setNoti_user_name(item.getNoti_user_name());
            holder.setNoti_time(item.getNoti_time());
            holder.setNoti_message(item.getNoti_message());

            if (item.getNoti_user_pic() == null || item.getNoti_user_pic().equals("")) {
                holder.default_img.setVisibility(View.VISIBLE);
                holder.default_img.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
                holder.noti_user_pic.setVisibility(View.GONE);
           /* holder.peopleimg.setBackground(null);
            holder.peopleimg.setImageDrawable(null);
            holder.peopleimg.setImageResource(R.drawable.maleuser);*/
                //holder.peopleimg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
            } else {
                holder.default_img.setVisibility(View.GONE);
                holder.noti_user_pic.setVisibility(View.VISIBLE);
                String image_url = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + item.getNoti_user_pic();
                holder.noti_user_pic.setBackground(null);
                holder.noti_user_pic.setImageDrawable(null);
                holder.noti_user_pic.setImageUrl(image_url, imageLoader);
            }
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(mContext , item.getNoti_post_id(), Toast.LENGTH_SHORT).show();

                final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Light);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.news_feed_item_notification_dialog);
                dialog1.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                postprofileimg = (NetworkImageView) dialog1.findViewById(R.id.postprofileimg);
                post_id = (TextView) dialog1.findViewById(R.id.post_id);
                post_user_id = (TextView) dialog1.findViewById(R.id.post_user_id);
                post_user_name = (TextView) dialog1.findViewById(R.id.post_user_name);
                post_time = (TextView) dialog1.findViewById(R.id.post_time);
                post_type = (TextView) dialog1.findViewById(R.id.post_type);
                post_text = (TextView) dialog1.findViewById(R.id.post_text);

                post_like_count = (TextView) dialog1.findViewById(R.id.post_like_count);
                post_comment_count = (TextView) dialog1.findViewById(R.id.post_comment_count);
                post_like_view = (TextView) dialog1.findViewById(R.id.post_like_view);
                post_comment_view = (TextView) dialog1.findViewById(R.id.post_comment_view);
                post_comment_view_all = (TextView) dialog1.findViewById(R.id.post_comment_view_all);

                post_gallry = (RecyclerView) dialog1.findViewById(R.id.post_gallry);
                LinearLayoutManager post_gallarylayoutManager
                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                post_gallry.setLayoutManager(post_gallarylayoutManager);

                noti_dialog_progressBar = (ProgressBar) dialog1.findViewById(R.id.noti_dialog_progressBar);

                txt_likeunlike_tag = (TextView) dialog1.findViewById(R.id.txt_likeunlike_tag);
                post_like_button = (ImageView) dialog1.findViewById(R.id.post_like_button);
                post_comment_button = (ImageView) dialog1.findViewById(R.id.post_comment_button);

                view_likes = (LinearLayout) dialog1.findViewById(R.id.view_likes);
                view_comments = (LinearLayout) dialog1.findViewById(R.id.view_comments);
                mainLayout_noti = (LinearLayout) dialog1.findViewById(R.id.mainLayout_noti);

                ImageView imgclose = (ImageView) dialog1.findViewById(R.id.imgclose);
                imgclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                new ServiceSync().execute(item.getNoti_post_id());

                view_likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final RecyclerView comment_dialog_recycler;
                        comment_popup_adater commentAdapter = null;

                        final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Light);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.commentdialog);
                        dialog1.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        ImageView Imgclose = (ImageView) dialog1.findViewById(R.id.closedialog);
                        TextView comment_header_divider1 = (TextView) dialog1.findViewById(R.id.comment_header_divider1);
                        comment_header_divider1.setVisibility(View.GONE);
                        LinearLayout add_cooment = (LinearLayout) dialog1.findViewById(R.id.add_cooment);
                        add_cooment.setVisibility(View.GONE);

                        TextView title = (TextView) dialog1.findViewById(R.id.clientnamedialog);
                        title.setText("Likes");

                        Imgclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        final TextView txtNoDataFound_comment = (TextView) dialog1.findViewById(R.id.txtNoDataFound_comment);
                        comment_dialog_recycler = (RecyclerView) dialog1.findViewById(R.id.comment_dialog_recycler);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
                        comment_dialog_recycler.setLayoutManager(gridLayoutManager);

                        if (post_likes.size() == 0) {
                            comment_dialog_recycler.setVisibility(View.GONE);
                            txtNoDataFound_comment.setVisibility(View.VISIBLE);
                        } else {
                            comment_dialog_recycler.setVisibility(View.VISIBLE);
                            txtNoDataFound_comment.setVisibility(View.GONE);
                        }

                        commentAdapter = new comment_popup_adater(mContext, post_comments, post_likes, 1);
                        comment_dialog_recycler.setAdapter(commentAdapter);
                        commentAdapter.notifyDataSetChanged();
                        dialog1.show();
                    }
                });

                view_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewComment();
                    }
                });

                post_comment_view_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewComment();
                    }
                });


                dialog1.show();

            }
        });

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.isHeader) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        // lp.setSlm(item.sectionManager == LINEAR ? LinearSLM.ID : GridSLM.ID);
        lp.setSlm(LinearSLM.ID);
        lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }

    public void ViewComment(){

        final RecyclerView comment_dialog_recycler;
        comment_popup_adater commentAdapter = null;

        final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Light);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View vw = LayoutInflater.from(mContext).inflate(R.layout.commentdialog, null, false);
        dialog1.setContentView(vw);
        dialog1.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout add_cooment = (LinearLayout) dialog1.findViewById(R.id.add_cooment);

        ImageView Imgclose = (ImageView) dialog1.findViewById(R.id.closedialog);
        TextView title = (TextView) dialog1.findViewById(R.id.clientnamedialog);
        title.setText("Comments");

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
                                        dialog1.dismiss();
                                    }
                                })
                                .start();
                    }
                }


            }
        });

        // comment_dialog_recycler

        final TextView txtNoDataFound_comment = (TextView) dialog1.findViewById(R.id.txtNoDataFound_comment);
        comment_dialog_recycler = (RecyclerView) dialog1.findViewById(R.id.comment_dialog_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        comment_dialog_recycler.setLayoutManager(gridLayoutManager);

        final ArrayList<post_comments_entity> cmt_array = post_comments;

        if (post_comments.size() == 0) {
            comment_dialog_recycler.setVisibility(View.GONE);
            txtNoDataFound_comment.setVisibility(View.VISIBLE);
        } else {
            comment_dialog_recycler.setVisibility(View.VISIBLE);
            txtNoDataFound_comment.setVisibility(View.GONE);
        }

        commentAdapter = new comment_popup_adater(mContext, cmt_array, post_likes, 0);
        comment_dialog_recycler.setAdapter(commentAdapter);
        if (cmt_array.size() > 0) {
            comment_dialog_recycler.smoothScrollToPosition(cmt_array.size() - 1);
        }
        commentAdapter.notifyDataSetChanged();

        final EditText comment_box = (EditText) dialog1.findViewById(R.id.comment_box);
        ImageView comment_send = (ImageView) dialog1.findViewById(R.id.comment_send);
        comment_box.setVisibility(View.GONE);
        comment_send.setVisibility(View.GONE);

        dialog1.show();

    }


    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();

    }

    public void notifyHeaderChanges() {
        for (int i = 0; i < mItems.size(); i++) {
            LineItem item = mItems.get(i);
            if (item.isHeader) {
                notifyItemChanged(i);
            }
        }
    }

    public class ServiceSync extends AsyncTask<String, Void, String> {
        private String res;

        //ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            noti_dialog_progressBar.setVisibility(View.VISIBLE);
            mainLayout_noti.setVisibility(View.GONE);
           /* mProgressDialog = ProgressDialog.show(
                    getActivity(), "Loading...",
                    "Loading Ongoing cases.");*/
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/notification_newsfeed.php?post_id=" + params[0]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null || res == " " || res.equals(" ")) {
                    noti_dialog_progressBar.setVisibility(View.GONE);
                    mainLayout_noti.setVisibility(View.GONE);
                }

                JSONArray jArray = new JSONArray(res.toString());

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json = jArray.getJSONObject(i);
                    news_feed_entity service = new news_feed_entity();
                    post_id.setText(json.getString("post_id"));
                    post_user_id.setText(json.getString("post_userid"));
                    post_user_name.setText(json.getString("post_username"));
                    post_time.setText(json.getString("post_date") + " " + json.getString("post_time"));
                    post_type.setText(json.getString("post_category"));
                    post_text.setText(json.getString("post_description"));
                    postprofileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + json.getString("post_userpic"), imageLoader);
                   /* service.setPost_id(json.getString("post_id"));
                    service.setPost_user_id(json.getString("post_userid"));
                    service.setPost_username(json.getString("post_username"));
                    service.setPost_userpic(json.getString("post_userpic"));
                    service.setPost_category(json.getString("post_category"));
                    service.setPost_description(json.getString("post_description"));
                    service.setPost_date(json.getString("post_date"));
                    service.setPost_time(json.getString("post_time"));*/

                    // Add post pictures
                    ArrayList<String> post_picture = new ArrayList<String>();
                    post_picture.clear();
                    JSONArray post_pictures = json.getJSONArray("post_picture");
                    for (int j = 0; j < post_pictures.length(); j++) {
                        JSONObject post_pictures_json = post_pictures.getJSONObject(j);
                        post_picture.add(post_pictures_json.getString("post_picture"));
                    }
                    service.setPost_picture(post_picture);

                    if (post_picture.size() > 0) {
                        if (post_picture.get(0) != "null" || !post_picture.get(0).equals("null")) {
                            post_gallry.setVisibility(View.VISIBLE);
                            post_images_gallary_adapter post_images_gallary_adapter = new post_images_gallary_adapter(mContext, post_picture);
                            post_gallry.setAdapter(post_images_gallary_adapter);
                            post_images_gallary_adapter.notifyDataSetChanged();
                        } else {
                            post_gallry.setVisibility(View.GONE);
                        }
                    }
                    //End Add post pictures


                    // Add post Likes
                    post_likes = new ArrayList<post_like_entity>();
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
                    post_like_count.setText(String.valueOf(post_likes.size()));
                    post_like_view.setText(String.valueOf(post_likes.size()));

                    //final ArrayList<post_like_entity> post_like_entities = prod.getPost_likes();

                    if (post_likes.size() == 0) {
                        String likeunlike_tag = txt_likeunlike_tag.getText().toString();
                        if (likeunlike_tag.equals("like")) {
                            post_like_button.setImageResource(R.drawable.like);
                            txt_likeunlike_tag.setText("like");

                        } else {
                            post_like_button.setImageResource(R.drawable.unlike);
                            txt_likeunlike_tag.setText("unlike");
                        }
                    }

                    post_like_button.setImageResource(R.drawable.unlike);
                    txt_likeunlike_tag.setText("unlike");

                    for (int j = 0; j < post_likes.size(); j++) {
                        if (post_likes.get(j).getLikes_id().equals(userid)) {
                            post_like_button.setImageResource(R.drawable.like);
                            txt_likeunlike_tag.setText("like");
                        }
                    }
                    //End Add post Likes

                    // Add post Comments
                    post_comments = new ArrayList<post_comments_entity>();
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
                    post_comment_count.setText(String.valueOf(post_comments.size()));
                    post_comment_view.setText(String.valueOf(post_comments.size()));
                    //End Add post Comments
                    //  Services.add(service);
                    noti_dialog_progressBar.setVisibility(View.GONE);
                    mainLayout_noti.setVisibility(View.VISIBLE);
                }

                noti_dialog_progressBar.setVisibility(View.GONE);
                // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mainLayout_noti.setVisibility(View.GONE);
                noti_dialog_progressBar.setVisibility(View.GONE);
                objEx.printStackTrace();
            }
        }
    }
    
    
   /* private static class LineItem {

        public int sectionManager;
        public int sectionFirstPosition;
        public boolean isHeader;

        public String date;
        public String noti_user_name;
        public String noti_time;

        public LineItem() { }

        public LineItem(String date, boolean isHeader, int sectionManager,
                int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.date = date;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
        }

        public String getNoti_user_name() {
            return noti_user_name;
        }

        public void setNoti_user_name(String noti_user_name) {
            this.noti_user_name = noti_user_name;
        }

        public String getNoti_time() {
            return noti_time;
        }

        public void setNoti_time(String noti_time) {
            this.noti_time = noti_time;
        }
    }*/
}
