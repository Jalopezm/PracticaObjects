package com.esliceu.PracticaObjects.model;

public class Bucket {
    int id;
    String uri;
    int idOwner;
    String metaData;

    public Bucket(int id, String uri, int idOwner) {
        this.id = id;
        this.uri = uri;
        this.idOwner = idOwner;
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

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }
}
