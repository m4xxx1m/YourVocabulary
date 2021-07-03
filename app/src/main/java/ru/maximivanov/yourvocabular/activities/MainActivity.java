package ru.maximivanov.yourvocabular.activities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ru.maximivanov.yourvocabular.R;
import ru.maximivanov.yourvocabular.models.Element;
import ru.maximivanov.yourvocabular.models.User;
import ru.maximivanov.yourvocabular.network.Translate;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout fromLayout;
    private TextInputEditText fromET;
    private TextInputEditText toET;
    private boolean addButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toET = findViewById(R.id.to_edit_text);
        fromLayout = findViewById(R.id.from_layout);
        fromET = findViewById(R.id.from_edit_text);
        MaterialButton translateButton = findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = Objects.requireNonNull(fromET.getText()).toString();
                if (from.equals("")) {
                    fromLayout.setError(getString(R.string.field_cant_be_empty));
                }
                else {
                    startTranslation(from);
                }
            }
        });
        findViewById(R.id.add_button).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = Objects.requireNonNull(fromET.getText()).toString();
                String to = Objects.requireNonNull(toET.getText()).toString();
                if (from.equals("")) {
                    fromLayout.setError(getString(R.string.field_cant_be_empty));
                }
                else if (to.equals("")) {
                    startTranslation(from);
                    addButtonPressed = true;
                }
                else {
                    User.addElement(new Element(from, to));
                    toET.setText("");
                    fromET.setText("");
                }
            }
        });
        fromET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!Objects.requireNonNull(fromET.getText()).toString().equals("")) {
                    fromET.setError(null);
                }
                return false;
            }
        });
    }

    private void startTranslation(String input) {
        Translate translateRequest = new Translate(input, setHandler());
        Thread thread = new Thread(translateRequest);
        thread.start();
    }

    private Handler setHandler() {
        return new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                toET.setText(((Element)msg.obj).getToText());
                if (addButtonPressed) {
                    User.addElement((Element) msg.obj);
                    toET.setText("");
                    fromET.setText("");
                    addButtonPressed = false;
                }
            }
        };
    }
}