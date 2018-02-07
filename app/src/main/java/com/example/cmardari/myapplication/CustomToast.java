package com.example.cmardari.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

    private Context context;
    private View layout;


    public CustomToast(Context context) {
        this.context = context;

        LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) ((AppCompatActivity)context).findViewById(R.id.toast_root));
    }


    public void makeToast(int imageRes, int textRes, int gravity, int xOffset, int yOffset){
        ImageView imageView = (ImageView) layout.findViewById(R.id.image_toast);
        imageView.setImageResource(imageRes);

        TextView textView = layout.findViewById(R.id.text_toast);
        textView.setText(textRes);

        Toast toast = new Toast(context);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void makeToast(int imageRes, int gravity, int xOffset, int yOffset){
        ImageView imageView = layout.findViewById(R.id.image_toast);
        imageView.setImageResource(imageRes);

        Toast toast = new Toast(context);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
