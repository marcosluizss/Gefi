package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marcossantos on 27/08/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gefi";
    private static final int DB_VERSAO = 12;

    private static final String TAG = "DATABASE";

    public static final String VALUE_ID_NULL = "-1";

    // tabelas
    public static final String TABLE_BANCO = "bancos";
    public static final String TABLE_TIPO_FINANCA = "tipos_financa";
    public static final String TABLE_FINANCA = "financas";
    public static final String TABLE_BANDEIRA_CARTAO = "bandeiras_cartao";
    public static final String TABLE_TIPO_CARTAO = "tipos_cartao";
    public static final String TABLE_CARTAO = "cartoes";
    public static final String TABLE_PERIODICIDADE = "periodicidades";
    public static final String TABLE_CONTA = "contas";
    public static final String TABLE_TRANSACAO = "transacoes";

    //campos
    public static final String KEY_ID = "_id";
    public static final String KEY_CODIGO = "codigo";
    public static final String KEY_NOME = "nome";
    public static final String KEY_QTD_PARECLAS = "qtd_parcelas";
    public static final String KEY_NUM_PARECLA = "num_parcela";
    public static final String KEY_TIPO = "tipo";

    //campos de dados bancarios
    public static final String KEY_COD_AGENCIA = "cod_agencia";
    public static final String KEY_NUM_CONTA_CORRENTE = "num_conta_corrente";
    public static final String KEY_ULTIMOS_4_NUM = "ultimos_4_num";

    //campos de valores
    public static final String KEY_VAL_SALDO_INICIAL = "val_saldo_inicial";
    public static final String KEY_VAL_LIMITE_TOTAL = "val_limite_total";
    public static final String KEY_VAL_LIMITE_UTILIZADO = "val_limite_utilizado";
    public static final String KEY_VAL_LIMITE_DISPONIVEL = "val_limite_disponivel";
    public static final String KEY_VAL_PARCELA = "val_parcela";
    public static final String KEY_VAL_TOTAL = "val_total";
    public static final String KEY_VAL_PAGO = "val_pago";

    //indicadores
    public static final String KEY_IND_ATRASADA = "ind_atrasada";
    public static final String KEY_IND_DEBITO_AUTOMATICO = "ind_debito_automatico";
    public static final String KEY_IND_PAGO = "ind_pago";
    public static final String KEY_IND_ATIVO = "ind_ativo";

    // campos de data
    public static final String KEY_DT_INCLUSAO = "dt_inclusao";
    public static final String KEY_DT_ATUALIZACAO = "dt_atualizacao";
    public static final String KEY_DT_VALIDADE = "dt_validade";
    public static final String KEY_DT_TRANSACAO = "dt_transacao";
    public static final String KEY_DT_PAGAMENTO = "dt_pagamento";
    public static final String KEY_DT_DESATIVACAO = "dt_desativacao";
    public static final String KEY_DT_VENCIMENTO = "dt_vencimento";

    // compos ids referenciados
    public static final String KEY_BANCO_ID = "banco_id";
    public static final String KEY_TIPO_FINANCA_ID = "tipo_financa_id";
    public static final String KEY_FINANCA_ID = "financa_id";
    public static final String KEY_BANDEIRA_CARTAO_ID = "bandeira_cartao_id";
    public static final String KEY_TIPO_CARTAO_ID = "tipo_cartao_id";
    public static final String KEY_CARTAO_ID = "cartao_id";
    public static final String KEY_PERIODICIDADE_ID = "periodicidade_id";
    public static final String KEY_CONTA_ID = "conta_id";
    public static final String KEY_TRANSACAO_ID = "transacao_id";

    //FOREIGN KEYs
    private static final String FOREIGNKEY_BANCO = "FOREIGN KEY("+KEY_BANCO_ID+") REFERENCES "+TABLE_BANCO+"("+KEY_ID+")";
    private static final String FOREIGNKEY_TIPO_FINANCA = "FOREIGN KEY("+KEY_TIPO_FINANCA_ID+") REFERENCES "+TABLE_TIPO_FINANCA+"("+KEY_ID+")";
    private static final String FOREIGNKEY_FINANCA = "FOREIGN KEY("+KEY_FINANCA_ID+") REFERENCES "+TABLE_FINANCA+"("+KEY_ID+")";
    private static final String FOREIGNKEY_BANDEIRA_CARTAO = "FOREIGN KEY("+KEY_BANDEIRA_CARTAO_ID+") REFERENCES "+TABLE_BANDEIRA_CARTAO+"("+KEY_ID+")";
    private static final String FOREIGNKEY_TIPO_CARTAO = "FOREIGN KEY("+KEY_TIPO_CARTAO_ID+") REFERENCES "+TABLE_TIPO_CARTAO+"("+KEY_ID+")";
    private static final String FOREIGNKEY_CARTAO = "FOREIGN KEY("+KEY_CARTAO_ID+") REFERENCES "+TABLE_CARTAO+"("+KEY_ID+")";
    private static final String FOREIGNKEY_PERIODICIDADE = "FOREIGN KEY("+KEY_PERIODICIDADE_ID+") REFERENCES "+TABLE_PERIODICIDADE+"("+KEY_ID+")";
    private static final String FOREIGNKEY_CONTA = "FOREIGN KEY("+KEY_CONTA_ID+") REFERENCES "+TABLE_CONTA+"("+KEY_ID+")";
    private static final String FOREIGNKEY_TRANSACAO = "FOREIGN KEY("+KEY_TRANSACAO_ID+") REFERENCES "+TABLE_TRANSACAO+"("+KEY_ID+")";

    private ContentValues values = new ContentValues();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_BANCO+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_CODIGO+" INTEGER UNIQUE, "+KEY_NOME+" TEXT" +
                    KEY_IND_ATIVO + " INT, "+ KEY_DT_DESATIVACAO +" DATE );");
        //-----
        db.execSQL("CREATE TABLE " + TABLE_TIPO_FINANCA + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NOME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_FINANCA + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NOME + " TEXT, " + KEY_TIPO_FINANCA_ID + " INT, " + KEY_BANCO_ID + " INT, " +
                KEY_COD_AGENCIA + " TEXT, " + KEY_NUM_CONTA_CORRENTE + " TEXT, " +
                KEY_VAL_SALDO_INICIAL + " DOUBLE, " + KEY_DT_INCLUSAO + " DATE, " + KEY_DT_ATUALIZACAO + " DATE, " +
                FOREIGNKEY_BANCO + " , " + FOREIGNKEY_TIPO_FINANCA + " );");
        //-----
        db.execSQL("CREATE TABLE " + TABLE_BANDEIRA_CARTAO + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NOME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_TIPO_CARTAO + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NOME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_CARTAO + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NOME + " TEXT, " + KEY_BANDEIRA_CARTAO_ID + " INT, " + KEY_ULTIMOS_4_NUM + " INT, " +
                KEY_FINANCA_ID + " INT, " + KEY_VAL_LIMITE_TOTAL + " DOUBLE, " + KEY_VAL_LIMITE_UTILIZADO + " DOUBLE, " +
                KEY_VAL_LIMITE_DISPONIVEL + " DOUBLE, " + KEY_DT_VALIDADE + " DATE, " + KEY_DT_INCLUSAO + " DATE, " +
                KEY_TIPO_CARTAO_ID + " INT, " + KEY_DT_ATUALIZACAO + " DATE," +
                FOREIGNKEY_BANDEIRA_CARTAO + " , " + FOREIGNKEY_FINANCA + " , " + FOREIGNKEY_TIPO_CARTAO + " );");
        //-----
        db.execSQL("CREATE TABLE " + TABLE_PERIODICIDADE + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NOME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_CONTA +" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NOME + " TEXT, " + KEY_TIPO+" int, "+ KEY_PERIODICIDADE_ID+" int, "+
                KEY_QTD_PARECLAS+" int, " + KEY_VAL_PARCELA + " DOUBLE, "+KEY_VAL_TOTAL+" DOUBLE, " +
                KEY_FINANCA_ID + " int, " + KEY_CARTAO_ID+" int, " +
                KEY_DT_VENCIMENTO + " DATE, " +KEY_DT_INCLUSAO+" DATE, "+KEY_DT_ATUALIZACAO+" DATE, " +
                KEY_IND_ATRASADA + " INT, " + KEY_IND_DEBITO_AUTOMATICO + " INT," +
                FOREIGNKEY_PERIODICIDADE + " , " + FOREIGNKEY_FINANCA + " , " + FOREIGNKEY_CARTAO + " );");

        //-----

        db.execSQL("CREATE TABLE " + TABLE_TRANSACAO + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_CONTA_ID + " INT, " + KEY_NUM_PARECLA + " INT, " + KEY_DT_TRANSACAO + " DATE, " +
                KEY_IND_PAGO + " INT, " + KEY_DT_PAGAMENTO + " DATE, " + KEY_VAL_PAGO + " DOUBLE, " +
                KEY_IND_ATIVO + " INT, " + KEY_DT_DESATIVACAO + " DATE, " + KEY_DT_VENCIMENTO + " DATE, " +
                KEY_DT_INCLUSAO + " DATE, " + KEY_DT_ATUALIZACAO + " DATE, " +
                FOREIGNKEY_CONTA + " );");

        adicionarDados(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //chamada quando existe uma nova versão para o banco de dados em relação a instalada no cliente
        Log.w(TAG, "Atualizando banco da version " + oldVersion + " para " + newVersion + ", destruindo todos as tabelas e dados");
        apagaBanco(db);
        onCreate(db);

    }

    public void apagaBanco(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANCO);
        //-----
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPO_FINANCA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINANCA);
        //-----
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANDEIRA_CARTAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPO_CARTAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTAO);
        //-----
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERIODICIDADE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTA);
        //-----
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACAO);
    }

    public void adicionarDados(SQLiteDatabase db){
        values.put(KEY_NOME,"Conta Corrente");
        db.insert(TABLE_TIPO_FINANCA,null,values);
        values.put(KEY_NOME,"Carteira");
        db.insert(TABLE_TIPO_FINANCA,null,values);
        //-------
        values.put(KEY_NOME,"Visa");
        db.insert(TABLE_BANDEIRA_CARTAO,null,values);
        values.put(KEY_NOME,"Master Card");
        db.insert(TABLE_BANDEIRA_CARTAO,null,values);
        //-------
        values.put(KEY_NOME,"Débito");
        db.insert(TABLE_TIPO_CARTAO, null, values);
        values.put(KEY_NOME, "Crédito");
        db.insert(TABLE_TIPO_CARTAO, null, values);
        values.put(KEY_NOME, "Multiplo");
        db.insert(TABLE_TIPO_CARTAO,null,values);
        values.put(KEY_NOME,"Alimentação");
        db.insert(TABLE_TIPO_CARTAO,null,values);
        //-------
        values.put(KEY_NOME,"Diario");
        db.insert(TABLE_PERIODICIDADE, null, values);
        values.put(KEY_NOME, "Semanal");
        db.insert(TABLE_PERIODICIDADE, null, values);
        values.put(KEY_NOME, "Mensal");
        db.insert(TABLE_PERIODICIDADE, null, values);
        values.put(KEY_NOME, "Anual");
        db.insert(TABLE_PERIODICIDADE, null, values);

    }

}