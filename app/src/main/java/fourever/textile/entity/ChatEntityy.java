package fourever.textile.entity;

public class ChatEntityy {

    private String msgid;
    private String to_user_id;
    private String from_user_id;
    private String msg;
    private String msgdate;
    private String msgtime;
    private String msgflag;

    public ChatEntityy() {

    }

    public ChatEntityy(String msgid, String to_user_id,
                       String from_user_id, String msg, String msgdate, String msgtime) {
        this.msgid = msgid;
        this.to_user_id = to_user_id;
        this.from_user_id = from_user_id;
        this.msg = msg;
        this.msgdate = msgdate;
        this.msgtime = msgtime;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgdate() {
        return msgdate;
    }

    public void setMsgdate(String msgdate) {
        this.msgdate = msgdate;
    }

    public String getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(String msgtime) {
        this.msgtime = msgtime;
    }

    public String getMsgflag() {
        return msgflag;
    }

    public void setMsgflag(String msgflag) {
        this.msgflag = msgflag;
    }
}
