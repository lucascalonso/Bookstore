package com.lucascorreia;

import com.lucascorreia.exception.*;
import com.lucascorreia.model.*;
import com.lucascorreia.service.ClienteService;
import com.lucascorreia.service.FaturaService;
import com.lucascorreia.service.LivroService;
import com.lucascorreia.service.PedidoService;

import java.util.*;

public class PrincipalFatura {

    private static final FaturaService faturaService = new FaturaService();
    private static final ClienteService clienteService = new ClienteService();
    private static final PedidoService pedidoService = new PedidoService();
    private static final LivroService livroService = new LivroService();

    public static void principalFatura() {
        boolean continua = true;
        while (continua) {
            System.out.println("========================================================");
            System.out.println("1 - Gerar Fatura");
            System.out.println("2 - Remover Fatura");
            System.out.println("3 - Cancelar Fatura");
            System.out.println("4 - Listar faturas de um cliente");
            System.out.println("5 - Lista contendo a quantidade faturada, nome do livro e data da fatura dos itens faturados de um determinado mês/ano");
            System.out.println("6 - Lista de livros nunca faturados");
            System.out.println("7 - Lista de produtos faturados em determinado mês ou ano");
            System.out.println("8 - Voltar");

            int opFatura = Console.readInt("Selecione a opção que deseja tratar:");

            switch (opFatura) {
                case 1 -> {
                    int idCliente = Console.readInt("Digite a id do Cliente que irá faturar:");
                    try {
                        Cliente umCliente = clienteService.recuperarPorId(idCliente);

                        int idPegaPedido = Console.readInt("Digite o número do pedido do cliente:");
                        try {
                            Pedido umPedido = pedidoService.recuperarPedidoPorId(idPegaPedido);

                            boolean pedidoDoCliente = false;
                            for (Pedido pedido : umCliente.getPedidos()) {
                                if (pedido.getNumPedido() == umPedido.getNumPedido()) {
                                    pedidoDoCliente = true;
                                    break;
                                }
                            }

                            if (!pedidoDoCliente) {
                                System.out.println("Pedido não pertence ao cliente!");
                                break;
                            }

                            List<ItemFaturado> itensFaturados = faturaService.pegaItensFaturados(umPedido);
                            for (ItemFaturado faturados : itensFaturados) {
                                faturaService.incluir(faturados.getFatura());
                            }

                            List<ItemDePedido> itensDePedido = faturaService.pegaItensDePedido(itensFaturados);

                            String dataFatura = Console.readLine("Digite a Data e Hora da criação da Fatura (DD/MM/AAAA HH:MM:SS):");
                            Fatura umaFatura = new Fatura(umCliente, dataFatura, itensFaturados);

                            umPedido.setEnderecoDeEntrega(umCliente.getEndereco());
                            umaFatura.setClienteDaFatura(umCliente);
                            umaFatura.setDataEmissao(dataFatura);

                            faturaService.incluir(umaFatura);
                            umCliente.getFaturas().add(umaFatura);

                            System.out.println("Fatura gerada com sucesso!");
                        } catch (PedidoNaoEncontradoException | DataHoraInvalidaException | NaoPodeFaturarException | PedidoCanceladoException | NadaAFaturarException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 2 -> {
                    int idFaturaRemove = Console.readInt("Digite o número da fatura a ser excluída:");
                    try {
                        faturaService.remover(idFaturaRemove);
                        System.out.println("Fatura removida com sucesso!");
                    } catch (FaturaNaoEncontradaException | FaturaCanceladaException | ItemFaturadoException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 3 -> {
                    int idCancelarFatura = Console.readInt("Digite o número da fatura a ser cancelada:");
                    try {
                        String cancelamentoFatura = Console.readLine("Digite a Data e Hora do cancelamento da Fatura (DD/MM/AAAA HH:MM:SS):");
                        faturaService.alterar(idCancelarFatura, cancelamentoFatura);
                        System.out.println("Fatura cancelada com sucesso!");
                    } catch (FaturaNaoEncontradaException | NaoPodeCancelarFaturaException | FaturaCanceladaException | DataHoraInvalidaException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 4 -> {
                    int idCliente = Console.readInt("Digite o id do cliente para listar as faturas:");
                    try {
                        Cliente umCliente = clienteService.recuperarPorId(idCliente);
                        List<Fatura> faturasCliente = umCliente.getFaturas();
                        if (faturasCliente.isEmpty()) {
                            System.out.println("Não há faturas para este cliente!");
                        } else {
                            for (Fatura olhaFatura : faturasCliente) {
                                System.out.println(olhaFatura);
                            }
                        }
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 5 -> {
                    int mes = Console.readInt("Digite o mês que você deseja procurar a informação:");
                    int ano = Console.readInt("Digite o ano:");
                    int idLivro = Console.readInt("Digite o id do Livro:");

                    List<String> relatorio = faturaService.RelatorioUm(livroService.recuperarPorId(idLivro), mes, ano);
                    for (String linha : relatorio) {
                        System.out.println(linha);
                    }
                }

                case 6 -> {
                    List<Livro> livrosNuncaFaturados = faturaService.RelatorioDois();
                    if (livrosNuncaFaturados.isEmpty()) {
                        System.out.println("Todos os livros já foram faturados!");
                    } else {
                        for (Livro livro : livrosNuncaFaturados) {
                            System.out.println(livro);
                        }
                    }
                }

                case 7 -> {
                    int mes = Console.readInt("Digite o mês:");
                    int ano = Console.readInt("Digite o ano:");

                    List<Map<String, Object>> relatorio = faturaService.listarProdutosFaturadosPorMesAno(mes, ano);
                    faturaService.imprimirRelatorioProdutosFaturados(relatorio);
                }

                case 8 -> continua = false;

                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}