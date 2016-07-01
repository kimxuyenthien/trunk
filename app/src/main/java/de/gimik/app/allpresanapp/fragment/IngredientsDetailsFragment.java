package de.gimik.app.allpresanapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.IngredientDbAdapter;
import de.gimik.app.allpresanapp.model.Ingredient;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class IngredientsDetailsFragment extends BaseFragment {
    public static final String TAG = IngredientsDetailsFragment.class.getSimpleName();

    @Bind(R.id.tv_ingredient_name)
    TextView tv_ingredient_name;

    @Bind(R.id.webView)
    WebView webView;

    private IngredientDbAdapter ingredientDbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ingredients_details, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.inci_lexicon);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        long ingredientId;
        if (bundle != null) {
            ingredientId = bundle.getLong(Constant.INGREDIENT_ID);
            ingredientDbAdapter = getIngredientDbAdapter();
            Ingredient ingredient = ingredientDbAdapter.queryForId(ingredientId);
            tv_ingredient_name.setText(ingredient.getName());
            webView.setWebViewClient(new MyWebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(ingredient.getDescription(),  "text/html; charset=utf-8",null);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.setBackgroundColor(0x00000000);
            if (Build.VERSION.SDK_INT >= 11)
                webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
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
}
