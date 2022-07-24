package io.github.maenrico.mscartoes.application.representation;

import io.github.maenrico.mscartoes.domain.Cartao;
import io.github.maenrico.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartoesClienteDto {

    private String nome;
    private String bandeira;
    private BigDecimal limite;


    public static CartoesClienteDto fromModel(ClienteCartao clienteCartao){
        return new CartoesClienteDto(
                clienteCartao.getCartao().getNome(),
                clienteCartao.getCartao().getBandeira().toString(),
                clienteCartao.getLimite()
        );
    }
}
