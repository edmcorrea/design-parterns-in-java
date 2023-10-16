package com.example.designpartterns.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.designpartterns.model.Address;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface IViaCepService {
  @GetMapping("/{cep}/json/")
	Address consultarCep(@PathVariable("cep") String cep);
}
