package io.gritacademy.textrecognitionml;

import static com.google.mlkit.vision.common.InputImage.fromBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button scanBtn;
    private EditText editTextMultiLine;
    TextToSpeech toSpeech;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String textToRead =  getIntent().getStringExtra("TEXT_TO_READ");

        scanBtn = findViewById(R.id.scanButton);
        editTextMultiLine = findViewById(R.id.editTextMultiLine);
        Button readBtn = findViewById(R.id.readButton);




            if(getIntent() !=null) {

                editTextMultiLine.setText(textToRead);
            }


        readBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(v -> {

            Intent intent = new Intent(this, LoadImageActivity.class);
            startActivity(intent);
            //     recognizeText(inputImage);
            //   editTextMultiLine.setText(text);

        });

        toSpeech = new TextToSpeech(this, status -> {
            try {
                toSpeech.setLanguage(new Locale("en", "US"));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("speak", "onInit: " + TextToSpeech.ERROR);
            }
        });

    }

    private void recognizeText(InputImage image) {

        // [START get_detector_default]
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        // [END get_detector_default]

        // [START run_detector]
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(visionText -> {

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
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
        // [END run_detector]
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