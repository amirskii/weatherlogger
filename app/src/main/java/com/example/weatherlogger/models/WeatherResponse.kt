package com.example.weatherlogger.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("coord") val coord : Coord,
    @SerializedName("weather") val weather : List<WeatherData>,
    @SerializedName("base") val base : String,
    @SerializedName("main") val main : Main,
    @SerializedName("visibility") val visibility : Int,
    @SerializedName("wind") val wind : Wind,
    @SerializedName("clouds") val clouds : Clouds,
    @SerializedName("dt") val dt : Int,
    @SerializedName("sys") val sys : Sys,
    @SerializedName("timezone") val timezone : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("cod") val cod : Int
)

data class Coord (
    @SerializedName("lon") val lon : Double,
    @SerializedName("lat") val lat : Double
)

data class WeatherData (
    @SerializedName("id") val id : Int,
    @SerializedName("main") val main : String,
    @SerializedName("description") val description : String,
    @SerializedName("icon") val icon : String
)

data class Main (
    @SerializedName("temp") val temp : Int,
    @SerializedName("pressure") val pressure : Int,
    @SerializedName("humidity") val humidity : Int,
    @SerializedName("temp_min") val temp_min : Int,
    @SerializedName("temp_max") val temp_max : Int
)

data class Clouds (
    @SerializedName("all") val all : Int
)

data class Sys (
    @SerializedName("type") val type : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("country") val country : String,
    @SerializedName("sunrise") val sunrise : Int,
    @SerializedName("sunset") val sunset : Int
)

data class Wind (
    @SerializedName("speed") val speed : Int,
    @SerializedName("deg") val deg : Int
)