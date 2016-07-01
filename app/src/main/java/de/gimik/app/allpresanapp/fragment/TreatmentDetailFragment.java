package de.gimik.app.allpresanapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.ProductDetailsActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.ProductDbAdapter;
import de.gimik.app.allpresanapp.database.ProductRecommendationDbAdapter;
import de.gimik.app.allpresanapp.model.Product;
import de.gimik.app.allpresanapp.model.ProductRecommendation;

import java.io.File;
import java.util.List;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class TreatmentDetailFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = TreatmentDetailFragment.class.getSimpleName();

    @Bind(R.id.lv_tip_products)
    ListView lv_tip_products;

    @Bind(R.id.iv_tip_detail)
    ImageView iv_tip_detail;

    @Bind(R.id.tv_tip_detail)
    TextView tv_tip_detail;

    private ProductRecommendationDbAdapter productRecommendationDbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_detail, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        Long productRecommentId = bundle.getLong(Constant.PRODUCT_RECOMMEND_ID, 0);
        if (productRecommentId != 0) {

            productRecommendationDbAdapter = getProductRecommendationDbAdapter();
            ProductRecommendation productRecommendation = productRecommendationDbAdapter.queryForId(productRecommentId);
            File imgFile = new File(getMainActivity().getDownloadBasePath(), productRecommendation.getLocalImage());

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_tip_detail.setImageBitmap(myBitmap);
            }
            if (productRecommendation.getName()!=null)
                tv_tip_detail.setText(productRecommendation.getName());
            else
                tv_tip_detail.setVisibility(View.GONE);

            ProductDbAdapter productDbAdapter = getProductDbAdapter();
            List<Product> products = productDbAdapter.queryByProductRecommend(productRecommentId);
            ProductAdapter adapter = new ProductAdapter(getActivity(), products);
            lv_tip_products.setAdapter(adapter);
            lv_tip_products.setOnItemClickListener(this);
            adapter.setNotifyOnChange(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.treatment_tip);
    }

    class ProductAdapter extends ArrayAdapter<Product> {
        private Context context;
        private List<Product> products;
        private LayoutInflater inflater;

        public ProductAdapter(Context context, List<Product> products) {
            super(context, 0, products);
            this.products = products;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return products == null ? 0 : products.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Product getItem(int position) {
            return products.get(position);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_product_tip, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            vh.updateView(position);

            return convertView;
        }

        class ViewHolder {
            private TextView txtProduct;

            public ViewHolder(View view) {
                txtProduct = (TextView) view.findViewById(R.id.txtProduct);
            }

            public void updateView(int position) {
                final Product item = products.get(position);
                txtProduct.setText(item.getName());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Bundle bundle = new Bundle();
//        bundle.putLong(Constant.PRODUCT_ID, id);
//        replaceFragment(new ProductDetailsActivity(), ProductDetailsActivity.TAG, false, true, bundle);
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra(Constant.PRODUCT_ID, id);
        getActivity().startActivity(intent);
    }
}
