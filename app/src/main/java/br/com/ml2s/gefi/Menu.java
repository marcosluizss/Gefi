package br.com.ml2s.gefi;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcossantos on 15/08/2014.
 */
public class Menu extends ListFragment {

    public static Menu init(){
        return new Menu();
    }

    // Array of strings storing country names
    String[] txtItem = new String[] {
            "Bancos",
            "Contas",
            "Cartões",
            "Movimentações"
    };

    // Array of integers points to images stored in /res/drawable/
    int[] img = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
    };

    // Array of strings to store currencies
    String[] qtdOcorrencias = new String[]{
            "1",
            "3",
            "7",
            "0",
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Resources resources = getResources();

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for (int i=0;i<txtItem.length;i++){
            HashMap<String,String> hm = new HashMap<String, String>();
            hm.put("txt",txtItem[i]);
            hm.put("qtd",qtdOcorrencias[i]);
            hm.put("ico",Integer.toString(img[i]));

            aList.add(hm);
        }

        String[] from = {"ico","txt","qtd"};
        int[] to = {R.id.iv_menu_item,R.id.tv_menu_texto,R.id.tv_menu_ocorrencias};

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.menu_item, from, to);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView lvMenu, View view, int posicao, long id ){
        Toast alerta = Toast.makeText(getActivity(), "Item clicado " + id, Toast.LENGTH_SHORT );
        alerta.show();
    }


}
