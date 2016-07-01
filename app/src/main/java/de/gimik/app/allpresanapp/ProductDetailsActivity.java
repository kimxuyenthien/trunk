package de.gimik.app.allpresanapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.*;
import de.gimik.app.allpresanapp.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class ProductDetailsActivity extends BaseActivity {
    public static final String TAG = ProductDetailsActivity.class.getSimpleName();

    @Bind(R.id.iv_product)
    ImageView iv_product;

    @Bind(R.id.iv_product_world_logo)
    ImageView iv_product_world_logo;

    @Bind(R.id.iv_product_icon)
    ImageView iv_product_icon;


    @Bind(R.id.iv_product_technonogy)
    ImageView iv_product_technonogy;

    @Bind(R.id.tv_product_group_title)
    TextView tv_product_group_title;

    @Bind(R.id.tv_product_name)
    TextView tv_product_name;

    @Bind(R.id.tv_product_number)
    TextView tv_product_number;

//    @Bind(R.id.tv_product_title)
//    TextView tv_product_title;

    @Bind(R.id.wvDescription)
    WebView wvDescription;

    @Bind(R.id.wvIngredient)
    WebView wvIngredient;

    @Bind(R.id.lv_eans)
    ListView lv_eans;

    @Bind(R.id.lv_ingredients)
    ListView lv_ingredients;

    @Bind(R.id.tv_key_ingredients)
    View tv_key_ingredients;

    @Bind(R.id.rlProductWorldTitle)
    RelativeLayout rlProductWorldTitle;

    @Bind(R.id.ivProductWorldTitle)
    ImageView ivProductWorldTitle;

    private List<Ean> eans;
    private ListViewAdapter adapter;

    private List<Ingredient> ingredients;
    private ArrayAdapter<String> ingredientsAdapter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    Product product = null;
    ProductWorld productWorld;
    ProductGroup productGroup;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.product);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        ProductDbAdapter productDbAdapter = new ProductDbAdapter(this);
        ProductWorldDbAdapter productWorldDbAdapter = new ProductWorldDbAdapter(this);
        Bundle bundle = getIntent().getExtras();
        long productId;
        String productGroupName = "";


        if (bundle != null) {
            productId = bundle.getLong(Constant.PRODUCT_ID);
            product = productDbAdapter.queryForId(productId);
            productGroupName = bundle.getString(Constant.PRODUCT_GROUP_NAME);
        }

        if (product != null) {
            setTitle(product.getName());
            File file = new File(getDownloadBasePath(), product.getLocalImage());
            Utils.loadImageFromFile(this, iv_product, file, product.getImage());

            productGroup = new ProductGroupDbAdapter(ProductDetailsActivity.this).queryForId(product.getProductGroupId());
            final long productWorldId = productGroup.getProductWorldId();
            productWorld = productWorldDbAdapter.queryForId(productWorldId);

            final File fileLogo = new File(getDownloadBasePath(), productWorld.getLocalLogo());
            Utils.loadImageFromFile(this, iv_product_world_logo, fileLogo, productWorld.getLogo());

            File fileIcon = new File(getDownloadBasePath(), product.getLocalIcon());
            Utils.loadImageFromFile(this, iv_product_icon, fileIcon, product.getIcon());

            if (product.isTechnology())
                iv_product_technonogy.setVisibility(View.VISIBLE);
            else {
                iv_product_technonogy.setVisibility(View.GONE);
            }

            if (product.getIcon() == null || product.getIcon().trim().equals(""))
                tv_product_number.setVisibility(View.GONE);
            else
                tv_product_number.setVisibility(View.VISIBLE);

            File worldIcon = new File(getDownloadBasePath(), productWorld.getLocalIcon());
            if (Utils.isNullOrEmpty(productWorld.getIcon())){
                rlProductWorldTitle.setVisibility(View.GONE);
            }
            else if (!worldIcon.exists()) {
                Ion.with(this)
                        .load(productWorld.getIcon())
                        .write(worldIcon)
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
                                    rlProductWorldTitle.setVisibility(View.VISIBLE);
                                    imageView.setColorFilter(Color.parseColor(productGroup.getColor()));
                                } else {
                                    rlProductWorldTitle.setVisibility(View.GONE);
                                    imageView.setVisibility(View.GONE);
                                }
                            }
                        }.init(ivProductWorldTitle));
            } else {
                Ion.with(ivProductWorldTitle).load(worldIcon.toURI().toString());
                rlProductWorldTitle.setVisibility(View.VISIBLE);
                ivProductWorldTitle.setColorFilter(Color.parseColor(productGroup.getColor()));
            }

            tv_product_group_title.setText(productGroupName);
            tv_product_number.setText(productGroup.getTitle());
            tv_product_name.setText(product.getName());
