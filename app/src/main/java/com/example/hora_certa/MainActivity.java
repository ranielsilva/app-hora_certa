package com.example.hora_certa;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int anoSelecionado = -1;
    private ImageButton botao_voltar_fluxo;
    private ConstraintLayout layout_consentimento, layout_termos_detalhados, layout_apelido, layout_perfil, layout_boas_vindas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.principal), (v, insets) -> {
            Insets barras_sistema = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras_sistema.left, barras_sistema.top, barras_sistema.right, barras_sistema.bottom);
            return insets;
        });

        // Containers
        layout_consentimento = findViewById(R.id.layout_consentimento);
        layout_termos_detalhados = findViewById(R.id.layout_termos_detalhados);
        layout_apelido = findViewById(R.id.layout_apelido);
        layout_perfil = findViewById(R.id.layout_perfil);
        layout_boas_vindas = findViewById(R.id.layout_boas_vindas);

        // Botão Voltar Global
        botao_voltar_fluxo = findViewById(R.id.botao_voltar_fluxo);

        // Views Consentimento
        TextView tv_link_termos = findViewById(R.id.tv_link_termos);
        CheckBox cb_concordo = findViewById(R.id.cb_concordo);
        Button botao_concordar = findViewById(R.id.botao_concordar);
        
        // Views Termos Detalhados
        Button botao_voltar_termos = findViewById(R.id.botao_voltar_termos);
        
        // Views Apelido
        EditText et_apelido = findViewById(R.id.et_apelido);
        Button botao_proximo_apelido = findViewById(R.id.botao_proximo_apelido);

        // Views Perfil
        TextView tv_saudacao_perfil = findViewById(R.id.tv_saudacao_perfil);
        AutoCompleteTextView auto_sexo = findViewById(R.id.auto_sexo);
        Button botao_escolher_ano = findViewById(R.id.botao_escolher_ano);
        Button botao_proximo_perfil = findViewById(R.id.botao_proximo_perfil);

        // Views Boas-vindas
        TextView tv_titulo_boas_vindas = findViewById(R.id.tv_titulo_boas_vindas);
        Button botao_comecar = findViewById(R.id.botao_comecar);

        // Animações
        Animation animacao_subir = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // --- Lógica do Botão Voltar ---
        botao_voltar_fluxo.setOnClickListener(v -> {
            voltarParaTelaAnterior();
        });

        // --- Lógica de Consentimento ---

        tv_link_termos.setOnClickListener(v -> {
            layout_consentimento.setVisibility(View.GONE);
            layout_termos_detalhados.setVisibility(View.VISIBLE);
            layout_termos_detalhados.startAnimation(animacao_subir);
            atualizarBotaoVoltar();
        });

        botao_voltar_termos.setOnClickListener(v -> {
            voltarParaTelaAnterior();
        });

        cb_concordo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            botao_concordar.setEnabled(isChecked);
            botao_concordar.setAlpha(isChecked ? 1.0f : 0.5f);
        });

        botao_concordar.setOnClickListener(v -> {
            layout_consentimento.setVisibility(View.GONE);
            layout_apelido.setVisibility(View.VISIBLE);
            layout_apelido.startAnimation(animacao_subir);
            atualizarBotaoVoltar();
        });

        // --- Lógica de Apelido ---

        botao_proximo_apelido.setOnClickListener(v -> {
            String apelido = et_apelido.getText().toString().trim();
            if (apelido.isEmpty()) {
                et_apelido.setError(getString(R.string.erro_apelido));
            } else {
                tv_saudacao_perfil.setText(getString(R.string.saudacao_perfil, apelido));
                layout_apelido.setVisibility(View.GONE);
                layout_perfil.setVisibility(View.VISIBLE);
                layout_perfil.startAnimation(animacao_subir);
                atualizarBotaoVoltar();
            }
        });

        // --- Lógica de Perfil ---

        String[] opcoes_sexo = {
                getString(R.string.sexo_feminino),
                getString(R.string.sexo_masculino),
                getString(R.string.sexo_nao_informar)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcoes_sexo);
        auto_sexo.setAdapter(adapter);

        botao_escolher_ano.setOnClickListener(v -> {
            final Calendar hoje = Calendar.getInstance();
            int anoAtual = hoje.get(Calendar.YEAR);
            
            final NumberPicker picker = new NumberPicker(this);
            picker.setMinValue(1900);
            picker.setMaxValue(anoAtual);
            picker.setValue(anoSelecionado == -1 ? 2000 : anoSelecionado);

            new AlertDialog.Builder(this)
                    .setTitle(R.string.label_ano_nascimento)
                    .setView(picker)
                    .setPositiveButton("OK", (dialog, which) -> {
                        anoSelecionado = picker.getValue();
                        botao_escolher_ano.setText(String.valueOf(anoSelecionado));
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        botao_proximo_perfil.setOnClickListener(v -> {
            String sexo = auto_sexo.getText().toString();
            if (sexo.isEmpty() || anoSelecionado == -1) {
                Toast.makeText(this, R.string.erro_perfil, Toast.LENGTH_SHORT).show();
            } else {
                String apelido = et_apelido.getText().toString().trim();
                tv_titulo_boas_vindas.setText(getString(R.string.titulo_boas_vindas, apelido));
                
                layout_perfil.setVisibility(View.GONE);
                layout_boas_vindas.setVisibility(View.VISIBLE);
                layout_boas_vindas.startAnimation(animacao_subir);
                atualizarBotaoVoltar();
            }
        });

        // --- Finalização ---
        botao_comecar.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando o Hora Certa!", Toast.LENGTH_SHORT).show();
        });
    }

    private void atualizarBotaoVoltar() {
        if (layout_consentimento.getVisibility() == View.VISIBLE) {
            botao_voltar_fluxo.setVisibility(View.GONE);
        } else {
            botao_voltar_fluxo.setVisibility(View.VISIBLE);
        }
    }

    private void voltarParaTelaAnterior() {
        if (layout_termos_detalhados.getVisibility() == View.VISIBLE) {
            layout_termos_detalhados.setVisibility(View.GONE);
            layout_consentimento.setVisibility(View.VISIBLE);
        } else if (layout_apelido.getVisibility() == View.VISIBLE) {
            layout_apelido.setVisibility(View.GONE);
            layout_consentimento.setVisibility(View.VISIBLE);
        } else if (layout_perfil.getVisibility() == View.VISIBLE) {
            layout_perfil.setVisibility(View.GONE);
            layout_apelido.setVisibility(View.VISIBLE);
        } else if (layout_boas_vindas.getVisibility() == View.VISIBLE) {
            layout_boas_vindas.setVisibility(View.GONE);
            layout_perfil.setVisibility(View.VISIBLE);
        }
        atualizarBotaoVoltar();
    }

    @Override
    public void onBackPressed() {
        if (layout_consentimento.getVisibility() != View.VISIBLE) {
            voltarParaTelaAnterior();
        } else {
            super.onBackPressed();
        }
    }
}
