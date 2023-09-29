package com.example.compareprogramming.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.compareprogramming.adapters.QuestionAdapter
import com.example.compareprogramming.R
import com.example.compareprogramming.ViewModelCF
import com.example.compareprogramming.adapters.ContestsAdapter
import com.example.compareprogramming.adapters.FriendsAdapter
import com.example.compareprogramming.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private val database =
        FirebaseDatabase.getInstance("https://comp-1111-default-rtdb.firebaseio.com/")
    private var curruuname = ""
    private var curucfid = ""
    private val viewModel: ViewModelCF by activityViewModels()
    var ref = database.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {

        val content: View = requireActivity().findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isokay()) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        auth = Firebase.auth
        viewModel.curruuid.value = auth.currentUser?.uid

        lifecycle.coroutineScope.launch {
            viewModel.upcomingContest()
            ref.child(auth.currentUser?.uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        curucfid = snapshot.child("cfid").value.toString()
                        curruuname = snapshot.child("uname").value.toString()
                        viewModel.curruname.value = curruuname
                        viewModel.userInfo(curucfid)
                        viewModel.currucfid.value = curucfid
                        viewModel.all(auth.currentUser?.uid.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Compare Programming"
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.Friends_bn -> {
                    var change = findNavController().navigate(R.id.action_home2_to_friends)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    viewModel.friendsCFIDList.value?.clear()
                    viewModel.friends.value = listOf()
                    viewModel.question.value = listOf()
                    viewModel.question_1.value = mutableListOf()
                    var logout = Firebase.auth.signOut()
                    var change = findNavController().navigate(R.id.action_home2_to_start2)
                    return@setOnItemSelectedListener true
                }
            }
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.friendsCFIDList_.observe(this.viewLifecycleOwner) {
            viewModel.getQuestions(it.toList())
        }
        viewModel.UserRating.observe(this.viewLifecycleOwner) {
            binding.Username.text = viewModel.curruname.value.toString()
            binding.Rating.text = "Rating - ${viewModel.UserRating.value.toString()}"
        }
        viewModel.lastOnline.observe(this.viewLifecycleOwner) {
            binding.lastOnline.text = "Last Online - $it"
        }


        val adapter = QuestionAdapter(requireContext())
        val adapter1 = ContestsAdapter()
        val adapter2 = FriendsAdapter {
            val action = HomeFragmentDirections.actionHome2ToCompareFragment(it)
            findNavController().navigate(action)
        }
        binding.QuestionRecycler.adapter = adapter
        binding.contestRecycler.adapter = adapter1
        binding.FriendsRecycler.adapter = adapter2

        adapter1.submitList(null)
        adapter.submitList(null)
        adapter2.submitList(null)
        viewModel.friends.observe(this.viewLifecycleOwner) {
            val submit = it
            if (it.isEmpty()) {
                binding.FriendsRecycler.visibility = View.GONE
                binding.NoFriendsTextview.visibility = View.VISIBLE
                binding.NoFriendsTextview.text = "No Friends :("
                if (viewModel.question_.value?.isEmpty() == true) {
                    binding.QuestionRecycler.visibility = View.GONE
                    binding.MakeFriendsTextview.visibility = View.VISIBLE
                    binding.MakeFriendsTextview.text = "MakeFriends First ;)"
                }
            } else {
                binding.FriendsRecycler.visibility = View.VISIBLE
                binding.NoFriendsTextview.visibility = View.GONE
                if (viewModel.question_.value?.isEmpty() == true) {
                    binding.QuestionRecycler.visibility = View.GONE
                    binding.MakeFriendsTextview.visibility = View.VISIBLE
                    binding.MakeFriendsTextview.text = "Error in Fetching Data"
                }
                adapter2.submitList(listOf())
                adapter2.submitList(submit)
            }
        }
        viewModel.upcomingContest.observe(this.viewLifecycleOwner) {
            val submit = it.toList().sortedBy { i -> i.time }
            adapter1.submitList(listOf())
            adapter1.submitList(submit)
        }
        viewModel.question_.observe(viewLifecycleOwner) {
            val submit = it.toList().sortedByDescending { i -> i.time }
            if (it.isNotEmpty()) {
                binding.QuestionRecycler.visibility = View.VISIBLE
                binding.MakeFriendsTextview.visibility = View.GONE
                adapter.submitList(listOf())
                adapter.submitList(submit)

            }
        }
    }
}




