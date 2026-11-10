package com.example.myapp.mymodel

data class Country (
    val name:String,
    val capital:String?,
    val region:String?,
    val population:Long?,
    val flags:Flags
)

data class Flags(val png:String)