package fourever.textile.fragment;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fourever.textile.mainclasses.R;

/**
 * Created by akhil on 13/10/15.
 */
public class newsfeed extends Fragment {

    CardView cv;
    private View.OnClickListener mOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfeed_design_main, null);
        cv = (CardView) view.findViewById(R.id.card_view);
        Snackbar snackbar = Snackbar
                .make(cv, R.string.app_name, Snackbar.LENGTH_LONG)
                .setAction(R.string.app_name, mOnClickListener);
        snackbar.setActionTextColor(Color.CYAN);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.YELLOW);//change Snackbar's background color;
        TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLUE);//change Snackbar's text color;
        snackbar.show();
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // checkBox.setChecked(false);
            }
        };
        
        return view;
    }
}
