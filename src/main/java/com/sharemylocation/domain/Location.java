package com.sharemylocation.domain;

public class Location {

    private final Type type;
    
    private final double[] lngLat;
    
    public Location(Type type , double[] lngLat) {
        this.type = type;
        this.lngLat= lngLat;
    }
    
    public double[] getLngLat() {
        return lngLat;
    }
    
    public Type getType() {
        return type;
    }
}
