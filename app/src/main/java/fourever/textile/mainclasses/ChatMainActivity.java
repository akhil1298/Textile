package fourever.textile.mainclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fourever.textile.adapter.ChatAdapter;
import fourever.textile.entity.ChatEntity;
import fourever.textile.entity.ChatEntityy;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

public class ChatMainActivity extends AppCompatActivity {

    Intent intent;
    String caseid, vendorid, customerid, sentfrom, PCFlag, msgdesc, res, res1;
    TextView txtNoDataFound1;
    ListView listView;
    EditText msg_edit;
    List<ChatEntityy> chat;
    ChatAdapter adapter;

    ImageView btnsend;
    private DATAReceiver dataReceiver;
    private IntentFilter mOnRegisteredFilter;
    private SharedPreferences Loginprefs;
    private String vendorname;
    private String status;

    public static TextView stickyViewSpacer, stickyView;
    private SharedPreferences prefs;
    private String userid;
    private String to_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        prefs = getSharedPreferences("Pref_Count", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("count", 1);
        edit.commit();

        intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        to_id = intent.getStringExtra("to_id");
        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        vendorname = Loginprefs.getString("user_name", null);

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

        if (userid == null) {
            Intent intent = new Intent(ChatMainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {
        }

        /*if (intent.getStringExtra("sentfrom").equals("FromCustomer")) {
            caseid = intent.getStringExtra("caseid");
            vendorid = intent.getStringExtra("vendorid");
            customerid = intent.getStringExtra("customerid");
            //sentfrom = intent.getStringExtra("sentfrom");
            PCFlag = "C";
        } else {
            caseid = intent.getStringExtra("caseid");
            vendorid = intent.getStringExtra("vendorid");
            customerid = intent.getStringExtra("customerid");
            //sentfrom = intent.getStringExtra("sentfrom");
            PCFlag = "V";
        }*/

        txtNoDataFound1 = (TextView) findViewById(R.id.txtNoDataFound1);
        btnsend = (ImageView) findViewById(R.id.btnsend);
        listView = (ListView) findViewById(R.id.ChatListView);
        msg_edit = (EditText) findViewById(R.id.msg_edit);

        stickyView = (TextView) findViewById(R.id.stickyView);
        listView.setHeaderDividersEnabled(true);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.stickyheader, listView, false);
        stickyViewSpacer = (TextView) header.findViewById(R.id.headerview);

        listView.addHeaderView(header, null, false);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                    int heroTopY = stickyViewSpacer.getTop();
                    stickyView.setY(Math.max(0, heroTopY + topY));
                }
            }
        });

       /* status = intent.getStringExtra("status");
        if (status.equals("C")) {
            msg_edit.setVisibility(View.GONE);
            btnsend.setVisibility(View.GONE);
        }*/

        try {
            new ServiceSync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataReceiver = new DATAReceiver();
        mOnRegisteredFilter = new IntentFilter();
        mOnRegisteredFilter.addAction(GcmMessageHandler.ACTION_ON_REGISTERED);

        /*
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Akhil Batlawala</font>"));*/
        /*
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        */
    }

    private class DATAReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                new ServiceSync().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(dataReceiver, mOnRegisteredFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(dataReceiver);
    }

    private class ServiceSync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() { }

        @Override
        protected String doInBackground(Void... params) {

            res = null;
            PutUtility objClient = new PutUtility();

            /*RestClient objClient = new RestClient(
                    "http://www.eazito.com/webservice/chat_get_data.php?case_id="
                            + caseid + "&vendor_id=" + vendorid
                            + "&customer_id=" + customerid);*/

            try {
                //  res = objClient.Execute(RequestMethod.GET);
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/chat_get_data.php?from_user_id="
                        + userid + "&to_user_id=" + to_id );
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    txtNoDataFound1.setVisibility(View.VISIBLE);
                    txtNoDataFound1.setText("No Chats !!");
                    txtNoDataFound1.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                    listView.setVisibility(View.GONE);

                } else {

                    listView.setVisibility(View.VISIBLE);
                    txtNoDataFound1.setVisibility(View.GONE);
                    chat = new ArrayList<ChatEntityy>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        ChatEntityy chatEntityy = new ChatEntityy();
                        chatEntityy.setMsgid(json.getString("chat_id"));
                        chatEntityy.setFrom_user_id(json.getString("from_user_id"));
                        chatEntityy.setTo_user_id(json.getString("to_user_id"));
                        chatEntityy.setMsg(json.getString("message"));
                        chatEntityy.setMsgdate(json.getString("date"));
                        chatEntityy.setMsgtime(json.getString("time"));

                        if (userid.equals(json.getString("from_user_id"))) {
                            chatEntityy.setMsgflag("my");
                        } else {
                            chatEntityy.setMsgflag("other");
                        }

                        chat.add(chatEntityy);

                        /*chat.add(new ChatEntity(json.getString("chat_id"), json
                                .getString("case_id"), json
                                .getString("vendor_id"), json
                                .getString("customer_id"), json
                                .getString("message_date"), json
                                .getString("message_time"), json
                                .getString("message_flag"), json
                                .getString("message_descp"), json
                                .getString("message_descp"), PCFlag));*/
                        adapter = new ChatAdapter(getApplicationContext(),
                                R.layout.activity_chat_controls, chat);
                        listView.setAdapter(adapter);
                    }
                }
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public void saveReply(View v) {

        msgdesc = msg_edit.getText().toString();
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate2 = df2.format(c.getTime());

        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        String formattedDate3 = df3.format(c.getTime());

        if (msgdesc.equals("") || msgdesc.equals(null)) {
            /*Toast.makeText(getApplicationContext(), "Please Enter Message !!!",
                    Toast.LENGTH_LONG).show();*/
            customtoast.ShowToast(getApplicationContext(), "Please Enter Message !!!", R.layout.red_toast);
        } else {

            PutUtility objClient = new PutUtility();
            msg_edit.setText("");

            try {

                objClient.setParam("to_user_id", to_id);
                objClient.setParam("from_user_id", userid);
                objClient.setParam("message", msgdesc);

                res1 = objClient.postData("http://192.168.0.150:550/TextileApp/webservice/chat_master_add.php");

               //    sendnoti(customerid, caseid, vendorid, sentfrom, vendorname, msgdesc, status);
                new ServiceSync().execute();
            } catch (Exception objEx) {
                objEx.printStackTrace();
            }
        }
    }

    public void sendnoti(String customerid, String caseid, String vendorid, String sentfrom,
                         String name, String message, String statu) {

        PutUtility objClient = new PutUtility();

        try {
            // res2 = objClient.Execute(RequestMethod.GET);
            res = objClient.getData("http://www.eazito.com/webservice/send_chat_noti.php?user_id="
                    + customerid
                    + "&case_id="
                    + caseid
                    + "&vendor_id="
                    + vendorid
                    + "&sentfrom="
                    + sentfrom
                    + "&name="
                    + Uri.encode(name)
                    + "&message=" + Uri.encode(message)
                    );
        } catch (Exception objEx) {
            objEx.printStackTrace();
        }
    }
}
