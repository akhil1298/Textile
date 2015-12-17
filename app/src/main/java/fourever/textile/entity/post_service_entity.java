package fourever.textile.entity;


/**
 * Created by akhil on 12/10/15.
 */
public class post_service_entity {

    String serviceid;
    String servicename;
    String servicedetail;

    public post_service_entity() {
    }

    public post_service_entity(String serviceid, String servicename, String servicedetail) {
        this.serviceid = serviceid;
        this.servicename = servicename;
        this.servicedetail = servicedetail;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getServicedetail() {
        return servicedetail;
    }

    public void setServicedetail(String servicedetail) {
        this.servicedetail = servicedetail;
    }
}
