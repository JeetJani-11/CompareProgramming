package com.example.compareprogramming

data class User_Json(
    var status: String , var result: List<User_Data> , var comment : String?
    )

data class User_Data(
    var handle: String,
    var email: String?,
    var vkId: String?,
    var openId: String?,
    var firstName: String?,
    var lastName: String?,
    var country: String?,
    var city: String?,
    var organization: String?,
    var contribution: Int,
    var rank: String,
    var rating: Int,
    var maxRank: String,
    var maxRating: Int,
    var lastOnlineTimeSeconds: Int,
    var registrationTimeSeconds: Int,
    var friendOfCount: Int,
    var avatar: String,
    var titlePhoto: String
)