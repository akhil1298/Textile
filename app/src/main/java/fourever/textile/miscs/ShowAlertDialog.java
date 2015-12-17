package fourever.textile.miscs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ShowAlertDialog {

	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		alertDialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				 dialog.cancel();
				//System.exit(0);
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
