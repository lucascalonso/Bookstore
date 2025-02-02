package com.lucascorreia.dao.impl;
import com.lucascorreia.dao.LivroDAO;
import com.lucascorreia.model.Livro;

import java.util.List;

public class LivroDaoImpl extends DAOGenericoImpl<Livro> implements LivroDAO {
    public List<Livro> recuperarTodosOsLivrosOrdenados() {
        return map.values().stream()
                .sorted((p1, p2) -> p1.getTitulo().compareTo(p2.getTitulo()))
                .toList();
    }
}
