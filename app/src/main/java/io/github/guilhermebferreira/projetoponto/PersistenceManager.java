package io.github.guilhermebferreira.projetoponto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PersistenceManager {
    private static PersistenceManager manager = null;
    private PersistenceManager(){
    }

    public static PersistenceManager getPersistenceManager() {
        if(manager == null){
            manager = new PersistenceManager();
        }
        return manager;
    }

    public void save(Context context, Company company){
        //dicionario de persistencia fácil
        //muito usado para salvar configurações do app

        SharedPreferences shared = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putString("nome", company.getCompanyName());
        editor.putString("endereco", company.getAddress());
        editor.putString("latitude", company.getLatitude());
        editor.putString("longitude", company.getLongitude());

        editor.commit();
        Log.i("TESTE", "parece que foi");
    }

    public Company getStored(Context context){
        SharedPreferences shared = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        if(shared.contains("nome")){
            Company company = new Company();
            company.setCompanyName(shared.getString("nome","--"));
            company.setAddress(shared.getString("endereco", "--"));
            company.setLatitude(shared.getString("latitude", "--"));
            company.setLongitude(shared.getString("longitude", "--"));

            return company;
        }
        return null;
    }
}
