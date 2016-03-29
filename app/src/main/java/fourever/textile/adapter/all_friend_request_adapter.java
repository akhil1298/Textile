package fourever.textile.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import fourever.textile.entity.all_friend_request_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

/**
 * Created by akhil on 2/2/16.
 */
public class all_friend_request_adapter extends RecyclerView.Adapter<all_friend_request_adapter.ViewHolder> {

    private final String userid;
    private Context mContext;
    private List<all_friend_request_entity> people;
    ImageLoader imageLoader;
    private SharedPreferences Loginprefs;

    public all_friend_request_adapter(Context c, List<all_friend_request_entity> people) {
        mContext = c;
        this.people = people;

        Loginprefs = mContext.getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

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
                .inflate(R.layout.friend_request_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public void onBindViewHolder(final all_friend_request_adapter.ViewHolder holder, final int position) {
      //  animate(holder);

        final all_friend_request_entity prod = people.get(position);
        // String image_url = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic();
       // holder.peopleimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic(), imageLoader);

        if(prod.getUser_pic() == null || prod.getUser_pic().equals("")) {
            holder.default_img.setVisibility(View.VISIBLE);
            holder.default_img.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
            holder.peopleimg.setVisibility(View.GONE);
           /* holder.peopleimg.setBackground(null);
            holder.peopleimg.setImageDrawable(null);
            holder.peopleimg.setImageResource(R.drawable.maleuser);*/
            //holder.peopleimg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.maleuser));
        }else{
            holder.default_img.setVisibility(View.GONE);
            holder.peopleimg.setVisibility(View.VISIBLE);
            String image_url = "http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic();
            holder.peopleimg.setBackground(null);
            holder.peopleimg.setImageDrawable(null);
            holder.peopleimg.setImageUrl(image_url, imageLoader);
        }

        holder.user_request_id.setText(prod.getUser_id());
        holder.people_name.setText(prod.getUser_name());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new accept_reject().execute(userid, prod.getUser_id(), "0", String.valueOf(position));
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new accept_reject().execute(userid , prod.getUser_id() , "1", String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView peopleimg;
        private final NetworkImageView default_img;
        private final TextView people_name;
        private final TextView user_request_id;
        private final Button accept;
        private final Button reject;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            peopleimg = (NetworkImageView) itemLayoutView.findViewById(R.id.peopleimg);
            default_img = (NetworkImageView) itemLayoutView.findViewById(R.id.default_img);
            people_name = (TextView) itemLayoutView.findViewById(R.id.people_name);
            user_request_id = (TextView) itemLayoutView.findViewById(R.id.user_request_id);
            accept = (Button) itemLayoutView.findViewById(R.id.accept);
            reject = (Button) itemLayoutView.findViewById(R.id.reject);

        }
    }

    public class accept_reject extends AsyncTask<String, Void, String> {
        private String res;
        ProgressDialog mProgressDialog;
        private int pos;

        @Override
        protected void onPreExecute() {
            res = null;

            mProgressDialog = ProgressDialog.show(
                    mContext, "",
                    "Loading...");
        }

        @Override
        protected String doInBackground(String... params) {
            pos = Integer.parseInt(params[3]);
            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/accept_reject_friend_request.php?user_id=" + params[0] + "&follower_id=" + params[1] + "&request_code=" + params[2]);
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
                            notifyDataSetChanged();
                            people.remove(pos);
                            notifyItemRemoved(pos);
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
