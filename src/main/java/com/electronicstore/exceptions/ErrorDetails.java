package com.electronicstore.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorDetails;
}
