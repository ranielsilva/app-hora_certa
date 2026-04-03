package com.example.hora_certa;

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

public class CadastroMedicamentoActivity extends AppCompatActivity {

    private LinearLayout layoutNome, layoutFrequencia, layoutTratamento;
    private EditText etNome, etTratamento;
    private RadioGroup rgFrequencia;
    private Animation animEntrada;

    private String nomeMed, frequenciaMed, tratamentoMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento);

        // Inicializar Views
        layoutNome = findViewById(R.id.layout_nome_med);
        layoutFrequencia = findViewById(R.id.layout_frequencia_med);
        layoutTratamento = findViewById(R.id.layout_tratamento_med);
        
        etNome = findViewById(R.id.et_nome_medicamento);
        etTratamento = findViewById(R.id.et_tratamento);
        rgFrequencia = findViewById(R.id.rg_frequencia);

        Button btnProximoNome = findViewById(R.id.btn_proximo_nome);
        Button btnProximoFreq = findViewById(R.id.btn_proximo_frequencia);
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
                exibirLayout(layoutTratamento);
            }
        });

        btnSalvar.setOnClickListener(v -> {
            tratamentoMed = etTratamento.getText().toString().trim();
            if (tratamentoMed.isEmpty()) {
                etTratamento.setError("Digite o tratamento");
            } else {
                salvarERetornar();
            }
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void exibirLayout(LinearLayout layoutParaExibir) {
        layoutNome.setVisibility(View.GONE);
        layoutFrequencia.setVisibility(View.GONE);
        layoutTratamento.setVisibility(View.GONE);

        layoutParaExibir.setVisibility(View.VISIBLE);
        layoutParaExibir.startAnimation(animEntrada);
    }

    private void salvarERetornar() {
        // Como ainda não temos banco de dados, passamos o resultado de volta para a TelaInicialActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NOME", nomeMed);
        resultIntent.putExtra("FREQUENCIA", frequenciaMed);
        resultIntent.putExtra("TRATAMENTO", tratamentoMed);
        setResult(RESULT_OK, resultIntent);
        
        Toast.makeText(this, "Medicamento salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}