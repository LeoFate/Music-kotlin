package com.example.admin.music.data

data class SongData(
    val `data`: List<Data>,
    val code: Int
)

data class Data(
    val br: Int,
    val canExtend: Boolean,
    val code: Int,
    val encodeType: String,
    val expi: Int,
    val fee: Int,
    val flag: Int,
    val freeTrialInfo: Any,
    val gain: Double,
    val id: Int,
    val level: String,
    val md5: String,
    val payed: Int,
    val size: Int,
    val type: String,
    val uf: Any,
    val url: String
)

data class SongCheck(
    val message: String,
    val success: Boolean
)
