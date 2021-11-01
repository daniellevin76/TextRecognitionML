package io.gritacademy.textrecognitionml;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class LoadImageActivity extends AppCompatActivity {

    ImageView imageView;
    Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);

        imageView = findViewById(R.id.loadImageView);
        loadButton = findViewById(R.id.loadImageBtn);
        loadButton.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });


    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if (result != null) {
                        imageView.setImageURI(result);
                    }

                }
            });

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String pathStr = String.valueOf(data.getData());

            //Log.d("tag", pathStr);
        }
    }

}