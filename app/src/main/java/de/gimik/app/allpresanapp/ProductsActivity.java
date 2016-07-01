package de.gimik.app.allpresanapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.ProductDbAdapter;
import de.gimik.app.allpresanapp.database.ProductGroupDbAdapter;
import de.gimik.app.allpresanapp.model.ItemMenu;
import de.gimik.app.allpresanapp.model.Product;
import de.gimik.app.allpresanapp.model.ProductGroup;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quyenDT on 9/22/2015.
 */
public class ProductsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = ProductsActivity.class.getSimpleName();

    @Bind(R.id.lv_products)
    ListView lv_products;

    //@Bind(R.id.iv_product_group)
    //ImageView iv_product_group;

    private List<Product> products;
    private ProductItemAdapter adapter;
    private ProductGroup productGroup;
    private long productGroupId;
    private List<ItemMenu> items;
    private String colour;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_products);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        items = new ArrayList();
        ProductGroupDbAdapter productGroupDbAdapter = new ProductGroupDbAdapter(this);
        ProductDbAdapter productDbAdapter = new ProductDbAdapter(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            productGroupId = bundle.getLong(Constant.PRODUCT_GROUP_ID);
            colour = bundle.getString(Constant.COLOR);
            productGroup = productGroupDbAdapter.queryForId(productGroupId);
            if (productGroup != null) {
                setTitle(productGroup.getName());
            } else
                setTitle(R.string.product_group);
        }

        products = productDbAdapter.queryByProductGroupdId(productGroupId);
        ItemMenu itemMenu = new ItemMenu();
        itemMenu.object = Constant.IMAGE;
        items.add(itemMenu);

        //productGroups = productGroupDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(products)) {
            for (Product product : products) {
                itemMenu = new ItemMenu();
                itemMenu.object = product;
                items.add(itemMenu);
            }
        }

        adapter = new ProductItemAdapter(this, 0, items);
        lv_products.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_products.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    class ProductItemAdapter extends ArrayAdapter<ItemMenu> {
        private List<ItemMenu> items;
        private LayoutInflater inflater;

        public ProductItemAdapter(Context context, int resource, List<ItemMenu> items) {
            super(context, resource, items);
            this.items = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemMenu itemMenu = items.get(position);
            if (Constant.IMAGE.equals(itemMenu.object)) {
                ImageViewHolder holder;
                if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ImageViewHolder)) {
                    holder = new ImageViewHolder();
                    convertView = inflater.inflate(R.layout.row_image, parent, false);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    convertView.setTag(holder);
                } else {
                    holder = (ImageViewHolder) convertView.getTag();
                }
                File file = new File(getDownloadBasePath(), productGroup.getLocalImage());
                Utils.loadImageFromFile(getApplicationContext(), holder.imageView, file, productGroup.getImage());
            }
            else {
                ViewHolder holder;
                if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ViewHolder)) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_products, parent, false);
                    holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_productitem_title);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                Product item = (Product) itemMenu.object;
                holder.tvTitle.setText(item.getTitle());
            }

            return convertView;
        }

        class ViewHolder {
            private TextView tvTitle;
        }

        class ImageViewHolder {
            private ImageView imageView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemMenu itemMenu = items.get(position);
        if (Constant.IMAGE.equals(itemMenu.object)) {
            //do nothing
        }  else{

            Product item = (Product) itemMenu.object;
            Intent intent = new Intent(this, ProductDetailsActivity.class);
            intent.putExtra(Constant.PRODUCT_ID, item.getId());
            intent.putExtra(Constant.PRODUCT_GROUP_ID, productGroupId);
            intent.putExtra(Constant.PRODUCT_GROUP_NAME, productGroup.getName());
            this.startActivity(intent);
        }
    }
}
