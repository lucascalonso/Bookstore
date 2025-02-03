package com.lucascorreia.service;

import java.util.List;

import com.lucascorreia.dao.ClienteDAO;
import com.lucascorreia.exception.ClienteComPedidoException;
import com.lucascorreia.exception.ClienteNaoEncontradoException;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.util.FabricaDeDaos;

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
                System.out.println('\n' + "Cliente removido com sucesso!");
                return cliente;
            }else{
                throw new ClienteComPedidoException("O cliente possui pedidos em aberto. Não pode ser removido!");
            }

        }
        throw new ClienteNaoEncontradoException("Cliente inexistente!");
    }

    public Cliente recuperarPorId(int id) {
        Cliente cliente = clienteDAO.recuperarPorId(id);
        if (cliente != null) {
            return cliente;
        }
        throw new ClienteNaoEncontradoException("Cliente inexistente!");
    }


    public List<Cliente> recuperarClientes() {
        return clienteDAO.recuperarTodos();
    }

    public List<Cliente> recuperarClientesOrdenadosPorNome() {
        return clienteDAO.recuperarClientesOrdenadosPorNome();
    }
}
