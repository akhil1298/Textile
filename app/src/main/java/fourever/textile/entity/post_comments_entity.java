package fourever.textile.entity;

/**
 * Created by akhil on 20/1/16.
 */
public class post_comments_entity {

    String total_comments;
    String commenter_id;
    String commenter_name;
    String comment;
    String commenter_pic;
    String commente_date;
    String commente_time;

    public post_comments_entity(){}

    public post_comments_entity(String total_comments, String commenter_id, String commenter_name, String commenter_pic, String commente_date, String commente_time) {
        this.total_comments = total_comments;
        this.commenter_id = commenter_id;
        this.commenter_name = commenter_name;
        this.commenter_pic = commenter_pic;
        this.commente_date = commente_date;
        this.commente_time = commente_time;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public String getCommenter_id() {
        return commenter_id;
    }

    public void setCommenter_id(String commenter_id) {
        this.commenter_id = commenter_id;
    }

    public String getCommenter_name() {
        return commenter_name;
    }

    public void setCommenter_name(String commenter_name) {
        this.commenter_name = commenter_name;
    }

    public String getCommenter_pic() {
        return commenter_pic;
    }

    public void setCommenter_pic(String commenter_pic) {
        this.commenter_pic = commenter_pic;
    }

    public String getCommente_date() {
        return commente_date;
    }

    public void setCommente_date(String commente_date) {
        this.commente_date = commente_date;
    }

    public String getCommente_time() {
        return commente_time;
    }

    public void setCommente_time(String commente_time) {
        this.commente_time = commente_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
