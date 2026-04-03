package com.example.hora_certa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TelaInicialActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CADASTRO = 101;

    private RecyclerView rvMedicamentos;
    private MedicamentoAdapter adapter;
    private List<Medicamento> listaMedicamentos;
    private LinearLayout llCalendario;
    private TextView tvVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        // Inicialização de componentes
        rvMedicamentos = findViewById(R.id.rv_medicamentos);
        llCalendario = findViewById(R.id.ll_calendario_container);
        tvVazio = findViewById(R.id.tv_vazio);
        FloatingActionButton fabAdicionar = findViewById(R.id.fab_adicionar);

        // Configurar Calendário Horizontal
        configurarCalendario();

        // Configurar Lista
        listaMedicamentos = new ArrayList<>();
        atualizarInterface();

        // Lógica do FAB
        fabAdicionar.setOnClickListener(v -> mostrarOpcoesAdicionar());
    }

    private void configurarCalendario() {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("EEE", new Locale("pt", "BR"));
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -3);

        for (int i = 0; i < 7; i++) {
            View viewDia = LayoutInflater.from(this).inflate(R.layout.item_dia_calendario, llCalendario, false);
            TextView tvSemana = viewDia.findViewById(R.id.tv_dia_semana);
            TextView tvDia = viewDia.findViewById(R.id.tv_dia_mes);
            LinearLayout container = viewDia.findViewById(R.id.ll_dia_container);

            tvSemana.setText(sdfSemana.format(cal.getTime()));
            tvDia.setText(sdfDia.format(cal.getTime()));

            if (i == 3) {
                container.setBackgroundResource(R.drawable.fundo_dia_item_selecionado);
                tvSemana.setTextColor(Color.WHITE);
                tvDia.setTextColor(Color.WHITE);
            }

            llCalendario.addView(viewDia);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void atualizarInterface() {
        if (listaMedicamentos.isEmpty()) {
            tvVazio.setVisibility(View.VISIBLE);
            rvMedicamentos.setVisibility(View.GONE);
        } else {
            tvVazio.setVisibility(View.GONE);
            rvMedicamentos.setVisibility(View.VISIBLE);
            
            if (adapter == null) {
                adapter = new MedicamentoAdapter(listaMedicamentos);
                rvMedicamentos.setLayoutManager(new LinearLayoutManager(this));
                rvMedicamentos.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void mostrarOpcoesAdicionar() {
        String[] opcoes = {"Adicionar medicamento", "Agendar consulta médica", "Criar lembrete"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("O que deseja fazer?")
                .setItems(opcoes, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(this, CadastroMedicamentoActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_CADASTRO);
                    } else {
                        Toast.makeText(this, "Funcionalidade em desenvolvimento...", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CODE_CADASTRO && resultCode == RESULT_OK && data != null) {
            String nome = data.getStringExtra("NOME");
            String horario = data.getStringExtra("HORARIO");
            String freq = data.getStringExtra("FREQUENCIA");
            
            // Adiciona o medicamento com todas as informações coletadas
            listaMedicamentos.add(new Medicamento(nome, horario, freq, "Pendente"));
            atualizarInterface();
        }
    }
}