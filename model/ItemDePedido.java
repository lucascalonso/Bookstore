package com.lucascorreia.model;
import com.lucascorreia.util.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemDePedido implements Serializable {
    @Id
    int id;
    int qtdPedida;
    int qtdAFaturar;
    double precoCobrado;
    Livro pegaLivro;
    List<ItemFaturado>itemFaturados;
    Pedido pedido;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ItemDePedido(int qtdPedida, Livro pegaLivro) {
        this.qtdPedida = qtdPedida;
        this.pegaLivro = pegaLivro;
        this.qtdAFaturar=qtdPedida;
        this.itemFaturados=new ArrayList<>();
        this.precoCobrado = pegaLivro.getPrecoUnitario();
    }

    public int getId() {
        return id;
    }

    public int getQtdPedida() {
        return qtdPedida;
    }

    public int getQtdAFaturar() {
        return qtdAFaturar;
    }

    public double getPrecoCobrado() {
        return precoCobrado;
    }

    public Livro getPegaLivro() {
        return pegaLivro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQtdPedida(int qtdPedida) {
        this.qtdPedida = qtdPedida;
    }

    public void setQtdAFaturar(int qtdAFaturar) {
        this.qtdAFaturar = qtdAFaturar;
    }

    public void setPrecoCobrado(double precoCobrado) {
        this.precoCobrado = precoCobrado;
    }

    public String toString(){
        return '\n'+ "Livro "+ pegaLivro.getTitulo()+
                '\n'+" Quantidade Pedida" +qtdPedida;
    }
}
