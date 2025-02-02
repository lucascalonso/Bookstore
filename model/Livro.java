package com.lucascorreia.model;

import com.lucascorreia.util.Id;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class Livro implements Serializable{

    @Id
    int id;
    String isbn;
    String titulo;
    String descricao;
    int quant_est;
    double precoUnitario;

    private static final NumberFormat NF;
    static {
        NF = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        NF.setMaximumFractionDigits(2);
        NF.setMinimumFractionDigits(2);
    }

    public Livro(String isbn, String titulo, String descricao, int quant_est, double preco) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.descricao = descricao;
        this.quant_est = quant_est;
        this.precoUnitario = preco;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuant_est() {
        return quant_est;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public String getPrecoFormat(){
        return NF.format(precoUnitario);
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public void setQuant_est(int quant_est) {
        this.quant_est = quant_est;
    }


    public void setPreco(double preco) {
        this.precoUnitario = preco;
    }

    @Override
    public String toString(){
        return  "Id: "+getId()+
                " ISBN: "+getIsbn()+
                " Titulo: "+getTitulo()+
                " Descrição: "+getDescricao()+
                " Estoque: "+getQuant_est()+
                " Preço: "+getPrecoFormat();
    }
}
