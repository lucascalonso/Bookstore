package com.lucascorreia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.lucascorreia.dao.ClienteDAO;
import com.lucascorreia.dao.FaturaDAO;
import com.lucascorreia.dao.ItemDePedidoDAO;
import com.lucascorreia.dao.ItemFaturadoDAO;
import com.lucascorreia.dao.LivroDAO;
import com.lucascorreia.dao.PedidoDAO;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.model.Fatura;
import com.lucascorreia.model.ItemDePedido;
import com.lucascorreia.model.ItemFaturado;
import com.lucascorreia.model.Livro;
import com.lucascorreia.model.Pedido;
import com.lucascorreia.util.FabricaDeDaos;
import com.lucascorreia.util.TestarSistema;

public class Main {
    public static void main(String[] args) {
        PrincipalCliente principalCliente = new PrincipalCliente();
        recuperaDados();

        boolean continua = true;
        while (continua) {
            System.out.println("========================================================");
            System.out.println("1 - Tratar Clientes.");
            System.out.println("2 - Tratar Livros.");
            System.out.println("3 - Tratar Pedidos.");
            System.out.println("4 - Tratar Faturas.");
            System.out.println("5 - Testar Sistema.");
            System.out.println("6 - Sair do Programa.");

            int opcao = Console.readInt("Selecione a opção que deseja tratar:");

            System.out.println();

            switch (opcao) {
                case 1 -> principalCliente.principal();
                case 2 -> PrincipalLivro.principalLivro();
                case 3 -> PrincipalPedido.principalPedidos();
                case 4 -> PrincipalFatura.principalFatura();
                case 5 -> TestarSistema.testarSistema();
                case 6 ->{
                    salvaDados();
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }

    private static void salvaDados(){
        System.out.println('\n' + "Salvando os dados em livraria.dat...");
        LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);
        ClienteDAO clienteDAO =FabricaDeDaos.getDAO(ClienteDAO.class);
        PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);
        ItemDePedidoDAO itemDePedidoDAO = FabricaDeDaos.getDAO(ItemDePedidoDAO.class);
        ItemFaturadoDAO itemFaturadoDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);
        FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

        Map<Integer, Livro> mapDeLivros =livroDAO.getMap();
        int contadorLivros=livroDAO.getContador();

        Map<Integer, Cliente> mapDeClientes = clienteDAO.getMap();
        int contadorClientes=clienteDAO.getContador();

        Map<Integer, Pedido> mapDePedidos = pedidoDAO.getMap();
        int contadorPedidos= pedidoDAO.getContador();

        Map<Integer, ItemDePedido> mapItemDePedidos = itemDePedidoDAO.getMap();
        int contadorItemDePedido = itemDePedidoDAO.getContador();

        Map<Integer, ItemFaturado> mapItemFaturados = itemFaturadoDAO.getMap();
        int contadorItemFaturados = itemFaturadoDAO.getContador();

        Map<Integer, Fatura> mapFaturados = faturaDAO.getMap();
        int contadorFaturas = faturaDAO.getContador();

        try {
            FileOutputStream fos = new FileOutputStream("livraria.dat");
            ObjectOutputStream oos =new ObjectOutputStream(fos);

            oos.writeObject(mapDeLivros);
            oos.writeInt(contadorLivros);

            oos.writeObject(mapDeClientes);
            oos.writeInt(contadorClientes);

            oos.writeObject(mapDePedidos);
            oos.writeInt(contadorPedidos);

            oos.writeObject(mapItemDePedidos);
            oos.writeInt(contadorItemDePedido);

            oos.writeObject(mapItemFaturados);
            oos.writeInt(contadorItemFaturados);

            oos.writeObject(mapFaturados);
            oos.writeInt(contadorFaturas);

            oos.close();

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void recuperaDados(){
        FileInputStream fis = null;
        ObjectInputStream ois =null;

        try{
            fis = new FileInputStream("livraria.dat");
            ois = new ObjectInputStream(fis);

            Map<Integer, Livro> mapDeLivro =(Map<Integer, Livro>) ois.readObject();
            int contadorLivros= ois.readInt();

            Map<Integer, Cliente> mapDeCliente = (Map<Integer, Cliente>) ois.readObject();
            int contadorClientes=ois.readInt();

            Map<Integer, Pedido> mapDePedidos = (Map<Integer, Pedido>) ois.readObject();
            int contadorPedidos=ois.readInt();

            Map<Integer, ItemDePedido> mapDeItemDePedido = (Map<Integer, ItemDePedido>) ois.readObject();
            int contadorDeItemDePedido=ois.readInt();

            Map<Integer,ItemFaturado> mapDeItemFaturado = (Map<Integer, ItemFaturado>) ois.readObject();
            int contadorItemFaturados = ois.readInt();

            Map<Integer, Fatura> mapDeFaturas = (Map<Integer, Fatura>) ois.readObject();
            int contadorFaturas = ois.readInt();

            LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);
            ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);
            PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);
            ItemDePedidoDAO itemDePedidoDAO = FabricaDeDaos.getDAO(ItemDePedidoDAO.class);
            ItemFaturadoDAO itemFaturadoDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);
            FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

            livroDAO.setMap(mapDeLivro);
            livroDAO.setContador(contadorLivros);

            clienteDAO.setMap(mapDeCliente);
            clienteDAO.setContador(contadorClientes);

            pedidoDAO.setMap(mapDePedidos);
            pedidoDAO.setContador(contadorPedidos);

            itemDePedidoDAO.setMap(mapDeItemDePedido);
            itemDePedidoDAO.setContador(contadorDeItemDePedido);

            itemFaturadoDAO.setMap(mapDeItemFaturado);
            itemFaturadoDAO.setContador(contadorItemFaturados);

            faturaDAO.setMap(mapDeFaturas);
            faturaDAO.setContador(contadorFaturas);
        } catch (FileNotFoundException e){
            System.out.println("O arquivo para livaria.dat não existe! Será criado ao final da execução...");
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        } finally {
            try {
                if(ois!=null){
                    ois.close();
                }
                if(fis!=null){
                    fis.close();
                }
            } catch (IOException e){
                System.err.println("Erro ao fechar o stream: " + e.getMessage());
            }
        }
    }
}