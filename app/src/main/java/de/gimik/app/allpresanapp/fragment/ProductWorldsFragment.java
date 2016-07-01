package de.gimik.app.allpresanapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.IngredientsActivity;
import de.gimik.app.allpresanapp.ProductGroupsActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.ProductWorldDbAdapter;
import de.gimik.app.allpresanapp.model.ProductWorld;
import de.gimik.app.allpresanapp.view.GridViewAdapter;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class ProductWorldsFragment extends BaseFragment {
    public static final String TAG = ProductWorldsFragment.class.getSimpleName();

    @Bind(R.id.gv_productworld)
    GridView gv_productworld;

    GridViewAdapter gridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_worlds, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.product));

       // setDataToTest();

        gv_productworld = (GridView) getActivity().findViewById(R.id.gv_productworld);
        gridAdapter = new GridViewAdapter(getBaseActivity(), R.layout.grid_item_productworld, getData());
        gv_productworld.setAdapter(gridAdapter);

        gv_productworld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (id != 0) {
                    Intent intent = new Intent(getActivity(), ProductGroupsActivity.class);
                    intent.putExtra(Constant.PRODUCT_WORLD_ID, id);
                    getActivity().startActivity(intent);
//                    replaceFragment(new ProductGroupsActivity(), ProductGroupsActivity.TAG, false, true, bundle);
                } else {
//                    replaceFragment(new IngredientsActivity(), IngredientsActivity.TAG, false, true, null);
                    getActivity().startActivity(new Intent(getActivity(), IngredientsActivity.class));
                }
            }

        });
    }

    private List<ProductWorld> getData() {
        ProductWorldDbAdapter productWorldDbAdapter = getProductWorldDbAdapter();
        List<ProductWorld> productWorlds = productWorldDbAdapter.queryAll();
        if (productWorlds==null)
            productWorlds = new ArrayList();

        ProductWorld productWorld = new ProductWorld();
        productWorld.setId(0L);
        productWorld.setName("    ");
        productWorlds.add(productWorld);
        return productWorlds;
    }
}
