package fourever.textile.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;

import fourever.textile.entity.all_people_entity;
import fourever.textile.mainclasses.Followers_Profile;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

/**
 * Created by akhil on 2/2/16.
 */
public class followers_unfollow_adapter extends RecyclerView.Adapter<followers_unfollow_adapter.ViewHolder> {

    private final String userid;
    private final String u_id;

    private Context mContext;
    private List<all_people_entity> people;
    ImageLoader imageLoader;
    private String followBtnString;
    private Dialog unfollow_dialog;
    private SharedPreferences Loginprefs;
    String userType; //MainUser , FollowUser

    public followers_unfollow_adapter(Context c, List<all_people_entity> people, String userid, String userType) {
        mContext = c;
        this.people = people;
        this.userid = userid;
        this.userType = userType;

        Loginprefs = mContext.getSharedPreferences("logindetail", 0);
        u_id = Loginprefs.getString("user_id", null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_people_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public void onBindViewHolder(final followers_unfollow_adapter.ViewHolder holder, int position) {
        animate(holder);
        final all_people_entity prod = people.get(position);

        //String image_url = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic();
        //holder.peopleimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic(), imageLoader);

        if (prod.getUser_pic() == null || prod.getUser_pic().equals("")) {
            holder.default_img.setVisibility(View.VISIBLE);
            holder.default_img.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
            holder.peopleimg.setVisibility(View.GONE);
           /* holder.peopleimg.setBackground(null);
            holder.peopleimg.setImageDrawable(null);
            holder.peopleimg.setImageResource(R.drawable.maleuser);*/
            //holder.peopleimg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
        } else {
            holder.default_img.setVisibility(View.GONE);
            holder.peopleimg.setVisibility(View.VISIBLE);
            String image_url = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic();
            holder.peopleimg.setBackground(null);
            holder.peopleimg.setImageDrawable(null);
            holder.peopleimg.setImageUrl(image_url, imageLoader);
        }

        /*imageLoader.get(image_url, ImageLoader.getImageListener(
                holder.peopleimg, R.drawable.user, R.drawable.maleuser));*/

      /*  imageLoader.get(image_url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Image Load Error: " + error.getMessage());
                if(prod.getUser_pic() == null || prod.getUser_pic().equals("")) {
                    holder.peopleimg.setBackground(null);
                    holder.peopleimg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    if(prod.getUser_pic()!= null || !prod.getUser_pic().equals("")) {
                        holder.peopleimg.setBackground(null);
                        holder.peopleimg.setImageUrl(response.getRequestUrl(), imageLoader);
                    }
                }
            }
        });*/

        // Row Clicked

        holder.mainBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prod.getIs_follow().equals("4")) {
                    Intent intent = new Intent(mContext, Followers_Profile.class);
                    intent.putExtra("people_user_id", prod.getUser_id());
                    intent.putExtra("people_is_follow", prod.getIs_follow());

                /*if(prod.getIs_follow().equals("3"))
                    intent.putExtra("people_is_follow", "1");
                else if(prod.getIs_follow().equals("1"))
                    intent.putExtra("people_is_follow", "2");
                else
                    intent.putExtra("people_is_follow", prod.getIs_follow());*/

                    mContext.startActivity(intent);
                }
            }
        });

        // End Row Clicked

        followBtnString = prod.getIs_follow();  //0 = pending, 1 = proove, 2 = unfollow
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.follow.getText().toString().equalsIgnoreCase("Requested")) {

                    Drawable img = ContextCompat.getDrawable(mContext, R.drawable.follow_add);
                    holder.follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    holder.follow.setBackgroundResource(R.drawable.follow_button);
                    holder.follow.setText("Follow");
                    holder.follow.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    if(userType.equals("other_user_profile")){
                        new follow_unfollow().execute(prod.getUser_id(), u_id , "2", Uri.encode(userType));
                    }else{
                        new follow_unfollow().execute(userid, prod.getUser_id(), "2", Uri.encode(userType));
                    }
                    return;
                }

                if (holder.follow.getText().toString().equalsIgnoreCase("Follow")) {

                    holder.follow.setText("Requested");
                    holder.follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.requested, 0, 0, 0);
                    holder.follow.setBackgroundResource(R.drawable.requested_button);
                    holder.follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));

                    if(userType.equals("other_user_profile")){
                        new follow_unfollow().execute(prod.getUser_id(), u_id , "0", Uri.encode(userType));
                    }else{
                        new follow_unfollow().execute(userid, prod.getUser_id(), "0", Uri.encode(userType));
                    }
                    return;
                }

                if (holder.follow.getText().toString().equalsIgnoreCase("Following")) {

                    unfollow_dialog = new Dialog(mContext, android.R.style.Theme_Light);
                    unfollow_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    unfollow_dialog.setContentView(R.layout.unfollow_dialog);
                    unfollow_dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
                    // viewHolder.itemView.setAnimation(animAnticipateOvershoot);

                    unfollow_dialog.getWindow().getAttributes().windowAnimations = R.style.ProfileDialogAnimation;
                    unfollow_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    unfollow_dialog.show();

                    final TextView people_name = (TextView) unfollow_dialog.findViewById(R.id.people_name);
                    people_name.setText(prod.getUser_name() + " ?");
                    final NetworkImageView peopleimg = (NetworkImageView) unfollow_dialog.findViewById(R.id.peopleimg);
                    String image_url = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic();

                    imageLoader.get(image_url, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "Image Load Error: " + error.getMessage());
                            holder.peopleimg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null) {
                                // load image into imageview
                                peopleimg.setImageUrl(response.getRequestUrl(), imageLoader);
                            }
                        }
                    });

                    Button cancel = (Button) unfollow_dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unfollow_dialog.dismiss();
                        }
                    });

                    final Button unfollow = (Button) unfollow_dialog.findViewById(R.id.unfollow);
                    unfollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(userType.equals("other_user_profile")){
                                new follow_unfollow().execute(prod.getUser_id(), u_id , "3", Uri.encode(userType));
                            }else{
                                new follow_unfollow().execute(userid, prod.getUser_id(), "3", Uri.encode(userType));
                            }

                            Drawable img = ContextCompat.getDrawable(mContext, R.drawable.follow_add);
                            holder.follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                            holder.follow.setBackgroundResource(R.drawable.follow_button);
                            holder.follow.setText("Follow");
                            holder.follow.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
                            unfollow_dialog.dismiss();
                        }
                    });
                }
            }
        });

        // holder.peopleimg.setImageUrl(, imageLoader);
        holder.people_name.setText(prod.getUser_name());

        if (prod.getIs_follow().equals("4")) {
            holder.follow.setVisibility(View.GONE);
        }

        if (prod.getIs_follow().equals("3")) {
            holder.follow.setVisibility(View.VISIBLE);
            //followBtnString = "follow";
            holder.follow.setText("Following");
            holder.follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
            holder.follow.setBackgroundResource(R.drawable.following_button);
            holder.follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));
            return;
        }

        if (prod.getIs_follow().equals("0")) {
            holder.follow.setVisibility(View.VISIBLE);
            // followBtnString = "unfollow";
            holder.follow.setText("Requested");
            holder.follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.requested, 0, 0, 0);
            holder.follow.setBackgroundResource(R.drawable.requested_button);
            holder.follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));
            return;
        }

        if (prod.getIs_follow().equals("1") || prod.getIs_follow().equals("2")) {
            holder.follow.setVisibility(View.VISIBLE);
            Drawable img = ContextCompat.getDrawable(mContext, R.drawable.follow_add);
            holder.follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.follow.setBackgroundResource(R.drawable.follow_button);
            holder.follow.setText("Follow");
            holder.follow.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
            return;
        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView peopleimg;
        private final NetworkImageView default_img;
        private final TextView people_name;
        private final Button follow;
        private final LinearLayout mainBoxLayout;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            peopleimg = (NetworkImageView) itemLayoutView.findViewById(R.id.peopleimg);
            default_img = (NetworkImageView) itemLayoutView.findViewById(R.id.default_img);
            people_name = (TextView) itemLayoutView.findViewById(R.id.people_name);
            follow = (Button) itemLayoutView.findViewById(R.id.follow);
            mainBoxLayout = (LinearLayout) itemLayoutView.findViewById(R.id.mainBoxLayout);
        }
    }

    public class follow_unfollow extends AsyncTask<String, Void, String> {
        private String res;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            mProgressDialog = ProgressDialog.show(
                    mContext, "", "Loading...");
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/profile_follow_unfollow.php?user_id=" + params[0] + "&follower_id=" + params[1] + "&follow=" + params[2]+ "&userType=" + params[3]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {
                    mProgressDialog.dismiss();
                    customtoast.ShowToast(mContext, "Something went Wrong.", R.layout.red_toast);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    mProgressDialog.dismiss();
                    customtoast.ShowToast(mContext, "Something went Wrong.", R.layout.red_toast);
                } else {

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        String errorcode = json.getString("errorcode");
                        if (errorcode.equals("0")) {
                            // customtoast.ShowToast(mContext,"Following.",R.layout.red_toast);
                        } else {
                            customtoast.ShowToast(mContext, json.getString("errormsg"), R.layout.red_toast);
                        }
                    }
                }
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mProgressDialog.dismiss();
                customtoast.ShowToast(mContext, "Something went Wrong.", R.layout.red_toast);
                objEx.printStackTrace();
            }
        }
    }

}
