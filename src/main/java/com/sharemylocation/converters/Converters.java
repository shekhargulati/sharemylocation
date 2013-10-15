package com.sharemylocation.converters;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class Converters {

    @Inject
    private Instance<Converter<?>> converters;
    
    public Converter<?> converter(String type){
        for (Converter<?> converter : converters) {
            if(converter.getName().equals(type)){
                return converter;
            }
        }
        throw new RuntimeException("No suitable converter found");
        
    }
}
