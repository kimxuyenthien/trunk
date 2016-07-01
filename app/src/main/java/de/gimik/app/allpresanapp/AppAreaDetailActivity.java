package de.gimik.app.allpresanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.Utils.Constant;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class AppAreaDetailActivity extends BaseActivity {
    public static final String TAG = AppAreaDetailActivity.class.getSimpleName();


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    int intTitle;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        int intLayout = mIntent.getIntExtra(Constant.ExtraKey.LAYOUT_ID, 0);
        intTitle = mIntent.getIntExtra(Constant.ExtraKey.TITLE_ID, 0);

        setContentView(intLayout);
        setTitle(intTitle);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle(intTitle);

    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(intTitle);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
