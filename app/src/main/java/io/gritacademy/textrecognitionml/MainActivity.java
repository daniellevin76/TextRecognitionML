package io.gritacademy.textrecognitionml;

import static com.google.mlkit.vision.common.InputImage.fromBitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.graphics.Point;
import android.graphics.Rect;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.mlkit.vision.text.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button readBtn, scanBtn;
    private EditText editTextMultiLine;
    TextToSpeech toSpeech;
    String text;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

       InputImage inputImage = fromBitmap(bitmap, 0);


        scanBtn = findViewById(R.id.scanButton);
        editTextMultiLine = findViewById(R.id.editTextMultiLine);
        readBtn = findViewById(R.id.readButton);

        readBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(v -> {
          Intent intent = new Intent(this, CameraXActivity.class);
          startActivity(intent);

        });

        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                try {
                    toSpeech.setLanguage(new Locale("en", "US"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("speak", "onInit: " + TextToSpeech.ERROR);
                }
            }
        });

    }
        private void recognizeText(InputImage image){

            // [START get_detector_default]
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            // [END get_detector_default]

            // [START run_detector]
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {

                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                                        Rect boundingBox = block.getBoundingBox();
                                        Point[] cornerPoints = block.getCornerPoints();
                                         text = block.getText();




                                        for (Text.Line line : block.getLines()) {

                                            for (Text.Element element : line.getElements()) {

                                            }
                                        }
                                    }
                                    // [END get_text]
                                    // [END_EXCLUDE]
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });
            // [END run_detector]
        }

        private void processTextBlock (Text result){
            // [START mlkit_process_text_block]
            String resultText = result.getText();
            for (Text.TextBlock block : result.getTextBlocks()) {
                String blockText = block.getText();
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
                for (Text.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
                    for (Text.Element element : line.getElements()) {
                        String elementText = element.getText();
                        Point[] elementCornerPoints = element.getCornerPoints();
                        Rect elementFrame = element.getBoundingBox();
                    }
                }
            }
            // [END mlkit_process_text_block]
        }





    @Override
    public void onClick(View v) {


        String toRead = editTextMultiLine.getText().toString();

        toSpeech.speak(toRead, TextToSpeech.QUEUE_FLUSH, null);


    }

    @Override
    protected void onStop() {
        super.onStop();
        toSpeech.stop();
        toSpeech.shutdown();
    }
}