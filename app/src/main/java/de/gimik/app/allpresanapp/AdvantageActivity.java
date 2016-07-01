package de.gimik.app.allpresanapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by quyenDT on 9/22/2015.
 */
public class AdvantageActivity extends BaseActivity{

    public static final String TAG = AdvantageActivity.class.getSimpleName();

    @Bind(R.id.vv_creme)
    VideoView vv_creme;

    @Bind(R.id.vv_fuss)
    VideoView vv_fuss;

    @Bind(R.id.txt_advantage_detail_1)
    TextView txt_advantage_detail_1;

    @Bind(R.id.txt_advantage_detail_4)
    TextView txt_advantage_detail_4;

    @Bind(R.id.txt_advantage_detail_5)
    TextView txt_advantage_detail_5;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_advantage);
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
        setTitle(R.string.advantages);
        txt_advantage_detail_1.setText(Html.fromHtml(getText(R.string.advantage_detail_1).toString()));
        txt_advantage_detail_1.setMovementMethod(LinkMovementMethod.getInstance());

        txt_advantage_detail_4.setText(Html.fromHtml(getText(R.string.advantage_detail_4).toString()));
        txt_advantage_detail_4.setMovementMethod(LinkMovementMethod.getInstance());

        txt_advantage_detail_5.setText(Html.fromHtml(getText(R.string.advantage_detail_5).toString()));
        txt_advantage_detail_5.setMovementMethod(LinkMovementMethod.getInstance());

        playVideo(vv_creme, R.raw.f_anwendungsfilm_2014_vgl_creme);
        playVideo(vv_fuss, R.raw.f_anwendungsfilm_2014_vgl_fuss);
    }

    private void playVideo(final VideoView vv, int videoResId) {
        if (vv.isPlaying())
            return;
        MediaController mediaController = new MediaController(this);
        vv.setMediaController(mediaController);
        vv.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + videoResId));
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.setLayoutParams(new LinearLayout.LayoutParams(vv.getMeasuredWidth(), LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        });
        vv.start();
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
