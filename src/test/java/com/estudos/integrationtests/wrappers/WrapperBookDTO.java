package com.estudos.integrationtests.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperBookDTO implements Serializable {
    @JsonProperty("_embedded")
    private BookEmbeddedVO embedded;

    public WrapperBookDTO() {
    }

    public BookEmbeddedVO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(BookEmbeddedVO embedded) {
        this.embedded = embedded;
    }
}
