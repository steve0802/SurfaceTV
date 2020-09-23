package com.steve.surfacetv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DramaDetailActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_DRAMA_NAME = "BUNDLE_KEY_DRAMA_NAME";
    public static final String BUNDLE_KEY_DRAMA_RATING = "BUNDLE_KEY_DRAMA_RATING";
    public static final String BUNDLE_KEY_DRAMA_CREATED_AT = "BUNDLE_KEY_DRAMA_CREATED_AT";
    public static final String BUNDLE_KEY_DRAMA_TOTAL_VIEWS = "BUNDLE_KEY_DRAMA_TOTAL_VIEWS";
    public static final String BUNDLE_KEY_DRAMA_THUMB = "BUNDLE_KEY_DRAMA_THUMB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_detail);

        ((TextView)findViewById(R.id.drama_detail_text_name)).setText(getIntent().getStringExtra(BUNDLE_KEY_DRAMA_NAME));
        ((TextView)findViewById(R.id.drama_detail_text_total_views)).setText("觀看次數：" + getIntent().getStringExtra(BUNDLE_KEY_DRAMA_TOTAL_VIEWS) + "次");
        ((TextView)findViewById(R.id.drama_detail_text_created_at)).setText(getIntent().getStringExtra(BUNDLE_KEY_DRAMA_CREATED_AT));
        ((TextView)findViewById(R.id.drama_detail_text_rating)).setText(getIntent().getStringExtra(BUNDLE_KEY_DRAMA_RATING));
        ((ImageView)findViewById(R.id.drama_detail_image_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this)
                .load(getIntent().getStringExtra(BUNDLE_KEY_DRAMA_THUMB))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView)findViewById(R.id.drama_detail_image_thumb));
    }
}