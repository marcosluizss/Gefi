package br.com.ml2s.gefi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcossantos on 05/09/2014.
 */
public class DataSourceTools {

    private DatabaseHelper handler;
    private SQLiteDatabase db;

    private Map<String, Object> item = new HashMap<String, Object>();;
    private List<Map<String, Object>> aItens;
    private int qtdRegistros;
    private int qtdColunas;

    public DataSourceTools(DatabaseHelper handler) {
        this.handler = handler;
    }

    public int save(String table, ContentValues data) {
        try {
            db = openWrite(this.handler);
            if (db != null) {
               if(db.insert(table, null, data) != -1) {
                   return R.string.salvar_sucesso;
               }
            }
        }
        finally {
            close(db);
        }
        return R.string.salvar_erro;
    }

    public int update(String table, ContentValues dataToUpdate, String where, String[] whereArgs) {
        try {
            db = openWrite(this.handler);
            if (db != null) {
                if(db.update(table, dataToUpdate, where, whereArgs) != -1) {
                    return R.string.salvar_sucesso;
                }
            }
        }
        finally {
            close(db);
        }
        return R.string.salvar_erro;
    }

    public int update(String table, ContentValues dataToUpdate, String itemId ) {
        try {
            db = openWrite(this.handler);
            if (db != null) {
                String whereClause = DatabaseHelper.KEY_ID + "=" + itemId;
                String[] whereArgs = null;
                if(db.update(table, dataToUpdate, whereClause, whereArgs) != -1) {
                    return R.string.salvar_sucesso;
                }
            }
        }
        finally {
            close(db);
        }
        return R.string.salvar_erro;
    }

    public int delete(String table, String where, String[] whereArgs) {
        try {
            db = openWrite(this.handler);
            if (db != null) {
                if( db.delete(table, where, whereArgs) > 0){
                    return R.string.salvar_sucesso;
                }
            }
        }
        finally {
            close(db);
        }
        return R.string.salvar_erro;
    }

    public int delete(String table, String itemId) {
        try {
            db = openWrite(this.handler);
            if (db != null) {
                String where = DatabaseHelper.KEY_ID + "=" + itemId;
                String[] whereArgs = null;
                if( db.delete(table, where, whereArgs) > 0){
                    return R.string.salvar_sucesso;
                }
            }
        }
        finally {
            close(db);
        }
        return R.string.salvar_erro;
    }

    public Map<String, Object> find(String table, String[] columns, String where, String[] whereArgs) {
        db = openRead(this.handler);
        Cursor c = null;
        qtdColunas = 0;
        try {
            c = db.query(table, columns, where, whereArgs, null, null, null);
            if(c.moveToFirst()){
                qtdColunas = c.getColumnCount();
                item = retornaItem(c);
            }
            return item;
        }
        finally {
            close(db);
        }
    }

    public Map<String, Object> find(String table, String[] columns, String itemId) {
        db = openRead(this.handler);
        Cursor c = null;
        qtdColunas = 0;
        try {
            String where = DatabaseHelper.KEY_ID + "=" + itemId;
            String[] whereArgs = null;
            c = db.query(table, columns, where, whereArgs, null, null, null);
            if(c.moveToFirst()) {
                qtdColunas = c.getColumnCount();
                item = retornaItem(c);
            }
            return item;
        }
        finally {
            close(db);
        }
    }

    public List<Map<String, Object>> findAll(String table, String[] columns, boolean distinct, String groupBy, String having, String orderBy,String limit) {
        db = openRead(this.handler);
        Cursor c = null;
        aItens = new ArrayList<Map<String, Object>>();;
        qtdRegistros = 0;
        qtdColunas = 0;
        try {
            c = db.query(distinct, table, columns, null, null, groupBy, having, orderBy, limit);
            if(c.moveToFirst()) {
                qtdRegistros = c.getCount();
                qtdColunas = c.getColumnCount();
                for (int i = 0; i < qtdRegistros; i++) {
                    item = retornaItem(c);
                    aItens.add(item);
                    c.moveToNext();
                }
            }
            return aItens;
        }
        finally {
            close(db);
        }
    }

    public List<Map<String, Object>> findAll(String table, String[] columns,String orderBy,String limit) {
        db = openRead(this.handler);
        Cursor c = null;
        aItens = new ArrayList<Map<String, Object>>();;
        qtdRegistros = 0;
        qtdColunas = 0;
        try {
            c = db.query(table, columns, null, null, null, null, orderBy, limit);
            if(c.moveToFirst()) {
                qtdRegistros = c.getCount();
                qtdColunas = c.getColumnCount();
                for (int i = 0; i < qtdRegistros; i++) {
                    item = retornaItem(c);
                    aItens.add(item);
                    c.moveToNext();
                }
            }
            return aItens;
        }
        finally {
            close(db);
        }
    }

    public List<Map<String, Object>> findAll(String table, String[] columns) {
        db = openRead(this.handler);
        Cursor c = null;
        aItens = new ArrayList<Map<String, Object>>();;
        qtdRegistros = 0;
        qtdColunas = 0;
        try {
            c = db.query(table, columns, null, null, null, null, null);
            if(c.moveToFirst()) {
                qtdRegistros = c.getCount();
                qtdColunas = c.getColumnCount();
                for (int i = 0; i < qtdRegistros; i++) {
                    item = retornaItem(c);
                    aItens.add(item);
                    c.moveToNext();
                }
            }
            return aItens;
        }
        finally {
            close(db);
        }
    }

    private final synchronized SQLiteDatabase openWrite(SQLiteOpenHelper handler) {
        if (handler != null) {
            return handler.getWritableDatabase();
        }
        return null;
    }

    private final synchronized SQLiteDatabase openRead(SQLiteOpenHelper handler) {
        if (handler != null) {
            return handler.getReadableDatabase();
        }
        return null;
    }

    private final synchronized void close(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private Map<String, Object> retornaItem(Cursor c){
        item = new HashMap<String, Object>();
        for (int j = 0; j < qtdColunas; j++) {
            switch (c.getType(j)){
                case Cursor.FIELD_TYPE_BLOB:
                    item.put(c.getColumnName(j),c.getBlob(j));
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    item.put(c.getColumnName(j),c.getFloat(j));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    item.put(c.getColumnName(j),c.getInt(j));
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    item.put(c.getColumnName(j),null);
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    item.put(c.getColumnName(j),c.getString(j));
                    break;
            }
        }
        return item;
    }

}
