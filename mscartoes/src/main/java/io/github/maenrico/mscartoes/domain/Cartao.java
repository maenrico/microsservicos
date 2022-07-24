package io.github.maenrico.mscartoes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class Cartao {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao(String nome, BandeiraCartao bandeira, BigDecimal renda, BigDecimal limiteBasico) {
        this.nome = nome;
        this.bandeira = bandeira;
        this.renda = renda;
        this.limite = limiteBasico;
    }
}
