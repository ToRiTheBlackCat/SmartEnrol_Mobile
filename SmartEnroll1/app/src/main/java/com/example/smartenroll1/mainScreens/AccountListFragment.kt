package com.example.smartenroll1.mainScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.smartenroll1.mainScreens.Models.AccountListViewModel
import com.example.smartenroll1.databinding.FragmentInfoBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 * A fragment representing a list of Items.
 */
class AccountListFragment : Fragment() {
    private var columnCount = 1
    private lateinit var binding: FragmentInfoBinding
    private val viewModel: AccountListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentInfoBinding.inflate(layoutInflater)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        viewModel.isSearchPage.value = true

        val recycler = binding.rvAccountList
        recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.monthCount.collectLatest {
                        binding.tvMonthCount.text = "${it}"
                    }
                }
                launch {
                    viewModel.listAccount.collectLatest {
                        recycler.adapter = MyItemRecyclerViewAdapter(it, findNavController())
                    }
                }
                launch {
                    viewModel.monthEntries.collectLatest {
                        setBarChart(it)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = binding.root
        return view

//        val recycler = binding.rvAccountList
//        recycler.layoutManager = LinearLayoutManager(requireContext())
//        getAllAccounts { accounts ->
//            recycler.adapter = MyItemRecyclerViewAdapter(accounts)
//        }
    }

    private fun setBarChart(newEntries: ArrayList<BarEntry>) {
//        val entries = ArrayList<BarEntry>()
//        entries.add(BarEntry(0f, 4f))
//        entries.add(BarEntry(1f, 10f))
//        entries.add(BarEntry(2f, 30f))
//        entries.add(BarEntry(3f, 15f))
//        entries.add(BarEntry(4f, 24f))
//        entries.add(BarEntry(5f, 50f))
        val entries = newEntries

        val barDataSet = BarDataSet(entries, "Registation")

        val labels = ArrayList<String>()
        for (entry in entries) {
            val itemLabel = when (entry.data) {
                1 -> "JAN"
                2 -> "FEB"
                3 -> "MAR"
                4 -> "APR"
                5 -> "MAY"
                6 -> "JUN"
                7 -> "JUL"
                8 -> "AUG"
                9 -> "SEP"
                10 -> "OCT"
                11 -> "NOV"
                12 -> "DEC"
                else -> "None"
            }

            labels.add(itemLabel)
        }
//        labels.add("Sun");
//        labels.add("Mon");
//        labels.add("Tue");
//        labels.add("Wed");
//        labels.add("Thu");
//        labels.add("Fri");
//        labels.add("Sat");

        val data = BarData(barDataSet)
        binding.chartContainer.data = data // set the data and list of lables into chart

        binding.chartContainer.xAxis.valueFormatter = IndexAxisValueFormatter(labels);
        binding.chartContainer.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartContainer.xAxis.setGranularity(1f);
        binding.chartContainer.xAxis.setCenterAxisLabels(true);

//        binding.chartContainer.xAxis.setAxisMinimum(data.xMin - .5f)
//        binding.chartContainer.xAxis.setAxisMaximum(data.xMax + .5f)
//        binding.chartContainer.xAxis.setLabelCount(12)


        val description = Description()
        description.text = ""

        binding.chartContainer.setDescription(description)  // set the description

        binding.chartContainer.animateY(5000)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            AccountListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

