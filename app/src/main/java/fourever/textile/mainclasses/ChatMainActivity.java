package fourever.textile.mainclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fourever.textile.adapter.ChatAdapter;
import fourever.textile.entity.ChatEntity;

public class ChatMainActivity extends AppCompatActivity {

    Intent intent;
    String caseid, vendorid, customerid, sentfrom, PCFlag, msgdesc, res, res1;
    TextView txtNoDataFound1;
    ListView listView;
    EditText msg_edit;
    List<ChatEntity> chat;
    ChatAdapter adapter;

    ImageView btnsend;
    private DATAReceiver dataReceiver;
    private IntentFilter mOnRegisteredFilter;
    private SharedPreferences Loginprefs;
    private String vendorname;
    private String status;

    public static TextView stickyViewSpacer, stickyView;
    private SharedPreferences prefs;

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

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        vendorname = Loginprefs.getString("user_name", null);

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
          //  new ServiceSync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataReceiver = new DATAReceiver();
        mOnRegisteredFilter = new IntentFilter();
        mOnRegisteredFilter.addAction(GcmMessageHandler.ACTION_ON_REGISTERED);


        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Akhil Batlawala</font>"));*/
        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));*/

    }

    private class DATAReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
              //  new ServiceSync().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
