package com.example.hora_certa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HoraCerta.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Tabela Medicamentos
    public static final String TABLE_MEDICAMENTOS = "medicamentos";
    public static final String COL_ID = "id";
    public static final String COL_NOME = "nome";
    public static final String COL_HORARIO = "horario";
    public static final String COL_FREQUENCIA = "frequencia";
    public static final String COL_TRATAMENTO = "tratamento";
    public static final String COL_STATUS = "status";

    // Tabela Histórico
    public static final String TABLE_HISTORICO = "historico";
    public static final String COL_HIST_ID = "id";
    public static final String COL_MED_ID = "medicamento_id";
    public static final String COL_DATA_HORA = "data_hora";
    public static final String COL_HIST_STATUS = "status";

    // Tabela Consultas (NOVA)
    public static final String TABLE_CONSULTAS = "consultas";
    public static final String COL_CONS_ID = "id";
    public static final String COL_CONS_ESPECIALIDADE = "especialidade";
    public static final String COL_CONS_MEDICO = "medico";
    public static final String COL_CONS_DATA = "data";
    public static final String COL_CONS_HORA = "hora";
    public static final String COL_CONS_LOCAL = "local";

    // Tabela Lembretes Gerais (NOVA)
    public static final String TABLE_LEMBRETES = "lembretes";
    public static final String COL_LEMB_ID = "id";
    public static final String COL_LEMB_TITULO = "titulo";
    public static final String COL_LEMB_DESCRICAO = "descricao";
    public static final String COL_LEMB_DATA = "data";
    public static final String COL_LEMB_HORA = "hora";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMedTable = "CREATE TABLE " + TABLE_MEDICAMENTOS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME + " TEXT, " +
                COL_HORARIO + " TEXT, " +
                COL_FREQUENCIA + " TEXT, " +
                COL_TRATAMENTO + " TEXT, " +
                COL_STATUS + " TEXT)";

        String createHistTable = "CREATE TABLE " + TABLE_HISTORICO + " (" +
                COL_HIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MED_ID + " INTEGER, " +
                COL_DATA_HORA + " TEXT, " +
                COL_HIST_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COL_MED_ID + ") REFERENCES " + TABLE_MEDICAMENTOS + "(" + COL_ID + "))";

        String createConsultasTable = "CREATE TABLE " + TABLE_CONSULTAS + " (" +
                COL_CONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CONS_ESPECIALIDADE + " TEXT, " +
                COL_CONS_MEDICO + " TEXT, " +
                COL_CONS_DATA + " TEXT, " +
                COL_CONS_HORA + " TEXT, " +
                COL_CONS_LOCAL + " TEXT)";

        String createLembretesTable = "CREATE TABLE " + TABLE_LEMBRETES + " (" +
                COL_LEMB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LEMB_TITULO + " TEXT, " +
                COL_LEMB_DESCRICAO + " TEXT, " +
                COL_LEMB_DATA + " TEXT, " +
                COL_LEMB_HORA + " TEXT)";

        db.execSQL(createMedTable);
        db.execSQL(createHistTable);
        db.execSQL(createConsultasTable);
        db.execSQL(createLembretesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String createConsultasTable = "CREATE TABLE " + TABLE_CONSULTAS + " (" +
                    COL_CONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CONS_ESPECIALIDADE + " TEXT, " +
                    COL_CONS_MEDICO + " TEXT, " +
                    COL_CONS_DATA + " TEXT, " +
                    COL_CONS_HORA + " TEXT, " +
                    COL_CONS_LOCAL + " TEXT)";

            String createLembretesTable = "CREATE TABLE " + TABLE_LEMBRETES + " (" +
                    COL_LEMB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_LEMB_TITULO + " TEXT, " +
                    COL_LEMB_DESCRICAO + " TEXT, " +
                    COL_LEMB_DATA + " TEXT, " +
                    COL_LEMB_HORA + " TEXT)";
            db.execSQL(createConsultasTable);
            db.execSQL(createLembretesTable);
        }
    }

    // CRUD Medicamentos
    public long inserirMedicamento(Medicamento med) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME, med.getNome());
        values.put(COL_HORARIO, med.getHorario());
        values.put(COL_FREQUENCIA, med.getFrequencia());
        values.put(COL_TRATAMENTO, med.getTratamento());
        values.put(COL_STATUS, med.getStatus());
        return db.insert(TABLE_MEDICAMENTOS, null, values);
    }

    public List<Medicamento> listarMedicamentos() {
        List<Medicamento> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICAMENTOS, null);

        if (cursor.moveToFirst()) {
            do {
                Medicamento med = new Medicamento(
                        cursor.getInt(0), // id
                        cursor.getString(1), // nome
                        cursor.getString(2), // horario
                        cursor.getString(3), // frequencia
                        cursor.getString(4), // tratamento
                        cursor.getString(5)  // status
                );
                lista.add(med);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public void atualizarStatus(int id, String novoStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, novoStatus);
        db.update(TABLE_MEDICAMENTOS, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Histórico
    public void inserirHistorico(int medId, String dataHora, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MED_ID, medId);
        values.put(COL_DATA_HORA, dataHora);
        values.put(COL_HIST_STATUS, status);
        db.insert(TABLE_HISTORICO, null, values);
    }

    // CRUD Consultas
    public long inserirConsulta(String especialidade, String medico, String data, String hora, String local) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONS_ESPECIALIDADE, especialidade);
        values.put(COL_CONS_MEDICO, medico);
        values.put(COL_CONS_DATA, data);
        values.put(COL_CONS_HORA, hora);
        values.put(COL_CONS_LOCAL, local);
        return db.insert(TABLE_CONSULTAS, null, values);
    }

    // CRUD Lembretes
    public long inserirLembrete(String titulo, String descricao, String data, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LEMB_TITULO, titulo);
        values.put(COL_LEMB_DESCRICAO, descricao);
        values.put(COL_LEMB_DATA, data);
        values.put(COL_LEMB_HORA, hora);
        return db.insert(TABLE_LEMBRETES, null, values);
    }
}