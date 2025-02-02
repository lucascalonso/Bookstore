package com.lucascorreia.service;
import com.lucascorreia.dao.LivroDAO;
import com.lucascorreia.util.FabricaDeDaos;
import com.lucascorreia.model.Livro;
import com.lucascorreia.exception.LivroNaoEncontradoException;
import java.util.List;

public class LivroService {

    private final LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);

    public Livro incluir(Livro livro){return livroDAO.incluir(livro);}

    public Livro alterar(Livro livro) {
        return livroDAO.alterar(livro);
    }

    public Livro recuperarPorId(int id){
        Livro livro = livroDAO.recuperarPorId(id);
        if (livro != null) {
            return livro;
        }
        throw new LivroNaoEncontradoException("Livro não existe.");

    }

    public Livro remover(int id){
        Livro livro = livroDAO.recuperarPorId(id);
        if (livro != null) {
            livroDAO.remover(id);
            return livro;
        }
        throw new LivroNaoEncontradoException("Livro não existe.");
    }

    public List<Livro> recuperarTodosOsLivrosOrdenados(){
        return livroDAO.recuperarTodosOsLivrosOrdenados();
    }

    public void adicionarAoEstoque(int i, int id){
        Livro livro = livroDAO.recuperarPorId(id);
        livro.setQuant_est(livro.getQuant_est() + i);
    }

    public List<Livro> recuperarLivros() {
        return livroDAO.recuperarTodos();
    }
}