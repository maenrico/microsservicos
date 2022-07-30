package io.github.maenrico.msavaliadorcredito.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maenrico.msavaliadorcredito.domain.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

     private final RabbitTemplate rabbitTemplate;

     private final Queue queueEmissaoCartoes;

     public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
          String json = convertIntoJson(dados);
          rabbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
     }

     private String convertIntoJson(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException{
          ObjectMapper mapper = new ObjectMapper();
          return mapper.writeValueAsString(dados);
     }

}
