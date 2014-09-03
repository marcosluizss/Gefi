package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

    private Spinner banco;
    private Spinner tipoFinanca;
    private List<Map<String, Object>> aBancos;
    private List<Map<String, Object>> aTiposFinanca;
    private Map<String, Object> item;

    private EditText nome,bancoId,codAgencia,numContaCorrente,valSaldoInicial;
    private Button btSalvar, btCancelar;
    private String financaId;

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
        financaId = extras.getString("id");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            aBancos = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_banco", " Selecione ");
            aBancos.add(item);

            Cursor cBancos = db.rawQuery("Select _id, codigo, nome from bancos", null);
            cBancos.moveToFirst();

            int qtdRegistros = cBancos.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cBancos.getString(0);
                int cod_banco = cBancos.getInt(1);
                String nome_banco = cBancos.getString(2);

                item.put("id", id);
                item.put("nome_banco", cod_banco + " - " + nome_banco);

                aBancos.add(item);

                cBancos.moveToNext();
            }
            cBancos.close();

            String[] de = {"nome_banco"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aBancos,android.R.layout.simple_spinner_item, de, para);
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

            banco = (Spinner) getActivity().findViewById(R.id.sp_banco_cc_financa);
            banco.setAdapter(simpleAdapter);

        }catch (Exception e){}

        try {
            aTiposFinanca = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_tipo_financa", " Selecione ");
            aTiposFinanca.add(item);

            Cursor cTiposFinanca = db.rawQuery("Select _id, nome from tipo_financa", null);
            cTiposFinanca.moveToFirst();

            int qtdRegistros = cTiposFinanca.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cTiposFinanca.getString(0);
                String nome_tipo_financa = cTiposFinanca.getString(1);

                item.put("id", id);
                item.put("nome_tipo_financa", nome_tipo_financa);

                aTiposFinanca.add(item);

                cTiposFinanca.moveToNext();
            }
            cTiposFinanca.close();

            String[] de = {"nome_tipo_financa"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aTiposFinanca,android.R.layout.simple_spinner_item, de, para);
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

            tipoFinanca = (Spinner) getActivity().findViewById(R.id.sp_tipo_financa);
            tipoFinanca.setAdapter(simpleAdapter);

        }catch (Exception e){}

        nome = (EditText)getActivity().findViewById(R.id.et_nome_financa);
        codAgencia = (EditText)getActivity().findViewById(R.id.et_agencia_cc_financa);
        numContaCorrente = (EditText)getActivity().findViewById(R.id.et_numero_cc_conta_financa);
        valSaldoInicial = (EditText)getActivity().findViewById(R.id.et_val_saldo_inicial_financa);

        btSalvar = (Button)getActivity().findViewById(R.id.bt_salvar_financa);
        btCancelar = (Button)getActivity().findViewById(R.id.bt_cancelar_financa);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarFinanca(view);
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltaTela();
            }
        });

        if(financaId != "-1"){
            preparaEdicao();
        }

    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    public void salvarFinanca(View view){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome",nome.getText().toString());

        try {
            values.put("tipo_financa_id", (String) aTiposFinanca.get(tipoFinanca.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("tipo_financa_id", "");
        }
        values.put("banco_id", (String) aBancos.get(banco.getSelectedItemPosition()).get("id"));
        values.put("num_agencia",codAgencia.getText().toString());
        values.put("num_conta_corrente",numContaCorrente.getText().toString());
        values.put("saldo_inicial",valSaldoInicial.getText().toString());

        long result = -1;
        if(financaId == "-1") {
            result = db.insert("financas", null, values);
        }else{
            result = db.update("financas", values, "_id = ?", new String[]{ financaId });
        }

        if(result != -1){
            Toast.makeText(getActivity(),"Registo salvo",Toast.LENGTH_SHORT).show();
            voltaTela();
        }else{
            Toast.makeText(getActivity(),"Erro ao salvar registro",Toast.LENGTH_SHORT).show();
        }
    }

    public void preparaEdicao(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome, tipo_financa_id, banco_id " +
                                    ", num_agencia, num_conta_corrente, saldo_inicial " +
                                    " FROM financas WHERE _id = ?", new String[]{ financaId });
        cursor.moveToFirst();

        nome.setText(cursor.getString(0));
        try {
            tipoFinanca.setSelection(Integer.parseInt((String) aBancos.get(cursor.getInt(1)).get("id")));
        }catch (Exception e){}
        banco.setSelection(Integer.parseInt((String)aBancos.get(cursor.getInt(2)).get("id")));
        codAgencia.setText(cursor.getString(3));
        numContaCorrente.setText(cursor.getString(4));
        valSaldoInicial.setText(cursor.getString(5));

        cursor.close();

    }

    public void voltaTela(){
        getActivity().onBackPressed();
    }

    public void alert(String msg){
        Toast alerta = Toast.makeText(getActivity(), msg , Toast.LENGTH_SHORT );
        alerta.show();
    }

}
