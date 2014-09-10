package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcossantos on 21/08/2014.
 */
public class CadastroCartao extends Fragment {

    private List<Map<String, Object>> aItens, aContaCorrente,aTipoCartao, aBandeira;
    private Map<String, Object> item, result;

    private EditText nomeCartao,ultimosNum,dtValidade,valLimite,valUtilizado,valDisponivel;
    private Spinner tipoCartao,bandeira,contaCorrente;
    private Button btSalvar, btCancelar;
    private String cartaoId;
    private int msgId;

    private DatabaseHelper helper;

    public static CadastroCartao init(){
        return new CadastroCartao();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_meus_cartoes,container, false);

        helper = new DatabaseHelper(getActivity());
        Bundle extras = getArguments();
        cartaoId = extras.getString(DatabaseHelper.KEY_ID);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        result = new HashMap<String, Object>();

        aContaCorrente = new ArrayList<Map<String, Object>>();
        contaCorrente = (Spinner) getActivity().findViewById(R.id.sp_conta_cartao);
        result = getAdapterSpinner(DatabaseHelper.TABLE_FINANCA);
        contaCorrente.setAdapter((SimpleAdapter)result.get("adapter"));
        aContaCorrente = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        aBandeira = new ArrayList<Map<String, Object>>();
        bandeira = (Spinner) getActivity().findViewById(R.id.sp_bandeira_cartao);
        result = getAdapterSpinner(DatabaseHelper.TABLE_BANDEIRA_CARTAO);
        bandeira.setAdapter((SimpleAdapter)result.get("adapter"));
        aBandeira = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        aTipoCartao = new ArrayList<Map<String, Object>>();
        tipoCartao = (Spinner) getActivity().findViewById(R.id.sp_tipo_cartao);
        result = getAdapterSpinner(DatabaseHelper.TABLE_TIPO_CARTAO);
        tipoCartao.setAdapter((SimpleAdapter)result.get("adapter"));
        aTipoCartao = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        nomeCartao = (EditText)getActivity().findViewById(R.id.et_nome_cartao);
        ultimosNum = (EditText)getActivity().findViewById(R.id.et_ult_num_cartao);
        dtValidade = (EditText)getActivity().findViewById(R.id.et_dt_validade_cartao);
        valLimite = (EditText)getActivity().findViewById(R.id.et_val_limite_cartao);
        valUtilizado = (EditText)getActivity().findViewById(R.id.et_val_utilizado_cartao);
        valDisponivel = (EditText)getActivity().findViewById(R.id.et_val_disponivel_cartao);

        btSalvar = (Button)getActivity().findViewById(R.id.bt_salvar_cartao);
        btCancelar = (Button)getActivity().findViewById(R.id.bt_cancelar_cartao);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar(view);
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltaTela();
            }
        });

        if(!cartaoId.equals(DatabaseHelper.VALUE_ID_NULL)){
            preparaEdicao();
        }

    }

    public void salvar(View view){
        DataSourceTools db = new DataSourceTools(helper);

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.KEY_NOME,nomeCartao.getText().toString());
        values.put(DatabaseHelper.KEY_ULTIMOS_4_NUM,ultimosNum.getText().toString());
        values.put(DatabaseHelper.KEY_VAL_LIMITE_TOTAL,valLimite.getText().toString());
        values.put(DatabaseHelper.KEY_VAL_LIMITE_UTILIZADO,valUtilizado.getText().toString());
        values.put(DatabaseHelper.KEY_VAL_LIMITE_DISPONIVEL,valDisponivel.getText().toString());
        values.put(DatabaseHelper.KEY_DT_VALIDADE,dtValidade.getText().toString());

        values.put(DatabaseHelper.KEY_TIPO_CARTAO_ID, aTipoCartao.get(tipoCartao.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());
        values.put(DatabaseHelper.KEY_FINANCA_ID, aContaCorrente.get(contaCorrente.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());
        values.put(DatabaseHelper.KEY_BANDEIRA_CARTAO_ID, aBandeira.get(bandeira.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());

        if(cartaoId.equals(DatabaseHelper.VALUE_ID_NULL)) {
            msgId = db.save(DatabaseHelper.TABLE_CARTAO, values);
        }else{
            msgId = db.update(DatabaseHelper.TABLE_CARTAO, values, cartaoId);
        }

        Toast.makeText(getActivity(),msgId,Toast.LENGTH_SHORT).show();
        if(msgId == R.string.salvar_sucesso){
            voltaTela();
        }

    }

    public void preparaEdicao(){

        DataSourceTools db = new DataSourceTools(helper);
        Map<String,Object> cartao = db.find(DatabaseHelper.TABLE_CARTAO,null,cartaoId);

        nomeCartao.setText(cartao.get(DatabaseHelper.KEY_NOME).toString());
        tipoCartao.setSelection(getPosition(aTipoCartao,cartao.get(DatabaseHelper.KEY_TIPO_CARTAO_ID).toString()));
        contaCorrente.setSelection(getPosition(aContaCorrente,cartao.get(DatabaseHelper.KEY_FINANCA_ID).toString()));
        bandeira.setSelection(getPosition(aBandeira,cartao.get(DatabaseHelper.KEY_BANDEIRA_CARTAO_ID).toString()));
        ultimosNum.setText(cartao.get(DatabaseHelper.KEY_ULTIMOS_4_NUM).toString());
        valLimite.setText(cartao.get(DatabaseHelper.KEY_VAL_LIMITE_TOTAL).toString());
        valUtilizado.setText(cartao.get(DatabaseHelper.KEY_VAL_LIMITE_UTILIZADO).toString());
        valDisponivel.setText(cartao.get(DatabaseHelper.KEY_VAL_LIMITE_DISPONIVEL).toString());
        dtValidade.setText(cartao.get(DatabaseHelper.KEY_DT_VALIDADE).toString());

    }

    public int getPosition(List<Map<String, Object>> aItens, String valor){
        int count;
        count = aItens.size();
        for(int i=1;i<count;i++){
            if( aItens.get(i).get(DatabaseHelper.KEY_ID).toString().equals(valor) ){
                return i;
            }
        }
        return 0;
    }

    public void voltaTela(){
        getActivity().onBackPressed();
    }

    public Map<String,Object> getAdapterSpinner(String table){

        DataSourceTools db = new DataSourceTools(helper);

        //busca a lista de de itens
        aItens = new ArrayList<Map<String, Object>>();
        item = new HashMap<String, Object>();
        item.put(DatabaseHelper.KEY_ID, "-1");
        item.put(DatabaseHelper.KEY_NOME,getText(R.string.selecione));
        aItens.add(item);

        aItens.addAll(db.findAll(table, null));

        String[] de = {DatabaseHelper.KEY_NOME};
        int[] para = {android.R.id.text1};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aItens,android.R.layout.simple_spinner_item, de, para);
        simpleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                // We configured the SimpleAdapter to create TextViews (see
                // the 'to' array, above), so this cast should be safe:
                TextView textView = (TextView) view;
                textView.setText(textRepresentation);
                return true;
            }
        };
        simpleAdapter.setViewBinder(viewBinder);

        result = new HashMap<String, Object>();
        result.put("adapter",simpleAdapter);
        result.put("itens",aItens);
        return result;
    }

}
