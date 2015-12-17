package fourever.textile.customgallary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import java.util.ArrayList;
import fourever.textile.mainclasses.Create_NewsFeed;
import fourever.textile.mainclasses.R;

public class GalleryselectedAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
    ImageLoader imageLoader;

    private boolean isActionMultiplePick;

    public GalleryselectedAdapter(Context c, ImageLoader imageLoader) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        this.imageLoader = imageLoader;

        // clearCache();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = infalter.inflate(R.layout.gallery_item, null);
            holder = new ViewHolder();

            holder.imgQueue = (ImageView) convertView
                    .findViewById(R.id.imgQueue);

            holder.imgClose = (ImageView) convertView
                    .findViewById(R.id.imgClose);
            holder.imgClose.setVisibility(View.VISIBLE);

            holder.insertimg = (ImageView) convertView
                    .findViewById(R.id.insertimg);
            holder.imgQueueMultiSelected = (ImageView) convertView
                    .findViewById(R.id.imgQueueMultiSelected);

            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
            }

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgQueue.setTag(position);
        holder.insertimg.setTag(position);

        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                Create_NewsFeed.dataT.remove(position);
                notifyDataSetChanged();
               // Toast.makeText(mContext,"Closed item " + position, Toast.LENGTH_SHORT).show();
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
                /*File imgFile = new  File(data.get(position).sdcardPath);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    holder.imgQueue.setImageBitmap(myBitmap);

                }*/

             //   Toast.makeText(mContext,data.get(position).sdcardPath,Toast.LENGTH_SHORT).show();
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

        return convertView;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgClose;
        ImageView imgQueueMultiSelected;
        ImageView insertimg;
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
