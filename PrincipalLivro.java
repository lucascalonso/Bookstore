package com.lucascorreia;

import com.lucascorreia.exception.LivroNaoEncontradoException;
import com.lucascorreia.model.Livro;
import com.lucascorreia.service.LivroService;

import java.util.List;

public class PrincipalLivro {

    private static final LivroService livroService = new LivroService();

    public static void principalLivro(){

        Livro umLivro;
        boolean continua = true;

        while (continua){

            System.out.println("========================================================");
            System.out.println("1 - Cadastrar um livro");
            System.out.println("2 - Alterar um livro");
            System.out.println("3 - Remover um livro");
            System.out.println("4 - Listar livros");
            System.out.println("5 - Voltar");
            int opcaoLivro= Console.readInt("Selecione a opção que deseja tratar:");

            switch (opcaoLivro){

                case 1->{
                    String isbnLivro=Console.readLine("Digite o isbn do livro:");
                    String tituloLivro=Console.readLine("Digite o titulo do livro:");
                    String descricaoLivro=Console.readLine("Digite a descrição do livro:");
                    int quant_estLivro=Console.readInt("Digite a quantidade de estados do livro:");
                    double precoLivro=Console.readDouble("Digite o preço do livro:");
                    umLivro=new Livro(isbnLivro,tituloLivro,descricaoLivro,quant_estLivro,precoLivro);
                    livroService.incluir(umLivro);
                    System.out.println("Livro "+umLivro.getTitulo()+" Cadastrado com sucesso!");

                }

                case 2->{

                    int idlivro2=Console.readInt("Digite o id do livro a ser alterado:");

                    try {
                        umLivro=livroService.recuperarPorId(idlivro2);

                    }catch (LivroNaoEncontradoException e){
                        System.out.println('\n'+ e.getMessage());
                        break;
                    }
                    System.out.println("1 - isbn");
                    System.out.println("2 - Título");
                    System.out.println("3 - Descrição");
                    System.out.println("4 - Estoque");
                    System.out.println("5 - Preço do livro");

                    int opcaoLivroAlteracao = Console.readInt("Digite a informação do livro deseja alterar:");

                    switch (opcaoLivroAlteracao){

                        case 1 ->{

                            String isbnNovo=Console.readLine("Digite o novo ISBN:");
                            umLivro.setIsbn(isbnNovo);
                            livroService.alterar(umLivro);
                            System.out.println("ISBN do Livro alterado com sucesso!");
                        }

                        case 2->{
                            String titNovo=Console.readLine("Digite o novo titulo:");
                            umLivro.setTitulo(titNovo);
                            livroService.alterar(umLivro);
                            System.out.println("Titulo alterado com sucesso!");

                        }

                        case 3->{
                            String descricaoNova=Console.readLine("Digite a nova descrição:");
                            umLivro.setDescricao(descricaoNova);
                            livroService.alterar(umLivro);
                            System.out.println("Descrição alterada com sucesso!");

                        }

                        case 4->{
                            int quantEstNova = Console.readInt("Digite o novo total do estoque");
                            umLivro.setQuant_est(quantEstNova);
                            livroService.alterar(umLivro);
                            System.out.println("Estoque alterado com sucesso!");

                        }

                        case 5->{
                            double precoNovo = Console.readDouble("Digite o novo preço");
                            umLivro.setPreco(precoNovo);
                            livroService.alterar(umLivro);
                            System.out.println("Preco alterado com sucesso!");
                        }
                        default -> System.out.println("Opção Inválida!");
                    }
                }

                case 3->{
                    int idLivro = Console.readInt("Digite o id do livro para ser removido:");
                    try {
                        livroService.remover(idLivro);
                        System.out.println("Livro removido com sucesso!");
                    }catch (LivroNaoEncontradoException e){
                        System.out.println('\n'+ e.getMessage());
                    }

                }

                case 4->{
                    List<Livro> livros= livroService.recuperarLivros();
                    if(livros.isEmpty()){
                        System.out.println('\n'+ " Nao há Livros disponíveis!");
                    }else{
                        for (Livro livro : livros) {
                            System.out.println(livro);
                        }
                    }
                }

                case 5->{
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");

            }
        }
    }
}
