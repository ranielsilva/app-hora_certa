package com.example.hora_certa;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class CadastroLembreteActivity extends AppCompatActivity {

    private EditText etTitulo, etDescricao;
    private Button btnData, btnHora, btnSalvar;
    private String dataSelecionada = "", horaSelecionada = "";
    private int ano, mes, dia, hora, minuto;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_lembrete);

        dbHelper = new DatabaseHelper(this);

        etTitulo = findViewById(R.id.et_titulo_lembrete);
        etDescricao = findViewById(R.id.et_descricao_lembrete);
        btnData = findViewById(R.id.btn_data_lembrete);
        btnHora = findViewById(R.id.btn_hora_lembrete);
        btnSalvar = findViewById(R.id.btn_salvar_lembrete);
        ImageButton btnVoltar = findViewById(R.id.btn_voltar_lembrete);

        btnVoltar.setOnClickListener(v -> finish());

        btnData.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                this.ano = year;
                this.mes = month;
                this.dia = dayOfMonth;
                dataSelecionada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                btnData.setText(dataSelecionada);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnHora.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                this.hora = hourOfDay;
                this.minuto = minute;
                horaSelecionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                btnHora.setText(horaSelecionada);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        btnSalvar.setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String desc = etDescricao.getText().toString().trim();

            if (titulo.isEmpty() || dataSelecionada.isEmpty() || horaSelecionada.isEmpty()) {
                Toast.makeText(this, "Preencha o título, data e hora", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = dbHelper.inserirLembrete(titulo, desc, dataSelecionada, horaSelecionada);
            if (id != -1) {
                agendarNotificacaoLembrete((int) id, titulo, desc);
                Toast.makeText(this, "Lembrete criado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar lembrete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agendarNotificacaoLembrete(int id, String titulo, String desc) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("TITULO", "Lembrete: " + titulo);
        intent.putExtra("MENSAGEM", desc.isEmpty() ? "Atenção ao seu lembrete de saúde." : desc);
        intent.putExtra("ID", id + 3000); // Offset para não colidir com outros IDs

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id + 3000, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes, dia, hora, minuto, 0);

        if (alarmManager != null && calendar.after(Calendar.getInstance())) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}