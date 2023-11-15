package com.teoriagrafos.teoriagrafos.model;

import lombok.Getter;

@Getter
public class Metadata {
    public String attribution;
    public String service;
    public long timestamp;
    public Query query;
    public Engine engine;
}
