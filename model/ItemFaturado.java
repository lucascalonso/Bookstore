package com.lucascorreia.model;
import com.lucascorreia.util.Id;

import java.io.Serializable;

public class ItemFaturado implements Serializable {
    @Id
    int id;
    int qtdFaturada;
    ItemDePedido itemDePedido;
    Fatura fatura;

    public ItemFaturado(ItemDePedido itemDePedido, int qtdFaturada) {
        this.itemDePedido = itemDePedido;
        this.qtdFaturada = qtdFaturada;

    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public ItemDePedido getItemDePedido() {
        return itemDePedido;
    }

    public void setItemDePedido(ItemDePedido itemDePedido) {
        this.itemDePedido = itemDePedido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQtdFaturada() {
        return qtdFaturada;
    }

    public void setQtdFaturada(int qtdFaturada) {
        this.qtdFaturada = qtdFaturada;
    }
}
