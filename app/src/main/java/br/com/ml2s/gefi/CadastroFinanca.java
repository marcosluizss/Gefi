package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class CadastroFinanca extends Fragment {

    private List<Map<String, Object>> aItens,aBancos,aTiposFinanca;
    private Map<String, Object> item, result;

    private EditText nome,codAgencia,numContaCorrente,valSaldoInicial;
    private Spinner banco,tipoFinanca;
    private Button btSalvar, btCancelar;
    private String financaId;
    private int msgId;

    private DatabaseHelper helper;

    public static CadastroFinanca init(){
        return new CadastroFinanca();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_minhas_financas,container, false);

        helper = new DatabaseHelper(getActivity());
        Bundle extras = getArguments();
        financaId = extras.getString(DatabaseHelper.KEY_ID);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        result = new HashMap<String, Object>();

        banco = (Spinner) getActivity().findViewById(R.id.sp_banco_cc_financa);
        result = getAdapterSpinner(DatabaseHelper.TABLE_BANCO);
        banco.setAdapter((SimpleAdapter)result.get("adapter"));
        aBancos = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        tipoFinanca = (Spinner) getActivity().findViewById(R.id.sp_tipo_financa);
        result = getAdapterSpinner(DatabaseHelper.TABLE_TIPO_FINANCA);
        tipoFinanca.setAdapter((SimpleAdapter)result.get("adapter"));
        aTiposFinanca = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        nome = (EditText)getActivity().findViewById(R.id.et_nome_financa);
        codAgencia = (EditText)getActivity().findViewById(R.id.et_agencia_cc_financa);
        numContaCorrente = (EditText)getActivity().findViewById(R.id.et_numero_cc_conta_financa);
        valSaldoInicial = (EditText)getActivity().findViewById(R.id.et_val_saldo_inicial_financa);

        btSalvar = (Button)getActivity().findViewById(R.id.bt_salvar_financa);
        btCancelar = (Button)getActivity().findViewById(R.id.bt_cancelar_financa);

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

        if(financaId != DatabaseHelper.VALUE_ID_NULL){
            preparaEdicao();
        }

    }

    public void salvar(View view){
        DataSourceTools db = new DataSourceTools(helper);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_NOME,nome.getText().toString());

        values.put(DatabaseHelper.KEY_TIPO_FINANCA_ID, aTiposFinanca.get(tipoFinanca.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());
        values.put(DatabaseHelper.KEY_BANCO_ID, aBancos.get(banco.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());
        values.put(DatabaseHelper.KEY_COD_AGENCIA,codAgencia.getText().toString());
        values.put(DatabaseHelper.KEY_NUM_CONTA_CORRENTE,numContaCorrente.getText().toString());
        values.put(DatabaseHelper.KEY_VAL_SALDO_INICIAL,valSaldoInicial.getText().toString());

        long result = -1;
        if(financaId == DatabaseHelper.VALUE_ID_NULL) {
            msgId = db.save(DatabaseHelper.TABLE_FINANCA, values);
        }else{
            msgId = db.update(DatabaseHelper.TABLE_FINANCA, values, financaId);
        }

        Toast.makeText(getActivity(),msgId,Toast.LENGTH_SHORT).show();
        if(msgId == R.string.salvar_sucesso){
            voltaTela();
        }
    }

    public void preparaEdicao(){
        DataSourceTools db = new DataSourceTools(helper);
        Map<String,Object> financa = db.find(DatabaseHelper.TABLE_FINANCA,null,financaId);

        Log.w("financa",financa.toString());

        nome.setText(financa.get(DatabaseHelper.KEY_NOME).toString());
        tipoFinanca.setSelection(getPosition(aTiposFinanca,financa.get(DatabaseHelper.KEY_TIPO_FINANCA_ID).toString()));
        banco.setSelection(getPosition(aBancos, financa.get(DatabaseHelper.KEY_BANCO_ID).toString()));
        codAgencia.setText(financa.get(DatabaseHelper.KEY_COD_AGENCIA).toString());
        numContaCorrente.setText(financa.get(DatabaseHelper.KEY_NUM_CONTA_CORRENTE).toString());
        valSaldoInicial.setText(financa.get(DatabaseHelper.KEY_VAL_SALDO_INICIAL).toString());

    }

    public int getPosition(List<Map<String, Object>> aItens, String valor){
        int count;
        count = aItens.size();
        for(int i=1;i<count;i++){
            if( aItens.get(i).get(DatabaseHelper.KEY_ID).toString() == valor){
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
        item.put(DatabaseHelper.KEY_ID, null);
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
