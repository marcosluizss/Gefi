package br.com.ml2s.gefi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcossantos on 27/08/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gefi";
    private static int versao = 1;

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE bancos (_id INTEGER PRIMARY KEY, codigo INTEGER UNIQUE, nome TEXT);");
        //-----
        db.execSQL("CREATE TABLE tipos_financa (_id INTEGER PRIMARY KEY, nome TEXT);");

        db.execSQL("CREATE TABLE financas (_id INTEGER PRIMARY KEY," +
                    " nome TEXT, tipo_financa_id int, banco_id int," +
                    " num_agencia text, num_conta_corrente text," +
                    " saldo_inicial DOUBLE, dt_inclusao DATE, dt_atualizacao DATE," +
                    " FOREIGN KEY(banco_id) REFERENCES bancos(_id)," +
                    " FOREIGN KEY(tipo_financa_id) REFERENCES tipos_financa(_id));");
        //-----
        db.execSQL("CREATE TABLE bandeiras_cartao (_id INTEGER PRIMARY KEY, nome TEXT);");

        db.execSQL("CREATE TABLE tipos_cartao (_id INTEGER PRIMARY KEY, nome TEXT);");

        db.execSQL("CREATE TABLE cartoes (_id INTEGER PRIMARY KEY," +
                " nome TEXT, bandeira_cartao_id int, ultimos_4_num int," +
                " financa_id int, limite_total DOUBLE, limite_utilizado DOUBLE" +
                " limite_disponivel DOUBLE, dt_validade DATE, dt_inclusao DATE," +
                " tipo_cartao_id int, dt_atualizacao DATE," +
                " FOREIGN KEY(bandeira_cartao_id) REFERENCES bandeiras_cartao(_id)," +
                " FOREIGN KEY(financa_id) REFERENCES financas(_id)," +
                " FOREIGN KEY(tipo_cartao_id) REFERENCES tipos_cartao(_id));");
        //-----
        db.execSQL("CREATE TABLE periodicidades (_id INTEGER PRIMARY KEY, nome TEXT);");

        db.execSQL("CREATE TABLE contas (_id INTEGER PRIMARY KEY," +
                " nome TEXT, tipo int, periodicidade_id int, qtd_parcelas int," +
                " valor_parcela DOUBLE, valor_total DOUBLE," +
                " financa_id int, cartao_id int," +
                " dt_vencimento DATE, dt_inclusao DATE, dt_atualizacao DATE," +
                " ind_atrasada TEXT, dt_ultima_trasacao DATE, ind_debito_automatico TEXT," +
                " FOREIGN KEY(periodicidade_id) REFERENCES periodicidades(_id)," +
                " FOREIGN KEY(financa_id) REFERENCES financas(_id)," +
                " FOREIGN KEY(cartao_id) REFERENCES cartoes(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion){
        //chamada quando existe uma nova versão para o banco de dados em relação a instalada no cliente
    }
}
