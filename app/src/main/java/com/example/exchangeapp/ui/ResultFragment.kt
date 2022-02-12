package com.example.exchangeapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.R
import com.example.exchangeapp.adapter.ExchangeAdapter
import com.example.exchangeapp.application.MyApp
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

class ResultFragment : Fragment() {

    private lateinit var adapter : ExchangeAdapter
    private  lateinit var viewModel : AppViewModel
    private lateinit var viewModelFactory : AppViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressbar : View
    private lateinit var etSearch : TextInputLayout
    private lateinit var clearText : View
    private lateinit var job : Job

    private val args : ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        recyclerView = view.findViewById(R.id.recycler)
        progressbar = view.findViewById(R.id.progress_bar)
        etSearch = view.findViewById(R.id.search)
        clearText = view.findViewById(R.id.iv_clear)

        job = Job()
        initViewModel()
        initRecyclerView()

        adapter.setOnClickListener {
            if (MyApp.instance.checkForInternet(requireContext())){
                viewModel.insertCurrency(it)
                viewModel.getExchangeCourse("${args.argCurrency.id}_${it.id}")
            } else {
                viewModel.readAllExchangeCurrency.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result->
                    var isDataShown = false
                    for (item in result!!){
                        if(item.to == it.id){
                            isDataShown = true
                            Toast.makeText(requireContext(), "${item.fr} to ${item.to} -> ${item.`val`}",Toast.LENGTH_LONG).show()
                        }
                    }
                    if (!isDataShown) Toast.makeText(requireContext(),"This data wasn't saved", Toast.LENGTH_SHORT).show()
                })
            }
        }

        clearText.setOnClickListener {
            etSearch.editText?.setText("")
        }

        viewModel.exchangeData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Resource.Succes ->{

                    Toast.makeText(requireContext(), "${it.data?.fr} to ${it.data?.to} -> ${it.data?.`val`}",Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message,Toast.LENGTH_LONG).show()
                }
            }
        })

        Toast.makeText(requireContext(), "Please long tap to see currency course", Toast.LENGTH_LONG).show()

        etSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()){
                    clearText.visibility = View.GONE
                } else clearText.visibility = View.VISIBLE
                job.cancel()
                job = lifecycleScope.launch {
                    delay(500)
                    filterItems(s.toString())
                }
            }

        })

        initData()
        viewModel.currenciesListData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Resource.Succes->{
                    progressbar.visibility = View.GONE
                    it.data?.let { it1 ->
                        adapter.list.addAll(itemsToPair(it1))
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error->{
                    progressbar.visibility = View.GONE
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading->{
                    progressbar.visibility = View.VISIBLE
                }
            }

        })


        return view
    }

    private fun initViewModel(){
        viewModelFactory = AppViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel::class.java)
    }

    private fun initRecyclerView(){
        adapter = ExchangeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun filterItems(string : String){
        val list = ArrayList<Pair<Currency,Currency>>()
        for (item in viewModel.currenciesListData.value?.data!!) {
            if (item.currencyName.toLowerCase(Locale.getDefault()).contains(string.toLowerCase(
                    Locale.getDefault()))
                || item.id.toLowerCase(Locale.getDefault()).contains(string.toLowerCase(Locale.getDefault()))) {
                list.add(Pair(args.argCurrency, item))
            }
        }
        adapter.list.clear()
        adapter.list.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun initData(){
        if (MyApp.instance.checkForInternet(requireContext())){
            viewModel.getCurrenciesList()
        }else {
            viewModel.readAllCurrency.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
                adapter.list.addAll(itemsToPair(it))
                adapter.notifyDataSetChanged()

            })
        }
    }

    private fun itemsToPair(list : List<Currency>) : List<Pair<Currency, Currency>>{
        val result = ArrayList<Pair<Currency, Currency>>()
        for (item in list){
            if (item.id != args.argCurrency.id) result.add(Pair(args.argCurrency, item))
        }
        return result
    }

}