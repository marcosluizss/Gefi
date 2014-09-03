package br.com.ml2s.gefi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by marcossantos on 22/08/2014.
 */
public class BancosList extends ListFragment implements DialogInterface.OnClickListener {

    private FragmentManager fm;
    private FragmentTransaction ft;

    private List<Map<String, Object>> aBancos;
    private AlertDialog dialogConfirmacao;
    private AdapterView.AdapterContextMenuInfo info;
    private int ItemSelecionado;

    private List<Fragment> aFrag;

    private DatabaseHelper helper;

    private String bancoId;

    public static BancosList init() {
        return new BancosList();
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

        aBancos = new ArrayList<Map<String, Object>>();
        Map<String, Object> botaoAdd = new HashMap<String, Object>();
        botaoAdd.put("id","-1");
        botaoAdd.put("nome_banco","Novo Banco");
        aBancos.add(botaoAdd);

        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cBancos = db.rawQuery("Select _id, codigo, nome from bancos", null);

            cBancos.moveToFirst();
            int qtdRegistros = cBancos.getCount();

            for (int i = 0; i < qtdRegistros; i++) {

                Map<String, Object> banco = new HashMap<String, Object>();
                String id = cBancos.getString(0);
                int cod_banco = cBancos.getInt(1);
                String nome_banco = cBancos.getString(2);

                banco.put("id", id);
                banco.put("nome_banco", cod_banco + " - " + nome_banco);

                aBancos.add(banco);

                cBancos.moveToNext();
            }
            cBancos.close();
        }catch (Exception e){}

        String[] de = {"nome_banco"};
        int[] para = {R.id.tv_menu_texto};

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aBancos,R.layout.menu_item, de, para);
        setListAdapter(adapter);

        registerForContextMenu(getListView());

        this.dialogConfirmacao = criaDialogConfirmacao();

    }


    @Override
    public void onListItemClick(ListView lvMenu, View view, int posicao, long id) {

        FrameLayout frame = (FrameLayout) getActivity().findViewById(R.id.fl_menu_container);
        frame.removeAllViews();

        bancoId = (String) aBancos.get(posicao).get("id");
        Bundle data = new Bundle();
        data.putString("id", bancoId);

        CadastroBanco newFragment = new CadastroBanco();
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
                bancoId = (String) aBancos.get(ItemSelecionado).get("id");
                aBancos.remove(ItemSelecionado);
                removerBanco(bancoId);
                getListView().invalidateViews();

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    private void removerBanco(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] where = new String[]{ id };
        db.delete("bancos","_id = ? ", where);
    }

}
