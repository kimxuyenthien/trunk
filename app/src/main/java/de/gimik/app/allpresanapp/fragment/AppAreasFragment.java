package de.gimik.app.allpresanapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.AppAreaDetailActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.TreatmentContactActivity;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.model.ItemMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quyenDT on 9/22/2015.
 */
public class AppAreasFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = AppAreasFragment.class.getSimpleName();

    @Bind(R.id.lv_areas)
    ListView lv_areas;

    List<ItemMenu> items;
    AppAreaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_areas, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        setTitle(R.string.app_areas);
        items = new ArrayList();
        items.add(new ItemMenu(getString(R.string.sensitive_skin)));
        items.add(new ItemMenu(getString(R.string.dry_skin)));
        items.add(new ItemMenu(getString(R.string.cornea_fissures)));
        items.add(new ItemMenu(getString(R.string.diabetic_skin)));
        items.add(new ItemMenu(getString(R.string.athlete_foot)));
        items.add(new ItemMenu(getString(R.string.nail_problems)));
        items.add(new ItemMenu(getString(R.string.foot_sweaty_odor)));
        items.add(new ItemMenu(getString(R.string.sport)));
        items.add(new ItemMenu(getString(R.string.wohl_fuehl_pflege)));
        items.add(new ItemMenu(getString(R.string.send_a_picture)));

        adapter = new AppAreaAdapter(getActivity(), 0, items);
        lv_areas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_areas.setOnItemClickListener(this);
    }

    class AppAreaAdapter extends ArrayAdapter<ItemMenu> {
        private Context context;
        private List<ItemMenu> items;
        private LayoutInflater inflater;

        public AppAreaAdapter(Context context, int resource, List<ItemMenu> items) {
            super(context, resource, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void updateItems(List<ItemMenu> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_list_view, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            vh.updateView(position);

            return convertView;
        }

        class ViewHolder {
            private ImageView ivIcon;
            private TextView tvTitle;
            private ImageView ivRightBlack;
            private ImageView ivRightWhite;

            public ViewHolder(View view) {
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                ivRightBlack = (ImageView) view.findViewById(R.id.ivRightBlack);
                ivRightWhite = (ImageView) view.findViewById(R.id.ivRightWhite);
            }

            public void updateView(int position) {
                ItemMenu item = items.get(position);
                tvTitle.setText(item.getTitle());
                tvTitle.setTextColor(item.selected ? Color.WHITE : Color.BLACK);
                ivRightBlack.setVisibility(item.selected ? View.GONE : View.VISIBLE);
                ivRightWhite.setVisibility(!item.selected ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        switch (position) {
            case 0:
                showDetail(R.string.sensitive_skin, R.layout.fragment_app_area_detail_sensitive_skin);
                break;
            case 1:
                showDetail(R.string.dry_skin, R.layout.fragment_app_area_detail_dry_skin);
                break;
            case 2:
                showDetail(R.string.cornea_fissures, R.layout.fragment_app_area_detail_cornea_fissures);
                break;
            case 3:
                showDetail(R.string.diabetic_skin, R.layout.fragment_app_area_detail_diabetic_skin);
                break;
            case 4:
                showDetail(R.string.athlete_foot, R.layout.fragment_app_area_detail_athlete_foot);
                break;
            case 5:
                showDetail(R.string.nail_problems, R.layout.fragment_app_area_detail_nail_problem);
                break;
            case 6:
                showDetail(R.string.foot_sweaty_odor, R.layout.fragment_app_area_detail_foot_sweaty_odor);
                break;
            case 7:
                showDetail(R.string.sport, R.layout.fragment_app_area_detail_sport);
                break;
            case 8:
                showDetail(R.string.wohl_fuehl_pflege, R.layout.fragment_app_area_detail_wohl_fuehl_pflege);
                break;
            case 9:
                showImageSender();
                break;
        }
    }

    private void showDetail(int titleResId, int viewResId) {

        Intent intent = new Intent(getActivity(), AppAreaDetailActivity.class);
        intent.putExtra(Constant.ExtraKey.LAYOUT_ID, viewResId);
        intent.putExtra(Constant.ExtraKey.TITLE_ID, titleResId);
        startActivity(intent);

    }

    private void showImageSender() {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA);
        if (permissionCheck!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    Constant.CAMERA_REQUEST_CODE);
        }
        else{
//            startCameraActivity(DateTimeUtility.Format_yyyyMMdd_HHmmss.format(new Date()));
            showImageSenderScreen();
        }
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
//                startCameraActivity(DateTimeUtility.Format_yyyyMMdd_HHmmss.format(new Date()));
                showImageSenderScreen();
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }
    
    private void showImageSenderScreen() {
        Intent intent = new Intent(getActivity(), TreatmentContactActivity.class);
        startActivity(intent);
    }
}
