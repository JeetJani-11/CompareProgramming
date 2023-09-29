package com.example.compareprogramming

data class Party(
    var contestId: Int?,
    var members: MutableList<Member>,
    var participantType: Participantypeenum,
    var teamId: Int?,
    var teamName: String?,
    var ghost: Boolean,
    var room: Int?,
    var startTimeSeconds: Int?
)

