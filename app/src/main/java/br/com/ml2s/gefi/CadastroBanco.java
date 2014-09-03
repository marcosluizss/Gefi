package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Created by marcossantos on 21/08/2014.
 */
public class CadastroBanco extends Fragment {

    private EditText codBanco,nomeBanco;
    private Button btSalvar, btCancelar;
    private String bancoId;
    private DatabaseHelper helper;

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
        bancoId = extras.getString("id");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        codBanco = (EditText)getActivity().findViewById(R.id.et_codigo_banco);
        nomeBanco = (EditText)getActivity().findViewById(R.id.et_nome_banco);

        btSalvar = (Button)getActivity().findViewById(R.id.bt_salvar_banco);
        btCancelar = (Button)getActivity().findViewById(R.id.bt_cancelar_banco);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarBanco(view);
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltaTela();
            }
        });

        if(bancoId != "-1"){
            preparaEdicao();
        }

    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    public void salvarBanco(View view){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("codigo",codBanco.getText().toString());
        values.put("nome", nomeBanco.getText().toString());

        long result = -1;
        if(bancoId == "-1") {
            result = db.insert("bancos", null, values);
        }else{
            result = db.update("bancos", values, "_id = ?", new String[]{ bancoId });
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
        Cursor cursor = db.rawQuery("SELECT codigo, nome FROM bancos WHERE _id = ?", new String[]{ bancoId });
        cursor.moveToFirst();
        codBanco.setText(cursor.getString(0));
        nomeBanco.setText(cursor.getString(1));
        cursor.close();
    }

    public void voltaTela(){
        getActivity().onBackPressed();
    }

}
