package com.baoying.enginex.executor.engine.thread;

import com.baoying.enginex.executor.common.basefactory.CustomBeanFactory;
import com.baoying.enginex.executor.engine.service.EngineApiService;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.Callable;

public class EngineCallable implements Callable<String> {

    private Map<String, Object> paramJson;

    public EngineCallable(Map<String, Object> paramJson){
        this.paramJson = paramJson;
    }

    @Override
    public String call() {
        ApplicationContext context = CustomBeanFactory.getContext();
        EngineApiService engineApiService = (EngineApiService) context.getBean("engineApiServiceImpl");
        String result = engineApiService.engineApi(paramJson);
        return result;
    }
}
