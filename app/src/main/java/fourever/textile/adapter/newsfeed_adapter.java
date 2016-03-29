package fourever.textile.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fourever.textile.entity.news_feed_entity;
import fourever.textile.entity.post_comments_entity;
import fourever.textile.entity.post_like_entity;
import fourever.textile.mainclasses.Create_NewsFeed;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.Utils;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

public class newsfeed_adapter extends RecyclerView.Adapter<newsfeed_adapter.ViewHolder> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private final String userid, username, userimg;
    private Context mContext;
    private List<news_feed_entity> news_feed;
    com.android.volley.toolbox.ImageLoader imageLoader;
    private SharedPreferences Loginprefs;
    int sdk;
    LinearLayoutManager linearLayoutManager;

    private int drawingStartLocation;

    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    public newsfeed_adapter(Context c, List<news_feed_entity> news_feed) {
        sdk = android.os.Build.VERSION.SDK_INT;
        mContext = c;

        drawingStartLocation = 0;

        this.news_feed = news_feed;
        Loginprefs = mContext.getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        username = Loginprefs.getString("user_name", null);
        userimg = Loginprefs.getString("user_img", null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        // clearCache();
    }

    @Override
    public int getItemCount() {
        return news_feed.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else if (position >= 1) {
            return 1;
        } else {
            return 1;
        }
    }

    public news_feed_entity getItem(int position) {
        return news_feed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView profileimg;
        private final TextView txtname;
        private final NetworkImageView postprofileimg;

        private final TextView post_id, post_user_name, post_user_id, post_time, post_type, post_text;
        private final LinearLayout post_header;

        //Like and comment view
        private final ImageView post_like_button, post_comment_button;
        private final TextView post_like_count, post_comment_count;
        private final TextView post_like_view, post_comment_view;
        private final LinearLayout view_likes;

        //Comments View
        private final NetworkImageView post_commenter_pic1, post_commenter_pic2;
        private final TextView post_commenter_name1, post_comment_by1;
        private final TextView post_commenter_name2, post_comment_by2;
        private final TextView post_comment_view_all;
        private final LinearLayout linear_post_comment1, linear_post_comment2;
        private final TextView txt_likeunlike_tag;
        private final LinearLayout view_comments;

        RecyclerView post_gallry;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            //Layout 1 views
            txtname = (TextView) itemLayoutView.findViewById(R.id.txtname);
            profileimg = (NetworkImageView) itemLayoutView.findViewById(R.id.profileimg);

            //Layout 2 Views
            post_header = (LinearLayout) itemLayoutView.findViewById(R.id.post_header);
            postprofileimg = (NetworkImageView) itemLayoutView.findViewById(R.id.postprofileimg);
            post_id = (TextView) itemLayoutView.findViewById(R.id.post_id);
            post_user_id = (TextView) itemLayoutView.findViewById(R.id.post_user_id);
            post_user_name = (TextView) itemLayoutView.findViewById(R.id.post_user_name);
            post_time = (TextView) itemLayoutView.findViewById(R.id.post_time);
            post_type = (TextView) itemLayoutView.findViewById(R.id.post_type);
            post_text = (TextView) itemLayoutView.findViewById(R.id.post_text);

            //Like and comment view
            post_like_button = (ImageView) itemLayoutView.findViewById(R.id.post_like_button);
            txt_likeunlike_tag = (TextView) itemLayoutView.findViewById(R.id.txt_likeunlike_tag);
            post_comment_button = (ImageView) itemLayoutView.findViewById(R.id.post_comment_button);
            post_like_count = (TextView) itemLayoutView.findViewById(R.id.post_like_count);
            post_comment_count = (TextView) itemLayoutView.findViewById(R.id.post_comment_count);
            post_like_view = (TextView) itemLayoutView.findViewById(R.id.post_like_view);
            post_comment_view = (TextView) itemLayoutView.findViewById(R.id.post_comment_view);

            linear_post_comment1 = (LinearLayout) itemLayoutView.findViewById(R.id.linear_post_comment1);
            linear_post_comment2 = (LinearLayout) itemLayoutView.findViewById(R.id.linear_post_comment2);
            view_likes = (LinearLayout) itemLayoutView.findViewById(R.id.view_likes);

            //Comments View

            post_commenter_pic1 = (NetworkImageView) itemLayoutView.findViewById(R.id.post_commenter_pic1);
            post_commenter_name1 = (TextView) itemLayoutView.findViewById(R.id.post_commenter_name1);
            post_comment_by1 = (TextView) itemLayoutView.findViewById(R.id.post_comment_by1);

            post_commenter_pic2 = (NetworkImageView) itemLayoutView.findViewById(R.id.post_commenter_pic2);
            post_commenter_name2 = (TextView) itemLayoutView.findViewById(R.id.post_commenter_name2);
            post_comment_by2 = (TextView) itemLayoutView.findViewById(R.id.post_comment_by2);

            post_comment_view_all = (TextView) itemLayoutView.findViewById(R.id.post_comment_view_all);
            view_comments = (LinearLayout) itemLayoutView.findViewById(R.id.view_comments);
            post_gallry = (RecyclerView) itemLayoutView.findViewById(R.id.post_gallry);
        }
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                view.animate()
                        .translationY(0).alpha(1.f)
                        .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                        .setInterpolator(new DecelerateInterpolator(2.f))
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationsLocked = true;
                            }
                        })
                        .start();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newsfeed_design_main_head, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        } else {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_feed_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // runEnterAnimation(holder.itemView, position);

        try {
            if (position == 0) { // Layout 1
                holder.txtname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, Create_NewsFeed.class);
                        mContext.startActivity(intent);
                    }
                });

                holder.profileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + Loginprefs.getString("user_img", null), imageLoader);
            }

            if (position >= 1) { //Layout 2

                animate(holder);
                final news_feed_entity prod = news_feed.get(position);
                holder.post_id.setText(prod.getPost_id());
                holder.post_user_id.setText(prod.getPost_user_id());
                holder.post_user_name.setText(prod.getPost_username());
                holder.post_time.setText(prod.getPost_date() + " " + prod.getPost_time());
                holder.post_type.setText(prod.getPost_category());
                holder.post_text.setText(prod.getPost_description());
                holder.postprofileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_userpic(), imageLoader);

                //Start like comment view
                holder.post_like_count.setText(String.valueOf(prod.getPost_likes().size()));
                holder.post_comment_count.setText(String.valueOf(prod.getPost_comments().size()));

                holder.post_like_view.setText(String.valueOf(prod.getPost_likes().size()));
                holder.post_comment_view.setText(String.valueOf(prod.getPost_comments().size()));

                // Main post images gallary
                LinearLayoutManager post_gallarylayoutManager
                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.post_gallry.setLayoutManager(post_gallarylayoutManager);

                if (prod.getPost_picture().size() > 0) {
                    if(prod.getPost_picture().get(0) != "null" || !prod.getPost_picture().get(0).equals("null")){
                        holder.post_gallry.setVisibility(View.VISIBLE);
                        post_images_gallary_adapter post_images_gallary_adapter = new post_images_gallary_adapter(mContext, prod.getPost_picture());
                        holder.post_gallry.setAdapter(post_images_gallary_adapter);
                        post_images_gallary_adapter.notifyDataSetChanged();
                    }else {
                        holder.post_gallry.setVisibility(View.GONE);
                    }
                }

                // End Main post images gallary
                //Main post Comments Display
                if (prod.getPost_comments().size() == 1) {
                    holder.linear_post_comment2.setVisibility(View.VISIBLE);

                    if (userid.equals(prod.getPost_comments().get(prod.getPost_comments().size() - 1).getCommenter_id())) {
                        holder.post_commenter_name2.setText("You");
                    } else {
                        holder.post_commenter_name2.setText(prod.getPost_comments().get(prod.getPost_comments().size() - 1).getCommenter_name());
                    }

                    holder.post_comment_by2.setText(prod.getPost_comments().get(prod.getPost_comments().size() - 1).getComment());
                    holder.post_commenter_pic2.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_comments().get(prod.getPost_comments().size() - 1).getCommenter_pic(), imageLoader);
                }

                if (prod.getPost_comments().size() >= 2) {
                    //1nd Comment
                    holder.linear_post_comment2.setVisibility(View.VISIBLE);
                    //holder.post_commenter_name1.setText(prod.getPost_comments().get(0).getCommenter_name());
                    if (userid.equals(prod.getPost_comments().get(prod.getPost_comments().size() - 1).getCommenter_id())) {
                        holder.post_commenter_name2.setText("You");
                    } else {
                        holder.post_commenter_name2.setText(prod.getPost_comments().get(prod.getPost_comments().size() - 1).getCommenter_name());
                    }

                    holder.post_comment_by2.setText(prod.getPost_comments().get(prod.getPost_comments().size() - 1).getComment());
                    holder.post_commenter_pic2.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_comments().get(prod.getPost_comments().size() - 1).getCommenter_pic(), imageLoader);

                    //2nd Comment
                    holder.linear_post_comment1.setVisibility(View.VISIBLE);
                    if (userid.equals(prod.getPost_comments().get(prod.getPost_comments().size() - 2).getCommenter_id())) {
                        holder.post_commenter_name1.setText("You");
                    } else {
                        holder.post_commenter_name1.setText(prod.getPost_comments().get(prod.getPost_comments().size() - 2).getCommenter_name());
                    }
                    //holder.post_commenter_name2.setText(prod.getPost_comments().get(1).getCommenter_name());
                    holder.post_comment_by1.setText(prod.getPost_comments().get(prod.getPost_comments().size() - 2).getComment());
                    holder.post_commenter_pic1.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_comments().get(prod.getPost_comments().size() - 2).getCommenter_pic(), imageLoader);
                }

                //End Main post Comments Display

                /*if (prod.getPost_comments().size() == 1) {
                    holder.linear_post_comment1.setVisibility(View.VISIBLE);
                    if (userid.equals(prod.getPost_comments().get(0).getCommenter_id())) {
                        holder.post_commenter_name1.setText("You");
                    } else {
                        holder.post_commenter_name1.setText(prod.getPost_comments().get(0).getCommenter_name());
                    }
                    holder.post_comment_by1.setText(prod.getPost_comments().get(0).getComment());
                    holder.post_commenter_pic1.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_comments().get(0).getCommenter_pic(), imageLoader);
                }

                if (prod.getPost_comments().size() >= 2) {
                    holder.linear_post_comment1.setVisibility(View.VISIBLE);
                    //holder.post_commenter_name1.setText(prod.getPost_comments().get(0).getCommenter_name());
                    if (userid.equals(prod.getPost_comments().get(0).getCommenter_id())) {
                        holder.post_commenter_name1.setText("You");
                    } else {
                        holder.post_commenter_name1.setText(prod.getPost_comments().get(0).getCommenter_name());
                    }

                    holder.post_comment_by1.setText(prod.getPost_comments().get(0).getComment());
                    holder.post_commenter_pic1.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_comments().get(0).getCommenter_pic(), imageLoader);

                    holder.linear_post_comment2.setVisibility(View.VISIBLE);
                    if (userid.equals(prod.getPost_comments().get(1).getCommenter_id())) {
                        holder.post_commenter_name2.setText("You");
                    } else {
                        holder.post_commenter_name2.setText(prod.getPost_comments().get(1).getCommenter_name());
                    }
                    //holder.post_commenter_name2.setText(prod.getPost_comments().get(1).getCommenter_name());
                    holder.post_comment_by2.setText(prod.getPost_comments().get(1).getComment());
                    holder.post_commenter_pic2.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getPost_comments().get(1).getCommenter_pic(), imageLoader);
                }*/

                //End like comment view

                // Give Like

                final ArrayList<post_like_entity> post_like_entities = prod.getPost_likes();

                if (post_like_entities.size() == 0) {
                    String likeunlike_tag = holder.txt_likeunlike_tag.getText().toString();
                    if (likeunlike_tag.equals("like")) {
                        holder.post_like_button.setImageResource(R.drawable.like);
                        holder.txt_likeunlike_tag.setText("like");

                    } else {
                        holder.post_like_button.setImageResource(R.drawable.unlike);
                        holder.txt_likeunlike_tag.setText("unlike");
                    }
                }

                holder.post_like_button.setImageResource(R.drawable.unlike);
                holder.txt_likeunlike_tag.setText("unlike");

                for (int i = 0; i < post_like_entities.size(); i++) {
                    if (post_like_entities.get(i).getLikes_id().equals(userid)) {
                        /*Drawable background = ContextCompat.getDrawable(mContext, R.drawable.like);
                        int primaryColor = ContextCompat.getColor(mContext, R.color.primaryColorDark);
                        background.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);

                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            holder.post_like_button.setBackgroundDrawable(background);
                        } else {
                            holder.post_like_button.setBackground(background);
                        }*/
                        holder.post_like_button.setImageResource(R.drawable.like);
                        holder.txt_likeunlike_tag.setText("like");

                    } /*else {
                        holder.post_like_button.setImageResource(R.drawable.unlike);
                        holder.txt_likeunlike_tag.setText("unlike");
                        *//*Drawable background = ContextCompat.getDrawable(mContext, R.drawable.like);
                        int primaryColor = ContextCompat.getColor(mContext, R.color.ltgrey);
                        background.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);

                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            holder.post_like_button.setBackgroundDrawable(background);
                        } else {
                            holder.post_like_button.setBackground(background);
                        }*//*
                        // holder.post_like_button.setImageResource(R.drawable.like);
                    }*/
                }


                holder.post_like_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String likeunlike_tag = holder.txt_likeunlike_tag.getText().toString();

                        if (likeunlike_tag.equals("like")) {
                            holder.txt_likeunlike_tag.setText("unlike");
                            // holder.post_like_button.setImageResource(R.drawable.unlike);
                            ImageViewAnimatedChange(mContext, holder.post_like_button, R.drawable.unlike);

                            final ArrayList<post_like_entity> likes_array = prod.getPost_likes();

                            for(int i=0;i<=prod.getPost_likes().size()-1;i++){
                                String likers_id = prod.getPost_likes().get(i).getLikes_id();
                                if(userid.equals(likers_id)){
                                    likes_array.remove(i);
                                }
                            }

                            prod.setPost_likes(likes_array);
                            notifyDataSetChanged();
                            notifyItemChanged(position);
                          //  int total_like = Integer.parseInt(prod.getPost_likes().get(position).getTotal_likes()) + 1;
                         //   post_likes_entity.setTotal_likes(String.valueOf(total_like));
                          //  likes_array.add(post_likes_entity);
                            //post_likes_entity.setTotal_likes(prod.getPost_likes().get(position).getTotal_likes() + 1);

                          //  prod.setPost_likes(likes_array);

                            new like_unlike().execute(prod.getPost_id(), userid);

                        }

                        if (likeunlike_tag.equals("unlike")) {
                            holder.txt_likeunlike_tag.setText("like");
                            // holder.post_like_button.setImageResource(R.drawable.like);
                            ImageViewAnimatedChange(mContext, holder.post_like_button, R.drawable.like);

                            //Animation start
                            AnimatorSet animatorSet = new AnimatorSet();

                            ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.post_like_button, "rotation", 0f, 360f);
                            rotationAnim.setDuration(300);
                            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.post_like_button, "scaleX", 0.2f, 1f);
                            bounceAnimX.setDuration(300);
                            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.post_like_button, "scaleY", 0.2f, 1f);
                            bounceAnimY.setDuration(300);
                            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    holder.post_like_button.setImageResource(R.drawable.like);
                                }
                            });

                            animatorSet.play(rotationAnim);
                            animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                            animatorSet.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    // resetLikeAnimationState(holder);
                                }
                            });

                            animatorSet.start();

                            //Animation End

                            final ArrayList<post_like_entity> likes_array = prod.getPost_likes();
                            post_like_entity post_likes_entity = new post_like_entity();
                            post_likes_entity.setLikes_id(userid);
                            post_likes_entity.setLikes_name(username);
                            post_likes_entity.setLikes_pic(userimg);
                            post_likes_entity.setTotal_likes("");
                            likes_array.add(post_likes_entity);

                            prod.setPost_likes(likes_array);

                            notifyDataSetChanged();
                            notifyItemChanged(position);

                         //   int total_like = Integer.parseInt(prod.getPost_likes().get(position).getTotal_likes()) - 1;
                          //  post_likes_entity.setTotal_likes(String.valueOf(total_like));
                          //  likes_array.remove(post_likes_entity);
                            new like_unlike().execute(prod.getPost_id(), userid);
                        }
                    }
                });
                //End Give Like

                holder.view_likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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

                        if(prod.getPost_likes().size() == 0){
                            comment_dialog_recycler.setVisibility(View.GONE);
                            txtNoDataFound_comment.setVisibility(View.VISIBLE);
                        }else{
                            comment_dialog_recycler.setVisibility(View.VISIBLE);
                            txtNoDataFound_comment.setVisibility(View.GONE);
                        }

                        commentAdapter = new comment_popup_adater(mContext,  prod.getPost_comments(), prod.getPost_likes(), 1);
                        comment_dialog_recycler.setAdapter(commentAdapter);
                        commentAdapter.notifyDataSetChanged();
                        dialog1.show();
                    }
                });

                //  Comment Dialog
                holder.post_comment_view_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int[] startingLocation = new int[2];
                        view.getLocationOnScreen(startingLocation);
                        showComments(prod, position,startingLocation[1] );
                    }
                });

                holder.post_comment_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int[] startingLocation = new int[2];
                        view.getLocationOnScreen(startingLocation);
                        showComments(prod, position,startingLocation[1]);
                    }
                });
                holder.view_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int[] startingLocation = new int[2];
                        view.getLocationOnScreen(startingLocation);
                        showComments(prod, position , startingLocation[1]);
                    }
                });
                //End Comment Dialog

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showComments(final news_feed_entity prod, final int position, final int dialogOpenLocation){

         /*ListView comment_dialog_recycler;
                        comment_popup commentAdapter;*/
        final RecyclerView comment_dialog_recycler;
        comment_popup_adater commentAdapter = null;

        final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Light);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View vw = LayoutInflater.from(mContext).inflate(R.layout.commentdialog,null,false);
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

       /* comment_dialog_recycler = (ListView) dialog1.findViewById(R.id.newfeeds);
        commentAdapter = new comment_popup(activity, prod.getPost_comments(),R.layout.comment_popup_item);
        comment_dialog_recycler.setAdapter(commentAdapter);*/

        final TextView txtNoDataFound_comment = (TextView) dialog1.findViewById(R.id.txtNoDataFound_comment);
        comment_dialog_recycler = (RecyclerView) dialog1.findViewById(R.id.comment_dialog_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        comment_dialog_recycler.setLayoutManager(gridLayoutManager);

        final ArrayList<post_comments_entity> cmt_array = prod.getPost_comments();

        if(prod.getPost_comments().size() == 0){
            comment_dialog_recycler.setVisibility(View.GONE);
            txtNoDataFound_comment.setVisibility(View.VISIBLE);
        }else{
            comment_dialog_recycler.setVisibility(View.VISIBLE);
            txtNoDataFound_comment.setVisibility(View.GONE);
        }

        commentAdapter = new comment_popup_adater(mContext, cmt_array, prod.getPost_likes(),0);
        comment_dialog_recycler.setAdapter(commentAdapter);
        if(cmt_array.size() > 0) {
            comment_dialog_recycler.smoothScrollToPosition(cmt_array.size() - 1);
        }
        commentAdapter.notifyDataSetChanged();

        final EditText comment_box = (EditText) dialog1.findViewById(R.id.comment_box);
        ImageView comment_send = (ImageView) dialog1.findViewById(R.id.comment_send);
        final comment_popup_adater finalCommentAdapter = commentAdapter;
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = comment_box.getText().toString().trim();
                if (!comment.equals("")) {

                    comment_dialog_recycler.setVisibility(View.VISIBLE);
                    txtNoDataFound_comment.setVisibility(View.GONE);

                    post_comments_entity post_comments_entity = new post_comments_entity();
                    post_comments_entity.setCommenter_id(userid);
                    post_comments_entity.setCommenter_name(username);
                    post_comments_entity.setCommenter_pic(userimg);
                    post_comments_entity.setComment(comment);
                    post_comments_entity.setCommente_date("");
                    post_comments_entity.setCommente_time("");
                    cmt_array.add(post_comments_entity);

                    comment_dialog_recycler.smoothScrollToPosition(cmt_array.size() - 1);
                    finalCommentAdapter.notifyDataSetChanged();
                    notifyItemChanged(position);

                    new add_comment().execute(prod.getPost_id(), userid, comment);
                    comment_box.setText("");
                } else {
                    customtoast.ShowToast(mContext, "Please enter something.", R.layout.red_toast);
                }
            }
        });

        startIntroAnimation(vw, dialog1, add_cooment , dialogOpenLocation);
        //dialog1.show();
        animateContent(add_cooment);
    }

    private void startIntroAnimation(View contentRoot, Dialog d, final LinearLayout add_cooment, int dialogOpenLocation) {
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(dialogOpenLocation);
        add_cooment.setTranslationY(100);
        d.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                contentRoot.animate()
                        .scaleY(1)
                        .setDuration(500)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animateContent(add_cooment);
                            }
                        })
                        .start();
            }
        }
    }

    private void animateContent(LinearLayout add_cooment) {
        //notifyDataSetChanged();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                add_cooment.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(1000)
                        .start();
            }
        }
    }

    private class like_unlike extends AsyncTask<String, Void, String> {

        private String res;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/post_like_unlike.php?post_id=" + params[0] + "&user_id=" + params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        protected void onPostExecute(String res) {
            try {
                JSONArray jArray = new JSONArray(res.toString());
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json = jArray.getJSONObject(i);
                    String errorcode = json.getString("errorcode");

                    if (errorcode.equals("0")) {
                        // customtoast.ShowToast(mContext, "Like", R.layout.blue_toast);
                    } else {
                        customtoast.ShowToast(mContext, json.getString("errormsg"), R.layout.red_toast);
                    }
                }

                //  mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    private class add_comment extends AsyncTask<String, Void, String> {

        private String res;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/add_comment.php?post_id=" + params[0] + "&user_id=" + params[1] + "&comment=" + Uri.encode(params[2]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        protected void onPostExecute(String res) {
            try {
                JSONArray jArray = new JSONArray(res.toString());
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    String errorcode = json.getString("errorcode");
                    if (errorcode.equals("0")) {
                        // customtoast.ShowToast(mContext, "Like", R.layout.blue_toast);
                    } else {
                        customtoast.ShowToast(mContext, json.getString("errormsg"), R.layout.red_toast);
                    }
                }

                //  mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        /*final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);*/
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.right_to_left);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.left_to_right);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
