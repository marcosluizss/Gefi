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

    private EditText nomeConta,dtVencimento,qtdParcelas,valParcela,valTotal;
    private RadioGroup tipoConta;
    private CheckBox debitoAutomatico;
    private Spinner financa,cartao,periodicidade;
    private Button btSalvar, btCancelar;
    private String contaId;
    private DatabaseHelper helper;

    private List<Map<String, Object>> aFinanca;
    private List<Map<String, Object>> aCartao;
    private List<Map<String, Object>> aPeriodicidade;
    private Map<String, Object> item;

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
        contaId = extras.getString("id");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            aCartao = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_cartao", " Selecione ");
            aCartao.add(item);

            Cursor cCartao = db.rawQuery("Select _id, nome from cartoes", null);
            cCartao.moveToFirst();

            int qtdRegistros = cCartao.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cCartao.getString(0);
                String nome_cartao = cCartao.getString(1);

                item.put("id", id);
                item.put("nome_cartao", nome_cartao);

                aCartao.add(item);

                cCartao.moveToNext();
            }
            cCartao.close();

            String[] de = {"nome_cartao"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aCartao,android.R.layout.simple_spinner_item, de, para);
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

            cartao = (Spinner)getActivity().findViewById(R.id.sp_cartao_conta);
            cartao.setAdapter(simpleAdapter);

        }catch (Exception e){
            Log.d("DEBUG",e.toString());
        }

        try {
            aFinanca = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_financa", " Selecione ");
            aFinanca.add(item);

            Cursor cFinanca = db.rawQuery("Select _id, nome from financas", null);
            cFinanca.moveToFirst();

            int qtdRegistros = cFinanca.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cFinanca.getString(0);
                String nome_financa = cFinanca.getString(1);

                item.put("id", id);
                item.put("nome_financa", nome_financa);

                aFinanca.add(item);

                cFinanca.moveToNext();
            }
            cFinanca.close();

            String[] de = {"nome_financa"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aFinanca,android.R.layout.simple_spinner_item, de, para);
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

            financa = (Spinner)getActivity().findViewById(R.id.sp_financa_conta);
            financa.setAdapter(simpleAdapter);

        }catch (Exception e){
            Log.d("DEBUG",e.toString());
        }

        //try {
            aPeriodicidade = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_periodicidade", " Selecione ");
            aPeriodicidade.add(item);

            Cursor cPeriodicidade = db.rawQuery("Select _id, nome from periodicidades", null);
            cPeriodicidade.moveToFirst();

            int qtdRegistros = cPeriodicidade.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cPeriodicidade.getString(0);
                String nome_periodicidade = cPeriodicidade.getString(1);

                item.put("id", id);
                item.put("nome_periodicidade", nome_periodicidade);

                aPeriodicidade.add(item);

                cPeriodicidade.moveToNext();
            }
            cPeriodicidade.close();

            String[] de = {"nome_periodicidade"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aPeriodicidade,android.R.layout.simple_spinner_item, de, para);
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

            periodicidade = (Spinner)getActivity().findViewById(R.id.sp_periodicidade_conta);
            periodicidade.setAdapter(simpleAdapter);

        /*}catch (Exception e){
            Log.d("DEBUG",e.toString());
        }*/

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
                salvarConta(view);
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltaTela();
            }
        });

        if(contaId != "-1"){
            preparaEdicao();
        }
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    public void salvarConta(View view){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("nome",nomeConta.getText().toString());
        values.put("tipo",tipoConta.getCheckedRadioButtonId()==R.id.rb_conta_receita?1:2);
        values.put("qtd_parcelas",qtdParcelas.getText().toString());
        values.put("valor_parcelas",valParcela.getText().toString());
        values.put("valor_total",valTotal.getText().toString());
        values.put("dt_vencimento",dtVencimento.getText().toString());
        values.put("ind_debito_automatico",debitoAutomatico.isChecked()?"S":"N");

        try {
            values.put("periodicidade_id", (String) aPeriodicidade.get(periodicidade.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("tipo_cartao_id", "");
        }

        try {
            values.put("financa_id", (String) aFinanca.get(financa.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("financa_id", "");
        }

        try {
            values.put("cartao_id", (String) aCartao.get(cartao.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("cartao_id", "");
        }

        long result = -1;
        if(contaId == "-1") {
            result = db.insert("contas", null, values);
        }else{
            result = db.update("contas", values, "_id = ?", new String[]{ contaId });
        }

        if(result != -1){
            Toast.makeText(getActivity(), "Registo salvo", Toast.LENGTH_SHORT).show();
            voltaTela();
        }else{
            Toast.makeText(getActivity(),"Erro ao salvar registro",Toast.LENGTH_SHORT).show();
        }
    }

    public void preparaEdicao(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome, tipo, periodicidade_id, qtd_parcelas" +
                ", valor_parcela, valor_total, financa_id, cartao_id" +
                " dt_vencimento, ind_debito_automatico FROM contas WHERE _id = ?", new String[]{ contaId });

        nomeConta.setText(cursor.getString(0));
        tipoConta.check(cursor.getInt(1)==1?R.id.rb_conta_receita:R.id.rb_conta_despesa);

        try {
            periodicidade.setSelection(
                    Integer.parseInt((String) aPeriodicidade.get(cursor.getInt(2)).get("id")))
            ;
        }catch (Exception e){}
        qtdParcelas.setText(cursor.getString(3));
        valTotal.setText(cursor.getString(4));
        try {
            financa.setSelection(Integer.parseInt((String) aFinanca.get(cursor.getInt(5)).get("id")));
        }catch (Exception e){}
        try {
            cartao.setSelection(Integer.parseInt((String) aCartao.get(cursor.getInt(6)).get("id")));
        }catch (Exception e){}

        dtVencimento.setText(cursor.getString(7));
        debitoAutomatico.setChecked(cursor.getString(8)=="S"?true:false);

        cursor.moveToFirst();
        cursor.close();
    }

    public void voltaTela(){
        getActivity().onBackPressed();
    }

}
