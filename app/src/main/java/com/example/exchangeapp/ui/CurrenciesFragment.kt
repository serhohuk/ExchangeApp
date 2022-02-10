package com.example.exchangeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.R
import com.example.exchangeapp.adapter.ExchangeAdapter
import com.example.exchangeapp.utils.Resource
import com.example.exchangeapp.viewmodel.AppViewModel

class CurrenciesFragment : Fragment() {

    private lateinit var adapter : ExchangeAdapter
    private  val viewModel : AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_currencies, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        adapter = ExchangeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)


        viewModel.currenciesListData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Succes->{
                    it.data?.let { it1 -> adapter.list.addAll(it1)
                        adapter.notifyDataSetChanged()} }
                is Resource.Error->{

                }
                is Resource.Loading->{
                    Toast.makeText(requireContext(),"loading", Toast.LENGTH_SHORT).show()
                }
            }

        })

        viewModel.getCurrenciesList()


        return view
    }


}