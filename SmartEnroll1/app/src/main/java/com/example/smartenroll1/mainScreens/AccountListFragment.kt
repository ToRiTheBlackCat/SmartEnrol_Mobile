package com.example.smartenroll1.mainScreens

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartenroll1.databinding.FragmentInfoBinding
import com.example.smartenroll1.mainScreens.Models.AccountListViewModel
import com.example.smartenroll1.mainScreens.Singleton.NotificationRepository
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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

        viewModel.isSearchPage.value = false

        val recycler = binding.rvAccountList
        recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getChartData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.monthCount.collectLatest {
                        binding.tvMonthCount.text = "${it}"
                    }
                }
                launch {
                    viewModel.listAccount.collectLatest {
                        binding.rvAccountList.adapter =
                            MyItemRecyclerViewAdapter(it, findNavController())
                    }
                }
                launch {
                    viewModel.monthEntries.collectLatest {
                        setBarChart(it)
                    }
                }
                launch {
                    NotificationRepository.notificationReceived.collectLatest {
                        viewModel.apply {
                            getChartData()
                            fetchFromSeverFast()
                        }
                    }
                }
            }
        }

        val view = binding.root
        return view
    }

    // Setting up bar chart
    private fun setBarChart(newEntries: ArrayList<BarEntry>) {
        val entries = newEntries

        val barDataSet = BarDataSet(entries, "Registration per month")

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
                else -> "Error"
            }

            labels.add(itemLabel)
        }

        val data = BarData(barDataSet)
        data.setValueFormatter(IntegerFormatter())

        binding.chartContainer.data = data // set the data and list of labels into chart
        binding.chartContainer.axisLeft.valueFormatter = IntegerFormatter()
        binding.chartContainer.axisRight.isEnabled = false

        binding.chartContainer.xAxis.valueFormatter = IndexAxisValueFormatter(labels);
        binding.chartContainer.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartContainer.xAxis.setGranularity(1f);
        binding.chartContainer.xAxis.setCenterAxisLabels(false);

        val description = Description()
        description.text = ""
        binding.chartContainer.description = description  // set the description

        binding.chartContainer.animateY(1000)
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

        class IntegerFormatter : ValueFormatter() {
            private val mFormat = DecimalFormat("###,###,##0")

            override fun getFormattedValue(value: Float): String {
                return "" + (value.toInt())
            }
        }

    }
}

