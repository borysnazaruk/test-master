package com.test.common.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDTO implements Serializable {


    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("Password")
    private String password;

    @SerializedName("Email")
    private String email;

}
