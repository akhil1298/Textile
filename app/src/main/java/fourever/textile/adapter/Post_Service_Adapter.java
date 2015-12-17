package fourever.textile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;

import fourever.textile.entity.post_service_entity;
import fourever.textile.mainclasses.Create_NewsFeed;
import fourever.textile.mainclasses.R;

public class Post_Service_Adapter extends ArrayAdapter<post_service_entity> {

    private Context activity;
    private ArrayList<post_service_entity> data;
    public Resources res;
    post_service_entity tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public Post_Service_Adapter(
            Context activitySpinner,
            int textViewResourceId,
            ArrayList<post_service_entity> objects,
            Resources resLocal
    )
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_items, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = (post_service_entity) data.get(position);

        TextView label        = (TextView)row.findViewById(R.id.servicename);
        //TextView sub          = (TextView)row.findViewById(R.id.sub);

            // Set values for spinner each row
            label.setText(tempValues.getServicename());
           // sub.setText(tempValues.getServicedetail());


        return row;
    }
}