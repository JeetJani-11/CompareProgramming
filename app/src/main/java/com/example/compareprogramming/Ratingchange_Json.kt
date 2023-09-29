package com.example.compareprogramming
data class Ratingchange_Json( var status : String , var result : List<Ratingchange_Data> , var comment : String?)

data class Ratingchange_Data(
    var contestId: Int,
    var contestName: String,
    var handle: String,
    var rank: Int ,
    var ratingUpdateTimeSeconds: Int,
    var oldRating: Int,
    var newRating: Int
)
