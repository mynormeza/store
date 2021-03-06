package com.example.store.presentation.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.core.Event
import com.example.store.core.Failure
import com.example.store.core.NetworkValidator
import com.example.store.databinding.FragmentMainBinding
import com.example.store.databinding.ProductBottomSheetBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.model.FullProduct
import com.example.store.presentation.ui.main.adapter.ProductsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private val viewModel by viewModels<MainViewModel>()
    private var _binding: FragmentMainBinding? = null
    private lateinit var adapter: ProductsAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.loadProducts()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductsAdapter(
            ProductsAdapter.OnClickListener {
                showProductDetails(it)
            }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        with(viewModel) {

            products.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    showNoDataMessage(getString(R.string.empty_msg))
                } else {
                    adapter.submitList(it)
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvEmptyMsg.visibility = View.GONE
                }
                hideProgress()
            }

            failure.observe(viewLifecycleOwner) {
                if (it == Failure.NetworkConnection) {
                   showNoDataMessage(getString(R.string.not_network))
                } else {
                    showError(it)
                }
                hideProgress()
            }

            event.observe(viewLifecycleOwner) {
                when (it) {
                    is Event.Message -> showMessage(getString(it.stringRes, it.message))
                    else -> {}
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_cart -> {
                findNavController().navigate(R.id.cartFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNoDataMessage(msg: String) {
        binding.tvEmptyMsg.text = msg
        binding.rvProducts.visibility = View.GONE
        binding.tvEmptyMsg.visibility = View.VISIBLE
    }

    private fun showProductDetails(product: FullProduct) {

        val dialog = BottomSheetDialog(requireContext())
        dialog.behavior.skipCollapsed = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val view = layoutInflater.inflate(R.layout.product_bottom_sheet, null)
        val binding = ProductBottomSheetBinding.bind(view)
        binding.tvProductName.text = product.name
        binding.tvProductPrice.text = getString(R.string.price, product.price)
        if (product.discountLabel.isNotEmpty()) {
            binding.tvPromotion.text = product.discountLabel
            binding.tvPromotionDesc.text = product.discountMessage
            binding.tvPromotion.visibility = View.VISIBLE
            binding.tvPromotionDesc.visibility = View.VISIBLE
            binding.tvSeenCheckout.visibility = View.VISIBLE
        } else {
            binding.tvPromotion.visibility = View.GONE
            binding.tvPromotionDesc.visibility = View.GONE
            binding.tvSeenCheckout.visibility = View.INVISIBLE
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart(product)
        }
        dialog.setContentView(view)
        dialog.show()
    }
}
