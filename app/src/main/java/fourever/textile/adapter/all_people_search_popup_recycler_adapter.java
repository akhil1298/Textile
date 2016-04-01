package fourever.textile.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

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
public class all_people_search_popup_recycler_adapter extends RecyclerView.Adapter<all_people_search_popup_recycler_adapter.ViewHolder> {

    private final SharedPreferences Loginprefs;
    private final String userid;
    private Context mContext;
    private LayoutInflater inflater;
    private List<all_people_entity> people;
    ImageLoader imageLoader;
    private Dialog unfollow_dialog;

    public all_people_search_popup_recycler_adapter(Context c, List<all_people_entity> people) {

        mContext = c;
        this.people = people;

        Loginprefs = mContext.getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        // clearCache();
    }

    public void clear() {
        people.clear();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_people_popup_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final all_people_search_popup_recycler_adapter.ViewHolder holder, int position) {

        final all_people_entity prod = people.get(position);
        holder.txt_name.setText(prod.getUser_name());
        holder.profileimg.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + prod.getUser_pic(), imageLoader);

        holder.mainBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Followers_Profile.class);
                intent.putExtra("people_user_id", prod.getUser_id());
                intent.putExtra("people_is_follow", prod.getIs_follow());
                mContext.startActivity(intent);
            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.follow_label.getText().toString().equalsIgnoreCase("Requested")) {
                    holder.follow.setBackgroundResource(R.drawable.follow_button_popup);
                    holder.follow_label.setText("Follow");
                    new follow_unfollow().execute(userid, prod.getUser_id(), "2");

                    return;
                }

                if (holder.follow_label.getText().toString().equalsIgnoreCase("Follow")) {

                    holder.follow_label.setText("Requested");
                    holder.follow.setBackgroundResource(R.drawable.requested_button_popup);
                    new follow_unfollow().execute(userid, prod.getUser_id(), "0");
                    return;
                }

                if (holder.follow_label.getText().toString().equalsIgnoreCase("Following")) {

                    unfollow_dialog = new Dialog(mContext, android.R.style.Theme_Light);
                    unfollow_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    unfollow_dialog.setContentView(R.layout.unfollow_dialog);
                    unfollow_dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    unfollow_dialog.getWindow().getAttributes().windowAnimations = R.style.ProfileDialogAnimation;
                    unfollow_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    unfollow_dialog.show();

                    final TextView people_name = (TextView) unfollow_dialog.findViewById(R.id.people_name);
                    people_name.setText(prod.getUser_name() + " ?");
                    final NetworkImageView peopleimg = (NetworkImageView) unfollow_dialog.findViewById(R.id.peopleimg);
                    String image_url = "http://192.168.0.150:550/TextileApp/profile_pictures/" + prod.getUser_pic();

                    imageLoader.get(image_url, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "Image Load Error: " + error.getMessage());
                            holder.profileimg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
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
                            new follow_unfollow().execute(userid, prod.getUser_id(), "2");

                            holder.follow_label.setText("Follow");
                            holder.follow.setBackgroundResource(R.drawable.follow_button_popup);
                            unfollow_dialog.dismiss();
                        }
                    });

                }
            }
        });

        if (prod.getIs_follow().equals("1")) {

            holder.follow_label.setText("Following");
            //follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
            holder.follow.setBackgroundResource(R.drawable.following_button_popup);
            //follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));
        }

        if (prod.getIs_follow().equals("0")) {

            holder.follow_label.setText("Requested");
            //follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
            holder.follow.setBackgroundResource(R.drawable.requested_button_popup);
            //follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));
        }

        if (prod.getIs_follow().equals("2") || prod.getIs_follow().equals("3")) {
            Drawable img = ContextCompat.getDrawable(mContext, R.drawable.follow_add);
            //follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.follow_label.setText("Follow");
            holder.follow.setBackgroundResource(R.drawable.follow_button_popup);
            //follow.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView profileimg;
        private final TextView txt_name;
        private final ImageButton follow;
        private final TextView follow_label;
        private final RelativeLayout mainBoxLayout;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            follow = (ImageButton) itemLayoutView.findViewById(R.id.follow);
            txt_name = (TextView) itemLayoutView.findViewById(R.id.txt_name);
            follow_label = (TextView) itemLayoutView.findViewById(R.id.follow_label);
            profileimg = (NetworkImageView) itemLayoutView.findViewById(R.id.profileimg);
            mainBoxLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.mainLayout);

        }
    }

    public class follow_unfollow extends AsyncTask<String, Void, String> {
        private String res;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;

            mProgressDialog = ProgressDialog.show(
                    mContext, "",
                    "Loading...");
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/follow_unfollow.php?user_id=" + params[0] + "&follower_id=" + params[1] + "&follow=" + params[2]);
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
                    customtoast.ShowToast(mContext,"Something went Wrong.",R.layout.red_toast);
                } else {

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        String errorcode = json.getString("errorcode");
                        if (errorcode.equals("0")) {
                            // customtoast.ShowToast(mContext,"Following.",R.layout.red_toast);
                        }else{
                            customtoast.ShowToast(mContext, json.getString("errormsg"),R.layout.red_toast);
                        }
                    }
                }
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                mProgressDialog.dismiss();
                customtoast.ShowToast(mContext,"Something went Wrong.",R.layout.red_toast);
                objEx.printStackTrace();
            }
        }
    }

}
