package com.example.compareprogramming.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.compareprogramming.CodeForcesApi
import com.example.compareprogramming.ViewModelCF
import com.example.compareprogramming.databinding.FragmentCompareBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class CompareFragment : Fragment() {
    private lateinit var binding: FragmentCompareBinding
    private val navArgs: CompareFragmentArgs by navArgs()
    private val viewModel: ViewModelCF by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "You V/S " + navArgs.friend

        val it = mutableMapOf<String, MutableList<Entry>>()
        lifecycleScope.launch {
            for (handle in listOf(navArgs.friend, viewModel.currucfid.value!!)) {
                try {
                    val result = CodeForcesApi.retrofitService.getUserRating(handle).result.toList()
                    for (i in result) {
                        if (it[handle] == null) {
                            it[handle] = mutableListOf(
                                Entry(
                                    i.ratingUpdateTimeSeconds.toFloat(),
                                    i.newRating.toFloat()
                                )
                            )
                        } else {
                            it[handle]!!.add(
                                Entry(
                                    i.ratingUpdateTimeSeconds.toFloat(),
                                    i.newRating.toFloat()
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    it[""]?.add(Entry(0F, 0F))
                    e.printStackTrace()
                }
            }

            val lineDataSet1 =
                LineDataSet(it[viewModel.currucfid.value], "${viewModel.currucfid.value}'s Rating")

            val lineDataSet = LineDataSet(it[navArgs.friend], "${navArgs.friend}'s Rating")

            lineDataSet.isHighlightEnabled = true
            lineDataSet.lineWidth = 3f
            lineDataSet.circleRadius = 6f
            lineDataSet.circleHoleRadius = 3f
            lineDataSet.color = Color.RED
            lineDataSet.setDrawHighlightIndicators(true)
            lineDataSet.highLightColor = Color.RED

            lineDataSet1.isHighlightEnabled = true
            lineDataSet1.lineWidth = 3f
            lineDataSet1.circleRadius = 6f
            lineDataSet1.circleHoleRadius = 3f
            lineDataSet1.setDrawHighlightIndicators(true)
            lineDataSet1.highLightColor = Color.YELLOW
            lineDataSet.highLightColor = Color.YELLOW

            binding.linechart.animateXY(3000, 3000)

            val xAxis = binding.linechart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            class MyFormatter : ValueFormatter() {
                @SuppressLint("SimpleDateFormat")
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    val sdf = SimpleDateFormat("dd-MM-yyyy")
                    val netDate = Date(value.toLong() * 1000)
                    return sdf.format(netDate)
                }
            }
            xAxis.valueFormatter = MyFormatter()
            val data = LineData(lineDataSet, lineDataSet1)
            binding.linechart.data = data
            binding.linechart.invalidate()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompareBinding.inflate(inflater, container, false)
        viewModel.userInfo(navArgs.friend)

        binding.frindHandle.text = navArgs.friend
        binding.HandleName.text = viewModel.currucfid.value
        binding.lastOnlineCurr.text = "Last Online - ${viewModel.lastOnline.value}"
        binding.percentCorrectCurr.text =
            "Percent Correct - ${(viewModel.percentcorrect[viewModel.currucfid.value!!]?.times(100))}"
        binding.percentCorrectFriend.text =
            "Percent Correct - ${(viewModel.percentcorrect[navArgs.friend]?.times(100))}"
        viewModel.lastOnline_Friend.observe(this.viewLifecycleOwner) {
            binding.lastOnlineFriend.text = "Last Online - $it"
        }
        return binding.root
    }

}



