package com.test.common.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherDTO implements Serializable {

    @SerializedName("City")
    private String city;

    public String getCity() {
        return city;
    }

    @SerializedName("Temperature")
    private String temperature;

    @SerializedName("Humidity")
    private String humidity;

    @SerializedName("WeatherDescription")
    private String weatherDescription;

    @SerializedName("WindSpeed")
    private String windSpeed;

    @SerializedName("WindDirectionDegree")
    private String windDirectionDegree;


}
