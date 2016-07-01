package de.gimik.app.allpresanapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.TreatmentContactActivity;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.DateTimeUtility;
import de.gimik.app.allpresanapp.database.ProductRecommendationDbAdapter;
import de.gimik.app.allpresanapp.model.ProductRecommendation;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class TreatmentTipFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    public static final String TAG = TreatmentTipFragment.class.getSimpleName();

    @Bind(R.id.slider)
    SliderLayout slider;

    private ProductRecommendationDbAdapter productRecommendationDbAdapter;
    @Bind(R.id.ib_camera)
    ImageButton ib_camera;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_tip, container, false);
        ButterKnife.bind(this, v);
        productRecommendationDbAdapter = getProductRecommendationDbAdapter();
        List<ProductRecommendation> productRecommendationList = productRecommendationDbAdapter.queryAll();

        if (CollectionUtils.isNotEmpty(productRecommendationList)) {

            for (ProductRecommendation productRecommendation : productRecommendationList) {
                File file = new File(getMainActivity().getDownloadBasePath(), productRecommendation.getLocalImage());

                TextSliderView textSliderView = new TextSliderView(getActivity());
                // initialize a SliderLayout
                textSliderView
                        .description(productRecommendation.getName())
                        .image(file)
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putLong(Constant.PRODUCT_RECOMMEND_ID, productRecommendation.getId());

                slider.addSlider(textSliderView);
            }
            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(4000);
            slider.addOnPageChangeListener(this);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.treatment_tip));
        ib_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assume thisActivity is the current activity
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            Constant.CAMERA_REQUEST_CODE);
                } else {
                    startCameraActivity(DateTimeUtility.Format_yyyyMMdd_HHmmss.format(new Date()));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.treatment_tip);
    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        replaceFragment(new TreatmentDetailFragment(), TreatmentDetailFragment.TAG, false, true, slider.getBundle());
        //Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        try {
            if (requestCode == Constant.takePhoto) {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IMAGE_CAMERA_PATH, newImagePath);
                    intent = new Intent(getActivity(), TreatmentContactActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    AlertUtils.showErrorAlertDialog(mContext, R.string.take_photo_error);
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constant.CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                startCameraActivity(DateTimeUtility.Format_yyyyMMdd_HHmmss.format(new Date()));
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }

}
