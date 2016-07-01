package de.gimik.app.allpresanapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import de.gimik.app.allpresanapp.R;


public class AlertUtils {

	public static void showInfoAlertDialog(Context context, int alert) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.info);
		builder.setMessage(alert);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	public static void showAlertDialog(Context context, int title, int alert) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(alert);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	public static void showAlertDialog(Context context, String title, String alert) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(alert);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

    public static void showConfirmDialog(Context context, int messageResId,
                                         final OnConfirmClickListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.confirm_title);
        builder.setMessage(messageResId);
        builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.onPositiveButtonClicked();
					}
				});
        builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
        builder.create().show();
    }
	/**
	 * Show an alert dialog confirm ic_delete
	 * 
	 * @param callback
	 */
	public static void showConfirmDialog(Context context,
			final OnConfirmClickListener callback) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.confirm_title);
		builder.setMessage(R.string.confirm_content);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.onPositiveButtonClicked();
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	/**
	 * Show an alert dialog confirm ic_delete
	 * 
	 * @param
	 */
	public static void showConfirmAlertDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.confirm_title);
		builder.setMessage(R.string.confirm_selected);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	/**
	 * Show an alert dialog confirm ic_delete
	 *
	 * @param
	 */
	public static void showErrorAlertDialog(Context context, int alert) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.error);
		builder.setMessage(alert);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	/**
	 * Show an alert dialog confirm ic_delete
	 *
	 * @param
	 */
	public static void showErrorAlertDialog(Context context, String alert) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.error);
		builder.setMessage(alert);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
	/**
	 * Show an alert dialog confirm ic_delete
	 *
	 * @param
	 */
	public static void showErrorAlertDialog(Context context, int title, int alert) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(alert);
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
	/**
	 * interface for handling alert callback
	 * 
	 * @author maythy
	 *
	 */
	public static interface OnConfirmClickListener {
		public void onPositiveButtonClicked();

	}
}
