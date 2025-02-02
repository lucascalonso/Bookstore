package com.lucascorreia.model;

import com.lucascorreia.dao.ItemFaturadoDAO;
import com.lucascorreia.exception.DataHoraInvalidaException;
import com.lucascorreia.util.FabricaDeDaos;
import com.lucascorreia.util.Id;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.time.DateTimeException;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

public class Fatura implements Serializable {

    @Id
    int id;
    private ZonedDateTime dataEmissao;
    private ZonedDateTime dataCancelamento;
    private static final NumberFormat NF;
    String status;
    List<ItemFaturado> itensFaturados;
    double valorTotalFatura;
    double valorTotalDesconto;
    Cliente clienteDaFatura;


    public String getStatus() {
        return status;
    }

    public int getId() { return id; }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getClienteDaFatura() {
        return clienteDaFatura;
    }

    public void setClienteDaFatura(Cliente clienteDaFatura) {
        this.clienteDaFatura = clienteDaFatura;
    }

    public Fatura(Cliente cliente, String dataEmissao, List<ItemFaturado> itensFaturados) {

        setDataEmissao(dataEmissao);
        this.itensFaturados = itensFaturados;
        this.valorTotalDesconto=0;
        this.valorTotalFatura=0;
        this.clienteDaFatura = cliente;
        this.clienteDaFatura.getFaturas().add(this);
        ItemFaturadoDAO itemFaturado = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);
        for (ItemFaturado item:itensFaturados){
            item.setFatura(this);
            valorTotalFatura += item.getQtdFaturada()*item.getItemDePedido().precoCobrado;
            itemFaturado.incluir(item);
        }
        if (clienteDaFatura.getFaturas().size() > 3){
            valorTotalDesconto = 0.05 * valorTotalFatura;
        }
        this.status="Gerada";

    }

    public ZonedDateTime getDataEmissaoObject(){
        return this.dataEmissao;
    }

    private static final DateTimeFormatter DTF;

    static {
        NF =NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        DTF=DateTimeFormatter.ofPattern("dd/MM/yyyy");

        NF.setMaximumFractionDigits (2);
        NF.setMinimumFractionDigits (2);

    }
    public String getDataEmissao() {
        return DTF.format(dataEmissao.withZoneSameInstant(ZoneId.of("America/Sao_Paulo")));
    }

    public ZonedDateTime getDataCancelar(){
        return dataCancelamento;
    }

    public List<ItemFaturado> getItensFaturados() {
        return itensFaturados;
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

        } catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            throw new DataHoraInvalidaException("Data e hora inválida.");
        }
    }


    public void setDataCancelamento(String dataCancelamento) throws DataHoraInvalidaException {
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

        }catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            throw new DataHoraInvalidaException("Data e hora inválida.");
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fatura ").append(id).append(" (Data: ").append(getDataEmissao()).append("), valorTotalDaFatura = ");
        for (int i = 0; i < itensFaturados.size(); i++) {
            ItemFaturado item = itensFaturados.get(i);
            sb.append(item.getQtdFaturada()).append(" x ").append(NF.format(item.getItemDePedido().precoCobrado));
            if (i < itensFaturados.size() - 1) {
                sb.append(" + ");
            }
        }
        sb.append(" = ").append(NF.format(valorTotalFatura)).append(", valorTotalDoDesconto = ").append(NF.format(valorTotalDesconto)).append("\n");
        for (ItemFaturado item : itensFaturados) {
            sb.append("Livro ").append(item.getItemDePedido().getPegaLivro().getId()).append(" - qtdFaturada = ").append(item.getQtdFaturada()).append("\n");
        }
        return sb.toString();
    }
}
