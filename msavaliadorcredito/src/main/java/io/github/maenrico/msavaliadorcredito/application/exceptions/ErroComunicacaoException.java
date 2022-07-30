package io.github.maenrico.msavaliadorcredito.application.exceptions;

import lombok.Getter;

public class ErroComunicacaoException extends Exception {

    @Getter
    private Integer status;

    public ErroComunicacaoException(String mensagem, Integer status){
        super(mensagem);
        this.status = status;
    }

}
