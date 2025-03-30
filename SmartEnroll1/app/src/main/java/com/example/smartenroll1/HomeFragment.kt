package com.example.smartenroll1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.smartenroll1.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHomeBinding

    override fun onResume() {
        super.onResume()

        getAllComments()
        val pages = resources.getStringArray(R.array.HomePages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, pages)
        binding.acvPageSelectHome.setText(pages[0])
        binding.acvPageSelectHome.setAdapter(arrayAdapter)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.acvPageSelectHome.setOnItemClickListener { parent, view, position, id ->
            HomePageChange(binding.acvPageSelectHome.text.toString())
        }


    }

    private fun HomePageChange(pageText: String?) {
        if (pageText == null)
            return

        val pageStrings = resources.getStringArray(R.array.HomePages)
        val selectedPage = when (pageText) {
            pageStrings[0] -> R.id.homeFragment
            pageStrings[1] -> R.id.chatFragment
            pageStrings[2] -> R.id.infoFragmentNav
            pageStrings[3] -> R.id.homeFragment
            pageStrings[4] -> R.id.infoFragmentNav

            else -> return
        }
        val navController = findNavController()
        navController.navigate(selectedPage)
    }


    private val BASE_URL = "https://jsonplaceholder.typicode.com"
    private val TAG: String = "CHECK_RESPONSE"

    private fun getAllComments() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SmartEnrolApi::class.java)

        val apiTest = api.getComments().enqueue(object : Callback<List<Comments>> {
            override fun onResponse(
                call: Call<List<Comments>>,
                response: Response<List<Comments>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (comment in it) {
                            Log.i(TAG, "On response: ${comment.body}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Comments>>, throwable: Throwable) {
                val test = throwable
                Log.i(TAG, "On response: ${throwable.stackTrace}")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, binding.root, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}