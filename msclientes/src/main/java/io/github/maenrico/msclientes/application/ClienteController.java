package io.github.maenrico.msclientes.application;

import io.github.maenrico.msclientes.application.representation.ClienteDto;
import io.github.maenrico.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String status(){
        log.info("Obtendo o status do cliente");
        return "ok";
    }

    @GetMapping(params = "cpf")
    public ResponseEntity dadosCliente(@RequestParam String cpf){

        Optional<Cliente> cliente = clienteService.getByCpf(cpf);

        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteDto clienteDto){
        Cliente cliente = clienteDto.toModel();

        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).body(clienteService.save(cliente));
    }

}
