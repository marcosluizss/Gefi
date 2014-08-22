package br.com.ml2s.gefi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by marcossantos on 21/08/2014.
 */
public class CadastroConta extends Fragment {

    private Spinner bancos;
    private Spinner tipoConta;

    public static CadastroConta init(){
        return new CadastroConta();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_minhas_contas,container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.bancos, android.R.layout.simple_spinner_item);
        bancos = (Spinner) getActivity().findViewById(R.id.sp_banco_conta);
        bancos.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.tipos_conta, android.R.layout.simple_spinner_item);
        tipoConta = (Spinner) getActivity().findViewById(R.id.sp_tipo_conta);
        tipoConta.setAdapter(adapter);
    }

    public void alert(String msg){
        Toast alerta = Toast.makeText(getActivity(), msg , Toast.LENGTH_SHORT );
        alerta.show();
    }

}
