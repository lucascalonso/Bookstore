package com.lucascorreia.dao;

import java.util.List;
import com.lucascorreia.model.Cliente;

public interface ClienteDAO extends DAOGenerico<Cliente>{
    List<Cliente> recuperarClientesOrdenadosPorNome();
}