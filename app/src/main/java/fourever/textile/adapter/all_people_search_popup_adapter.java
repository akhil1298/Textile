package fourever.textile.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import fourever.textile.entity.all_people_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;

public class all_people_search_popup_adapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<all_people_entity> people;
    private int layout;
    ImageLoader imageLoader;

    public all_people_search_popup_adapter(Context mContext,
                                           List<all_people_entity> people, int layout) {
        this.mContext = mContext;
        this.people = people;
        this.layout = layout;

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
    }

    public void clear() {
        people.clear();
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int location) {
        return people.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(layout, null);

        ImageButton follow = (ImageButton) convertView.findViewById(R.id.follow);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView follow_label = (TextView) convertView.findViewById(R.id.follow_label);
        NetworkImageView profileimg = (NetworkImageView) convertView.findViewById(R.id.profileimg);

        // getting movie data for the row
        all_people_entity prod = people.get(position);
        txt_name.setText(prod.getUser_name());
        profileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getUser_pic(), imageLoader);


        if (prod.getIs_follow().equals("1")) {

            follow_label.setText("Following");
            //follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
            follow.setBackgroundResource(R.drawable.following_button_popup);
            //follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));
        }

        if (prod.getIs_follow().equals("0")) {

            follow_label.setText("Requested");
            //follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.following, 0, 0, 0);
            follow.setBackgroundResource(R.drawable.requested_button_popup);
            //follow.setTextColor(ContextCompat.getColor(mContext, R.color.accentcolor));
        }

        if (prod.getIs_follow().equals("2") || prod.getIs_follow().equals("3")) {
            Drawable img = ContextCompat.getDrawable(mContext, R.drawable.follow_add);
            //follow.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            follow_label.setText("Follow");
            follow.setBackgroundResource(R.drawable.follow_button_popup);
            //follow.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
        }

        return convertView;
    }

}
