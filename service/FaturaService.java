package com.lucascorreia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.lucascorreia.dao.FaturaDAO;
import com.lucascorreia.dao.ItemDePedidoDAO;
import com.lucascorreia.dao.ItemFaturadoDAO;
import com.lucascorreia.exception.FaturaCanceladaException;
import com.lucascorreia.exception.FaturaNaoEncontradaException;
import com.lucascorreia.exception.ItemFaturadoException;
import com.lucascorreia.exception.NaoPodeCancelarFaturaException;
import com.lucascorreia.exception.NaoPodeFaturarException;
import com.lucascorreia.exception.PedidoNaoEncontradoException;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.model.Fatura;
import com.lucascorreia.model.ItemDePedido;
import com.lucascorreia.model.ItemFaturado;
import com.lucascorreia.model.Livro;
import com.lucascorreia.model.Pedido;
import com.lucascorreia.util.FabricaDeDaos;

public class FaturaService {

    private static final LivroService livroService = new LivroService();
    private final FaturaDAO FaturaDAO =FabricaDeDaos.getDAO(FaturaDAO.class);
    private final ItemFaturadoDAO itemFaturadoDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);
    private final ItemDePedidoDAO itemDePedidoDAO = FabricaDeDaos.getDAO(ItemDePedidoDAO.class);

    public Fatura faturar(Pedido pedido, String data){
        int total = 0;
        for(ItemDePedido item:pedido.getItemDePedido()){
            total += item.getQtdAFaturar();
        }
            if(total == 0 || pedido.getDataCancelar() != null){
                throw new NaoPodeFaturarException("Não é possível faturar um pedido cancelado ou um pedido totalmente faturado!");
            }

            List<ItemFaturado> itemfaturado = new ArrayList<>(pedido.getItemDePedido().size());
            int i = 0;
            List<ItemDePedido> itensDePedido = pedido.getItemDePedido();

            while (i < itensDePedido.size()) {
                ItemDePedido itemPedido = itensDePedido.get(i);
                Livro livro = itemPedido.getPegaLivro();
                int estoqueLivro = livro.getQuant_est();
                int quantidadeAFaturar = itemPedido.getQtdAFaturar();
                int quantidadeFaturada = 0;
                if (estoqueLivro > 0 && quantidadeAFaturar>0) {
                    if (estoqueLivro >= quantidadeAFaturar) {
                        livro.setQuant_est(estoqueLivro - quantidadeAFaturar);
                        quantidadeFaturada = quantidadeAFaturar;
                    } else {
                        quantidadeFaturada = estoqueLivro;
                        livro.setQuant_est(0);
                    }
                    itemPedido.setQtdAFaturar(quantidadeAFaturar-quantidadeFaturada);
                    ItemFaturado itemfatura = new ItemFaturado(itemPedido,quantidadeFaturada);
                    itemfaturado.add(itemfatura);
                }
                i++;
            }
        
        if (itemfaturado.isEmpty()) {
            pedido.setStatus("Não faturado");
            throw new NaoPodeFaturarException("Não foi possível faturar o pedido pois não há estoque de nenhum livro!");
        } else if (itemfaturado.size() != pedido.getItemDePedido().size()) {
            pedido.setStatus("Parcialmente faturado.");
        } else {
            boolean checkIguais = true;
            for(int j =0; j< itemfaturado.size(); j++){
               if(pedido.getItemDePedido().get(j).getQtdPedida() != itemfaturado.get(j).getQtdFaturada()) {
                   checkIguais = false;
                   break;
               }
            }
            if(checkIguais){
                pedido.setStatus("Totalmente faturado.");
            }else{
                pedido.setStatus("Parcialmente faturado.");
            }
        }
        return new Fatura(pedido.getCliente(),data,itemfaturado);
    }

    public Fatura cancelarFatura(Fatura fatura, String data){

        if(fatura.getClienteDaFatura().getFaturas().size() > 3){
            for(ItemFaturado itemFaturado:fatura.getItensFaturados()){
                Livro livro = itemFaturado.getItemDePedido().getPegaLivro();
                livro.setQuant_est(livro.getQuant_est() + itemFaturado.getQtdFaturada());
            }
            fatura.setDataCancelamento(data);
            return fatura;
        }
        throw new NaoPodeCancelarFaturaException("Não é possível cancelar fatura de um cliente com menos de 3 pedidos faturados!");
    }

    public Fatura incluir(Fatura fatura){
        return FaturaDAO.incluir(fatura);
    }

    public List<ItemDePedido>pegaItensDePedido(List<ItemFaturado> itensFaturados){

        List<ItemDePedido>pedidos=new ArrayList<>();
        for(ItemFaturado faturado:itensFaturados){
            pedidos.add(faturado.getItemDePedido());
        }
        return pedidos;
    }


    public List<ItemFaturado>pegaItensFaturados(Pedido pedido){

        if(pedido==null){
            throw new PedidoNaoEncontradoException("Pedido inexistente!");
        }

        List<ItemDePedido> itensDePedido= pedido.getItemDePedido();
        List<ItemFaturado>itensFaturados = new ArrayList<>();
        for(ItemDePedido itens:itensDePedido){

            Livro livro=itens.getPegaLivro();
            if(livro.getQuant_est()>0){
                ItemFaturado itemFaturado;

                if(livro.getQuant_est()<itens.getQtdPedida()){

                    itens.setQtdAFaturar(itens.getQtdPedida() - livro.getQuant_est());
                    itemFaturado = new ItemFaturado(itens,livro.getQuant_est());
                    itens.setQtdPedida(itens.getQtdAFaturar());
                    livro.setQuant_est(0);

                } else {
                    itens.setQtdAFaturar(itens.getQtdPedida());
                    itemFaturado = new ItemFaturado(itens,itens.getQtdPedida());
                    livro.setQuant_est(livro.getQuant_est() - itens.getQtdAFaturar());
                    itens.setQtdAFaturar(0);
                    itens.setQtdPedida(0);
                }

                itensFaturados.add(itemFaturado);
            }
        }
        return itensFaturados;
    }


    public Fatura remover(int id) {
        Fatura fatura = FaturaDAO.recuperarPorId(id);
        if (fatura == null) {
            throw new FaturaNaoEncontradaException("Fatura inexistente!");
        }

        if (fatura.getDataCancelar() != null) {
            throw new FaturaCanceladaException("Fatura cancelada não pode ser removida!");
        }

        fatura.setStatus("Removida");

        List<ItemFaturado> itemFaturados = fatura.getItensFaturados();
        ItemDePedido itemDePedido;
        Livro livro;
        for (ItemFaturado item : itemFaturados) {
            itemDePedido = item.getItemDePedido();
            livro = itemDePedido.getPegaLivro();
            livro.setQuant_est(livro.getQuant_est() + item.getQtdFaturada());
        }
        fatura.getClienteDaFatura().getFaturas().remove(fatura);
        FaturaDAO.remover(id);

        System.out.println("Fatura removida com sucesso!");
        return fatura;
    }

    public Fatura alterar(int id, String cancelamentoFatura){

        Fatura fatura = FaturaDAO.recuperarPorId(id);


        if (fatura == null) {
            throw new FaturaNaoEncontradaException("Fatura inexistente!");
        }else if(Objects.equals(fatura.getStatus(), "Cancelada")){
            throw new FaturaCanceladaException("Fatura já foi cancelada!");
        }else {

            Cliente cliente=fatura.getClienteDaFatura();

            fatura.setDataCancelamento(cancelamentoFatura);

            List<ItemFaturado>itemFaturados=fatura.getItensFaturados();
            ItemDePedido pedido;
            Livro livro;

            List<Fatura> faturas = FaturaDAO.recuperarTodos();
            if(cliente.getFaturas().size() >=3){
                for(ItemFaturado itens : itemFaturados){
                    pedido=itens.getItemDePedido();
                    livro=pedido.getPegaLivro();
                    livro.setQuant_est(livro.getQuant_est()+ itens.getQtdFaturada());
                    removerItensFaturados(itens.getId());
                }
                itemFaturados.clear();
                fatura.setStatus("Cancelada");
            }else{
                throw new NaoPodeCancelarFaturaException("Não é possível cancelar fatura de um cliente sem 3 pedidos faturados!");
            }

        }
        return fatura;
    }

    public ItemFaturado removerItensFaturados(int id) {

        ItemFaturado itemFaturado = itemFaturadoDAO.recuperarPorId(id);

        if(itemFaturado==null){
            throw new ItemFaturadoException("Item faturado inexistente!");
        }
        itemFaturadoDAO.remover(id);
        return itemFaturado;
    }

    public Fatura recuperarPorId(int id) {
        Fatura fatura = FaturaDAO.recuperarPorId(id);
        if (fatura == null) {
            throw new FaturaNaoEncontradaException("Fatura inexistente!");
        }
        return fatura;
    }

    public List<Fatura> recuperarFaturas(){
        return FaturaDAO.recuperarTodos();
    }

    public ArrayList<String> RelatorioUm(Livro livro, int mes, int ano) {
        List<ItemFaturado> listaItemFaturado = itemFaturadoDAO.recuperarTodos();
        ArrayList<String> listaRetorna = new ArrayList<>();

        for (ItemFaturado itemFaturado : listaItemFaturado) {
            Fatura fatura = itemFaturado.getFatura();
            int itemMes = fatura.getDataEmissaoObject().getMonthValue();
            int itemAno = fatura.getDataEmissaoObject().getYear();
            Livro livroFaturado = itemFaturado.getItemDePedido().getPegaLivro();

            if (mes == itemMes && ano == itemAno && livroFaturado.getId() == livro.getId()) {
                Pedido pedidoFatura = itemFaturado.getItemDePedido().getPedido();
                String linha = "Livro " + livroFaturado.getId() + " | " + livroFaturado.getTitulo() +
                        " | Quantidade: " + itemFaturado.getQtdFaturada() +
                        " | Fatura Número: " + fatura.getId() +
                        " | Pedido Número: " + pedidoFatura.getNumPedido() +
                        " | Data da Fatura: " + fatura.getDataEmissao();
                listaRetorna.add(linha);
            }
        }

        return listaRetorna;
    }

    public List<Livro> RelatorioDois() {
        Set<Integer> livrosFaturados = new HashSet<>();
        List<Fatura>faturas2 = FaturaDAO.recuperarTodos();
        List<Livro> livros=livroService.recuperarLivros();

        for (Fatura fatura : faturas2) {
            for (ItemFaturado item : fatura.getItensFaturados()) {
                livrosFaturados.add(item.getItemDePedido().getPegaLivro().getId());
            }
        }
        List<Livro> livrosNuncaFaturados = new ArrayList<>();

        for (Livro livro : livros) {
            if (!livrosFaturados.contains(livro.getId())) {
                livrosNuncaFaturados.add(livro);
            }
        }

        return livrosNuncaFaturados;
    }

    public List<String> RelatorioTres(int mes, int ano) {
        List<ItemFaturado> itensFaturados = itemFaturadoDAO.recuperarTodos();
        Map<Integer, Integer> quantidadePorLivro = new HashMap<>();
        List<String> relatorio = new ArrayList<>();

        for (ItemFaturado item : itensFaturados) {
            Fatura fatura = item.getFatura();
            int faturaMes = fatura.getDataEmissaoObject().getMonthValue();
            int faturaAno = fatura.getDataEmissaoObject().getYear();

            if (faturaMes == mes && faturaAno == ano) {
                Livro livro = item.getItemDePedido().getPegaLivro();
                int livroId = livro.getId();

                quantidadePorLivro.put(livroId, quantidadePorLivro.getOrDefault(livroId, 0) + item.getQtdFaturada());
            }
        }

        for (Map.Entry<Integer, Integer> entry : quantidadePorLivro.entrySet()) {
            int livroId = entry.getKey();
            int quantidade = entry.getValue();
            Livro livro = livroService.recuperarPorId(livroId);

            if (livro != null) {
                String linha = "Livro: " + livro.getId() + " | " + livro.getTitulo() + " | Quantidade Faturada: " + quantidade;
                relatorio.add(linha);
            }
        }
        return relatorio;
    }
}