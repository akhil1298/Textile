package fourever.textile.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import fourever.textile.entity.CityEntity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {

    private final SharedPreferences Loginprefs;
    private final String userid;
    private Context mContext;
    private List<CityEntity> category;

    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private FrameLayout dialog_delete_loader, edit_dialog_loader;
    private LinearLayout delete_dialog_layout;
    private Dialog delete_confirm_dialog = null;
    private Dialog edit_dialog = null;
    String userType;
    public category_adapter(Context c, List<CityEntity> category, String userType) {

        mContext = c;
        this.category = category;

        this.userType = userType;

        Loginprefs = mContext.getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(category_adapter.ViewHolder holder, final int position) {

        final CityEntity prod = category.get(position);
        // holder.profileimg.setImageUrl("http://192.168.0.150:550/TextileApp/profile_pictures/" + prod.getCommenter_pic(), imageLoader);
        holder.txt_category_id.setText(prod.getCity_id());
        holder.txt_category_name.setText(prod.getCity_name());

        if(userType.equals("other_user_profile")){
            holder.action_delete.setVisibility(View.GONE);
        }else{
            holder.action_delete.setVisibility(View.VISIBLE);
        }

        holder.action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (delete_confirm_dialog == null) {
                        delete_confirm_dialog = new Dialog(mContext);
                    }

                    delete_confirm_dialog.setCancelable(false);
                    delete_confirm_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    delete_confirm_dialog.setContentView(R.layout.confirm_delete);
                    delete_confirm_dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    delete_confirm_dialog.getWindow().getAttributes().windowAnimations = R.style.confirmDeleteAnimation;
                    delete_confirm_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // dialogShownOnce = false;
                            delete_confirm_dialog = null;
                        }
                    });
                    /*GradientDrawable gradientDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.bottomcorner);
                    gradientDrawable.setColor(Color.BLACK);*/

                    dialog_delete_loader = (FrameLayout) delete_confirm_dialog.findViewById(R.id.delete_loader);
                    delete_dialog_layout = (LinearLayout) delete_confirm_dialog.findViewById(R.id.delete_dialog_layout);

                    Button cancel = (Button) delete_confirm_dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delete_confirm_dialog.dismiss();
                        }
                    });

                    Button delete = (Button) delete_confirm_dialog.findViewById(R.id.delete);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Delete_subuser().execute(userid, prod.getCity_id(), String.valueOf(position));
                        }
                    });

                    if (!delete_confirm_dialog.isShowing())
                        delete_confirm_dialog.show();

                } catch (Exception e) { }
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_category_id;
        private final TextView txt_category_name;
        private final ImageView action_delete;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            txt_category_id = (TextView) itemLayoutView.findViewById(R.id.txt_category_id);
            txt_category_name = (TextView) itemLayoutView.findViewById(R.id.txt_category_name);
            action_delete = (ImageView) itemLayoutView.findViewById(R.id.action_delete);
        }
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                view.animate()
                        .translationY(0).alpha(1.f)
                        .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                        .setInterpolator(new DecelerateInterpolator(2.f))
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationsLocked = true;
                            }
                        })
                        .start();
            }
        }
    }

    public class Delete_subuser extends AsyncTask<String, Void, String> {
        private String res;
        int pos;
        //ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            dialog_delete_loader.setVisibility(View.VISIBLE);
            /*mProgressDialog = ProgressDialog.show(
                    mContext, "",
                    "Loading...");*/
        }

        @Override
        protected String doInBackground(String... params) {
            pos = Integer.parseInt(params[2]);
            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/category_delete.php?userid=" + params[0]+ "&category_id=" + params[1]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {
                    dialog_delete_loader.setVisibility(View.GONE);
                    //  mProgressDialog.dismiss();
                    customtoast.ShowToast(mContext, "Error in deleting user. Please try again.", R.layout.red_toast);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    // mProgressDialog.dismiss();
                    dialog_delete_loader.setVisibility(View.GONE);
                    customtoast.ShowToast(mContext, "Error in deleting user. Please try again.", R.layout.red_toast);
                } else {

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        String errorcode = json.getString("errorcode");
                        if (errorcode.equals("0")) {
                            customtoast.ShowToast(mContext, "Deleted successfully.", R.layout.blue_toast);
                            notifyDataSetChanged();
                            category.remove(pos);
                            notifyItemRemoved(pos);
                        } else {
                            customtoast.ShowToast(mContext, json.getString("errormsg"), R.layout.red_toast);
                        }
                    }
                }

                dialog_delete_loader.setVisibility(View.GONE);
                delete_confirm_dialog.dismiss();
                // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                delete_confirm_dialog.dismiss();
                dialog_delete_loader.setVisibility(View.GONE);
                //mProgressDialog.dismiss();
                customtoast.ShowToast(mContext, "Error in deleting user. Please try again.", R.layout.red_toast);
                objEx.printStackTrace();
            }
        }
    }

}
