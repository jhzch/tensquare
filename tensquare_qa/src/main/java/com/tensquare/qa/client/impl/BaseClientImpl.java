package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.BaseClient;
import entity.Result;
import entity.StatusCode;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BaseClientImpl implements FallbackFactory<BaseClient> {
    @Override
    public BaseClient create(Throwable throwable) {
        return new BaseClient() {
            @Override
            public Result findById(String id) {
                return new Result(false,StatusCode.ERROR.getCode(),throwable.getMessage());
            }
        };
    }
//    @Override
//    public Result findById(String id) {
//        return new Result(false, StatusCode.ERROR.getCode(), "熔断器触发了");
//    }
}
