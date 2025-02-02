package com.lucascorreia.dao;

import com.lucascorreia.model.Livro;

import java.util.List;

public interface LivroDAO extends DAOGenerico<Livro>{
    List<Livro> recuperarTodosOsLivrosOrdenados();
}
