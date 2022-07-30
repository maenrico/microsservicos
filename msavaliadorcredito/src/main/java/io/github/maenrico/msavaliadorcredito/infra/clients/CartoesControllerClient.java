package io.github.maenrico.msavaliadorcredito.infra.clients;

import io.github.maenrico.msavaliadorcredito.domain.Cartao;
import io.github.maenrico.msavaliadorcredito.domain.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartoesControllerClient {

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartaoCliente>> getCartoesByCliente(@RequestParam String cpf);

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam Long renda);

}
