package com.star.web.controller;

import com.star.web.result.BasicResult;
import org.springframework.http.HttpStatus;

/**
 * @author XuJ
 */
public abstract class BasicController {
    protected BasicResult buildDefaultObject() {
        return BasicResult.builder()
                .status(true).code(String.valueOf(HttpStatus.OK.value()))
                .build();
    }

    protected  BasicResult buildDefaultObject(Object obj) {
        return BasicResult.builder()
                .status(true).code(String.valueOf(HttpStatus.OK.value()))
                .data(obj)
                .build();
    }
}
