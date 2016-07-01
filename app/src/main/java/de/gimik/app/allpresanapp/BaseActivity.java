package de.gimik.app.allpresanapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.fragment.BaseFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Dang on 07.10.2015.
 */
public class BaseActivity extends AppCompatActivity {
    String newImagePath;
    String mDirectoryPath;

    private File photo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Checks if there is an external storage on the device.
     *
     * @return <code>true</code> if the storage is available, <code>false</code>
     * otherwise.
     */
    public boolean isExternalStorage() {
        String externalStorageState = Environment.getExternalStorageState();

        // check whether to use the external or internal storage
        // we can read and write to external storage
        // we can neither read nor write to external storage, use internal
        // storage
        return externalStorageState.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Returns the path to the external application storage of the device.
     *
     * @return the path.
     */
    public String getExternalStoragePath() {

        if (isExternalStorage()) {
            return this.getExternalFilesDir("").getAbsolutePath();
        }

        return null;
    }

    public String getInternalStoragePath() {
        return getCacheDir().getAbsolutePath();
    }

    /**
     * Returns the path to the available application storage of the device,
     * either external or internal.
     *
     * @return the path.
     */
    protected String getAvailableStoragePath() {
        String storagePath;

        if (isExternalStorage()) {
            storagePath = getExternalStoragePath();
        } else {
            storagePath = getInternalStoragePath();
        }

        return storagePath;
    }

    public String getImagesBasePath() {
        return getExternalStoragePath() + "/images";
    }

    public String getDownloadBasePath() {
        return getExternalStoragePath() + "/download";
    }


    public BaseFragment getTopFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null)
            return null;
        int count = fragmentManager.getBackStackEntryCount();
        BaseFragment topFragment = null;
        if (count > 0) {
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(count - 1);
            String str = backEntry.getName();
            topFragment = (BaseFragment) fragmentManager.findFragmentByTag(str);
            if (topFragment == null)
                topFragment = (BaseFragment) fragmentManager.findFragmentById(backEntry.getId());
        }
        return topFragment;
    }

    public void closeKeyBoard() {
        if (this.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }
    protected void startCameraActivity(String photoName) {
        mDirectoryPath = BaseActivity.this.getImagesBasePath();
        newImagePath = mDirectoryPath + "/" + photoName + ".jpg";
        photo = new File(newImagePath);
        File parent = photo.getParentFile();
        if (!parent.exists()) {
            boolean ret = parent.mkdir();
            Log.d("startCameraActivity", "parent: " + parent.getAbsolutePath() + " " + ret);
        }
        if (photo.exists()) {
            photo.delete();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, Constant.takePhoto);

    }
}
