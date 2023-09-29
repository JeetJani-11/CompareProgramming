package com.example.compareprogramming

data class Submission( var status : String , var result : List<SubmissionData> , var comment : String?)

data class SubmissionData (
    var id: Int,
    var contestId: Int,
    var creationTimeSeconds: Int,
    var relativeTimeSeconds: Int,
    var problem: Problem,
    var author: Party,
    var programmingLanguage: String,
    var verdict: verdict?,
    var testset: testset,
    var passedTestCount: Int ,
    var timeConsumedMillis: Int,
    var memoryConsumedBytes: Int,
    var points: Float?
        )