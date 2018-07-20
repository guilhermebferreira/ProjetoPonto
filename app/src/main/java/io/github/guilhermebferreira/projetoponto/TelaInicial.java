package io.github.guilhermebferreira.projetoponto;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static java.lang.String.valueOf;

public class TelaInicial extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FragmentManager manager = null;
    private FragmentTransaction transaction = null;
    private FragmentConfiguracoes fragmentoConfiguracoes = null;
    private FragmentPonto fragmentoPonto = null;
    private BottomNavigationView menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        fragmentoConfiguracoes = new FragmentConfiguracoes();
        fragmentoPonto = new FragmentPonto();

        transaction.replace(R.id.TelaPrincipalFrame,fragmentoPonto);
        transaction.commit();

        menu = findViewById(R.id.bottomNavigationView);
        menu.setOnNavigationItemSelectedListener(this);


        new HandleServices(this).execute("http://www.mocky.io/v2/5b5227682e000074005c1c2b");
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        Log.i("INFO", "onNavigationItemSelected");
        switch (item.getItemId()) {
            case R.id.configuracoes:
                Log.i("INFO", "click on PROFILE");

                fragmentoConfiguracoes = new FragmentConfiguracoes();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.TelaPrincipalFrame, fragmentoConfiguracoes);
                //cria um empilhamento de telas, com os fragmentos
                //para o app ter para onde retornar, caso o usuário pressione o botão de voltar
                transaction.addToBackStack("TelaPrincipal");
                transaction.commit();
                break;
        }

        return true;
    }
}
