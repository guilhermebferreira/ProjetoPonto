package io.github.guilhermebferreira.projetoponto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);


        new HandleServices(this).execute("http://www.mocky.io/v2/5b5227682e000074005c1c2b");
    }
}
