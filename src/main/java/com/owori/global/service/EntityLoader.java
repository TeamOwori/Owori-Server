package com.owori.global.service;

public interface EntityLoader<T, ID> {
    T loadEntity(ID id);
}
