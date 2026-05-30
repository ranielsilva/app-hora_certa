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

public class CadastroConsultaActivity extends AppCompatActivity {

    private EditText etEspecialidade, etMedico, etLocal;
    private Button btnData, btnHora, btnSalvar;
    private String dataSelecionada = "", horaSelecionada = "";
    private int ano, mes, dia, hora, minuto;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_consulta);

        dbHelper = new DatabaseHelper(this);

        etEspecialidade = findViewById(R.id.et_especialidade);
        etMedico = findViewById(R.id.et_medico);
        etLocal = findViewById(R.id.et_local_consulta);
        btnData = findViewById(R.id.btn_data_consulta);
        btnHora = findViewById(R.id.btn_hora_consulta);
        btnSalvar = findViewById(R.id.btn_salvar_consulta);
        ImageButton btnVoltar = findViewById(R.id.btn_voltar_consulta);

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
            String espec = etEspecialidade.getText().toString().trim();
            String medico = etMedico.getText().toString().trim();
            String local = etLocal.getText().toString().trim();

            if (espec.isEmpty() || dataSelecionada.isEmpty() || horaSelecionada.isEmpty()) {
                Toast.makeText(this, "Preencha a especialidade, data e hora", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = dbHelper.inserirConsulta(espec, medico, dataSelecionada, horaSelecionada, local);
            if (id != -1) {
                agendarNotificacaoConsulta((int) id, espec, medico);
                Toast.makeText(this, "Consulta agendada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agendarNotificacaoConsulta(int id, String espec, String medico) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("TITULO", "Lembrete de Consulta");
        intent.putExtra("MENSAGEM", "Você tem uma consulta de " + espec + (medico.isEmpty() ? "" : " com o(a) " + medico) + " agora.");
        intent.putExtra("ID", id + 2000); // Offset para não colidir com IDs de medicamentos

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id + 2000, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes, dia, hora, minuto, 0);

        if (alarmManager != null && calendar.after(Calendar.getInstance())) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}