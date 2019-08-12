package com.test.common.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherFaultResponseDTO implements Serializable {

    @SerializedName("FaultId")
    private String faultId;

    @SerializedName("fault")
    private String fault;
}
