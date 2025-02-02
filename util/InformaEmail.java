package com.lucascorreia.util;
import com.lucascorreia.model.Cliente;

public class InformaEmail extends Thread{
    Cliente cliente;

    public InformaEmail(Cliente cliente){
        this.cliente = cliente;
    }

    @Override
    public void run(){
        System.out.println("\nEnviando email para " + cliente.getEmail() + " informando que o pedido foi cadastrado!");
    }

}
