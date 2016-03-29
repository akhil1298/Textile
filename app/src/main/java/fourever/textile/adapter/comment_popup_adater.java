package fourever.textile.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;
import fourever.textile.entity.post_comments_entity;
import fourever.textile.entity.post_like_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;


public class comment_popup_adater extends RecyclerView.Adapter<comment_popup_adater.ViewHolder> {

    private final Context mContext;
    private final List<post_comments_entity> comment;
    private final List<post_like_entity> like;
    private com.android.volley.toolbox.ImageLoader imageLoader;
    private final int flag;

    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private final boolean delayEnterAnimation = true;

    public comment_popup_adater(Context c, List<post_comments_entity> news_feed, List<post_like_entity> likes, int flags) {

        //flag = 0 = comment; flag = 1 = likes

        mContext = c;
        this.comment = news_feed;
        this.like = likes;
        this.flag = flags;

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        // clearCache();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_popup_item, parent, false);

        return  new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(comment_popup_adater.ViewHolder holder, int position) {
        if(flag == 0){
            runEnterAnimation(holder.itemView, position);
            final post_comments_entity prod = comment.get(position);
            holder.profileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getCommenter_pic(), imageLoader);
            holder.txt_name.setText(prod.getCommenter_name());
            holder.txt_comment.setText(prod.getComment());
            holder.txt_Comment_time.setText(prod.getCommente_date() + " " + prod.getCommente_time());
        }
        if(flag == 1){
           // runEnterAnimation(holder.itemView, position);
            final post_like_entity prod = like.get(position);
            holder.profileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getLikes_pic(), imageLoader);
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(15,35,0,0);
            holder.txt_name.setLayoutParams(buttonLayoutParams);

            holder.txt_name.setText(prod.getLikes_name());
            holder.txt_comment.setVisibility(View.GONE);
            holder.txt_Comment_time.setVisibility(View.GONE);
            holder.txt_Comment_divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(flag == 0) {
            return comment.size();
        }

        if(flag == 1){
            return like.size();
        }

        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView profileimg;
        private final TextView txt_name;
        private final TextView txt_comment;
        private final TextView txt_Comment_time;
        private final TextView txt_Comment_divider;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            txt_name = (TextView) itemLayoutView.findViewById(R.id.txt_name);
            profileimg = (NetworkImageView) itemLayoutView.findViewById(R.id.profileimg);
            txt_comment = (TextView) itemLayoutView.findViewById(R.id.txt_comment);
            txt_Comment_time = (TextView) itemLayoutView.findViewById(R.id.txt_Comment_time);
            txt_Comment_divider = (TextView) itemLayoutView.findViewById(R.id.txt_Comment_divider);
        }
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

}
