package br.com.ml2s.gefi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcossantos on 22/08/2014.
 */
public class FinancasList extends ListFragment implements DialogInterface.OnClickListener {

    private List<Map<String, Object>> aFinancas;
    private AlertDialog dialogConfirmacao;
    private int ItemSelecionado;

    private DatabaseHelper helper;

    private String financaId;

    public static FinancasList init() {
        return new FinancasList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_itens, container, false);

        helper = new DatabaseHelper(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aFinancas = new ArrayList<Map<String, Object>>();
        Map<String, Object> botaoAdd = new HashMap<String, Object>();
        botaoAdd.put(DatabaseHelper.KEY_ID,DatabaseHelper.VALUE_ID_NULL);
        botaoAdd.put(DatabaseHelper.KEY_NOME,getText(R.string.nova_financa));
        aFinancas.add(botaoAdd);

        DataSourceTools db = new DataSourceTools(helper);
        aFinancas.addAll(db.findAll(DatabaseHelper.TABLE_FINANCA, null));

        String[] de = {DatabaseHelper.KEY_NOME};
        int[] para = {R.id.tv_menu_texto};

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aFinancas,R.layout.menu_item, de, para);
        setListAdapter(adapter);

        registerForContextMenu(getListView());

        this.dialogConfirmacao = criaDialogConfirmacao();

    }

    @Override
    public void onListItemClick(ListView lvMenu, View view, int posicao, long id) {

        financaId = aFinancas.get(posicao).get(DatabaseHelper.KEY_ID).toString();
        Bundle data = new Bundle();
        data.putString(DatabaseHelper.KEY_ID, financaId);

        CadastroFinanca newFragment = new CadastroFinanca();
        newFragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_menu_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

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
                financaId = aFinancas.get(ItemSelecionado).get(DatabaseHelper.KEY_ID).toString();
                remover(financaId);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    private void remover(String id){
        int result;
        DataSourceTools db = new DataSourceTools(helper);
        result = db.delete(DatabaseHelper.TABLE_FINANCA,id);
        if(result == R.string.salvar_sucesso) {
            aFinancas.remove(ItemSelecionado);
            getListView().invalidateViews();
        }
    }

}
