package com.atalaykaan.e_commerce_backend.service;

import java.util.function.Consumer;

public abstract class BaseService {

    protected <T> void updateIfExists(T value, Consumer<T> setter) {

        if(value != null) {

            setter.accept(value);
        }
    }
}
