package io.github.maenrico.msavaliadorcredito.infra.clients;

import io.github.maenrico.msavaliadorcredito.domain.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msclientes", path = "/cliente")
public interface ClienteControllerClient {

    @GetMapping
    ResponseEntity<DadosCliente> dadosCliente(@RequestParam String cpf);

}
