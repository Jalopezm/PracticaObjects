package com.esliceu.PracticaObjects.model;

public class Bucket {
    int id;
    String uri;
    String owner;
    public Bucket(int id, String uri, String owner) {
        this.id = id;
        this.uri = uri;
        this.owner = owner;
    }
    public Bucket() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getOwner() {
        return owner;
    }
}
