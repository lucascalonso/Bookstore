package com.lucascorreia.dao.impl;

import java.util.List;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.dao.ClienteDAO;

public class ClienteDaoImpl extends DAOGenericoImpl<Cliente> implements ClienteDAO  {
    public List<Cliente> recuperarClientesOrdenadosPorNome()  {
        return map.values()
                .stream()
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .toList();
    }
}
