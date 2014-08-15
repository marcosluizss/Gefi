package br.com.ml2s.gefi;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by marcossantos on 15/08/2014.
 */
public class Menu extends ListFragment {

    public static Menu init(){
        return new Menu();
    }

    private String[] arrMenu;
    private ArrayAdapter<String> ArrayAdapter;

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
        arrMenu = getResources().getStringArray(R.array.menu_principal);
        ArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrMenu);
        setListAdapter(ArrayAdapter);
    }

    @Override
    public void onListItemClick(ListView lvMenu, View view, int posicao, long id ){
        Toast alerta = Toast.makeText(getActivity(), "Item clicado " + id, Toast.LENGTH_SHORT );
        alerta.show();
    }


}
