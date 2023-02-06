package com.esliceu.PracticaObjects.model;

public class File {
    int id;
    byte[] body;
    int contentLength;
    String hash;
    int version;
    int link;

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public String getHash() {
        return hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
