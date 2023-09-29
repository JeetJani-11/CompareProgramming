package com.example.compareprogramming

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


import kotlin.Exception

class ViewModelCF : ViewModel() {
    private val allusers_ = MutableLiveData(mutableListOf(User(" ", "")))
    val allusers: LiveData<MutableList<User>> = allusers_

    private val database =
        FirebaseDatabase.getInstance("https://comp-1111-default-rtdb.firebaseio.com/")
    var ref = database.getReference("Users")

    val que_wt = mutableListOf<Question>()
    var question = MutableLiveData(listOf<Question>())
    var question_: LiveData<List<Question>> = question
    var question_1 = MutableLiveData(mutableListOf<Question>())

    val listoffriends = MutableLiveData(mutableListOf(User("", " ")))

    val curruname: MutableLiveData<String> = MutableLiveData("")
    var currucfid: MutableLiveData<String> = MutableLiveData("")
    var curruuid = MutableLiveData<String>(" ")

    private val friendsList_ = MutableLiveData(mutableListOf<User>())
    var friends = MutableLiveData(listOf<User>())
    var friendsList: LiveData<MutableList<User>> = friendsList_

    val friendsCFIDList_ = MutableLiveData(mutableListOf<String>())
    var friendsCFIDList: LiveData<MutableList<String>> = friendsCFIDList_

    private val friendsUNAMElists_ = MutableLiveData(mutableListOf<String>())
    var friendsUNAMElists: LiveData<MutableList<String>> = friendsUNAMElists_

    private val users_uname_lists_ = MutableLiveData(mutableListOf<String>())
    var users_uname_lists: LiveData<MutableList<String>> = users_uname_lists_

    private val nonfriendsList_ = MutableLiveData(mutableListOf<User>())
    var nonfriendsList: LiveData<MutableList<User>> = nonfriendsList_

    private val nonfriendsunameList_ = MutableLiveData(mutableListOf<String>())
    var nonfriendsunameList: LiveData<MutableList<String>> = nonfriendsunameList_

    var UserRating: MutableLiveData<String> = MutableLiveData()
    var lastOnline: MutableLiveData<String> = MutableLiveData()

    var UserRating_Friend: MutableLiveData<String> = MutableLiveData()
    var lastOnline_Friend: MutableLiveData<String> = MutableLiveData()
    var friendprofile = MutableLiveData<User_Data>()

    private var uc_ = MutableLiveData(mutableListOf<Contest>())
    private var upcomingContest_ = MutableLiveData(listOf<Contest>())
    var upcomingContest: LiveData<List<Contest>> = upcomingContest_

    var cuursuprofile = MutableLiveData<User_Data>()

    var percentcorrect = mutableMapOf<String, Float>()

    private var num: Int = 0

    fun getQuestions(handles: List<String>) {
        viewModelScope.launch {
            num = 0
            try {
                percentcorrect.clear()
                question_1.value?.clear()
                for (i in handles) {
                    num++
                    val result = CodeForcesApi.retrofitService.getUserStatus(i).result
                    var count = 0
                    que_wt.clear()
                    for (submission in result) {
                        if (submission.verdict == verdict.OK) {
                            que_wt.add(
                                Question(
                                    submission.problem.name,
                                    "https://codeforces.com/problemset/problem/${submission.problem.contestId}/${submission.problem.index}",
                                    i, submission.creationTimeSeconds
                                )
                            )
                            count++
                        }

                    }
                    percentcorrect[i] = count.toFloat() / result.size.toFloat()

                    for (que in que_wt) {
                        if (question_1.value?.contains(
                                Question(
                                    que.name,
                                    que.url,
                                    i,
                                    que.time
                                )
                            ) == false
                        ) {
                            question_1.value?.add((Question(que.name, que.url, i, que.time)))
                        }
                    }

                }
                val result = CodeForcesApi.retrofitService.getUserStatus(currucfid.value!!).result
                var count = 0
                for (submission in result) {
                    if (submission.verdict == verdict.OK) {
                        count++
                    }
                }
                percentcorrect[currucfid.value!!] = count.toFloat() / result.size.toFloat()
                question.value = question_1.value!!.toList()
            } catch (e: Exception) {
                num = friends.value?.size!!
                e.printStackTrace()
            }
        }
    }

