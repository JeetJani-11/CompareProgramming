package com.example.compareprogramming.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.compareprogramming.User
import com.example.compareprogramming.ViewModelCF
import com.example.compareprogramming.databinding.FragmentFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var currUser: FirebaseUser
    private val database =
        FirebaseDatabase.getInstance("https://comp-1111-default-rtdb.firebaseio.com/")
    private val viewModel: ViewModelCF by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        lifecycleScope.apply {
            viewModel.all(auth.currentUser!!.uid)
        }
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currUser = auth.currentUser!!
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Add Friends"
        val ref = database.getReference("Users")
        var frienduid = ""
        var frienduname = ""
        var friendcfid = ""
        val searchlist = ArrayList<String>()
        val listAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            viewModel.nonfriendsunameList.value!!
        )
        binding.idLVFriends.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            binding.idSV.setQuery(parent.getItemAtPosition(position).toString(), false)
            binding.idSV.clearFocus()
            viewModel.all(auth.currentUser?.uid.toString())
        }

        binding.idSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (searchlist.contains(query)) {
                    listAdapter.filter.filter(query)
                } else {
                    Toast.makeText(
                        this@FriendsFragment.requireContext(),
                        "No User Found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })


        binding.idLVFriends.adapter = listAdapter
        viewModel.allusers.observe(this.viewLifecycleOwner) {
            val allusers = viewModel.allusers.value
            if (allusers != null) {
                for (i in allusers) {
                    if (viewModel.listoffriends.value?.contains(i) == false) {
                        searchlist.add(i.uname)
                    }
                }
            }
        }

        binding.apply {
            Addfriend.setOnClickListener {
                if ((viewModel.users_uname_lists.value?.contains(binding.idSV.query.toString()) == true) && (viewModel.friendsUNAMElists.value?.contains(
                        binding.idSV.query.toString()
                    ) == false)
                ) {

                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (user in snapshot.children) {
                                if ((user.child("uname").value.toString() == binding.idSV.query.toString())) {
                                    frienduid = user.key.toString()

                                    friendcfid = user.child("cfid").value.toString()
                                    frienduname = user.child("uname").value.toString()

                                    val refoflist = ref.child(frienduid).child("fl").push()
                                    refoflist.setValue(
                                        User(
                                            viewModel.curruname.value.toString(),
                                            viewModel.currucfid.value.toString()
                                        )
                                    )
                                    val refoflist_ = ref.child(currUser.uid).child("fl").push()
                                    refoflist_.setValue(User(frienduname, friendcfid))
                                    viewModel.friendsCFIDList.value?.add(friendcfid)
                                    viewModel.friendsUNAMElists.value?.add(frienduname)
                                    viewModel.friendsList.value?.add(User(frienduname, friendcfid))
                                    viewModel.nonfriendsList.value?.remove(
                                        User(
                                            frienduname,
                                            friendcfid
                                        )
                                    )
                                    viewModel.nonfriendsunameList.value!!.remove(frienduname)
                                    binding.idSV.setQuery("", true)
                                    binding.idLVFriends
                                    val listadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                        requireContext(),
                                        android.R.layout.simple_list_item_1,
                                        viewModel.nonfriendsunameList.value!!
                                    )
                                    viewModel.all(auth.currentUser!!.uid)
                                    binding.idLVFriends.adapter = listadapter
                                    break
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }

            }

        }


    }
}

