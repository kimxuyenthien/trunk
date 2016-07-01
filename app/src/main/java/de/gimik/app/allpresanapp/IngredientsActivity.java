package de.gimik.app.allpresanapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.EanDbAdapter;
import de.gimik.app.allpresanapp.database.IngredientDbAdapter;
import de.gimik.app.allpresanapp.fragment.BaseFragment;
import de.gimik.app.allpresanapp.fragment.IngredientsDetailsFragment;
import de.gimik.app.allpresanapp.model.Ean;
import de.gimik.app.allpresanapp.model.Ingredient;
import de.gimik.app.allpresanapp.zXing.IntentIntegrator;
import de.gimik.app.allpresanapp.zXing.IntentResult;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quyen on 9/22/2015.
 */
public class IngredientsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = IngredientsActivity.class.getSimpleName();

    @Bind(R.id.lv_ingredients)
    StickyListHeadersListView lv_ingredients;

    @Bind(R.id.btn_scan)
    Button btn_scan;

    @Bind(R.id.etSearch)
    EditText etSearch;

    private IngredientDbAdapter ingredientDbAdapter;
    private List<Ingredient> ingredients;
    private IngredientItemAdapter adapter;
    private IntentIntegrator scanIntegrator;

    @Bind(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredients);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scanIntegrator = new IntentIntegrator(this);
        ingredientDbAdapter = new IngredientDbAdapter(this);
        ingredients = ingredientDbAdapter.queryAll();
        setupListView();
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(R.string.inci_lexicon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListView() {
        if (ingredients == null)
            ingredients = new ArrayList();
        adapter = new IngredientItemAdapter(this, ingredients);
        lv_ingredients.setAdapter(adapter);
        lv_ingredients.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSearch.getText().toString().trim().equals(""))
                    ingredients = ingredientDbAdapter.queryAll();
                else
                    ingredients = ingredientDbAdapter.queryAll(etSearch.getText().toString());

                adapter.setItems(ingredients);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.inci_lexicon);
    }


    @OnClick(R.id.btn_scan)
    public void scan() {
        scanIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            EanDbAdapter eanDbAdapter = new EanDbAdapter(this);
            Ean ean = eanDbAdapter.queryForId(Long.parseLong(scanContent));
            if (ean != null) {
                ingredients = ingredientDbAdapter.queryByProductId(ean.getProductId());
                setupListView();
            }

        } else {
            Toast toast = Toast.makeText(this,
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void gotoIngredientsDetail(Ingredient item) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.INGREDIENT_ID, item.getId());
//        replaceFragment(new IngredientsDetailsFragment(), IngredientsDetailsFragment.TAG, false, true, bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Ingredient ingredient = (Ingredient)adapter.getItem(position);
//        gotoIngredientsDetail(ingredient);
    }

    public class IngredientItemAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private List<Ingredient> items;
        private final Context mContext;
        private LayoutInflater mInflater;

        public IngredientItemAdapter(Context context, List<Ingredient> items) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            this.items = items;
        }

        public void setItems(List<Ingredient> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            Ingredient ingredient = (Ingredient) getItem(position);
            HeaderViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                holder = new HeaderViewHolder();
                convertView = mInflater.inflate(R.layout.row_header_ingredient, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.tv_header_ingredient);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            holder.text.setText(ingredient.getName().substring(0, 1).toUpperCase());
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            Ingredient ingredient = (Ingredient) getItem(position);
            return ingredient.getName().substring(0, 1).toUpperCase().hashCode();
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Ingredient ingredient = (Ingredient) getItem(position);
            final IngredientViewHolder holder;
            if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof IngredientViewHolder)) {
                holder = new IngredientViewHolder();
                convertView = mInflater.inflate(R.layout.row_ingredient, parent, false);
                holder.txtIngredient = (TextView) convertView.findViewById(R.id.txtIngredient);
                holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
                convertView.setTag(holder);
            } else {
                holder = (IngredientViewHolder) convertView.getTag();
            }
            holder.txtIngredient.setText(ingredient.getName());
            holder.txtDescription.setText(ingredient.getDescription());
            holder.txtDescription.setVisibility(holder.isShowDescription ? View.VISIBLE : View.GONE);

            holder.txtIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.isShowDescription = !holder.isShowDescription;
                    holder.txtDescription.setVisibility(holder.isShowDescription ? View.VISIBLE : View.GONE);
                }
            });

            return convertView;
        }
    }

    class IngredientViewHolder {
        TextView txtIngredient;
        TextView txtDescription;
        boolean isShowDescription;
    }

    class HeaderViewHolder {
        TextView text;
    }
}



