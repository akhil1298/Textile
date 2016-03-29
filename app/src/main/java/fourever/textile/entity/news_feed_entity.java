package fourever.textile.entity;

import java.util.ArrayList;

/**
 * Created by akhil on 16/1/16.
 */
public class news_feed_entity {

 //main post
    String post_id;
    String post_user_id;
    String post_username;
    String post_userpic;
    String post_category;
    String post_description;
    String post_date;
    String post_time;
    ArrayList<String> post_picture;

    ArrayList<post_like_entity> post_likes;
    ArrayList<post_comments_entity> post_comments;

    public news_feed_entity(){ }

    public news_feed_entity(String post_id, String post_user_id, String post_username, String post_userpic, String post_category, String post_description, String post_date, String post_time, ArrayList<String> post_picture, ArrayList<post_like_entity> post_likes, ArrayList<post_comments_entity> post_comments) {
        this.post_id = post_id;
        this.post_user_id = post_user_id;
        this.post_username = post_username;
        this.post_userpic = post_userpic;
        this.post_category = post_category;
        this.post_description = post_description;
        this.post_date = post_date;
        this.post_time = post_time;
        this.post_picture = post_picture;
        this.post_likes = post_likes;
        this.post_comments = post_comments;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getPost_username() {
        return post_username;
    }

    public void setPost_username(String post_username) {
        this.post_username = post_username;
    }

    public String getPost_userpic() {
        return post_userpic;
    }

    public void setPost_userpic(String post_userpic) {
        this.post_userpic = post_userpic;
    }

    public String getPost_category() {
        return post_category;
    }

    public void setPost_category(String post_category) {
        this.post_category = post_category;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public ArrayList<String> getPost_picture() {
        return post_picture;
    }

    public void setPost_picture(ArrayList<String> post_picture) {
        this.post_picture = post_picture;
    }

    public ArrayList<post_like_entity> getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(ArrayList<post_like_entity> post_likes) {
        this.post_likes = post_likes;
    }

    public ArrayList<post_comments_entity> getPost_comments() {
        return post_comments;
    }

    public void setPost_comments(ArrayList<post_comments_entity> post_comments) {
        this.post_comments = post_comments;
    }
}
