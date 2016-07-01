package de.gimik.app.allpresanapp.Utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Utils {

    public static final String TAG = Utils.class.getSimpleName();
    public static final int MY_PERMISSIONS_CAMERA = 123;


    public static String getValueForKey(Context mContext, String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    public static void saveValueForKey(Context mContext, String key, String value) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.PREFS_NAME, 0);
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveIntForKey(Context mContext, String key, int value) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.PREFS_NAME, 0);
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntForKey(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return prefs.getInt(key, -1);
    }

    public static boolean getBooleanForKey(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    public static boolean getBooleanForKey(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return prefs.getBoolean(key, defaultValue);
    }

    public static void saveBooleanForKey(Context mContext, String key, boolean value) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.PREFS_NAME, 0);
        Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static long getLongForKey(Context context, String key, long defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return prefs.getLong(key, defaultValue);
    }

    public static void saveLongForKey(Context mContext, String key, long value) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.PREFS_NAME, 0);
        Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static Date getDateForKey(Context context, String key) {
        return parseDate(getLongForKey(context, key, 0L));
    }

    public static void saveDateForKey(Context context, String key, Date date) {
        saveLongForKey(context, key, date == null ? 0L : date.getTime());
    }

    private static Date parseDate(long tick) {
        if (tick == 0)
            return null;

        return new Date(tick);
    }

    /**
     * send email with subject and content
     *
     * @param mContext
     * @param subject
     * @param content
     */
    public static void sendEmail(Context mContext, String subject,
                                 String content, String address) {
        // Create a new Intent to send messages
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        // Add attributes to the intent
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, subject));
    }

    /**
     * Create email intent with attachment file
     *
     * @param context
     * @param title
     * @param content
     * @param fileUri
     * @return
     */
    public static Intent createSendEmailIntent(Context context, String title, String content, Uri fileUri) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

//			// need this to prompts email client only
            emailIntent.setType("text/plain");

            PackageManager pm = context.getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");

            Intent openInChooser = Intent.createChooser(emailIntent, "Choose an Email client");

            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
            for (int i = 0; i < resInfo.size(); i++) {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String appName = ri.activityInfo.name;
                if (appName.contains("mail")) {
                    emailIntent.setPackage(ri.activityInfo.packageName);

                    intentList.add(new LabeledIntent(emailIntent, ri.activityInfo.packageName, ri.loadLabel(pm), ri.icon));
                }
            }

            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

            return openInChooser;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * format date to string
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatDate(long time, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(time);
    }

    /**
     * parse string to date
     *
     * @param strDate
     * @param format
     * @return date from string
     */
    public static Date dateFromString(String strDate, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parse string to Date");
        }
        return new Date();
    }

    /**
     * hide soft keyboard with focus
     *
     * @param activity
     */
    public static void hideSoftKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * hide soft keyboard without focus
     *
     * @param activity
     */
    public static void hideSoftKeyBoard2(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Check if device areadly login with google account
     *
     * @param context
     * @return
     */
    public static boolean deviceHasGoogleAccount(Context context) {
        AccountManager accMan = AccountManager.get(context);
        Account[] accArray = accMan.getAccountsByType("com.google");
        return accArray.length >= 1 ? true : false;
    }


    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static final boolean isNullOrEmpty(Collection<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }

        return false;
    }

    public static final boolean isNullOrEmpty(String text) {
        if (text != null && text.trim().length() > 0) {
            return false;
        }

        return true;
    }

    public static final boolean contains(String text, String keyword) {
        if (isNullOrEmpty(text) || isNullOrEmpty(keyword)) {
            return false;
        }

        return text.toUpperCase().contains(keyword.toUpperCase());
    }

    public static void closeStream(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (Exception ex) {
        }
    }

    public static void readAbstract(Context context, String pdfPath) {
        File file = new File(pdfPath);

        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static final String getAppVersion(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getScreenResolution(Context context) {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            return String.format("%dx%d", width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date formatStringToDate(String dateStr, String format) {
        try {
            DateFormat outputSdf = new SimpleDateFormat(format);
            return outputSdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final boolean isTrue(Integer integer) {
        return integer != null && integer > 0;
    }

    private Drawable resize(Context context, Drawable image, int requiredWidth, int requireHeight) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, requiredWidth, requireHeight, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }

    static Bitmap loadScaledBitmap(String path, int requiredWidth, int requireHeight) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) requireHeight);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) requiredWidth);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, bmpFactoryOptions);
    }

    public static Bitmap loadScaledBitmap(String path, int requiredWidth) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmpFactoryOptions);

        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) requiredWidth);

        if (widthRatio > 1) {
            bmpFactoryOptions.inSampleSize = widthRatio;

        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, bmpFactoryOptions);
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void loadImageFromFile(Context context, ImageView imageView, File imgFile, String url) {
        if (!imgFile.exists()) {
            Ion.with(context)
                    .load(url)
                    .write(imgFile)
                    .setCallback(new FutureCallback<File>() {
                        ImageView imageView;

                        public FutureCallback<File> init(ImageView imageView) {
                            this.imageView = imageView;
                            return this;
                        }

                        @Override
                        public void onCompleted(Exception e, File file) {
                            if (file != null) {
                                Ion.with(imageView).load(file.toURI().toString());
                            }
                            else{
                                imageView.setImageBitmap(null);
                            }
                        }
                    }.init(imageView));
        } else {
            Ion.with(imageView).load(imgFile.toURI().toString());
        }
    }



}