package io.github.maenrico.mscartoes.application;

import io.github.maenrico.mscartoes.domain.Cartao;
import io.github.maenrico.mscartoes.infra.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public Cartao save(Cartao cartao){
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> getCartaoRendarMenorIgual(Long renda){
        BigDecimal rendaBigDecimal = BigDecimal.valueOf(renda);

        return cartaoRepository.findByRendaLessThanEqual(rendaBigDecimal);
    }

}
