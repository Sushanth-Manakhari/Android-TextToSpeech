package sush.com.texttospeechapp;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;



public class TextToSpeechAppActivity extends Activity implements OnInitListener {
    private int TTS_CHECK_CODE = 1;
    private TextToSpeech tts;
    private EditText inputText;
    private Button speakButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech_app);
        inputText = (EditText) findViewById(R.id.input_text);
        speakButton = (Button) findViewById(R.id.speak_button);
        speakButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String textToConvert = inputText.getText().toString();
                if (textToConvert!=null && textToConvert.length()>0) {
                    tts.speak(textToConvert, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, TTS_CHECK_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                tts = new TextToSpeech(this, this);
            else {
                Intent installVoiceIntent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installVoiceIntent);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(TextToSpeechAppActivity.this, "Text-To-Speech engine is initialized",
                    Toast.LENGTH_LONG).show();
            int setLang = tts.setLanguage(Locale.US);
            if (setLang == TextToSpeech.LANG_MISSING_DATA || setLang ==  TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(TextToSpeechAppActivity.this, "Text-To-Speech Language not supported",
                        Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(TextToSpeechAppActivity.this, "Error: Text-To-Speech engine could not be initialized", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}