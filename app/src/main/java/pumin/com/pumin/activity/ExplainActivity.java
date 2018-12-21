package pumin.com.pumin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import pumin.com.pumin.R;

public class ExplainActivity extends Activity {

    private ImageView viewById;
    private ImageView mclose_IMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        viewById = (ImageView)findViewById(R.id.Image_explain);
        mclose_IMG = (ImageView)findViewById(R.id.close_IMG);
        Glide.//加载图片并缓存
                with(this)
                .load(R.drawable.explain)
                .crossFade()
                .into(viewById);
        mclose_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExplainActivity.this.finish();
            }
        });
    }
}
