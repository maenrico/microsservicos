package io.github.maenrico.msavaliadorcredito.application;

import feign.FeignException;
import io.github.maenrico.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import io.github.maenrico.msavaliadorcredito.application.exceptions.ErroComunicacaoException;
import io.github.maenrico.msavaliadorcredito.application.exceptions.ErroSolicitacaoCartaoException;
import io.github.maenrico.msavaliadorcredito.domain.*;
import io.github.maenrico.msavaliadorcredito.infra.clients.CartoesControllerClient;
import io.github.maenrico.msavaliadorcredito.infra.clients.ClienteControllerClient;
import io.github.maenrico.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient clienteControllerClient;
    private final CartoesControllerClient cartoesControllerClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException,
            ErroComunicacaoException {

        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteControllerClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesClienteResponse = cartoesControllerClient.getCartoesByCliente(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesClienteResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){

            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoException {

        try {
            //pega os dados do cliente
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteControllerClient.dadosCliente(cpf);
            //pega os dados do cartao pela renda passada pelo parametro do metodo
            ResponseEntity<List<Cartao>> cartoesRendaResponse = cartoesControllerClient.getCartoesRendaAte(renda);
            //pega a lista de cartoes que ele pode ter
            List<Cartao> cartoes = cartoesRendaResponse.getBody();
            //aqui faz o mapeamento pela quantitade de cartoes disponivel e pra cada cartao faz a idade dividido por 10 e multiplicada pelo limite do cartao
            List<CartaoAprovado> listaCartoesAprovados = cartoes.stream().map(cartao -> {

                BigDecimal limite = cartao.getLimite();
                if(dadosClienteResponse.getBody().getIdade() == null ){
                    return null;
                }else{
                    BigDecimal idadeBD = BigDecimal.valueOf(dadosClienteResponse.getBody().getIdade());

                    BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));

                    BigDecimal limiteAprovado = fator.multiply(limite);

                    CartaoAprovado aprovado = new CartaoAprovado();
                    aprovado.setCartao(cartao.getNome());
                    aprovado.setBandeira(cartao.getBandeira());
                    aprovado.setLimiteAprovado(limiteAprovado);

                    return aprovado;
                }

            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);
        }catch (FeignException.FeignClientException e){

            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status) {

                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            String protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());


        }
    }


}
