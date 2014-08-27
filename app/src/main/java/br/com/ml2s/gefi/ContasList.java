package br.com.ml2s.gefi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcossantos on 22/08/2014.
 */
public class ContasList extends ListFragment implements DialogInterface.OnClickListener {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private List<String> aList;
    private AlertDialog dialogConfirmacao;
    private AdapterView.AdapterContextMenuInfo info;
    private int ItemSelecionado;

    public static ContasList init() {
        return new ContasList();
    }

    String[] txtItem = new String[]{
            "Nova Conta",
            "NAN"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_itens, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aList = new ArrayList<String>();

        for (int i = 0; i < txtItem.length; i++) {
            aList.add(txtItem[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, aList);
        setListAdapter(adapter);

        registerForContextMenu(getListView());

        this.dialogConfirmacao = criaDialogConfirmacao();

    }


    @Override
    public void onListItemClick(ListView lvMenu, View view, int posicao, long id) {

        switch (posicao) {

            case 0:
                FrameLayout frame = (FrameLayout) getActivity().findViewById(R.id.fl_menu_container);
                frame.removeAllViews();

                CadastroConta newFragment = new CadastroConta();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_menu_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (((AdapterView.AdapterContextMenuInfo) menuInfo).position == 0) {
            //retira a ação de click longo do primeiro item da lista
            return;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contas_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        info = null;
        ItemSelecionado = -1;
        if (item.getItemId() == R.id.remover) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ItemSelecionado = info.position;
            dialogConfirmacao.show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmacao_exclusao);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                aList.remove(ItemSelecionado);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

}
