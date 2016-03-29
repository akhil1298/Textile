package fourever.textile.entity;

/**
 * Created by akhil on 5/1/16.
 */
public class CityEntity {

    String City_id;
    String City_name;

    public CityEntity() {
    }

    public CityEntity(String city_id, String city_name) {
        City_id = city_id;
        City_name = city_name;
    }

    public String getCity_id() {
        return City_id;
    }

    public void setCity_id(String city_id) {
        City_id = city_id;
    }

    public String getCity_name() {
        return City_name;
    }

    public void setCity_name(String city_name) {
        City_name = city_name;
    }
}
