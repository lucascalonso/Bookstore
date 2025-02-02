package com.lucascorreia.service;

import com.lucascorreia.dao.ClienteDAO;
import com.lucascorreia.exception.ClienteComPedidoException;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.exception.ClienteNaoEncontradoException;
import com.lucascorreia.util.FabricaDeDaos;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);

    public Cliente incluir(Cliente cliente) {
        return clienteDAO.incluir(cliente);
    }

    public Cliente alterar(Cliente umCliente) {
        return clienteDAO.alterar(umCliente);
    }

    public Cliente remover(int id) {
        Cliente cliente = recuperarPorId(id);
        if (cliente != null) {
            if(cliente.getPedidos().isEmpty()){
                clienteDAO.remover(id);
                System.out.println('\n' + "Cliente removido!");
                return cliente;
            }else{
                throw new ClienteComPedidoException("O cliente possui um pedido,  não pode ser removido");
            }

        }
        throw new ClienteNaoEncontradoException("Cliente não existe.");
    }

    public Cliente recuperarPorId(int id) {
        Cliente cliente = clienteDAO.recuperarPorId(id);
        if (cliente != null) {
            return cliente;
        }
        throw new ClienteNaoEncontradoException("Cliente não existe.");
    }


    public List<Cliente> recuperarClientes() {
        return clienteDAO.recuperarTodos();
    }

    public List<Cliente> recuperarClientesOrdenadosPorNome() {
        return clienteDAO.recuperarClientesOrdenadosPorNome();
    }
}
