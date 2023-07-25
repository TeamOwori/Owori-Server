package com.owori.aspect;

@FunctionalInterface
public interface Logger {
    void log(String format, String signature, Object[] args);
}
