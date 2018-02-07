package com.example.cmardari.myapplication;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.cmardari.myapplication.utilities.DisplayUtilities;

public class AnimationActivity extends AppCompatActivity {

    private static final String CANVAS_TAG = "CANVAS";
    private ImageView curveLineContainer;
    private ImageView drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        curveLineContainer = findViewById(R.id.poly_container);
        drop = findViewById(R.id.little_drop);
        startAnimation();
        movingDrop();
    }

    private void movingDrop() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(drop, "translationY", drop.getY(), DisplayUtilities.getPixels(this,250));
        animator.setDuration(4000);
        animator.start();
    }

    private void startAnimation() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paint.setStyle(Paint.Style.FILL);

        curveLineContainer.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(
                (int) DisplayUtilities.getPixels(this, 1000),
                (int) DisplayUtilities.getPixels(this, 1000),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        PointF topLeftPoint = new PointF(0, 0);
        PointF bottomRightPoint = new PointF(canvas.getWidth(), canvas.getHeight());

        Log.d(CANVAS_TAG, "x = "+DisplayUtilities.getDp(this, bottomRightPoint.x) +"   y  = "+DisplayUtilities.getDp(this, bottomRightPoint.y));

        Path path = new Path();
        path.moveTo(topLeftPoint.x,bottomRightPoint.y);
        path.quadTo(bottomRightPoint.x/2, topLeftPoint.y, bottomRightPoint.x, bottomRightPoint.y);


        canvas.drawPath(path, paint);

        curveLineContainer.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

    }
}
