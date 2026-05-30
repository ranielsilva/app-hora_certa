package com.example.hora_certa;

public class Medicamento {
    private int id;
    private String nome;
    private String horario;
    private String frequencia;
    private String tratamento;
    private String status; // "Tomado" ou "Pendente"

    // Construtor para novos medicamentos (sem ID)
    public Medicamento(String nome, String horario, String frequencia, String tratamento, String status) {
        this.nome = nome;
        this.horario = horario;
        this.frequencia = frequencia;
        this.tratamento = tratamento;
        this.status = status;
    }

    // Construtor para medicamentos vindos do banco (com ID)
    public Medicamento(int id, String nome, String horario, String frequencia, String tratamento, String status) {
        this.id = id;
        this.nome = nome;
        this.horario = horario;
        this.frequencia = frequencia;
        this.tratamento = tratamento;
        this.status = status;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getHorario() { return horario; }
    public String getFrequencia() { return frequencia; }
    public String getTratamento() { return tratamento; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
}