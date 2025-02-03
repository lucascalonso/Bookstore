package com.lucascorreia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.lucascorreia.exception.ClienteNaoEncontradoException;
import com.lucascorreia.exception.DataHoraInvalidaException;
import com.lucascorreia.exception.ItemDePedidoNaoEncontradoException;
import com.lucascorreia.exception.LivroNaoEncontradoException;
import com.lucascorreia.exception.NaoPodeCancelarPedidoException;
import com.lucascorreia.exception.PedidoNaoEncontradoException;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.model.ItemDePedido;
import com.lucascorreia.model.Pedido;
import com.lucascorreia.service.ClienteService;
import com.lucascorreia.service.LivroService;
import com.lucascorreia.service.PedidoService;

public class PrincipalPedido {
    private static final PedidoService pedidoService = new PedidoService();
    private static final LivroService livroService = new LivroService();
    private static final ClienteService clienteService = new ClienteService();

    public static void principalPedidos() {
        boolean continua = true;
        while (continua) {
            System.out.println("========================================================");
            System.out.println("1 - Fazer Pedido");
            System.out.println("2 - Remover Pedido");
            System.out.println("3 - Cancelar Pedido");
            System.out.println("4 - Listar os Pedidos");
            System.out.println("5 - Voltar");

            int opcao = Console.readInt("Selecione a opção que deseja tratar:");

            switch (opcao) {
                case 1 -> {
                    int idCliente = Console.readInt("Digite o número do cliente que fará o pedido:");
                    try {
                        Cliente umCliente = clienteService.recuperarPorId(idCliente);

                        boolean fazerPedido = true;
                        List<ItemDePedido> itensDePedido = new ArrayList<>();
                        while (fazerPedido) {
                            int idLivro = Console.readInt("Digite o Id do livro:");
                            try {
                                livroService.recuperarPorId(idLivro);
                            } catch (LivroNaoEncontradoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            }

                            int quantLivros = Console.readInt("Digite a quantidade:");
                            ItemDePedido item = new ItemDePedido(quantLivros, livroService.recuperarPorId(idLivro));
                            pedidoService.incluir(item);
                            itensDePedido.add(item);
                            System.out.println("Livro " + livroService.recuperarPorId(idLivro).getTitulo() + " adicionado ao pedido!");

                            item.setPrecoCobrado(livroService.recuperarPorId(idLivro).getPrecoUnitario() * quantLivros);
                            String pedindo = Console.readLine("Deseja inserir outro livro no pedido? N ou n para parar.");
                            if (Objects.equals(pedindo, "n") || Objects.equals(pedindo, "N")) {
                                fazerPedido = false;
                            }
                        }

                        String novoPedido = Console.readLine("Digite a Data e Hora da criação do pedido (DD/MM/AAAA HH:MM:SS):");
                        try {
                            Pedido umPedido = new Pedido(umCliente, itensDePedido, novoPedido);
                            pedidoService.incluir(umPedido);
                            umCliente.getPedidos().add(umPedido);
                            System.out.println("Pedido realizado com sucesso!");
                        } catch (DataHoraInvalidaException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 2 -> {
                    int idPedidoRemove = Console.readInt("Digite o id do pedido a ser removido:");
                    try {
                        pedidoService.remover(idPedidoRemove);
                        System.out.println("Pedido removido com sucesso!");
                    } catch (PedidoNaoEncontradoException | ItemDePedidoNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 3 -> {
                    int idCancelaPedido = Console.readInt("Digite o id do pedido a ser cancelado:");
                    try {
                        String dataCancelamento = Console.readLine("Digite a data de cancelamento (DD/MM/AAAA HH:MM:SS):");
                        pedidoService.alterar(idCancelaPedido, dataCancelamento);
                        System.out.println("Pedido cancelado com sucesso!");
                    } catch (PedidoNaoEncontradoException | NaoPodeCancelarPedidoException | DataHoraInvalidaException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 4 -> {
                    int idCliente = Console.readInt("Digite o id do cliente para listar os pedidos:");
                    try {
                        Cliente umCliente = clienteService.recuperarPorId(idCliente);
                        if (umCliente.getPedidos().isEmpty()) {
                            System.out.println("Não há pedidos para este cliente!");
                        } else {
                            for (Pedido iterPedidos : umCliente.getPedidos()) {
                                System.out.println(iterPedidos);
                            }
                        }
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 5 -> continua = false;

                default -> System.out.println("Opção inválida!");
            }
        }
    }
}