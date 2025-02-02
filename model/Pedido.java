package com.lucascorreia.model;
import com.lucascorreia.exception.DataHoraInvalidaException;
import com.lucascorreia.util.Id;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pedido implements Serializable {
    @Id
    int numPedido;
    private ZonedDateTime dataEmissao;
    private ZonedDateTime dataCancelamento;
    String status;
    String enderecoDeEntrega;
    List<ItemDePedido> pedidos;
    Cliente cliente;
    int quantAFaturar;
    int quantPedida;

    public ZonedDateTime getDataCancelar(){
        return dataCancelamento;
    }

    public int getQuantAFaturar() {
        return quantAFaturar;
    }

    public void setQuantAFaturar(int quantAFaturar) {
        this.quantAFaturar = quantAFaturar;
    }

    public int getQuantPedida() {
        return quantPedida;
    }

    public void setQuantPedida(int quantPedida) {
        this.quantPedida = quantPedida;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    private static final DateTimeFormatter DTF;

    static {
        DTF=DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public Pedido(Cliente cliente, List<ItemDePedido> pedidos, String dataEmissao)throws DataHoraInvalidaException {
        setDataEmissao(dataEmissao);
        this.pedidos = pedidos;
        this.dataCancelamento=null;
        this.status="Não faturado";
        this.cliente = cliente;

        for(ItemDePedido item:pedidos){
            item.setPedido(this);
        }
    }



    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public String getDataEmissao() {
        System.out.println("Data e hora em UTC: " + dataEmissao);

        return DTF.format(dataEmissao.withZoneSameInstant(ZoneId.of("America/Sao_Paulo")));

    }

    public String getEnderecoDeEntrega() {
        return enderecoDeEntrega;
    }

    public void setEnderecoDeEntrega(String enderecoDeEntrega) {
        this.enderecoDeEntrega = enderecoDeEntrega;
    }


    public List<ItemDePedido> getItemDePedido() {
        return pedidos;
    }

    public void setDataEmissao(String dataEmissao) throws DataHoraInvalidaException {
        try{
            int dia = Integer.parseInt(dataEmissao.substring(0,2));
            int mes = Integer.parseInt(dataEmissao.substring(3,5));
            int ano = Integer.parseInt(dataEmissao.substring(6,10));

            int hora =    Integer.parseInt(dataEmissao.substring(11,13));
            int minuto =  Integer.parseInt(dataEmissao.substring(14,16));
            int segundo = Integer.parseInt(dataEmissao.substring(17,19));

            this.dataEmissao = ZonedDateTime
                    .of(ano, mes, dia, hora, minuto, segundo, 0,
                            ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.of("UTC"));

            System.out.println("Data e hora em UTC: " + this.dataEmissao);
        } catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            throw new DataHoraInvalidaException("Data e hora inválida!");
        }
    }

    public String getDataCancelamento() {
        System.out.println("Data e hora em UTC: " + dataCancelamento);
        return DTF.format(dataCancelamento.withZoneSameInstant(ZoneId.of("America/Sao_Paulo")));
    }

    public void setDataCancelamento(String dataCancelamento) {

        try{
            int dia = Integer.parseInt(dataCancelamento.substring(0,2));
            int mes = Integer.parseInt(dataCancelamento.substring(3,5));
            int ano = Integer.parseInt(dataCancelamento.substring(6,10));

            int hora =    Integer.parseInt(dataCancelamento.substring(11,13));
            int minuto =  Integer.parseInt(dataCancelamento.substring(14,16));
            int segundo = Integer.parseInt(dataCancelamento.substring(17,19));

            this.dataCancelamento = ZonedDateTime
                    .of(ano, mes, dia, hora, minuto, segundo, 0,
                            ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.of("UTC"));

            System.out.println("Data e hora em UTC: " + this.dataCancelamento);

        }catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            throw new DataHoraInvalidaException("Data e hora inválida!");
        }


    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setItemDePedido(List<ItemDePedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public String toString(){
        return "Status do pedido "+ getStatus() + pedidos.toString();
    }

}
