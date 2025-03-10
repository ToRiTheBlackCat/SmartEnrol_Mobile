package com.example.smartenroll1.MainScreens

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.smartenroll1.MainScreens.Models.AccountItemModel

import com.example.smartenroll1.R
import com.example.smartenroll1.databinding.AccountInfoBinding
import java.util.Locale

class MyItemRecyclerViewAdapter(
    private val values: List<AccountItemModel>,
    private val navController: NavController,
    private val fragmentContext: AccountListFragment
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.AccountInfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountInfoHolder {
        return AccountInfoHolder(
            AccountInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountInfoHolder, position: Int) {
        val item = values[position]
        holder.apply {
            nameView.text = item.accountName
            areaView.text = String.format(Locale.getDefault(), "%d", item.areaId)
            emailView.text = item.email
            joinDateView.text = item.createdDate
            cardView.setOnClickListener {
                Log.i("ACCOUNT_CLICK", "Clicked on: ${item.accountName}")

                val bundle = bundleOf()
                bundle.putString("accountId", item.accountId.toString())
                bundle.putString("accountName", item.accountName)
                bundle.putString("createdDate", item.createdDate)
                bundle.putString("email", item.email)
                bundle.putString("areaId", item.areaId.toString())

                navController.navigate(R.id.accountInfoDetail, bundle)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class AccountInfoHolder(binding: AccountInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.textName
        val areaView: TextView = binding.textArea
        val emailView: TextView = binding.textEmail
        val joinDateView: TextView = binding.textJoinDate
        val cardView: CardView = binding.accountContainer
    }

}