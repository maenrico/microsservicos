package io.github.maenrico.msavaliadorcredito.application.exceptions;

public class DadosClienteNotFoundException extends Exception {

    public DadosClienteNotFoundException() {
        super("Os dados do cliente informados não foram encontrados!");
    }

}
