package fourever.textile.customgallary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

import fourever.textile.mainclasses.Create_NewsFeed;
import fourever.textile.mainclasses.R;

/**
 * Created by akhil on 13/1/16.
 */
public class GalleryselectedAdapterRecycler extends RecyclerView.Adapter<GalleryselectedAdapterRecycler.ViewHolder>{

    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
    ImageLoader imageLoader;
    private boolean isActionMultiplePick;

    public GalleryselectedAdapterRecycler(Context c, ImageLoader imageLoader) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        this.imageLoader = imageLoader;

        // clearCache();
    }

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public CustomGallery getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMultiplePick(boolean isMultiplePick) {
        this.isActionMultiplePick = isMultiplePick;
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSeleted = selection;

        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<CustomGallery> getSelected() {
        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }
        return dataT;
    }

    public void addAll(ArrayList<CustomGallery> files) {

        try {
            this.data.clear();
            this.data.addAll(files);
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    public void changeSelection(View v, int position) {

        if (data.get(position).isSeleted) {
            data.get(position).isSeleted = false;
        } else {
            data.get(position).isSeleted = true;
        }

        ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
                .get(position).isSeleted);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgQueue;
        ImageView imgClose;
        ImageView imgQueueMultiSelected;
        ImageView insertimg;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            imgQueue = (ImageView) itemLayoutView.findViewById(R.id.imgQueue);
            imgClose = (ImageView) itemLayoutView.findViewById(R.id.imgClose);
            imgQueueMultiSelected = (ImageView) itemLayoutView.findViewById(R.id.imgQueueMultiSelected);
            insertimg = (ImageView) itemLayoutView.findViewById(R.id.insertimg);

            /*itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemLayoutView, getLayoutPosition());
                }
            });*/
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item_selected, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        holder.imgQueue.setTag(position);
        holder.insertimg.setTag(position);

        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                Create_NewsFeed.dataT.remove(position);
                notifyDataSetChanged();
                if(data.size() == 1)
                {
                    int height = holder.insertimg.getLayoutParams().height + 250;
                    Create_NewsFeed.gridGallery.setMinimumHeight(height);
                    Create_NewsFeed.gridGallery.getLayoutParams().height = height;
                }
            }
        });

        holder.insertimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == data.size() - 1) {

                    int str1 = Create_NewsFeed.dataT.size();
                    if (str1 > 0) {
                        for (int i = 0; i <= Create_NewsFeed.dataT.size(); i++) {
                            CustomGallery item = Create_NewsFeed.dataT.get(i);
                            if (item.sdcardPath.equals("addimg")) {
                                Create_NewsFeed.dataT.remove(i);
                            }
                        }
                    }

                    Intent i = new Intent(mContext, CustomGalleryActivity.class);
                    i.setAction(Action.ACTION_MULTIPLE_PICK);
                    AppCompatActivity origin = (AppCompatActivity)mContext;
                    origin.startActivityForResult(i, 200);
                }
            }
        });

        try {
            if (position == (data.size() - 1)) {
                holder.insertimg.setVisibility(View.VISIBLE);
                holder.imgClose.setVisibility(View.GONE);
                holder.imgQueue.setVisibility(View.GONE);
            } else {
                holder.imgClose.setVisibility(View.VISIBLE);
                holder.insertimg.setVisibility(View.GONE);
                holder.imgQueue.setVisibility(View.VISIBLE);

                imageLoader.displayImage("file://" + data.get(position).sdcardPath,
                        holder.imgQueue, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                holder.imgQueue
                                        .setImageResource(R.drawable.no_media);
                                super.onLoadingStarted(imageUri, view);
                            }
                        });
            }
            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected
                        .setSelected(data.get(position).isSeleted);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

}
