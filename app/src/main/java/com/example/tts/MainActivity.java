package com.example.tts;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
        import android.speech.tts.TextToSpeech;
        import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
        import android.text.Spanned;
        import android.text.style.ForegroundColorSpan;
        import android.util.Log;
        import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
        import java.util.Arrays;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private int paragraphCount = 0;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    EditText ed_text;
    TextView textView;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_text = findViewById(R.id.ed_text);
        textView = findViewById(R.id.tv_text);

        //creating TTS instance
        tts = new TextToSpeech(this, this);
        //textView.setText(getString(R.string.text));

        textView.setVisibility(View.GONE);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_text.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"dabble click to write something",Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void speakText() {
        if (paragraphCount == 0) {
            stringArrayList = new ArrayList<>(Arrays.asList(ed_text.getText().toString().split("<\n>")));
            //stringArrayList = new ArrayList<>(Arrays.asList(textView.getText().toString().split("<\n>")));
        }
        try {

            /*SpannableString spannableString = new SpannableString(textView.getText().toString());
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                    0, textView.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
             */

            SpannableString spannableString = new SpannableString(ed_text.getText().toString());
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                    0, ed_text.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            /*spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.violet)),
                    textView.getText().toString().indexOf(stringArrayList.get(paragraphCount)),
                    textView.getText().toString().indexOf(stringArrayList.get(paragraphCount)) +
                            stringArrayList.get(paragraphCount).length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);

             */
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.violet)),
                    ed_text.getText().toString().indexOf(stringArrayList.get(paragraphCount)),
                    ed_text.getText().toString().indexOf(stringArrayList.get(paragraphCount)) +
                            stringArrayList.get(paragraphCount).length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(stringArrayList.get(paragraphCount), TextToSpeech.QUEUE_FLUSH, null, "UniqueID");
            }

            //textView.setText(spannableString);
            textView.setText(spannableString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //*************
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            @Override
            public void onStart(String utteranceId) {
                Log.i("XXX", "utterance started");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.i("XXX", "utterance done");
            }

            @Override
            public void onError(String utteranceId) {
                Log.i("XXX", "utterance error");
            }

            @Override
            public void onRangeStart(String utteranceId,
                                     final int start,
                                     final int end,
                                     int frame) {
                Log.i("XXX", "onRangeStart() ... utteranceId: " + utteranceId + ", start: " + start
                        + ", end: " + end + ", frame: " + frame);

                // onRangeStart (and all UtteranceProgressListener callbacks) do not run on main thread
                // ... so we explicitly manipulate views on the main thread:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Spannable textWithHighlights = new SpannableString(ed_text.getText().toString());
                        //Spannable textWithHighlights = new SpannableString(getString(R.string.text));
                        textWithHighlights.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        textView.setText(textWithHighlights);

                    }
                });

            }

        });
        //#############

    }

    //Called to signal the completion of the TextToSpeech engine initialization.
    @Override
    public void onInit(int i) {

        //Listener for events relating to the progress of an utterance
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            //called when speaking starts
            @Override
            public void onStart(String utteranceId) {
                Log.i("TTS", "utterance started");
            }

            //called when speaking is finished.
            @Override
            public void onDone(String utteranceId) {
                if (stringArrayList.size() - 1 != paragraphCount) {
                    paragraphCount++;
                    speakText();
                } else {
                    paragraphCount = 0;
                }
                Log.i("TTS", "utterance done");
            }

            //called when an error has occurred during processing.
            @Override
            public void onError(String utteranceId) {
                Log.i("TTS", "utterance error");
            }

        });

    }
    private void speakTextStop() {
        ed_text.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        //----------------------
        tts.stop();

    }


    private void ThankYou() {
        Toast.makeText(MainActivity.this,"Thank You Kartishor Roy",Toast.LENGTH_SHORT).show();

    }
        //*********************//---

    public void speakClicked(View ignored) {
        ed_text.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);

        speakText();
    }
    public void ThankYou(View ignored) {
        ThankYou();
    }
    public void speakOfClicked(View ignored) {
        speakTextStop();
    }

}


