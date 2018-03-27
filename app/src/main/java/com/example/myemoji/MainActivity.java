package com.example.myemoji;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myemojilibrary.FaceFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.Text)
    EditText Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FaceFragment faceFragment = new FaceFragment();
        faceFragment.init(Text,15);
        getSupportFragmentManager().beginTransaction().add(R.id.container, faceFragment).commit();


    }


}
