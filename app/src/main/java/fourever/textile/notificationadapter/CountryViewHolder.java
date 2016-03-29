package fourever.textile.notificationadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import fourever.textile.mainclasses.R;

/**
 * Simple view holder for a single text view.
 */
class CountryViewHolder extends RecyclerView.ViewHolder {

    public TextView noti_user_name;
    public NetworkImageView noti_user_pic;
    public NetworkImageView default_img;
    public TextView noti_message;
    public TextView date;
    public TextView noti_time;

    CountryViewHolder(View view) {
        super(view);

        date = (TextView) view.findViewById(R.id.text);
        noti_user_name = (TextView) view.findViewById(R.id.noti_user_name);
        noti_user_pic = (NetworkImageView) view.findViewById(R.id.noti_user_pic);
        default_img = (NetworkImageView) view.findViewById(R.id.default_img);
        noti_message = (TextView) view.findViewById(R.id.noti_message);
        noti_time = (TextView) view.findViewById(R.id.noti_time);
    }

    public void bindItem(String text) {
        date.setText(text);
    }

    public void setNoti_user_name(String text) {
        noti_user_name.setText(text);
    }

    public void setNoti_time(String text) {
        noti_time.setText(text);
    }

    public void setNoti_message(String noti_messag) {
        noti_message.setText(noti_messag);
    }

    @Override
    public String toString() {
        return date.getText().toString();
    }
}