//            tv_product_title.setText(product.getTitle());
            wvDescription.setWebViewClient(new MyWebViewClient());
            wvDescription.getSettings().setJavaScriptEnabled(true);
            wvDescription.loadData(product.getDescription(), "text/html; charset=utf-8", null);
            wvDescription.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            wvDescription.getSettings().setDefaultTextEncodingName("utf-8");
            wvDescription.setBackgroundColor(0x00000000);
            wvDescription.getSettings().setDefaultFontSize(14);
            if (Build.VERSION.SDK_INT >= 11) {
                wvDescription.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                wvIngredient.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            }
            wvIngredient.setWebViewClient(new MyWebViewClient());
            wvIngredient.getSettings().setJavaScriptEnabled(true);
            wvIngredient.loadData("<b>Ingredients</b>: " + product.getComposition(), "text/html; charset=utf-8", null);
            wvIngredient.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            wvIngredient.getSettings().setDefaultTextEncodingName("utf-8");
            wvIngredient.setBackgroundColor(0x00000000);
            wvIngredient.getSettings().setDefaultFontSize(12);

            Ean title = new Ean();
            title.setItem(getText(R.string.item).toString());
            title.setSize(getText(R.string.size).toString());
            List<Ean> eans = new EanDbAdapter(ProductDetailsActivity.this).queryByProductId(product.getId());
            if (eans != null && eans.size() > 0) {
                lv_eans.setVisibility(View.VISIBLE);
                eans.add(0, title);
                adapter = new ListViewAdapter(ProductDetailsActivity.this, eans);
                lv_eans.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                lv_eans.setVisibility(View.GONE);
            }

            ingredients = new IngredientDbAdapter(ProductDetailsActivity.this).queryByProductId(product.getId());

            if (ingredients != null && ingredients.size() > 0) {
                List<String> ingredientNames = new ArrayList<String>();
                for (Ingredient ingredient : ingredients)
                    ingredientNames.add(ingredient.getName());
                lv_ingredients.setVisibility(View.VISIBLE);
                tv_key_ingredients.setVisibility(View.VISIBLE);
                ingredientsAdapter = new ArrayAdapter<String>(ProductDetailsActivity.this, R.layout.row_ingredient2, R.id.tv_name, ingredientNames);
                lv_ingredients.setAdapter(ingredientsAdapter);
                ingredientsAdapter.notifyDataSetChanged();

                lv_ingredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Ingredient ingredient = ingredients.get(position);
                        AlertUtils.showAlertDialog(ProductDetailsActivity.this, ingredient.getName(), ingredient.getDescription());
//                        Bundle bundle = new Bundle();
//                        bundle.putLong(Constant.INGREDIENT_ID, ingredient.getId());
//                        replaceFragment(new IngredientsDetailsFragment(), IngredientsDetailsFragment.TAG, false, true, bundle);
                    }
                });
            } else {
                lv_ingredients.setVisibility(View.GONE);
                tv_key_ingredients.setVisibility(View.GONE);
            }
        } else {
            setTitle(getString(R.string.product));
        }


    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setBackgroundColor(0x00000000);
            if (Build.VERSION.SDK_INT >= 11) view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
    }


    public class ListViewAdapter extends BaseAdapter {
        Context context;
        List<Ean> items;

        public ListViewAdapter(Context context, List<Ean> items) {
            super();
            this.context = context;
            this.items = items;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return items == null ? 0 : items.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        private class ViewHolder {
            TextView txtItem;
            TextView txtSize;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Ean ean = (Ean) items.get(position);
            // TODO Auto-generated method stub
            ViewHolder holder;
            LayoutInflater inflater = LayoutInflater.from(context);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_eans, null);
                holder = new ViewHolder();
                holder.txtItem = (TextView) convertView.findViewById(R.id.tv_ean_item);
                holder.txtSize = (TextView) convertView.findViewById(R.id.tv_ean_size);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtItem.setText(ean.getItem());
            holder.txtSize.setText(ean.getSize());

            return convertView;
        }

    }


}
