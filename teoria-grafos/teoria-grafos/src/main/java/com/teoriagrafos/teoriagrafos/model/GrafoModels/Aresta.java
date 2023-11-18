package com.teoriagrafos.teoriagrafos.model.GrafoModels;

import java.util.ArrayList;

public class Aresta<TIPO> {
    private ArrayList<Double> peso;
    private Vertice<TIPO> inicio;
    private Vertice<TIPO> fim;
    
    public Aresta(ArrayList<Double> peso, Vertice<TIPO> inicio, Vertice<TIPO> fim){
        this.peso = peso;
        this.inicio = inicio;
        this.fim = fim;
    }

    public ArrayList<Double> getPeso() {
        return peso;
    }

    public void setPeso(ArrayList<Double> peso) {
        this.peso = peso;
    }

    public Vertice<TIPO> getInicio() {
        return inicio;
    }

    public void setInicio(Vertice<TIPO> inicio) {
        this.inicio = inicio;
    }

    public Vertice<TIPO> getFim() {
        return fim;
    }

    public void setFim(Vertice<TIPO> fim) {
        this.fim = fim;
    }

}
