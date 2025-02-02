package com.lucascorreia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.lucascorreia.util.Id;

public class Cliente implements Serializable {
    @Id
    private int numero;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    List<Pedido> pedidos;
    List<Fatura> faturas;

    public Cliente(String cpf, String nome, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.pedidos = new ArrayList<>();
        this.faturas= new ArrayList<>();
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getNome() {
        return nome;
    }

    public int getNumero() {
        return numero;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public List<Fatura> getFaturas() {
        return faturas;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setFaturas(List<Fatura> faturas) {
        this.faturas = faturas;
    }

    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("Cliente não possui pedidos!");
        } else {
            System.out.println("Pedidos do Cliente " + nome + ":");
            for (Pedido pedido : pedidos) {
                System.out.println(pedido);
            }
        }
    }

    @Override
    public String toString() {
        return "id = "+ numero +
                "\nNome = " + nome +
                "\nCPF = " + cpf +
                "\nE-mail = " + email +
                "\nTelefone = " + telefone;
    }
}
