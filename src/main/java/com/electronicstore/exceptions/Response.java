package com.electronicstore.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private String code;
    private String description;


    public Response(T data, String code) {
        super();
        this.data = data;
        this.code = code;
    }

    public Response(T data, String code, String description) {
        super();
        this.data = data;
        this.code = code;
        this.description = description;
    }
    public Response(String code, String description, T data) {
        super();
        this.data = data;
        this.code = code;
        this.description = description;
    }

    public Response(String code, String description) {
        super();
        this.code = code;
        this.description = description;
    }



}
