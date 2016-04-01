package fourever.textile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import fourever.textile.entity.post_comments_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;

/**
 * Created by akhil on 2/2/16.
 */
public class post_images_gallary_adapter extends RecyclerView.Adapter<post_images_gallary_adapter.ViewHolder> {

    private Context mContext;
    private List<String> pictures;
    com.android.volley.toolbox.ImageLoader imageLoader;

    public post_images_gallary_adapter(Context c, List<String> pictures) {
        mContext = c;
        this.pictures = pictures;

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
                .inflate(R.layout.news_feed_post_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(post_images_gallary_adapter.ViewHolder holder, int position) {
        final String img = pictures.get(position);
        holder.post_img.setImageUrl("http://192.168.0.150:550/TextileApp/post_uploaded_images/" + img, imageLoader);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView post_img;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            post_img = (NetworkImageView) itemLayoutView.findViewById(R.id.post_img);
        }
    }

}
