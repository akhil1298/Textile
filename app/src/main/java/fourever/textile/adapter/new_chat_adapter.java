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

import java.util.List;

import fourever.textile.entity.all_people_entity;
import fourever.textile.mainclasses.ChatMainActivity;
import fourever.textile.mainclasses.Followers_Profile;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

/**
 * Created by akhil on 2/2/16.
 */

public class new_chat_adapter extends RecyclerView.Adapter<new_chat_adapter.ViewHolder> {

    private final String userid;
    private Context mContext;
    private List<all_people_entity> people;
    ImageLoader imageLoader;
    private String followBtnString;
    private Dialog unfollow_dialog;
    private SharedPreferences Loginprefs;

    public new_chat_adapter(Context c, List<all_people_entity> people) {
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
                .inflate(R.layout.new_chat_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public void onBindViewHolder(final new_chat_adapter.ViewHolder holder, int position) {
      //  animate(holder);

        final all_people_entity prod = people.get(position);
        // String image_url = "http://192.168.0.150:550/TextileApp/profile_pictures/" + prod.getUser_pic();
       // holder.peopleimg.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + prod.getUser_pic(), imageLoader);

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
            String image_url = "http://192.168.0.150:550/TextileApp/profile_pictures/" + prod.getUser_pic();
            holder.peopleimg.setBackground(null);
            holder.peopleimg.setImageDrawable(null);
            holder.peopleimg.setImageUrl(image_url, imageLoader);
        }

        //Row Clicked
        holder.mainBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatMainActivity.class);
                intent.putExtra("to_id", prod.getUser_id());
                intent.putExtra("name", prod.getUser_name());
                mContext.startActivity(intent);
            }
        });

        holder.people_name.setText(prod.getUser_name());

        if(!prod.getUser_status().equals("null"))
        {
            if(prod.getUser_status().equals("")) {
                holder.people_status.setText("***No Status***");
            }else{
                holder.people_status.setText(prod.getUser_status());
            }

        }else{
            if(prod.getUser_status().equals("null")) {
                holder.people_status.setText("***No Status***");
            }

            if(prod.getUser_status().equals("")) {
                holder.people_status.setText("***No Status***");
            }
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
        private final LinearLayout mainBoxLayout;
        private final TextView people_status;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            peopleimg = (NetworkImageView) itemLayoutView.findViewById(R.id.peopleimg);
            default_img = (NetworkImageView) itemLayoutView.findViewById(R.id.default_img);
            people_name = (TextView) itemLayoutView.findViewById(R.id.people_name);
            people_status = (TextView) itemLayoutView.findViewById(R.id.people_status);
            mainBoxLayout = (LinearLayout) itemLayoutView.findViewById(R.id.mainBoxLayout);
        }
    }

}
