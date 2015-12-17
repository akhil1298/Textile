package fourever.textile.miscs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fourever.textile.mainclasses.R;

public class customtoast {

	public static void ShowToast(Context context, String message, int layout) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		// Call toast.xml file for toast layout
		View toastRoot = inflater.inflate(layout, null);

		Toast toast = new Toast(context);
		TextView msg = (TextView)toastRoot.findViewById(R.id.toastmsg);
		msg.setText(message);
		// Set layout to toast
		toast.setView(toastRoot);
       /* toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);*/
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

}
