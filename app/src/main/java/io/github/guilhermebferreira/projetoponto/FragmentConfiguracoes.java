package io.github.guilhermebferreira.projetoponto;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class FragmentConfiguracoes extends android.support.v4.app.Fragment {



    public FragmentConfiguracoes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Company company = PersistenceManager.getPersistenceManager().getStored(getContext());

        View view = inflater.inflate(R.layout.fragment_configuracoes, container, false);
        TextView nome = view.findViewById(R.id.configuracoes_company_name);
        TextView endereco = view.findViewById(R.id.configuracoes_endereco);
        TextView latitude = view.findViewById(R.id.configuracoes_latitude);
        TextView longitude = view.findViewById(R.id.configuracoes_longitude);

        nome.setText(company.getCompanyName());
        endereco.setText(company.getAddress());
        latitude.setText(company.getLatitude());
        longitude.setText(company.getLongitude());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
