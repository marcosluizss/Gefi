package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
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
public class CadastroConta extends Fragment {

    private List<Map<String, Object>> aItens, aFinanca, aCartao, aPeriodicidade;
    private Map<String, Object> item, result;

    private EditText nomeConta,dtVencimento,qtdParcelas,valParcela,valTotal;
    private RadioGroup tipoConta;
    private CheckBox debitoAutomatico;
    private Spinner financa,cartao,periodicidade;
    private Button btSalvar, btCancelar;
    private String contaId;
    private int msgId;

    private DatabaseHelper helper;

    public static CadastroConta init(){
        return new CadastroConta();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_minhas_contas,container, false);

        helper = new DatabaseHelper(getActivity());
        Bundle extras = getArguments();
        contaId = extras.getString(DatabaseHelper.KEY_ID);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        aCartao = new ArrayList<Map<String, Object>>();
        cartao = (Spinner) getActivity().findViewById(R.id.sp_cartao_conta);
        result = getAdapterSpinner(DatabaseHelper.TABLE_CARTAO);
        cartao.setAdapter((SimpleAdapter)result.get("adapter"));
        aCartao = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        aFinanca = new ArrayList<Map<String, Object>>();
        financa = (Spinner) getActivity().findViewById(R.id.sp_financa_conta);
        result = getAdapterSpinner(DatabaseHelper.TABLE_FINANCA);
        financa.setAdapter((SimpleAdapter)result.get("adapter"));
        aFinanca = (List<Map<String, Object>>)result.get("itens");
        result.clear();

        aPeriodicidade = new ArrayList<Map<String, Object>>();
        periodicidade = (Spinner) getActivity().findViewById(R.id.sp_periodicidade_conta);
        result = getAdapterSpinner(DatabaseHelper.TABLE_PERIODICIDADE);
        periodicidade.setAdapter((SimpleAdapter)result.get("adapter"));
        aPeriodicidade = (List<Map<String, Object>>)result.get("itens");
        result.clear();


        nomeConta = (EditText)getActivity().findViewById(R.id.et_nome_conta);
        tipoConta = (RadioGroup)getActivity().findViewById(R.id.rg_tipo_conta);
        qtdParcelas = (EditText)getActivity().findViewById(R.id.et_qtd_parcelas_conta);
        valParcela = (EditText)getActivity().findViewById(R.id.et_valor_parcela_conta);
        valTotal = (EditText)getActivity().findViewById(R.id.et_valor_total_conta);
        dtVencimento = (EditText)getActivity().findViewById(R.id.et_data_vencimento_conta);
        debitoAutomatico = (CheckBox)getActivity().findViewById(R.id.cb_debito_automatico_conta);

        btSalvar = (Button)getActivity().findViewById(R.id.bt_salvar_conta);
        btCancelar = (Button)getActivity().findViewById(R.id.bt_cancelar_conta);

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

        if(!contaId.equals(DatabaseHelper.VALUE_ID_NULL)){
            preparaEdicao();
        }
    }

    public void salvar(View view){
        DataSourceTools db = new DataSourceTools(helper);

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.KEY_NOME,nomeConta.getText().toString());
        values.put(DatabaseHelper.KEY_TIPO,tipoConta.getCheckedRadioButtonId()==R.id.rb_conta_receita?DatabaseHelper.VALUE_RECEITA:DatabaseHelper.VALUE_DESPESA);
        values.put(DatabaseHelper.KEY_QTD_PARECLAS,qtdParcelas.getText().toString());
        values.put(DatabaseHelper.KEY_VAL_PARCELA,valParcela.getText().toString());
        values.put(DatabaseHelper.KEY_VAL_TOTAL,valTotal.getText().toString());
        values.put(DatabaseHelper.KEY_DT_VENCIMENTO,dtVencimento.getText().toString());
        values.put(DatabaseHelper.KEY_IND_DEBITO_AUTOMATICO,debitoAutomatico.isChecked()?DatabaseHelper.VALUE_SIM:DatabaseHelper.VALUE_NAO);

        values.put(DatabaseHelper.KEY_PERIODICIDADE_ID, aPeriodicidade.get(periodicidade.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());
        values.put(DatabaseHelper.KEY_FINANCA_ID, aFinanca.get(financa.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());
        values.put(DatabaseHelper.KEY_CARTAO_ID, aFinanca.get(cartao.getSelectedItemPosition()).get(DatabaseHelper.KEY_ID).toString());

        if(contaId.equals(DatabaseHelper.VALUE_ID_NULL)) {
            msgId = db.save(DatabaseHelper.TABLE_CONTA, values);
        }else{
            msgId = db.update(DatabaseHelper.TABLE_CONTA, values, contaId);
        }

        Toast.makeText(getActivity(),msgId,Toast.LENGTH_SHORT).show();
        if(msgId == R.string.salvar_sucesso){
            voltaTela();
        }

    }

    public void preparaEdicao(){

        DataSourceTools db = new DataSourceTools(helper);
        Map<String,Object> conta = db.find(DatabaseHelper.TABLE_CONTA,null,contaId);

        nomeConta.setText(conta.get(DatabaseHelper.KEY_NOME).toString());
        tipoConta.check(conta.get(DatabaseHelper.KEY_TIPO).toString().equals("1")?R.id.rb_conta_receita:R.id.rb_conta_despesa);


        periodicidade.setSelection(getPosition(aPeriodicidade,conta.get(DatabaseHelper.KEY_PERIODICIDADE_ID).toString()));

        qtdParcelas.setText(conta.get(DatabaseHelper.KEY_QTD_PARECLAS).toString());
        valTotal.setText(conta.get(DatabaseHelper.KEY_VAL_TOTAL).toString());

        financa.setSelection(getPosition(aFinanca,conta.get(DatabaseHelper.KEY_FINANCA_ID).toString()));
        cartao.setSelection(getPosition(aCartao,conta.get(DatabaseHelper.KEY_CARTAO_ID).toString()));

        dtVencimento.setText(conta.get(DatabaseHelper.KEY_DT_VENCIMENTO).toString());
        debitoAutomatico.setChecked(conta.get(DatabaseHelper.KEY_IND_DEBITO_AUTOMATICO).equals(1));;

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
