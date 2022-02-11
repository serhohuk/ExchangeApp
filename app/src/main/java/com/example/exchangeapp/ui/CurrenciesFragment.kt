package com.example.exchangeapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.R
import com.example.exchangeapp.adapter.ExchangeAdapter
import com.example.exchangeapp.data.Currency
import com.example.exchangeapp.utils.Resource
import com.example.exchangeapp.viewmodel.AppViewModel
import com.example.exchangeapp.viewmodel.AppViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CurrenciesFragment : Fragment() {

    private lateinit var adapter : ExchangeAdapter
    private  lateinit var viewModel : AppViewModel
    private lateinit var viewModelFactory : AppViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressbar : View
    private lateinit var etSearch : TextInputLayout
    private lateinit var clearText : View
    private lateinit var job : Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_currencies, container, false)

        recyclerView = view.findViewById(R.id.recycler)
        progressbar = view.findViewById(R.id.progress_bar)
        etSearch = view.findViewById(R.id.search)
        clearText = view.findViewById(R.id.iv_clear)

        job = Job()

        initViewModel()
        initRecyclerView()

        etSearch.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()){
                    clearText.visibility = GONE
                } else clearText.visibility = VISIBLE
                job.cancel()
                job = lifecycleScope.launch {
                    delay(500)
                    filterItems(s.toString())
                }
            }

        })

        adapter.setOnClickListener {
            val action = CurrenciesFragmentDirections.actionCurrenciesFragmentToResultFragment(it)
            findNavController().navigate(action)
        }

        clearText.setOnClickListener {
            etSearch.editText?.setText("")
        }

        viewModel.getCurrenciesList()
        viewModel.currenciesListData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Succes->{
                    progressbar.visibility = GONE
                    it.data?.let { it1 ->
                        adapter.list.addAll(it1)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error->{
                    progressbar.visibility = GONE
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading->{
                    progressbar.visibility = VISIBLE
                }
            }

        })

        return view
    }

    private fun filterItems(string : String){
        val list = ArrayList<Currency>()
        for (item in viewModel.currenciesListData.value?.data!!) {
            if (item.currencyName.toLowerCase(Locale.getDefault()).contains(string.toLowerCase(Locale.getDefault()))
                || item.id.toLowerCase(Locale.getDefault()).contains(string.toLowerCase(Locale.getDefault()))) {
                list.add(item)
            }
        }
        adapter.list.clear()
        adapter.list.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun initViewModel(){
        viewModelFactory = AppViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel::class.java)
    }

    private fun initRecyclerView(){
        adapter = ExchangeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
    }


}