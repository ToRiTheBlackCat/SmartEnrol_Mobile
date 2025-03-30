package com.example.smartenroll1

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartenroll1.databinding.FragmentChartBinding
import com.example.smartenroll1.mainScreens.Models.ChartViewModel
import com.github.mikephil.charting.components.Description

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*
import java.util.ArrayList

class ChartFragment : Fragment() {

    companion object {
        fun newInstance() = ChartFragment()
    }

    private val viewModel: ChartViewModel by viewModels()
    private lateinit var binding: FragmentChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentChartBinding.inflate(layoutInflater)

        setBarChart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_chart, container, false)
        return binding.root
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 4f))
        entries.add(BarEntry(1f, 10f))
        entries.add(BarEntry(2f, 30f))
        entries.add(BarEntry(3f, 15f))
        entries.add(BarEntry(4f, 24f))
        entries.add(BarEntry(5f, 50f))

        val barDataSet = BarDataSet(entries, "Registation")

        val labels = ArrayList<String>()
        labels.add("Sun");
        labels.add("Mon");
        labels.add("Tue");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");

        binding.chartContainer.xAxis.valueFormatter = IndexAxisValueFormatter(labels);

        val data = BarData(barDataSet)
        binding.chartContainer.data = data // set the data and list of lables into chart

        val description = Description()
        description.text = "Set Bar Chart Description"

        binding.chartContainer.setDescription(description)  // set the description

        binding.chartContainer.animateY(5000)
    }

}

