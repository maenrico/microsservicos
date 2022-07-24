package io.github.maenrico.mscartoes.application;

import io.github.maenrico.mscartoes.application.representation.CartaoDto;
import io.github.maenrico.mscartoes.application.representation.CartoesClienteDto;
import io.github.maenrico.mscartoes.domain.Cartao;
import io.github.maenrico.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartoes")
public class CartoesController {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status(){
        return "ok";
    }


    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam Long renda){
        List<Cartao> list = cartaoService.getCartaoRendarMenorIgual(renda);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoDto cartaoDto){
        Cartao cartao = cartaoDto.toModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoService.save(cartao));
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesClienteDto>> getCartoesByCpf(@RequestParam String cpf){
        List<ClienteCartao> lista = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesClienteDto> resultList = lista.stream()
                .map(CartoesClienteDto::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultList);
    }

}
