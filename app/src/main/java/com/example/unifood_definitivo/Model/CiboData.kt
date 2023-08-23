package com.example.unifood_definitivo.Model

class CiboData {
    var nomeCibo: String? = null
    var imgCibo: String? = null
    constructor(){}

    constructor(
        nomeCat: String?,
        imgCat: String?
    ){
        this.nomeCibo = nomeCat
        this.imgCibo = imgCat
    }
}