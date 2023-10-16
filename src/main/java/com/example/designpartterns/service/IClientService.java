package com.example.designpartterns.service;

import com.example.designpartterns.model.Client;


public interface IClientService {
  Iterable<Client> buscarTodos();

	Client buscarPorId(Long id);

	void inserir(Client client);

	void atualizar(Long id, Client client);

	void deletar(Long id);
}
