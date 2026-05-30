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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TelaInicialActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CADASTRO = 101;

    private RecyclerView rvMedicamentos;
    private MedicamentoAdapter adapter;
    private List<Medicamento> listaMedicamentos;
    private LinearLayout llCalendario;
    private TextView tvVazio;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        dbHelper = new DatabaseHelper(this);

        // Inicialização de componentes
        rvMedicamentos = findViewById(R.id.rv_medicamentos);
        llCalendario = findViewById(R.id.ll_calendario_container);
        tvVazio = findViewById(R.id.tv_vazio);
        FloatingActionButton fabAdicionar = findViewById(R.id.fab_adicionar);

        // Configurar Calendário Horizontal
        configurarCalendario();

        // Configurar Lista
        carregarMedicamentos();

        // Lógica do FAB
        fabAdicionar.setOnClickListener(v -> mostrarOpcoesAdicionar());
    }

    private void carregarMedicamentos() {
        listaMedicamentos = dbHelper.listarMedicamentos();
        atualizarInterface();
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
            
            adapter = new MedicamentoAdapter(listaMedicamentos);
            adapter.setOnItemClickListener(new MedicamentoAdapter.OnItemClickListener() {
                @Override
                public void onStatusClick(Medicamento med) {
                    registrarUso(med);
                }
            });
            rvMedicamentos.setLayoutManager(new LinearLayoutManager(this));
            rvMedicamentos.setAdapter(adapter);
        }
    }

    private void registrarUso(Medicamento med) {
        String novoStatus = med.getStatus().equals("Tomado") ? "Pendente" : "Tomado";
        dbHelper.atualizarStatus(med.getId(), novoStatus);
        
        if (novoStatus.equals("Tomado")) {
            String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            dbHelper.inserirHistorico(med.getId(), dataHora, "Tomado");
            Toast.makeText(this, med.getNome() + " marcado como tomado!", Toast.LENGTH_SHORT).show();
        }
        
        carregarMedicamentos();
    }

    private void mostrarOpcoesAdicionar() {
        String[] opcoes = {"Adicionar medicamento", "Agendar consulta médica", "Criar lembrete"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("O que deseja fazer?")
                .setItems(opcoes, (dialog, which) -> {
                    Intent intent;
                    switch (which) {
                        case 0:
                            intent = new Intent(this, CadastroMedicamentoActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_CADASTRO);
                            break;
                        case 1:
                            intent = new Intent(this, CadastroConsultaActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(this, CadastroLembreteActivity.class);
                            startActivity(intent);
                            break;
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CADASTRO && resultCode == RESULT_OK) {
            carregarMedicamentos();
        }
    }
}