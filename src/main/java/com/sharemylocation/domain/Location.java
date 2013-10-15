package com.sharemylocation.domain;

public class Location {

    private Type type;
    
    private double[] lngLat;
    
    
    public Location() {
        // TODO Auto-generated constructor stub
    }
    
    public Location(Type type , double[] lngLat) {
        this.type = type;
        this.lngLat= lngLat;
    }
    
    
    public void setLngLat(double[] lngLat) {
        this.lngLat = lngLat;
    }
    
    public double[] getLngLat() {
        return lngLat;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
}
