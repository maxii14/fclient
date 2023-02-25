package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;

public class PinpadActivity extends AppCompatActivity {

    TextView tvPin;
    String pin = "";
    final int MAX_KEYS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad);

        tvPin = findViewById(R.id.txtPin);

        ShuffleKeys();

        findViewById(R.id.btnOK).setOnClickListener((View) -> {
            Intent it = new Intent();
            // добавляем в новый intent данные с именем pin, которые потом получим по этому же имени
            it.putExtra("pin", pin);
            setResult(RESULT_OK, it);
            finish();
        });

        findViewById(R.id.btnReset).setOnClickListener((View) -> {
            pin = "";
            tvPin.setText("");
        });

    }

    public void keyClick(View v)
    {
        String key = ((TextView)v).getText().toString(); // берём значение кнопки, на которую нажали
        int sz = pin.length();

        if (sz < 4) // проверяем размер уже введённого пинкода
        {
            pin += key;
            tvPin.setText("****".substring(3 - sz)); // количество звёздочек в зависимости он размера пинкода
        }
    }

    protected void ShuffleKeys()
    {
        Button keys[] = new Button[] {
                findViewById(R.id.btnKey0),
                findViewById(R.id.btnKey1),
                findViewById(R.id.btnKey2),
                findViewById(R.id.btnKey3),
                findViewById(R.id.btnKey4),
                findViewById(R.id.btnKey5),
                findViewById(R.id.btnKey6),
                findViewById(R.id.btnKey7),
                findViewById(R.id.btnKey8),
                findViewById(R.id.btnKey9),
        };

        byte[] rnd = MainActivity.randomBytes(MAX_KEYS);
        for(int i = 0; i < MAX_KEYS; i++)
        {
            // rnd[i] & 0xFF переворачивает отрицательные числа (например, из -28 делает 228, из -45 211)
            // и оставляет без изменений положительные
            int idx = (rnd[i] & 0xFF) % 10;

            //Log.d("dbg", "index: " + idx + ", rnd[i]: " + rnd[i] + ", rnd[i]&0xFF: " + (rnd[i] & 0xFF) + "\n");

            // запоминаем цифру рандомно выбранной кнопки
            CharSequence txt = keys[idx].getText();

            // меняем местами текст рандомной кнопки и текущей
            keys[idx].setText(keys[i].getText());
            keys[i].setText(txt);
        }
    }
}