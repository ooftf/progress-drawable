package com.ooftf.progress.holder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ooftf.progress.GradualHorizontalProgressDrawable;

public class MainActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.post(new Runnable() {
            @Override
            public void run() {
                GradualHorizontalProgressDrawable drawable = new GradualHorizontalProgressDrawable(MainActivity.this,button);
                drawable.setIntrinsicWidth(button.getWidth());
                drawable.start();
                button.setCompoundDrawablesWithIntrinsicBounds(null,null,null,drawable);
            }
        });
    }
}
