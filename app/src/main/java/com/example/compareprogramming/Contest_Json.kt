package com.example.compareprogramming

data class Contest_Json(
    var status : String , var result : List<Contest_Json_data> , var comment : String?
)
data class Contest_Json_data (
    val id: Int,
    val name: String ,
    val type: type_c,
    val phase: phase,
    val frozen: Boolean,
    val durationSeconds: Int,
    val startTimeSeconds: Int?,
    val relativeTimeSeconds: Int?,
    val preparedBy: String?,
    val websiteUrl: String?,
    val description: String? ,
    val difficulty: Int?,
    val kind: String?,
    val icpcRegion:  String?,
    val country:  String?,
    val city:  String?,
    val season: String?
)
