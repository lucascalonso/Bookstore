package com.lucascorreia;

import com.lucascorreia.exception.ClienteComPedidoException;
import com.lucascorreia.exception.ClienteNaoEncontradoException;
import com.lucascorreia.model.Cliente;
import com.lucascorreia.service.ClienteService;

import java.util.List;

public class PrincipalCliente {
    private final ClienteService clienteService = new ClienteService();

    public  void principal() {

        String nome;
        String cpf;
        String email;
        String telefone;
        String endereco;
        Cliente umCliente;

        boolean continua = true;
        while (continua) {
            System.out.println("========================================================");
            System.out.println("1 - Cadastrar um cliente");
            System.out.println("2 - Alterar um cliente");
            System.out.println("3 - Remover um cliente");
            System.out.println("4 - Listar todos os clientes");
            System.out.println("5 - Listar todos os clientes ordenados por nome");
            System.out.println("6 - Voltar");

            int opcao = Console.readInt("Selecione a opção que deseja tratar:");

            System.out.println();

            switch (opcao) {
                case 1 -> {
                    cpf=Console.readLine("Informe o cpf do Cliente: ");
                    nome = Console.readLine("Informe o nome do Cliente: ");
                    email=Console.readLine("Informe o email do Cliente: ");
                    telefone=Console.readLine("Informe o telefone do cliente: ");
                    endereco=Console.readLine("Informe o endereço do cliente: ");
                    umCliente = new Cliente( cpf, nome,email,telefone);
                    clienteService.incluir(umCliente);
                    System.out.println("\n ID do cliente "+umCliente.getNumero());
                    System.out.println("\n Cliente " + umCliente.getNome() + " cadastrado com sucesso!");
                }
                case 2 ->
                {
                    int id_cli = Console.readInt("Informe o número do Cliente que você deseja alterar:");

                    try {
                        umCliente = clienteService.recuperarPorId(id_cli);
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. Nome");
                    System.out.println("2. E-mail");
                    System.out.println("3. Telefone");
                    System.out.println("4 -CPF");
                    System.out.println("5 - Endereço");

                    int opcaoAlteracao = Console.readInt("Digite a opção desejada:");

                    switch (opcaoAlteracao) {
                        case 1 -> {
                            String novoNome = Console.readLine("Informe o novo nome:");
                            umCliente.setNome(novoNome);
                            clienteService.alterar(umCliente);
                            System.out.println('\n' + "Alteração do nome efetuada com sucesso!");
                        }
                        case 2 -> {
                            String novoEmail = Console.readLine("Informe o novo e-mail: ");
                            umCliente.setEmail(novoEmail);
                            clienteService.alterar(umCliente);
                            System.out.println('\n' + "Alteração do e-mail efetuado com sucesso!");
                        }

                        case 3->{
                            String novoTelefone=Console.readLine("Informe o novo telefone: ");
                            umCliente.setTelefone(novoTelefone);
                            clienteService.alterar(umCliente);
                            System.out.println('\n'+ "Alteração do telefone efetuado com sucesso!");
                        }

                        case 4->{
                            String novoCpf=Console.readLine("Informe novo CPF");
                            umCliente.setCpf(novoCpf);
                            clienteService.alterar(umCliente);
                            System.out.println('\n'+"Alteração do CPF efetuado com sucesso!");
                        }

                        case 5 ->{
                            String novoEndereco=Console.readLine("Informe o novo endereco:");
                            umCliente.setEndereco(novoEndereco);
                            clienteService.alterar(umCliente);
                            System.out.println('\n'+ " Alteração do endereço efetuado com sucesso!");
                        }

                        default -> System.out.println('\n' + "Opção inválida!");
                    }
                }
                case 3 ->
                {
                    int id_rem = Console.readInt("Informe o número do cliente que você deseja remover:");

                    try {
                        clienteService.remover(id_rem);
                    } catch (ClienteNaoEncontradoException | ClienteComPedidoException e) {
                        System.out.println('\n' + e.getMessage());
                    }

                }
                case 4 -> {
                    List<Cliente> clientes = clienteService.recuperarClientes();
                    if(clientes.isEmpty()){
                        System.out.println('\n'+ " Nao há clientes disponíveis!");
                    }else{
                        for (Cliente cliente : clientes) {
                            System.out.println(cliente);
                        }
                    }

                }
                case 5 -> {
                    List<Cliente> clientes = clienteService.recuperarClientesOrdenadosPorNome();
                    if(clientes.isEmpty()){
                        System.out.println('\n'+ " Nao há clientes disponíveis!");
                    }else{
                        for (Cliente cliente : clientes) {
                            System.out.println(cliente);
                        }
                    }
                }
                case 6 ->
                        continua = false;

                default -> System.out.println('\n' + "Opção inválida!");
            }
        }

    }
}
