package com.lucascorreia.util;


import com.lucascorreia.model.*;
import com.lucascorreia.service.ClienteService;
import com.lucascorreia.service.FaturaService;
import com.lucascorreia.service.LivroService;
import com.lucascorreia.service.PedidoService;

import java.util.ArrayList;
import java.util.List;

public class TestarSistema {
        static LivroService livroService = new LivroService();
        static ClienteService clienteService = new ClienteService();
        static PedidoService pedidoService = new PedidoService();
        static FaturaService faturaService = new FaturaService();

        public static void testarSistema() {
            System.out.println("1) e 2) Cadastrando e listando os 5 livros:");
            Livro livro1 = new Livro ("10", "Aaa", "Aaaa", 10, 100.0);
            Livro livro2 = new Livro ("20", "Bbb", "Bbbb", 20, 200.0);
            Livro livro3 = new Livro ("30", "Ccc", "Cccc", 30, 300.0);
            Livro livro4 = new Livro ("40", "Ddd", "Dddd", 40, 400.0);
            Livro livro5 = new Livro ("50", "Eee", "Eeee", 50, 500.0);
            livroService.incluir(livro1);
            livroService.incluir(livro2);
            livroService.incluir(livro3);
            livroService.incluir(livro4);
            livroService.incluir(livro5);
            try {
                List<Livro> allLivros = livroService.recuperarTodosOsLivrosOrdenados();
                for(Livro livro:allLivros) {
                    System.out.println(livro.getTitulo()+". Quantidade em estoque: "+livro.getQuant_est());
                }

                System.out.println("3) e 4) Cadastrando 2 clientes e os 5 pedidos do cliente 1, também notificando por email:");
                Cliente cliente_1 = new Cliente("111", "Xxxx", "xxxx@gmail.com", "11111111");
                Cliente cliente_2 = new Cliente("222", "Yyyy", "yyyy@gmail.com", "22222222");
                clienteService.incluir(cliente_1);
                clienteService.incluir(cliente_2);


                ArrayList<ItemDePedido> itemdepedido = new ArrayList<>(2);
                itemdepedido.add(new ItemDePedido(5,livroService.recuperarPorId(1)));
                itemdepedido.add(new ItemDePedido(15,livroService.recuperarPorId(2)));
                Pedido pedido1 = new Pedido(clienteService.recuperarPorId(1),itemdepedido,"01/01/2025 00:00:00");
                pedidoService.incluir(pedido1);

                ArrayList<ItemDePedido> itemdepedido2 = new ArrayList<>(2);
                itemdepedido2.add(new ItemDePedido(10,livroService.recuperarPorId(1)));
                itemdepedido2.add(new ItemDePedido(40,livroService.recuperarPorId(3)));
                Pedido pedido2 = new Pedido(clienteService.recuperarPorId(1),itemdepedido2,"02/01/2025 00:00:00" );
                pedidoService.incluir(pedido2);

                ArrayList<ItemDePedido> itemdepedido3 = new ArrayList<>(2);
                itemdepedido3.add(new ItemDePedido(5,livroService.recuperarPorId(1)));
                itemdepedido3.add(new ItemDePedido(10,livroService.recuperarPorId(3)));
                Pedido pedido3 = new Pedido(clienteService.recuperarPorId(1),itemdepedido3,"03/01/2025 00:00:00");
                pedidoService.incluir(pedido3);

                ArrayList<ItemDePedido> itemdepedido4= new ArrayList<>(3);
                itemdepedido4.add(new ItemDePedido(10,livroService.recuperarPorId(2)));
                itemdepedido4.add(new ItemDePedido(10,livroService.recuperarPorId(3)));
                itemdepedido4.add(new ItemDePedido(10,livroService.recuperarPorId(4)));
                Pedido pedido4 = new Pedido(clienteService.recuperarPorId(1),itemdepedido4,"04/01/2025 00:00:00");
                pedidoService.incluir(pedido4);

                ArrayList<ItemDePedido> itemdepedido5= new ArrayList<>(3);
                itemdepedido5.add(new ItemDePedido(5,livroService.recuperarPorId(2)));
                itemdepedido5.add(new ItemDePedido(5,livroService.recuperarPorId(3)));
                itemdepedido5.add(new ItemDePedido(5,livroService.recuperarPorId(4)));
                Pedido pedido5 = new Pedido(clienteService.recuperarPorId(1),itemdepedido5,"05/01/2025 00:00:00");
                pedidoService.incluir(pedido5);

                System.out.println("5) Listando os 5 pedidos:");
                List<Pedido> pedidos = pedidoService.recuperarPedidos();

                for(Pedido pedido:pedidos){
                    System.out.println("Número do pedido: "+ pedido.getNumPedido() + "\nStatus: " + pedido.getStatus());
                    for(ItemDePedido item:pedido.getItemDePedido()){
                        System.out.println("Nome do livro: "+ item.getPegaLivro().getTitulo() + " quantidade: " + item.getQtdPedida() );
                    }
                }
                System.out.println("6) Listando os livros e estoque:");
                List<Livro> livros = livroService.recuperarLivros();
                for(Livro livro:livros){
                    System.out.println(livro.toString());
                }

                System.out.println("7) Faturando pedidos 1 e 2 para 10 de janeiro de 2025:");
                try {
                    Fatura fatura1 = faturaService.faturar(pedidoService.recuperarPedidoPorId(1), "10/01/2025 00:00:00");
                    faturaService.incluir(fatura1);
                    Fatura fatura2 = faturaService.faturar(pedidoService.recuperarPedidoPorId(2), "10/01/2025 00:00:00");
                    faturaService.incluir(fatura2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            System.out.println("8) Cancelando fatura 2:");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                faturaService.cancelarFatura(faturaService.recuperarPorId(2),"01/02/2025 00:00:00");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("9) Faturando pedidos 3 e 4 para 20 de janeiro de 2025:");
            try{
                Fatura fatura3 = faturaService.faturar(pedidoService.recuperarPedidoPorId(3), "20/01/2025 00:00:00");
                faturaService.incluir(fatura3);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                Fatura fatura4 = faturaService.faturar(pedidoService.recuperarPedidoPorId(4), "20/01/2025 00:00:00");
                faturaService.incluir(fatura4);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("10) Faturando pedido 5 para 28 de fevereiro de 2025:");
            try {
                Fatura fatura5 = faturaService.faturar(pedidoService.recuperarPedidoPorId(5), "28/02/2025 00:00:00");
                faturaService.incluir(fatura5);
            }  catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("11) Listando livros e estoque:");
            for(Livro livro: livroService.recuperarTodosOsLivrosOrdenados()){
                System.out.println(livro.toString());
            }
            System.out.println("12) Listando faturas:");
            for(Fatura fatura:faturaService.recuperarFaturas()){
                System.out.println(fatura.toString());
            }
            System.out.println("13) Cancelando pedido 5:");
            try{
                pedidoService.cancelarPedido(pedidoService.recuperarPedidoPorId(5),"28/02/2025 00:00:00");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("14) Cancelando fatura 3 informando 6 de janeiro de 2025:");
            faturaService.cancelarFatura(faturaService.recuperarPorId(3),"06/01/2025 00:00:00");

            System.out.println("15) Removendo fatura 3:");
            try{
                faturaService.remover(3);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("16) Removendo fatura 4:");
            try {
                faturaService.remover(4);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("17) Listando livros e estoque:");
            for(Livro livro: livroService.recuperarTodosOsLivrosOrdenados()){
                System.out.println(livro.toString());
            }

            System.out.println("18) Adicionando estoque dos livros:");
            livroService.adicionarAoEstoque(100,1);
            livroService.adicionarAoEstoque(200,2);
            livroService.adicionarAoEstoque(300,3);
            livroService.adicionarAoEstoque(400,4);
            livroService.adicionarAoEstoque(500,5);

            System.out.println("18) e 19) Listando livros após abastecer estoque:");
            for(Livro livro: livroService.recuperarTodosOsLivrosOrdenados()){
                System.out.println(livro.toString());
            }
            System.out.println("20) Tentando faturar pedido 1 a 5 para o mês de Fevereiro de 2025:");
            try{
                faturaService.faturar(pedidoService.recuperarPedidoPorId(1),"01/02/2025 00:00:00");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                Fatura fatura6 = faturaService.faturar(pedidoService.recuperarPedidoPorId(2), "01/02/2025 00:00:00");
                faturaService.incluir(fatura6);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                Fatura fatura7 = faturaService.faturar(pedidoService.recuperarPedidoPorId(3), "01/02/2025 00:00:00");
                faturaService.incluir(fatura7);
            } catch (Exception e) {
               System.out.println(e.getMessage());
            }
            try {
                Fatura fatura8 = faturaService.faturar(pedidoService.recuperarPedidoPorId(4), "01/02/2025 00:00:00");
                faturaService.incluir(fatura8);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                Fatura fatura9 = faturaService.faturar(pedidoService.recuperarPedidoPorId(5), "01/02/2025 00:00:00");
                faturaService.incluir(fatura9);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("21) Relatório 1:");
            for(String linha:faturaService.RelatorioUm(livroService.recuperarPorId(1),1,2025)){
                System.out.println(linha);
            }
            System.out.println("22) Relatório 2:");
            for(Livro livro:faturaService.RelatorioDois()){
                System.out.println(livro.toString());
            }
            System.out.println("23) Relatório 3:");
            for(String linha:faturaService.RelatorioTres(2,2025)){
                System.out.println(linha);
            }


        }
    }

