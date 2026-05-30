package com.example.hora_certa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class CadastroMedicamentoActivity extends AppCompatActivity {

    private LinearLayout layoutNome, layoutFrequencia, layoutHorario, layoutTratamento;
    private EditText etNome, etTratamento;
    private RadioGroup rgFrequencia;
    private Button btnSelecionarHorario;
    private Animation animEntrada;
    private DatabaseHelper dbHelper;

    private String nomeMed, frequenciaMed, horarioMed = "08:00", tratamentoMed;
    private int horaSelecionada = 8, minutoSelecionado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento);

        dbHelper = new DatabaseHelper(this);

        // Inicializar Views
        layoutNome = findViewById(R.id.layout_nome_med);
        layoutFrequencia = findViewById(R.id.layout_frequencia_med);
        layoutHorario = findViewById(R.id.layout_horario_med);
        layoutTratamento = findViewById(R.id.layout_tratamento_med);
        
        etNome = findViewById(R.id.et_nome_medicamento);
        etTratamento = findViewById(R.id.et_tratamento);
        rgFrequencia = findViewById(R.id.rg_frequencia);
        btnSelecionarHorario = findViewById(R.id.btn_selecionar_horario);

        Button btnProximoNome = findViewById(R.id.btn_proximo_nome);
        Button btnProximoFreq = findViewById(R.id.btn_proximo_frequencia);
        Button btnProximoHorario = findViewById(R.id.btn_proximo_horario);
        Button btnSalvar = findViewById(R.id.btn_salvar_med);
        ImageButton btnVoltar = findViewById(R.id.btn_voltar_cadastro);

        animEntrada = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Ações dos Botões
        btnProximoNome.setOnClickListener(v -> {
            nomeMed = etNome.getText().toString().trim();
            if (nomeMed.isEmpty()) {
                etNome.setError("Digite o nome do medicamento");
            } else {
                exibirLayout(layoutFrequencia);
            }
        });

        btnProximoFreq.setOnClickListener(v -> {
            int selectedId = rgFrequencia.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Selecione uma frequência", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton rb = findViewById(selectedId);
                frequenciaMed = rb.getText().toString();
                exibirLayout(layoutHorario);
            }
        });

        btnSelecionarHorario.setOnClickListener(v -> abrirTimePicker());

        btnProximoHorario.setOnClickListener(v -> exibirLayout(layoutTratamento));

        btnSalvar.setOnClickListener(v -> {
            tratamentoMed = etTratamento.getText().toString().trim();
            if (tratamentoMed.isEmpty()) {
                etTratamento.setError("Digite o tratamento");
            } else {
                salvarEConfigurarAlarme();
            }
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void abrirTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            horaSelecionada = hourOfDay;
            minutoSelecionado = minute;
            horarioMed = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            btnSelecionarHorario.setText(horarioMed);
        }, hora, minuto, true);

        timePickerDialog.show();
    }

    private void exibirLayout(LinearLayout layoutParaExibir) {
        layoutNome.setVisibility(View.GONE);
        layoutFrequencia.setVisibility(View.GONE);
        layoutHorario.setVisibility(View.GONE);
        layoutTratamento.setVisibility(View.GONE);

        layoutParaExibir.setVisibility(View.VISIBLE);
        layoutParaExibir.startAnimation(animEntrada);
    }

    private void salvarEConfigurarAlarme() {
        Medicamento novoMed = new Medicamento(nomeMed, horarioMed, frequenciaMed, tratamentoMed, "Pendente");
        long id = dbHelper.inserirMedicamento(novoMed);

        if (id != -1) {
            agendarNotificacao((int) id);
            Toast.makeText(this, "Medicamento salvo e lembrete configurado!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar medicamento", Toast.LENGTH_SHORT).show();
        }
    }

    private void agendarNotificacao(int id) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("NOME", nomeMed);
        intent.putExtra("ID", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, horaSelecionada);
        calendar.set(Calendar.MINUTE, minutoSelecionado);
        calendar.set(Calendar.SECOND, 0);

        // Se o horário já passou hoje, agenda para amanhã
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}