package com.example.hora_certa;

public class Medicamento {
    private String nome;
    private String horario;
    private String frequencia;
    private String status; // "Tomado" ou "Pendente"

    public Medicamento(String nome, String horario, String frequencia, String status) {
        this.nome = nome;
        this.horario = horario;
        this.frequencia = frequencia;
        this.status = status;
    }

    public String getNome() { return nome; }
    public String getHorario() { return horario; }
    public String getFrequencia() { return frequencia; }
    public String getStatus() { return status; }
}