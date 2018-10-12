package com.example.anotation.compiler;

public class IdAlreadyUsedException extends Exception {
    private final FactoryAnnotatedClass factoryAnnotatedClass;

    public IdAlreadyUsedException(FactoryAnnotatedClass factoryAnnotatedClass) {
        this.factoryAnnotatedClass = factoryAnnotatedClass;
    }

    public FactoryAnnotatedClass getExisting() {
        return factoryAnnotatedClass;
    }
}
