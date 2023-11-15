package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import lombok.Getter;

@Getter
public class Metadata {
    private String attribution;
    private String service;
    private long timestamp;
    private Query query;
    private Engine engine;
}
