package io.gritacademy.textrecognitionml;

import static com.google.mlkit.vision.common.InputImage.fromBitmap;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class LoadImageActivity extends AppCompatActivity {

    ImageView imageView;
    Button loadButton, getTextButton;
    InputImage image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);

        imageView = findViewById(R.id.loadImageView);
        loadButton = findViewById(R.id.loadImageBtn);
        getTextButton = findViewById(R.id.getTextBtn);

        loadButton.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });


        getTextButton.setOnClickListener(v -> {
            Log.d("TAG", "oCLICK: ");

            try {
                image = getImageFromImageView(imageView);
                recognizeText(image);
            } catch (Exception e) {
                e.printStackTrace();
            }


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


    private InputImage getImageFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        return fromBitmap(drawable.getBitmap(), 0);
    }

    public void switchActivity(String textToSend){
       // Log.d("TAG", "+-+--+--+-+-+-+-+-+-+-+-+-+-+--+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
     //   Log.d("TAG", "I switchActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TEXT_TO_READ", textToSend);

     startActivity(intent);
    }


    private void recognizeText(InputImage image) {
        Log.d("TAG", "I recognizeText");

        // [START get_detector_default]
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        // [END get_detector_default]

        // [START run_detector]
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(visionText -> {

                            String textToSend = "";
                            for (Text.TextBlock block : visionText.getTextBlocks()) {
                                Rect boundingBox = block.getBoundingBox();
                                Point[] cornerPoints = block.getCornerPoints();
                                String text = block.getText();
                                textToSend += text;

                                // globalText.concat(text);

                         //       Log.d("TAG", "globalText");
                           //     Log.d("TAG", globalText);

                                for (Text.Line line : block.getLines()) {

                                    for (Text.Element element : line.getElements()) {

                                    }
                                }
                            }
                           switchActivity(textToSend);

                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
    }


}