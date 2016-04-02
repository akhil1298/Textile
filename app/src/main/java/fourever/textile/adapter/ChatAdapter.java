package fourever.textile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fourever.textile.entity.ChatEntity;
import fourever.textile.mainclasses.ChatMainActivity;
import fourever.textile.mainclasses.R;


public class ChatAdapter extends ArrayAdapter<ChatEntity> {

    Context context;
    String getfromflag;
    private String msgrecentdate = "";

    public ChatAdapter(Context context, int resource, List<ChatEntity> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    private class ViewHolder {
        TextView msgid;
        TextView caseid;
        TextView vendorid;
        TextView customerid;
        TextView msgdate;
        TextView msgtime;
        TextView msgflag;
        TextView txtfrom;
        TextView txtmy;
        TextView msgtime1;
        TextView msgtime2;
        RelativeLayout rel2;
        RelativeLayout rel1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatEntity chat = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_chat_controls,
                    null);
            holder = new ViewHolder();
            holder.msgid = (TextView) convertView.findViewById(R.id.msgid);
            holder.caseid = (TextView) convertView.findViewById(R.id.caseid);
            holder.vendorid = (TextView) convertView
                    .findViewById(R.id.vendorid);
            holder.customerid = (TextView) convertView
                    .findViewById(R.id.customerid);
            holder.msgdate = (TextView) convertView.findViewById(R.id.msgdate);
            holder.msgtime = (TextView) convertView.findViewById(R.id.msgtime);
            holder.msgtime1 = (TextView) convertView.findViewById(R.id.msgtime1);
            holder.msgtime2 = (TextView) convertView.findViewById(R.id.msgtime2);

            holder.msgflag = (TextView) convertView.findViewById(R.id.msgflag);
            holder.txtfrom = (TextView) convertView.findViewById(R.id.txtfrom);
            holder.txtmy = (TextView) convertView.findViewById(R.id.txtmy);
            holder.rel1 = (RelativeLayout) convertView.findViewById(R.id.rel1);
            holder.rel2 = (RelativeLayout) convertView.findViewById(R.id.rel2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.msgid.setText(chat.getMsgid());
        holder.caseid.setText(chat.getCaseid());
        holder.vendorid.setText(chat.getVendorid());
        holder.customerid.setText(chat.getCustomerid());

        holder.msgdate.setText(chat.getMsgdate());
        holder.msgtime.setText(chat.getMsgtime());
        holder.msgflag.setText(chat.getMsgflag());
        holder.txtfrom.setText(chat.getTxtfrom());
        holder.txtmy.setText(chat.getTxtmy());

        String inputDate = chat.getMsgdate();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        holder.msgdate.setText(outputDate);
        ChatMainActivity.stickyView.setText(outputDate);

        if(!msgrecentdate.toString().equals("")) {
            if (holder.msgdate.getText().toString().equals(msgrecentdate.toString())) {
                holder.msgdate.setVisibility(View.GONE);
                msgrecentdate = chat.getMsgdate();
            }else{
                holder.msgdate.setVisibility(View.VISIBLE);
                msgrecentdate = chat.getMsgdate();
            }
        }else{
            holder.msgdate.setVisibility(View.VISIBLE);
            msgrecentdate = chat.getMsgdate();
        }

        getfromflag = chat.getGetfrom();


        if (getfromflag.equals("C")) {
            if (chat.getMsgflag().equals("C")) {
                /*holder.txtmy.setVisibility(View.VISIBLE);
                holder.txtmy.setText(chat.getTxtmy());
                holder.txtfrom.setVisibility(View.GONE);*/
                holder.rel2.setVisibility(View.VISIBLE);
                holder.txtmy.setVisibility(View.VISIBLE);
                holder.txtmy.setText(chat.getTxtmy());
                holder.msgtime2.setVisibility(View.VISIBLE);
                holder.msgtime2.setText(chat.getMsgtime());
                holder.rel1.setVisibility(View.GONE);
                holder.txtfrom.setVisibility(View.GONE);
                holder.msgtime1.setVisibility(View.GONE);
            } else {
               /* holder.txtfrom.setVisibility(View.VISIBLE);
                holder.txtfrom.setText(chat.getTxtfrom());
                holder.txtmy.setVisibility(View.GONE);*/
                holder.rel1.setVisibility(View.VISIBLE);
                holder.txtfrom.setVisibility(View.VISIBLE);
                holder.msgtime1.setVisibility(View.VISIBLE);
                holder.txtfrom.setText(chat.getTxtfrom());
                holder.msgtime1.setText(chat.getMsgtime());
                holder.rel2.setVisibility(View.GONE);
                holder.txtmy.setVisibility(View.GONE);
                holder.msgtime2.setVisibility(View.GONE);
            }
        } else {
            if (chat.getMsgflag().equals("C")) {
                /*holder.txtfrom.setVisibility(View.VISIBLE);
                holder.txtfrom.setText(chat.getTxtfrom());
                holder.txtmy.setVisibility(View.GONE);*/
                holder.rel1.setVisibility(View.VISIBLE);
                holder.txtfrom.setVisibility(View.VISIBLE);
                holder.msgtime1.setVisibility(View.VISIBLE);
                holder.txtfrom.setText(chat.getTxtfrom());
                holder.msgtime1.setText(chat.getMsgtime());
                holder.rel2.setVisibility(View.GONE);
                holder.txtmy.setVisibility(View.GONE);
                holder.msgtime2.setVisibility(View.GONE);
            } else {
                /*holder.txtmy.setVisibility(View.VISIBLE);
                holder.txtmy.setText(chat.getTxtmy());
                holder.txtfrom.setVisibility(View.GONE);*/
                holder.rel2.setVisibility(View.VISIBLE);
                holder.txtmy.setVisibility(View.VISIBLE);
                holder.msgtime2.setVisibility(View.VISIBLE);
                holder.txtmy.setText(chat.getTxtmy());
                holder.msgtime2.setText(chat.getMsgtime());
                holder.rel1.setVisibility(View.GONE);
                holder.txtfrom.setVisibility(View.GONE);
                holder.msgtime1.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

}
