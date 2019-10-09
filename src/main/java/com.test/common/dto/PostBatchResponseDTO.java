package com.test.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostBatchResponseDTO implements Serializable {

    private String status;
    private String order;

    public String getStatus() {
        return status;
    }

    public String getOrder() {
        return order;
    }
}
