package io.github.maenrico.msclientes.application;

import io.github.maenrico.msclientes.domain.Cliente;
import io.github.maenrico.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

  private final ClienteRepository clienteRepository;

  @Transactional
  public Cliente save(Cliente cliente){
    return  clienteRepository.save(cliente);
  }


  public Optional<Cliente> getByCpf(String cpf){
    return clienteRepository.findByCpf(cpf);
  }

}
