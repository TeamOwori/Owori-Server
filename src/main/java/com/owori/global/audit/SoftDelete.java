package com.owori.global.audit;

import org.hibernate.annotations.Where;

import javax.persistence.EntityListeners;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target(TYPE)
@Retention(RUNTIME)
@EntityListeners(AuditListener.class)
@Where(clause = "deleted_at is null")
public @interface SoftDelete {}
