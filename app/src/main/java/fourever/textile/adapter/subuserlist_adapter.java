package fourever.textile.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import fourever.textile.entity.post_comments_entity;
import fourever.textile.entity.post_like_entity;
import fourever.textile.entity.subuser_entity;
import fourever.textile.mainclasses.R;
import fourever.textile.miscs.AppController;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

/**
 * Created by akhil on 2/2/16.
 */
public class subuserlist_adapter extends RecyclerView.Adapter<subuserlist_adapter.ViewHolder> {

    private final SharedPreferences Loginprefs;
    private final String userid;
    private Context mContext;
    private List<subuser_entity> subuser;

    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private FrameLayout dialog_delete_loader, edit_dialog_loader;
    private LinearLayout delete_dialog_layout;
    private Dialog delete_confirm_dialog = null;
    private Dialog edit_dialog = null;

    public subuserlist_adapter(Context c, List<subuser_entity> subuser) {

        mContext = c;
        this.subuser = subuser;

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
                .inflate(R.layout.subuser_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(subuserlist_adapter.ViewHolder holder, final int position) {

        final subuser_entity prod = subuser.get(position);
        // holder.profileimg.setImageUrl("http://4eversolutions.co.in/projects/TextileApp/profile_pictures/" + prod.getCommenter_pic(), imageLoader);
        holder.txt_subuser_id.setText(prod.getSub_user_id());
        holder.txt_subuser_name.setText(prod.getSubuser_name());
        holder.txt_subusername.setText(prod.getUsername());
        holder.txt_subuserpassword.setText(prod.getPassword());

        holder.action_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edit_dialog == null) {
                        edit_dialog = new Dialog(mContext);
                    }

                    edit_dialog.setCancelable(false);
                    edit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    edit_dialog.setContentView(R.layout.edit_subuser_layout);
                    edit_dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    edit_dialog.getWindow().getAttributes().windowAnimations = R.style.confirmDeleteAnimation;

                    edit_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                           // dialogShownOnce = false;
                            edit_dialog = null;
                        }
                    });
                    /*GradientDrawable gradientDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.bottomcorner);
                    gradientDrawable.setColor(Color.BLACK);*/
                    edit_dialog_loader = (FrameLayout) edit_dialog.findViewById(R.id.edit_dialog_loader);

                    final EditText subuser_name = (EditText)edit_dialog.findViewById(R.id.subuser_name);
                    final EditText subusername = (EditText)edit_dialog.findViewById(R.id.subusername);
                    final EditText password = (EditText)edit_dialog.findViewById(R.id.password);

                    subuser_name.setText(prod.getSubuser_name());
                    subusername.setText(prod.getUsername());
                    password.setText(prod.getPassword());

                    Button cancel = (Button) edit_dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            edit_dialog.dismiss();
                        }
                    });

                    Button save = (Button) edit_dialog.findViewById(R.id.save);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                              new Edit_subuser().execute(prod.getSub_user_id(), subuser_name.getText().toString().trim(), subusername.getText().toString().trim(), password.getText().toString().trim());
                        }
                    });

                    if (!edit_dialog.isShowing())
                        edit_dialog.show();

                } catch (Exception e) { }
            }
        });

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
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                            new Delete_subuser().execute(prod.getSub_user_id(), String.valueOf(position));
                        }
                    });

                    if (!delete_confirm_dialog.isShowing())
                        delete_confirm_dialog.show();

                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subuser.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_subuser_id;
        private final TextView txt_subuser_name;
        private final TextView txt_subusername;
        private final TextView txt_subuserpassword;
        private final ImageView action_edit;
        private final ImageView action_delete;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            txt_subuser_id = (TextView) itemLayoutView.findViewById(R.id.txt_subuser_id);
            txt_subuser_name = (TextView) itemLayoutView.findViewById(R.id.txt_subuser_name);
            txt_subusername = (TextView) itemLayoutView.findViewById(R.id.txt_subusername);
            txt_subuserpassword = (TextView) itemLayoutView.findViewById(R.id.txt_subuserpassword);
            action_edit = (ImageView) itemLayoutView.findViewById(R.id.action_edit);
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
            pos = Integer.parseInt(params[1]);
            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/subuser_delete.php?sub_userid=" + params[0]);
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
                            subuser.remove(pos);
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
                dialog_delete_loader.setVisibility(View.GONE);
                //mProgressDialog.dismiss();
                customtoast.ShowToast(mContext, "Error in deleting user. Please try again.", R.layout.red_toast);
                objEx.printStackTrace();
            }
        }
    }

    public class Edit_subuser extends AsyncTask<String, Void, String> {
        private String res;
        int pos;
        //ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            res = null;
            edit_dialog_loader.setVisibility(View.VISIBLE);
            /*mProgressDialog = ProgressDialog.show(
                    mContext, "",
                    "Loading...");*/
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://4eversolutions.co.in/projects/TextileApp/webservice/subuser_edit.php?sub_userid=" + params[0] + "&subusername=" + params[2] + "&subuser_name="+params[1] + "&password=" + params[3]);
            } catch (Exception e) {
                // Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                if (res == null) {
                    edit_dialog_loader.setVisibility(View.GONE);
                    //  mProgressDialog.dismiss();
                    customtoast.ShowToast(mContext, "Error in updating user detail. Please try again.", R.layout.red_toast);
                }

                JSONArray jArray = new JSONArray(res.toString());
                if (jArray.length() == 0) {
                    // mProgressDialog.dismiss();
                    edit_dialog_loader.setVisibility(View.GONE);
                    customtoast.ShowToast(mContext, "Error in updating user detail. Please try again.", R.layout.red_toast);
                } else {

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        String errorcode = json.getString("errorcode");
                        if (errorcode.equals("0")) {
                            customtoast.ShowToast(mContext, "Updated successfully.", R.layout.blue_toast);
                            notifyDataSetChanged();
                        } else {
                            customtoast.ShowToast(mContext, json.getString("errormsg"), R.layout.red_toast);
                        }
                    }
                }

                edit_dialog_loader.setVisibility(View.GONE);
                //delete_confirm_dialog.dismiss();
                edit_dialog.dismiss();
                // mProgressDialog.dismiss();
            } catch (Exception objEx) {
                edit_dialog_loader.setVisibility(View.GONE);
                //mProgressDialog.dismiss();
                customtoast.ShowToast(mContext, "Error in updating user detail. Please try again.", R.layout.red_toast);
                objEx.printStackTrace();
            }
        }
    }

}
