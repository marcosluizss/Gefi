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

    private EditText nomeCartao,ultimosNum,dtValidade,valLimite,valUtilizado,valDisponivel;
    private Spinner tipoCartao,bandeira,contaCorrente;
    private Button btSalvar, btCancelar;
    private String cartaoId;
    private DatabaseHelper helper;

    private List<Map<String, Object>> aContaCorrente;
    private List<Map<String, Object>> aTipoCartao;
    private List<Map<String, Object>> aBandeira;
    private Map<String, Object> item;

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
        cartaoId = extras.getString("id");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            aContaCorrente = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_conta", " Selecione ");
            aContaCorrente.add(item);

            Cursor cContaCorrente = db.rawQuery("Select _id, nome from financas", null);
            cContaCorrente.moveToFirst();

            int qtdRegistros = cContaCorrente.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cContaCorrente.getString(0);
                String nome_conta = cContaCorrente.getString(1);

                item.put("id", id);
                item.put("nome_conta", nome_conta);

                aContaCorrente.add(item);

                cContaCorrente.moveToNext();
            }
            cContaCorrente.close();

            String[] de = {"nome_conta"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aContaCorrente,android.R.layout.simple_spinner_item, de, para);
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

            contaCorrente = (Spinner)getActivity().findViewById(R.id.sp_conta_cartao);
            contaCorrente.setAdapter(simpleAdapter);

        }catch (Exception e){}

        try {
            aBandeira = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_bandeira", " Selecione ");
            aBandeira.add(item);

            Cursor cBandeira = db.rawQuery("Select _id, nome from bandeiras_cartao", null);
            cBandeira.moveToFirst();

            int qtdRegistros = cBandeira.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cBandeira.getString(0);
                String nome_bandeira = cBandeira.getString(1);

                item.put("id", id);
                item.put("nome_bandeira", nome_bandeira);

                aBandeira.add(item);

                cBandeira.moveToNext();
            }
            cBandeira.close();

            String[] de = {"nome_bandeira"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aBandeira,android.R.layout.simple_spinner_item, de, para);
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

            bandeira = (Spinner)getActivity().findViewById(R.id.sp_bandeira_cartao);
            bandeira.setAdapter(simpleAdapter);

        }catch (Exception e){}

        try {
            aBandeira = new ArrayList<Map<String, Object>>();

            item = new HashMap<String, Object>();
            item.put("id", "-1");
            item.put("nome_tipo_cartao", " Selecione ");
            aBandeira.add(item);

            Cursor cTipoCartao = db.rawQuery("Select _id, nome from tipos_cartao", null);
            cTipoCartao.moveToFirst();

            int qtdRegistros = cTipoCartao.getCount();
            for (int i = 0; i < qtdRegistros; i++) {

                item = new HashMap<String, Object>();
                String id = cTipoCartao.getString(0);
                String nome_tipo_cartao = cTipoCartao.getString(1);

                item.put("id", id);
                item.put("nome_tipo_cartao", nome_tipo_cartao);

                aBandeira.add(item);

                cTipoCartao.moveToNext();
            }
            cTipoCartao.close();

            String[] de = {"nome_tipo_cartao"};
            int[] para = {android.R.id.text1};

            final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aBandeira,android.R.layout.simple_spinner_item, de, para);
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

            tipoCartao = (Spinner)getActivity().findViewById(R.id.sp_tipo_cartao);
            tipoCartao.setAdapter(simpleAdapter);

        }catch (Exception e){}


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
                salvarCartao(view);
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltaTela();
            }
        });

        if(cartaoId != "-1"){
            preparaEdicao();
        }

    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    public void salvarCartao(View view){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("nome",nomeCartao.getText().toString());
        values.put("ultimos_4_num",ultimosNum.getText().toString());
        values.put("limite_total",valLimite.getText().toString());
        values.put("limite_utilizado",valUtilizado.getText().toString());
        values.put("limite_disponivel",valDisponivel.getText().toString());
        values.put("dt_validade",dtValidade.getText().toString());

        try {
            values.put("tipo_cartao_id", (String) aTipoCartao.get(tipoCartao.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("tipo_cartao_id", "");
        }

        try {
            values.put("financa_id", (String) aContaCorrente.get(contaCorrente.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("financa_id", "");
        }

        try {
            values.put("bandeira_cartao_id", (String) aBandeira.get(bandeira.getSelectedItemPosition()).get("id"));
        }catch (Exception e){
            values.put("bandeira_cartao_id", "");
        }

        long result = -1;
        if(cartaoId == "-1") {
            result = db.insert("cartoes", null, values);
        }else{
            result = db.update("cartoes", values, "_id = ?", new String[]{ cartaoId });
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
        Cursor cursor = db.rawQuery("SELECT nome, tipo_cartao_id, financa_id, bandeira_cartao_id" +
                                    ", ultimos_4_num, saldo_limite, saldo_disponivel, salto_total" +
                                    " dt_validade FROM cartoes WHERE _id = ?", new String[]{ cartaoId });

        nomeCartao.setText(cursor.getString(0));
        try {
            tipoCartao.setSelection(Integer.parseInt((String) aTipoCartao.get(cursor.getInt(1)).get("id")));
        }catch (Exception e){}
        try {
            contaCorrente.setSelection(Integer.parseInt((String) aContaCorrente.get(cursor.getInt(2)).get("id")));
        }catch (Exception e){}
        try {
            bandeira.setSelection(Integer.parseInt((String) aBandeira.get(cursor.getInt(3)).get("id")));
        }catch (Exception e){}

        ultimosNum.setText(cursor.getString(4));
        valLimite.setText(cursor.getString(5));
        valUtilizado.setText(cursor.getString(6));
        valDisponivel.setText(cursor.getString(7));
        dtValidade.setText(cursor.getString(8));

        cursor.moveToFirst();
        cursor.close();
    }

    public void voltaTela(){
        getActivity().onBackPressed();
    }

}
