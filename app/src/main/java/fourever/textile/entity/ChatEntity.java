package fourever.textile.entity;

public class ChatEntity {

    private String msgid;
    private String caseid;
    private String vendorid;
    private String customerid;
    private String msgdate;
    private String msgtime;
    private String msgflag;
    private String txtfrom;
    private String txtmy;
    private String getfrom;

    public ChatEntity() {
    }

    public ChatEntity(String msgid, String caseid, String vendorid,
                      String customerid, String msgdate, String msgtime, String msgflag,
                      String txtfrom, String txtmy, String getfrom) {
        this.msgid = msgid;
        this.caseid = caseid;
        this.vendorid = vendorid;
        this.customerid = customerid;
        this.msgdate = msgdate;
        this.msgtime = msgtime;
        this.msgflag = msgflag;
        this.txtfrom = txtfrom;
        this.txtmy = txtmy;
        this.getfrom = getfrom;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getCaseid() {
        return caseid;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
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

    public String getTxtfrom() {
        return txtfrom;
    }

    public void setTxtfrom(String txtfrom) {
        this.txtfrom = txtfrom;
    }

    public String getTxtmy() {
        return txtmy;
    }

    public void setTxtmy(String txtmy) {
        this.txtmy = txtmy;
    }

    public String getGetfrom() {
        return getfrom;
    }

    public void setGetfrom(String getfrom) {
        this.getfrom = getfrom;
    }
}
