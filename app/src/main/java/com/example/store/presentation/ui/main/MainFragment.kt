package com.example.store.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.databinding.FragmentMainBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.ui.main.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    private val viewModel by viewModels<MainViewModel>()
    private var _binding: FragmentMainBinding? = null
    private lateinit var adapter: ProductsAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductsAdapter(
            ProductsAdapter.OnClickListener {
            }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        with(viewModel) {
            loadProducts()

            products.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.rvProducts.visibility = View.GONE
                    binding.tvEmptyMsg.visibility = View.VISIBLE
                } else {
                    adapter.submitList(it)
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvEmptyMsg.visibility = View.GONE
                }
            }

            failure.observe(viewLifecycleOwner) {
                hideProgress()
                showError(it)
            }
        }
    }
}
