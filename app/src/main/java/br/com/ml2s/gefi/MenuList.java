package br.com.ml2s.gefi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcossantos on 22/08/2014.
 */
public class MenuList extends ListFragment {

    private FragmentManager fm;
    private FragmentTransaction ft;

    public static MenuList init(){
        return new MenuList();
    }

    String[] txtItem = new String[] {
            "Minhas Contas",
            "NAN",
            "Cartões",
            "Movimentações"
    };

    int[] img = new int[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
    };

    String[] qtdOcorrencias = new String[]{
            "",
            "",
            "",
            "",
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list_menu,container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


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
        Toast alerta = Toast.makeText(getActivity(), "Item clicado " + posicao, Toast.LENGTH_SHORT );
        alerta.show();

        switch (posicao) {

            case 0:
                FrameLayout frame = (FrameLayout) getActivity().findViewById(R.id.fl_menu_container);
                frame.removeAllViews();

                CadastroConta newFragment = new CadastroConta();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_menu_container, newFragment);
                transaction.setBreadCrumbTitle("Minhas Contas");
                transaction.addToBackStack(null);
                transaction.commit();

        }


    }

}
