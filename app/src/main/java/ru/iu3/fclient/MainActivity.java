package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import ru.iu3.fclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int res = initRng();
        byte[] v = randomBytes(16);

        //Работает!!
        for(int i = 0; i < v.length; i++) {
            Log.d("bytes", v[i] + ", ");
        }

        //Передаём 16-байтовый ключ, преобразуем строку в байты и передаём в функцию encrypt
        String codedStr = "code this";
        byte [] codedStrBytes = codedStr.getBytes(StandardCharsets.UTF_8);
        byte [] coded = encrypt(v, codedStrBytes);

        //Передаём в функцию decrypt тот же ключ и закодированные байты
        String decoded = new String(decrypt(v, coded), StandardCharsets.UTF_8);
        Log.d("decoded", decoded); //Получаем исходную строку

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
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