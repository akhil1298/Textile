package fourever.textile.entity;

/**
 * Created by akhil on 5/1/16.
 */
public class subuser_entity {

    String sub_user_id;
    String user_id;
    String subuser_name;
    String username;
    String password;

    public String getSub_user_id() {
        return sub_user_id;
    }

    public void setSub_user_id(String sub_user_id) {
        this.sub_user_id = sub_user_id;
    }

    public String getSubuser_name() {
        return subuser_name;
    }

    public void setSubuser_name(String subuser_name) {
        this.subuser_name = subuser_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
