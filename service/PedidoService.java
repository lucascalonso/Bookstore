package com.lucascorreia.service;
import com.lucascorreia.dao.ItemDePedidoDAO;
import com.lucascorreia.dao.PedidoDAO;
import com.lucascorreia.model.*;
import com.lucascorreia.exception.ItemDePedidoNaoEncontradoException;
import com.lucascorreia.exception.NaoPodeCancelarPedidoException;
import com.lucascorreia.util.FabricaDeDaos;
import com.lucascorreia.exception.PedidoNaoEncontradoException;
import com.lucascorreia.util.InformaEmail;

import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private final PedidoDAO pedidoDAO =FabricaDeDaos.getDAO(PedidoDAO.class);

    private final ItemDePedidoDAO itemDePedidoDAO = FabricaDeDaos.getDAO(ItemDePedidoDAO.class);

    public Pedido incluir(Pedido pedido){

        InformaEmail informaEmail = new InformaEmail(pedido.getCliente());
        informaEmail.start();
        return pedidoDAO.incluir(pedido);}

    public ItemDePedido incluir(ItemDePedido itemDePedido){return itemDePedidoDAO.incluir(itemDePedido);}

    public Pedido remover(int id){
        Pedido pedido = pedidoDAO.recuperarPorId(id);
        List<ItemDePedido>itemDePedidos=pedido.getItemDePedido();

        if (pedido == null) {
            throw new PedidoNaoEncontradoException("Pedido inexistente.");
        }else{

            for(ItemDePedido itens : itemDePedidos){
                removerItemDePedido(itens.getId());
            }
            itemDePedidos.clear();
        }
        pedidoDAO.remover(id);
        return pedido;
    }

    public void cancelarPedido(Pedido pedido, String data){
        FaturaService faturaService = new FaturaService();
        List<Fatura> listaFatura = faturaService.recuperarFaturas();
        boolean var = false;
        for(Fatura fatura:listaFatura){
          Pedido pedidos = fatura.getItensFaturados().getFirst().getItemDePedido().getPedido();
          if(pedido.getNumPedido() == pedidos.getNumPedido()){
              if(fatura.getDataCancelar() == null){
                  var = true;
                  break;
              }
          }
        }
        if(!var){
            pedido.setDataCancelamento(data);
            pedido.setStatus("cancelado");
        }else{
            throw new NaoPodeCancelarPedidoException("Não é possível cancelar um pedido faturado!");
        }
    }
    public ItemDePedido removerItemDePedido(int id) {
        ItemDePedido itemDePedido = itemDePedidoDAO.recuperarPorId(id);
        if (itemDePedido == null) {
            throw new ItemDePedidoNaoEncontradoException("Item de Pedido não encontrado");
        }
        itemDePedidoDAO.remover(id);
        return itemDePedido;
    }

    public Pedido alterar(int id, String dataCancelamento){
        Pedido pedido= pedidoDAO.recuperarPorId(id);

        if(pedido== null){
            throw new PedidoNaoEncontradoException("Pedido não encontrado");
        }

        Cliente umCliente = pedido.getCliente();
        List<Fatura>faturas = umCliente.getFaturas();
        List<Fatura>faturasNaoCanceladas = new ArrayList<>();
        List<ItemDePedido>itensDePedido = pedido.getItemDePedido();

        for(Fatura fatura:faturas){
            if(!fatura.getStatus().equals("Cancelada")){
                faturasNaoCanceladas.add(fatura);
            }
        }
        boolean flag = false;

        for(Fatura fatura: faturas){
            List<ItemFaturado>itensFaturados = fatura.getItensFaturados();
            for(ItemFaturado itemFaturado:itensFaturados){
                ItemDePedido itemDePedido = itemFaturado.getItemDePedido();
                for(ItemDePedido itemDePedido1: itensDePedido){
                    if(itemDePedido1.getId() == itemDePedido.getId()){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    break;
                }
            }
            if(flag)
                break;
        }
        if(flag){
            throw new NaoPodeCancelarPedidoException("O Pedido não possui todas faturas canceladas ou já faturou");
        }else{
            pedido.setDataCancelamento(dataCancelamento);
            pedido.setStatus("Cancelada");
        }

        return pedido;
    }

    public Pedido recuperarPedidoPorId(int id) {
        Pedido pedido = pedidoDAO.recuperarPorId(id);
        if (pedido == null) {
            throw new PedidoNaoEncontradoException("Pedido inexistente.");
        }
        return pedido;
    }

    public ItemDePedido recuperarPorId(int id) {
        ItemDePedido itemDePedido = itemDePedidoDAO.recuperarPorId(id);
        if (itemDePedido == null) {
            throw new ItemDePedidoNaoEncontradoException("Item de Pedido inexistente");
        }
        return itemDePedido;
    }

    public List<Pedido> recuperarPedidos(){
        return pedidoDAO.recuperarTodos();
    }
}
