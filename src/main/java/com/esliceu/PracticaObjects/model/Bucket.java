package com.esliceu.PracticaObjects.model;

public class Bucket {
    int id;
    String uri;
    String Owner;
    String metaData;

    public Bucket(int id, String uri, String Owner) {
        this.id = id;
        this.uri = uri;
        this.Owner = Owner;
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
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }
}
