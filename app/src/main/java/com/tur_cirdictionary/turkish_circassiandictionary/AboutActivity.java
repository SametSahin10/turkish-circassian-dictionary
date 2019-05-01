package com.tur_cirdictionary.turkish_circassiandictionary;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tv_open_source_desc;
    Button btn_view_source_code;
    TextView tv_first_paragraph;
    TextView tv_credits_content_one;
    TextView tv_credits_content_two;
    TextView tv_credits_content_three;
    TextView tv_credits_content_four;
    TextView tv_credits_content_five;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv_open_source_desc = findViewById(R.id.tv_open_source_desc);
        btn_view_source_code = findViewById(R.id.btn_view_the_source_code);
        btn_view_source_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://github.com/SametSahin10/turkish-circassian-dictionary");
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        tv_credits_content_one = findViewById(R.id.tv_credits_content_one);
        tv_credits_content_two = findViewById(R.id.tv_credits_content_two);
        tv_credits_content_three = findViewById(R.id.tv_credits_content_three);
        tv_credits_content_four = findViewById(R.id.tv_credits_content_four);
        tv_credits_content_five = findViewById(R.id.tv_credits_content_five);

        tv_credits_content_one.setMovementMethod(LinkMovementMethod.getInstance());
        tv_credits_content_two.setMovementMethod(LinkMovementMethod.getInstance());
        tv_credits_content_three.setMovementMethod(LinkMovementMethod.getInstance());
        tv_credits_content_four.setMovementMethod(LinkMovementMethod.getInstance());
        tv_credits_content_five.setMovementMethod(LinkMovementMethod.getInstance());

        tv_first_paragraph = findViewById(R.id.tv_first_paragraph);
        tv_first_paragraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "demirag.necat@gmail.com");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
