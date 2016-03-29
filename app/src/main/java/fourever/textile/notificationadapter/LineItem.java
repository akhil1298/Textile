package fourever.textile.notificationadapter;

/**
 * Created by akhil on 23/3/16.
 */
public class LineItem {

    public int sectionManager;
    public int sectionFirstPosition;
    public boolean isHeader;


    public String noti_user_id;
    public String noti_post_id;
    public String noti_user_name;
    public String noti_user_pic;
    public String noti_message;
    public String date;
    public String noti_time;

    public LineItem() { }

    public LineItem(String date, boolean isHeader, int sectionManager,
                    int sectionFirstPosition) {
        this.isHeader = isHeader;
        this.date = date;
        this.sectionManager = sectionManager;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public String getNoti_user_name() {
        return noti_user_name;
    }

    public void setNoti_user_name(String noti_user_name) {
        this.noti_user_name = noti_user_name;
    }

    public String getNoti_time() {
        return noti_time;
    }

    public void setNoti_time(String noti_time) {
        this.noti_time = noti_time;
    }



    public String getNoti_user_id() {
        return noti_user_id;
    }

    public void setNoti_user_id(String noti_user_id) {
        this.noti_user_id = noti_user_id;
    }

    public String getNoti_post_id() {
        return noti_post_id;
    }

    public void setNoti_post_id(String noti_post_id) {
        this.noti_post_id = noti_post_id;
    }

    public String getNoti_user_pic() {
        return noti_user_pic;
    }

    public void setNoti_user_pic(String noti_user_pic) {
        this.noti_user_pic = noti_user_pic;
    }

    public String getNoti_message() {
        return noti_message;
    }

    public void setNoti_message(String noti_message) {
        this.noti_message = noti_message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
