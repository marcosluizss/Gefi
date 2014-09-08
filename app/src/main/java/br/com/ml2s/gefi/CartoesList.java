package br.com.ml2s.gefi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcossantos on 22/08/2014.
 */
public class CartoesList extends ListFragment implements DialogInterface.OnClickListener {

    private FragmentManager fm;
    private FragmentTransaction ft;

    private List<Map<String, Object>> aCartoes;
    private AlertDialog dialogConfirmacao;
    private AdapterView.AdapterContextMenuInfo info;
    private int ItemSelecionado;

    private DatabaseHelper helper;

    private String cartaoId;

    public static CartoesList init() {
        return new CartoesList();
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

        aCartoes = new ArrayList<Map<String, Object>>();
        Map<String, Object> botaoAdd = new HashMap<String, Object>();
        botaoAdd.put("id","-1");
        botaoAdd.put("nome","Novo Cartão");
        aCartoes.add(botaoAdd);

        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cCartoes = db.rawQuery("Select _id, nome from cartoes", null);

            cCartoes.moveToFirst();
            int qtdRegistros = cCartoes.getCount();

            for (int i = 0; i < qtdRegistros; i++) {

                Map<String, Object> cartao = new HashMap<String, Object>();
                String id = cCartoes.getString(0);
                String nome = cCartoes.getString(1);

                cartao.put("id", id);
                cartao.put("nome", nome);

                aCartoes.add(cartao);

                cCartoes.moveToNext();
            }
            cCartoes.close();
        }catch (Exception e){}

        String[] de = {"nome"};
        int[] para = {R.id.tv_menu_texto};

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aCartoes,R.layout.menu_item, de, para);
        setListAdapter(adapter);

        registerForContextMenu(getListView());

        this.dialogConfirmacao = criaDialogConfirmacao();

    }


    @Override
    public void onListItemClick(ListView lvMenu, View view, int posicao, long id) {

        cartaoId = (String) aCartoes.get(posicao).get("id");
        Bundle data = new Bundle();
        data.putString("id", cartaoId);

        CadastroCartao newFragment = new CadastroCartao();
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
                cartaoId = (String) aCartoes.get(ItemSelecionado).get("id");
                aCartoes.remove(ItemSelecionado);
                removerCartao(cartaoId);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    private void removerCartao(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] where = new String[]{ id };
        db.delete("cartoes","_id = ? ", where);
    }

}
