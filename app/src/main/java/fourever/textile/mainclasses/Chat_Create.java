package fourever.textile.mainclasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fourever.textile.adapter.all_friend_request_adapter;
import fourever.textile.adapter.new_chat_adapter;
import fourever.textile.entity.all_friend_request_entity;
import fourever.textile.entity.all_people_entity;
import fourever.textile.miscs.PutUtility;

public class Chat_Create extends AppCompatActivity {

    RecyclerView new_people;
    private FrameLayout error_frame;
    private FrameLayout loading_visitor_frame;
    private SharedPreferences Loginprefs;
    private String userid;
    private ArrayList<all_people_entity> Services;
    private new_chat_adapter new_chat_adaptr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_create);

        Loginprefs = getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(Chat_Create.this, Login.class);
            startActivity(intent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        error_frame = (FrameLayout) findViewById(R.id.error_frame);
        loading_visitor_frame = (FrameLayout) findViewById(R.id.loading_visitor_frame);

        new_people = (RecyclerView)findViewById(R.id.new_people);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Chat_Create.this, 1);
        new_people.setLayoutManager(gridLayoutManager);

        new Chat_contacts().execute(userid);

    }

    private class Chat_contacts extends AsyncTask<String, Void, String> {

        private String res;

        @Override
        protected void onPreExecute() {
            loading_visitor_frame.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            res = null;
            PutUtility put = new PutUtility();

            try {
                res = put.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/chat_contacts.php?user_id="+params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        protected void onPostExecute(String res) {
            try {

                if (res == null || res == " " || res.equals(" ")) {
                    loading_visitor_frame.setVisibility(View.GONE);
                    new_people.setVisibility(View.GONE);
                    error_frame.setVisibility(View.VISIBLE);
                   // txt_error.setText("No visitor found");
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    loading_visitor_frame.setVisibility(View.GONE);
                    new_people.setVisibility(View.GONE);
                    error_frame.setVisibility(View.VISIBLE);
                }else{
                    Services = new ArrayList<all_people_entity>();
                    Services.clear();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        all_people_entity service = new all_people_entity();
                        service.setUser_id(json.getString("user_id"));
                        service.setUser_name(json.getString("user_name"));
                        service.setUser_pic(json.getString("user_pic"));
                        service.setUser_status(json.getString("user_status"));
                        Services.add(service);
                    }

                    if (Services.size() > 0) {

                        new_chat_adaptr = new new_chat_adapter(Chat_Create.this, Services);
                        new_people.setAdapter(new_chat_adaptr);
                        new_chat_adaptr.notifyDataSetChanged();
                    } else {
                        loading_visitor_frame.setVisibility(View.GONE);
                        new_people.setVisibility(View.GONE);
                        error_frame.setVisibility(View.VISIBLE);
                    }

                    loading_visitor_frame.setVisibility(View.GONE);
                    error_frame.setVisibility(View.GONE);
                    new_people.setVisibility(View.VISIBLE);

                }



               /* for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    String errorcode = json.getString("errorcode");
                    if (errorcode.equals("0") || errorcode.equals("2")) {

                        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
                        SharedPreferences.Editor edit = Loginprefs.edit();
                        edit.putString("user_id", json.getString("user_id"));
                        edit.putString("user_name", json.getString("user_name"));
                        edit.putString("user_img", json.getString("user_img"));
                        edit.putString("user_deviceId", json.getString("deviceID"));
                        edit.commit();

                        customtoast.ShowToast(getApplicationContext(), "Login Successfully.", R.layout.blue_toast);

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        customtoast.ShowToast(getApplicationContext(), "Invalid Username and Password", R.layout.red_toast);
                        *//*Toast.makeText(Login.this,
                                "Invalid Username and Password", Toast.LENGTH_LONG)
                                .show();*//*
                    }
                }*/


            } catch (Exception objEx) {
                new_people.setVisibility(View.GONE);
                error_frame.setVisibility(View.VISIBLE);
                //txt_error.setText("Something went wrong..!!");
                objEx.printStackTrace();
            }
        }
    }

}
