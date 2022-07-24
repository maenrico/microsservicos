package io.github.maenrico.mscartoes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Entity
@Data

public class ClienteCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;
    private BigDecimal limite;

    public ClienteCartao(String nome, Cartao cartao, BigDecimal limite) {
        this.cpf = nome;
        this.cartao = cartao;
        this.limite = limite;
    }


}
