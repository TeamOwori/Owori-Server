package com.owori.global.converter;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;

/*
 *  Sort 객체를 QueryDSL의 OrderSpecifier 배열로 변환하는 인터페이스입니다.
 * */
public interface OrderConverter {
    OrderSpecifier<?>[] convert(Sort sort);
}