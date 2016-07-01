package de.gimik.app.allpresanapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.MainActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.EanDbAdapter;
import de.gimik.app.allpresanapp.database.EventDbAdapter;
import de.gimik.app.allpresanapp.database.IngredientDbAdapter;
import de.gimik.app.allpresanapp.database.MemberDbAdapter;
import de.gimik.app.allpresanapp.database.ProductDbAdapter;
import de.gimik.app.allpresanapp.database.ProductGroupDbAdapter;
import de.gimik.app.allpresanapp.database.ProductIngredientDbAdapter;
import de.gimik.app.allpresanapp.database.ProductRecommendationDbAdapter;
import de.gimik.app.allpresanapp.database.ProductWorldDbAdapter;
import de.gimik.app.allpresanapp.database.ServiceQuestionDbAdapter;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class BaseFragment extends Fragment {
    public interface OnDisplay {
        void onDisplay();
    }

    private OnDisplay onDisplay;

    public BaseActivity mContext;

    String newImagePath;
    String mDirectoryPath;
    private File photo;

    private String mTitle;
    private MemberDbAdapter memberDbAdapter;
    private ProductRecommendationDbAdapter productRecommendationDbAdapter;
    private ProductDbAdapter productDbAdapter;
    private ProductWorldDbAdapter productWorldDbAdapter;
    private EventDbAdapter eventDbAdapter;
    private ProductGroupDbAdapter productGroupDbAdapter;
    private ProductIngredientDbAdapter productIngredientDbAdapter;
    private IngredientDbAdapter ingredientDbAdapter;
    private ServiceQuestionDbAdapter serviceQuestionDbAdapter;
    private EanDbAdapter eanDbAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BaseActivity) getActivity();
    }

    public void selectMenuItem(int position) {
        ((MainActivity) mContext).selectMenuItem(position);
    }

    public void switchMenuItem(int position) {
        ((MainActivity) mContext).switchMenuItem(position);
    }

    public void replaceFragment(BaseFragment newFragment, String TAG, boolean clearBackStack, boolean addToBackStack, Bundle bundle) {

        ((MainActivity) mContext).replaceFragment(newFragment, TAG, clearBackStack, addToBackStack, bundle);
    }

    public void setTitle(String title) {
        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(title);
        this.mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(int title) {
        mContext.setTitle(title);
        this.mTitle = getText(title).toString();
    }

    public String getDownloadBasePath() {
        return getBaseActivity().getDownloadBasePath();
    }

    public MemberDbAdapter getMemberDbAdapter() {
        if (memberDbAdapter == null)
            memberDbAdapter = new MemberDbAdapter(getBaseActivity());
        return memberDbAdapter;
    }

    public ProductRecommendationDbAdapter getProductRecommendationDbAdapter() {
        if (productRecommendationDbAdapter == null)
            productRecommendationDbAdapter = new ProductRecommendationDbAdapter(getBaseActivity());
        return productRecommendationDbAdapter;
    }

    public IngredientDbAdapter getIngredientDbAdapter() {
        if (ingredientDbAdapter == null)
            ingredientDbAdapter = new IngredientDbAdapter(getBaseActivity());
        return ingredientDbAdapter;
    }

    public ProductWorldDbAdapter getProductWorldDbAdapter() {
        if (productWorldDbAdapter == null)
            productWorldDbAdapter = new ProductWorldDbAdapter(getBaseActivity());
        return productWorldDbAdapter;
    }

    public ProductGroupDbAdapter getProductGroupDbAdapter() {
        if (productGroupDbAdapter == null)
            productGroupDbAdapter = new ProductGroupDbAdapter(getBaseActivity());
        return productGroupDbAdapter;
    }

    public ProductDbAdapter getProductDbAdapter() {
        if (productDbAdapter == null)
            productDbAdapter = new ProductDbAdapter(getBaseActivity());
        return productDbAdapter;
    }

    public ServiceQuestionDbAdapter getServiceQuestionDbAdapter() {
        if (serviceQuestionDbAdapter == null)
            serviceQuestionDbAdapter = new ServiceQuestionDbAdapter(getBaseActivity());
        return serviceQuestionDbAdapter;
    }

    public EventDbAdapter getEventDbAdapter() {
        if (eventDbAdapter == null)
            eventDbAdapter = new EventDbAdapter(getBaseActivity());
        return eventDbAdapter;
    }

    public EanDbAdapter getEanDbAdapter() {
        if (eanDbAdapter == null)
            eanDbAdapter = new EanDbAdapter(getBaseActivity());
        return eanDbAdapter;
    }

    public ProductIngredientDbAdapter getProductIngredientDbAdapter() {
        if (productIngredientDbAdapter == null)
            productIngredientDbAdapter = new ProductIngredientDbAdapter(getBaseActivity());
        return productIngredientDbAdapter;
    }
    public void closeKeyBoard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public MainActivity getMainActivity() {
        if (mContext instanceof MainActivity) {
            return (MainActivity) mContext;
        }
        return null;
    }

    public OnDisplay getOnDisplay() {
        return onDisplay;
    }

    public void setOnDisplay(OnDisplay onDisplay) {
        this.onDisplay = onDisplay;
    }

    protected void startCameraActivity(String photoName) {
        mDirectoryPath = mContext.getImagesBasePath();
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
    public Bitmap convertBitmap(String path)   {

        Bitmap bitmap=null;
        BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;                     //Disable Dithering mode
        bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
        bfOptions.inTempStorage=new byte[32 * 1024];


        File file=new File(path);
        FileInputStream fs=null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if(fs!=null)
            {
                bitmap=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }
    protected boolean checkValidEmail(String emailAddress) {
        if (emailAddress == null || emailAddress.toString().equals("")) {
            AlertUtils.showErrorAlertDialog(getActivity(), getString(R.string.email_address_empty));
            return false;
        }
        return true;
    }


    protected final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }
}
