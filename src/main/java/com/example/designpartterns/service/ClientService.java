package com.example.designpartterns.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.designpartterns.model.Address;
import com.example.designpartterns.model.AddressRepository;
import com.example.designpartterns.model.Client;
import com.example.designpartterns.model.ClientRepository;

public class ClientService implements IClientService {
  // Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClientRepository clienteRepository;
	@Autowired
	private AddressRepository enderecoRepository;
	@Autowired
	private IViaCepService iViaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Client> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Client buscarPorId(Long id) {
		// Buscar Cliente por ID.
		Optional<Client> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Client cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Client cliente) {
		// Buscar Cliente por ID, caso exista:
		Optional<Client> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Client client) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = client.getEndereco().getCep();
		Address endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Address novoEndereco = iViaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		client.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(client);
	}
}
