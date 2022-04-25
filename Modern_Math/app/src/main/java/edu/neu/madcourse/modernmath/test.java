package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {

    private final int REQUEST_CODE = 9882;
    private TextView test_text;
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private boolean is_listening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        this.test_text = findViewById(R.id.test_text);
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        this.speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        this.speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                test_text.setText("...");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                test_text.setText("No content heard");
                is_listening = false;
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> words = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                String user_input = words.get(0);

                boolean isNumeric = false;

                try
                {
                    user_input = user_input.replaceAll("\\s+","");

                    if (Integer.parseInt(user_input) >= 0)
                    {
                        isNumeric = true;
                    }
                    else
                    {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(test.this, "Not numeric", Toast.LENGTH_SHORT).show();
                }

                if (isNumeric)
                {
                    test_text.setText(user_input);
                }
                else
                {
                    test_text.setText("");
                }

                is_listening = false;
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


    }

    private boolean checkPermission()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void getPermissions()
    {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
    }

    public void testOnClick(View view)
    {
        if (checkPermission())
        {
            if(this.is_listening)
            {
                this.speechRecognizer.stopListening();
                this.is_listening = false;
            }
            else
            {
                this.speechRecognizer.startListening(this.speechIntent);
                this.is_listening = true;
            }
        }
        else
        {
            getPermissions();
        }
    }

    @Override
    public void onDestroy()
    {
        speechRecognizer.destroy();
        super.onDestroy();
    }
}