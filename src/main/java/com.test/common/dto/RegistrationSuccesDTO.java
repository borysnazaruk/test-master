package com.test.common.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationSuccesDTO implements Serializable {

    @SerializedName("SuccessCode")
    private String successCode;

    @SerializedName("Message")
    private  String message;
}
