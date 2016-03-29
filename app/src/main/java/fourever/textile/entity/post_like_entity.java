package fourever.textile.entity;

/**
 * Created by akhil on 20/1/16.
 */
public class post_like_entity {

    String total_likes;
    String likes_id;
    String likes_name;
    String likes_pic;

    public post_like_entity(){}
    public post_like_entity(String lks_id){
        likes_id = lks_id;
    }

    public post_like_entity(String total_likes, String likes_id, String likes_name, String likes_pic) {
        this.total_likes = total_likes;
        this.likes_id = likes_id;
        this.likes_name = likes_name;
        this.likes_pic = likes_pic;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public String getLikes_id() {
        return likes_id;
    }

    public void setLikes_id(String likes_id) {
        this.likes_id = likes_id;
    }

    public String getLikes_name() {
        return likes_name;
    }

    public void setLikes_name(String likes_name) {
        this.likes_name = likes_name;
    }

    public String getLikes_pic() {
        return likes_pic;
    }

    public void setLikes_pic(String likes_pic) {
        this.likes_pic = likes_pic;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

       // if (v != null && v instanceof post_like_entity) {
            retVal = this.likes_id == ((post_like_entity) v).likes_id;
       // }
          //  post_like_entity ptr = (post_like_entity) v;
          //  retVal = ptr.likes_id.equals(this.likes_id) || ptr.likes_id == (this.likes_id);

        return retVal;
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.likes_id == null ? 0 : this.likes_id.hashCode());

        return result;
    }
}
