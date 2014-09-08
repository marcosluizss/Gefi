package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by marcossantos on 21/08/2014.
 */
public class CadastroBanco extends Fragment {

    private EditText codBanco,nomeBanco;
    private Button btSalvar, btCancelar;
    private String bancoId;
    private DatabaseHelper helper;
    private int msgId;

    public static CadastroBanco init(){
        return new CadastroBanco();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_meus_bancos,container, false);

        helper = new DatabaseHelper(getActivity());
        Bundle extras = getArguments();
        bancoId = extras.getString(DatabaseHelper.KEY_ID);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        codBanco = (EditText)getActivity().findViewById(R.id.et_codigo_banco);
        nomeBanco = (EditText)getActivity().findViewById(R.id.et_nome_banco);

        codBanco.addTextChangedListener(Mask.number(codBanco));

        btSalvar = (Button)getActivity().findViewById(R.id.bt_salvar_banco);
        btCancelar = (Button)getActivity().findViewById(R.id.bt_cancelar_banco);

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

        if(bancoId != DatabaseHelper.VALUE_ID_NULL){
            preparaEdicao();
        }

    }

    public void salvar(View view){
        DataSourceTools db = new DataSourceTools(helper);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_CODIGO,codBanco.getText().toString());
        values.put(DatabaseHelper.KEY_NOME, nomeBanco.getText().toString());

        if(bancoId == DatabaseHelper.VALUE_ID_NULL) {
            msgId = db.save(DatabaseHelper.TABLE_BANCO, values);
        }else{
            msgId = db.update(DatabaseHelper.TABLE_BANCO, values, bancoId);
        }

        Toast.makeText(getActivity(),msgId,Toast.LENGTH_SHORT).show();
        if(msgId == R.string.salvar_sucesso){
            voltaTela();
        }
    }

    public void preparaEdicao(){
        // bancoId;
        DataSourceTools db = new DataSourceTools(helper);

        Map<String,Object> banco = db.find(DatabaseHelper.TABLE_BANCO,null,bancoId);
        codBanco.setText(banco.get(DatabaseHelper.KEY_CODIGO).toString());
        nomeBanco.setText(banco.get(DatabaseHelper.KEY_NOME).toString());

    }

    public void voltaTela(){
        getActivity().onBackPressed();
    }

}
