package com.germinare.jef_simulado;

public class Exame {
    public String cracha;
    public String nome;
    public String inicio;
    public String termino;
    public String responsavel;

    public Exame(String cracha, String nomeColaborador, String dataInicio, String dataTermino) {}

    public Exame(String cracha, String nome, String inicio, String termino, String responsavel) {
        this.cracha = cracha;
        this.nome = nome;
        this.inicio = inicio;
        this.termino = termino;
        this.responsavel = responsavel;
    }
}
