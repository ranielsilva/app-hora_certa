package com.example.hora_certa;

public class Medicamento {
    private String nome;
    private String horario;
    private String status; // "Tomado" ou "Pendente"

    public Medicamento(String nome, String horario, String status) {
        this.nome = nome;
        this.horario = horario;
        this.status = status;
    }

    public String getNome() { return nome; }
    public String getHorario() { return horario; }
    public String getStatus() { return status; }
}