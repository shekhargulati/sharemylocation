package com.sharemylocation.domain;

public enum Type {

    POINT("Point"), LINE_STRING("LineString"), POLYGON("Polygon");

    private String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
