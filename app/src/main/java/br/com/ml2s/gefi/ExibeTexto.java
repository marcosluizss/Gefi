package br.com.ml2s.gefi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by marcossantos on 15/08/2014.
 */
public class ExibeTexto extends Fragment {

    static ExibeTexto init(){
        ExibeTexto exibeTexto = new ExibeTexto();
        Bundle bundle = new Bundle();
        exibeTexto.setArguments(bundle);
        return exibeTexto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_texto,container,false);
        TextView tvText = (TextView)view.findViewById(R.id.tv_texto);
        tvText.setText("Fragment ExibeTexto");
        return view;
    }



}
