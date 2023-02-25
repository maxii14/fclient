package ru.iu3.fclient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

import ru.iu3.fclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;
    ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int keyBytesCount = 16;

        int res = initRng();
        byte[] key = randomBytes(keyBytesCount);

        for(int i = 0; i < key.length; i++) {
            Log.d("bytes", key[i] + ", ");
        }

        //Передаём 16-байтовый ключ, преобразуем строку в байты и передаём в функцию encrypt
        String codedStr = "code this please";
        byte [] codedStrBytes = codedStr.getBytes();
        byte [] coded = encrypt(key, codedStrBytes);

        //Передаём в функцию decrypt тот же ключ и закодированные байты
        String decoded = new String(decrypt(key, coded));
        Log.d("decoded", decoded); //Получаем исходную строку


        activityResultLauncher  = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback() {
                @Override
                public void onActivityResult(Object result) {
                    ActivityResult res = (ActivityResult)result;
                    if (res.getResultCode() == Activity.RESULT_OK) {
                        Intent data = res.getData();
                        // обработка результата
                        String pin = data.getStringExtra("pin");// получаем переданный ранее pin
                        Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }

    public void onButtonClick(View v)
    {
        Intent it = new Intent(this, PinpadActivity.class);
        //startActivity(it);
        activityResultLauncher.launch(it);
    }


    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}