    fun all(uid: String) {
        viewModelScope.launch {
            ref.child(uid).child("fl").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    friendsList_.value?.clear()
                    friendsCFIDList_.value?.clear()
                    friendsUNAMElists_.value?.clear()
                    for (i in snapshot.children) {
                        friendsList_.value?.add(
                            User(
                                i.child("uname").value.toString(),
                                i.child("handle").value.toString()
                            )
                        )
                        friendsCFIDList_.value?.add(i.child("handle").value.toString())
                        friendsUNAMElists_.value?.add(i.child("uname").value.toString())
                    }
                    friendsCFIDList.value?.let {
                        getQuestions(it.toList())
                    }
                    friends.value = friendsList_.value

                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            allusers_.value?.clear()
                            nonfriendsList_.value?.clear()
                            users_uname_lists_.value?.clear()
                            nonfriendsunameList_.value?.clear()
                            for (huh in snapshot.children) {
                                allusers_.value?.add(
                                    User(
                                        huh.child("uname").value.toString(),
                                        huh.child("cfid").value.toString()
                                    )
                                )
                                users_uname_lists_.value?.add(huh.child("uname").value.toString())
                                if ((friendsList_.value?.contains(
                                        User(
                                            huh.child("uname").value.toString(),
                                            huh.child("cfid").value.toString()
                                        )
                                    ) == false) && (User(
                                        curruname.value.toString(),
                                        currucfid.value.toString()
                                    ) != User(
                                        huh.child("uname").value.toString(),
                                        huh.child("cfid").value.toString()
                                    ))
                                ) {
                                    nonfriendsList_.value?.add(
                                        User(
                                            huh.child("uname").value.toString(),
                                            huh.child("cfid").value.toString()
                                        )
                                    )
                                    nonfriendsunameList_.value?.add(huh.child("uname").value.toString())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })

                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    fun userInfo(handle: String) {
        viewModelScope.launch {
            try {
                val UserInfo = CodeForcesApi.retrofitService.getUser(handle).result
                if (handle == currucfid.value) {
                    cuursuprofile.value = UserInfo[0]
                    UserRating.value =
                        UserInfo[0].rating.toString() + " (Max - " + UserInfo[0].maxRating.toString() + " )"
                    lastOnline.value = getDateTime(
                        UserInfo[0].lastOnlineTimeSeconds.toString(),
                        "dd-MM-yyyy HH:mm:ss"
                    )
                } else {
                    friendprofile.value = UserInfo[0]
                    UserRating_Friend.value =
                        UserInfo[0].rating.toString() + " (Max - " + UserInfo[0].maxRating.toString() + " )"
                    lastOnline_Friend.value = getDateTime(
                        UserInfo[0].lastOnlineTimeSeconds.toString(),
                        "dd-MM-yyyy H:mm:ss"
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isokay(): Boolean {


        return ((num == (((friends.value?.size)) ?: false)) && (upcomingContest_.value?.isNotEmpty() ?: false) && (!lastOnline.value.isNullOrEmpty()))
    }

    fun upcomingContest() {
        viewModelScope.launch {
            try {

                val it = CodeForcesApi.retrofitService.getContest(false).result
                uc_.value?.clear()
                for (i in it) {
                    if (i.phase == phase.BEFORE) {
                        uc_.value?.add(
                            Contest(
                                i.name,
                                getDateTime(i.startTimeSeconds.toString(), "dd-MM-yyyy")!!,
                                i.startTimeSeconds!!
                            )
                        )
                    }
                }
                upcomingContest_.value = uc_.value

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String, pattern: String): String? {
        return try {
            val sdf = SimpleDateFormat(pattern)
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

}



