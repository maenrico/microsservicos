package io.github.maenrico.msavaliadorcredito.application;

import feign.FeignException;
import feign.RetryableException;
import io.github.maenrico.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import io.github.maenrico.msavaliadorcredito.application.exceptions.ErroComunicacaoException;
import io.github.maenrico.msavaliadorcredito.application.exceptions.ErroSolicitacaoCartaoException;
import io.github.maenrico.msavaliadorcredito.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoController {

    public final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status(String status) {
        return "ok";
    }


    @GetMapping( value = "situacao-cliente",params = "cpf")
    public ResponseEntity consultarSituacaoCliente(@RequestParam String cpf){

        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        }
        catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (ErroComunicacaoException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
        catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("O serviço está fora do ar temporariamente!");
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao){
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService
                    .realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());

            return ResponseEntity.ok(retornoAvaliacaoCliente);
        }
        catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (ErroComunicacaoException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
        catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("O serviço está fora do ar temporariamente!");
        }
    }

    @PostMapping("/solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try{
            ProtocoloSolicitaoCartao protocoloSolicitaoCartao = avaliadorCreditoService
                    .solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitaoCartao);
        }catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
