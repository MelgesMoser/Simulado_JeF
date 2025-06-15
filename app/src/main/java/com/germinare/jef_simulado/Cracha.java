package com.germinare.jef_simulado;

import java.util.Date;

public class Cracha {
    private int numeroCracha;
    private Date inicioAtendimento;
    private Date fimAtendimento;

    public Cracha() {}

    public Cracha(int numeroCracha, Date inicioAtendimento, Date fimAtendimento) {
        this.numeroCracha = numeroCracha;
        this.inicioAtendimento = inicioAtendimento;
        this.fimAtendimento = fimAtendimento;
    }

    public int getNumeroCracha() {
        return numeroCracha;
    }

    public void setNumeroCracha(int numeroCracha) {
        this.numeroCracha = numeroCracha;
    }

    public Date getInicioAtendimento() {
        return inicioAtendimento;
    }

    public void setInicioAtendimento(Date inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
    }

    public Date getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(Date fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }
}
