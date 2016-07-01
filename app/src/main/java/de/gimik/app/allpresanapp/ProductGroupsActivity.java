package de.gimik.app.allpresanapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.ProductDbAdapter;
import de.gimik.app.allpresanapp.database.ProductGroupDbAdapter;
import de.gimik.app.allpresanapp.database.ProductWorldDbAdapter;
import de.gimik.app.allpresanapp.model.ItemMenu;
import de.gimik.app.allpresanapp.model.Product;
import de.gimik.app.allpresanapp.model.ProductGroup;
import de.gimik.app.allpresanapp.model.ProductWorld;

/**
 * Created by quyenDT on 9/22/2015.
 */
public class ProductGroupsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = ProductGroupsActivity.class.getSimpleName();

    @Bind(R.id.lv_product_groups)
    ListView lv_product_groups;

    List<ProductGroup> productGroups;
    ProductGroupItemAdapter adapter;
    ProductGroupDbAdapter productGroupDbAdapter;
    ProductWorld productWorld;
    long productWorldId;

    List<ItemMenu> items;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_groups);
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
        ProductWorldDbAdapter productWorldDbAdapter = new ProductWorldDbAdapter(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            productWorldId = bundle.getLong(Constant.PRODUCT_WORLD_ID);
            productWorld = productWorldDbAdapter.queryForId(productWorldId);
            if (productWorld != null) {
                setTitle(productWorld.getName());

            }
        }

        ItemMenu itemMenu = new ItemMenu();
        itemMenu.object = Constant.IMAGE;
        items.add(itemMenu);

        productGroups = productGroupDbAdapter.queryByProductWorldId(productWorldId);

        //productGroups = productGroupDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productGroups)) {
            for (ProductGroup productGroup : productGroups) {
                itemMenu = new ItemMenu();
                itemMenu.object = productGroup;
                items.add(itemMenu);
            }
        }

        adapter = new ProductGroupItemAdapter(this, 0, items, productWorld);
        lv_product_groups.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_product_groups.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    class ProductGroupItemAdapter extends ArrayAdapter<ItemMenu> {
        private Context context;
        private List<ItemMenu> items;
        private LayoutInflater inflater;
        private ProductWorld productWorld;

        public ProductGroupItemAdapter(Context context, int resource, List<ItemMenu> items, ProductWorld productWorld) {
            super(context, resource, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.productWorld = productWorld;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemMenu itemMenu = items.get(position);
            if (Constant.IMAGE.equals(itemMenu.object)) {
                ImageViewHolder holder;
                if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ImageViewHolder)) {
                    holder = new ImageViewHolder();
                    convertView = inflater.inflate(R.layout.row_image2, parent, false);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    holder.txtProductGroupTitle = (TextView) convertView.findViewById(R.id.txtProductGroupTitle);
                    holder.txtProductGroupTitle.setVisibility(View.VISIBLE);
                    String groupTitle = productWorld.getProductGroupTitle();
                    String title;
                    int undIndex = groupTitle.indexOf("und");
                    title = groupTitle.substring(0,undIndex) + "\n" + groupTitle.substring(undIndex,groupTitle.length());
                    holder.txtProductGroupTitle.setText(title);

                    holder.productGroupSubTitle = (TextView) convertView.findViewById(R.id.productGroupSubTitle);
                    holder.productGroupSubTitle.setVisibility(View.VISIBLE);
                    holder.productGroupSubTitle.setText(productWorld.getProductGroupSubTitle());

                    //set color if product world is pedione
                    if (productWorld.getName().toLowerCase().contains(Constant.PRODUCT_WORLD_PEDIONE)) {
                        holder.productGroupSubTitle.setTextColor(Color.parseColor(Constant.PRODUCT_WORLD_COLOR));
                        holder.txtProductGroupTitle.setTextColor(Color.parseColor(Constant.PRODUCT_WORLD_COLOR));
                    }

                    convertView.setTag(holder);
                } else {
                    holder = (ImageViewHolder) convertView.getTag();
                }

                // product world
                File file = new File(getDownloadBasePath(), productWorld.getLocalGroupImage());
                Utils.loadImageFromFile(getApplicationContext(), holder.imageView, file, productWorld.getProductGroupImage());

            } else {
                final ViewHolder holder;
                if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ViewHolder)) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_product_group, parent, false);
                    holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_productgroupitem_title);
                    holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_productgroupitem_number);
                    holder.rlProductWorldTitle = (RelativeLayout) convertView.findViewById(R.id.rlProductWorldTitle);
                    holder.ivProductWorldTitle = (ImageView) convertView.findViewById(R.id.ivProductWorldTitle);
                    holder.ivRightBlack = (ImageView) convertView.findViewById(R.id.ivRightBlack);
                    holder.ivRightWhite = (ImageView) convertView.findViewById(R.id.ivRightWhite);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final ProductGroup item = (ProductGroup) itemMenu.object;
                holder.tvTitle.setText(item.getName());
                holder.tvNumber.setText(String.valueOf(item.getTitle()));
                if (productWorld.getIcon() == null || productWorld.getIcon().trim().equals("")) {
                    holder.tvNumber.setVisibility(View.GONE);
                } else {
                    holder.tvNumber.setVisibility(View.VISIBLE);
                }

                ProductDbAdapter productDbAdapter = new ProductDbAdapter(ProductGroupsActivity.this);
                List<Product> products = productDbAdapter.queryByProductGroupdId(item.getId());
                if (products == null || products.size() <= 0) {
                    holder.ivRightBlack.setVisibility(View.GONE);
                    holder.ivRightWhite.setVisibility(View.GONE);
                } else {
                    holder.ivRightBlack.setVisibility(itemMenu.selected ? View.GONE : View.VISIBLE);
                    holder.ivRightWhite.setVisibility(!itemMenu.selected ? View.GONE : View.VISIBLE);
                }

                File worldIcon = new File(getDownloadBasePath(), productWorld.getLocalIcon());
                if (Utils.isNullOrEmpty(productWorld.getIcon())){
                    holder.ivProductWorldTitle.setVisibility(View.GONE);
                }
                else if (!worldIcon.exists()) {
                    Ion.with(context)
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
                                        holder.rlProductWorldTitle.setVisibility(View.VISIBLE);
                                        holder.ivProductWorldTitle.setColorFilter(Color.parseColor(item.getColor()));
                                    } else {
                                        holder.ivProductWorldTitle.setVisibility(View.GONE);
                                    }
                                }
                            }.init(holder.ivProductWorldTitle));
                } else {
                    Ion.with(holder.ivProductWorldTitle).load(worldIcon.toURI().toString());
                    holder.rlProductWorldTitle.setVisibility(View.VISIBLE);
                    holder.ivProductWorldTitle.setColorFilter(Color.parseColor(item.getColor()));
                }
            }

            return convertView;
        }

        class ViewHolder {
            private TextView tvTitle;
            private TextView tvNumber;
            RelativeLayout rlProductWorldTitle;
            ImageView ivProductWorldTitle;
            private ImageView ivRightBlack;
            private ImageView ivRightWhite;
        }

        class ImageViewHolder {
            private ImageView imageView;
            private TextView txtProductGroupTitle;
            private TextView productGroupSubTitle;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemMenu itemMenu = items.get(position);
        if (Constant.IMAGE.equals(itemMenu.object) || itemMenu.object instanceof ProductWorld) {
            //do nothing
        } else {

            ProductGroup item = (ProductGroup) itemMenu.object;

            ProductDbAdapter productDbAdapter = new ProductDbAdapter(this);
            List<Product> products = productDbAdapter.queryByProductGroupdId(item.getId());
            Intent intent = new Intent(this, ProductsActivity.class);
            intent.putExtra(Constant.PRODUCT_GROUP_ID, item.getId());
            intent.putExtra(Constant.PRODUCT_GROUP_NAME, item.getName());
            intent.putExtra(Constant.COLOR, item.getColor());

            if (products != null && products.size() > 1) {
//                replaceFragment(new ProductsActivity(), ProductsActivity.TAG, false, true, bundle);
                this.startActivity(intent);
            } else if (products != null && products.size() == 1) {
//                replaceFragment(new ProductsActivity(), ProductsActivity.TAG, false, true, bundle);
                this.startActivity(intent);
            } else {
                AlertUtils.showInfoAlertDialog(this, R.string.No_product);
            }
        }
    }
}
