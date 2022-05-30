package com.example.store.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.core.Event
import com.example.store.databinding.FragmentCartBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.ui.cart.adapter.CartProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment() {

    private val viewModel by viewModels<CartViewModel>()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        viewModel.loadCartProducts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        with(viewModel) {
            products.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            summary.observe(viewLifecycleOwner) {
                binding.tvSubTotalAmount.text = getString(R.string.price, it.subTotal)
                if (it.discount > 0) {
                    binding.tvDiscountAmount.visibility = View.VISIBLE
                    binding.tvDiscounts.visibility = View.VISIBLE
                    binding.tvDiscountAmount.text = getString(R.string.price, it.discount)
                } else {
                    binding.tvDiscountAmount.visibility = View.GONE
                    binding.tvDiscounts.visibility = View.GONE
                }
                binding.tvTotalAmount.text = getString(R.string.price, it.total)
            }

            failure.observe(viewLifecycleOwner) {
                hideProgress()
                showError(it)
            }

            event.observe(viewLifecycleOwner) {
                when (it) {
                    is Event.OrderPlaced -> {
                        showMessage(getString(R.string.order_placed))
                        findNavController().popBackStack()
                    }
                    is Event.OrderCanceled -> {
                        showMessage(getString(R.string.order_canceled))
                        findNavController().popBackStack()
                    }
                    else -> {}
                }
            }

            discounts.observe(viewLifecycleOwner) {
                if (it.count > 0) {
                    binding.tvDiscountSummary.visibility = View.VISIBLE
                    binding.tvDiscountSummary.text = resources.getQuantityString(R.plurals.discount_summary, it.count, it.count, it.discounts, it.items)
                } else {
                    binding.tvDiscountSummary.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        adapter = CartProductAdapter()
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        binding.btnCancelOrder.setOnClickListener {
            viewModel.clearCart(true)
        }

        binding.btnPlaceOrder.setOnClickListener {
            viewModel.clearCart()
        }

        val swipeGesture = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteFromCart(item.id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.rvProducts)
    }
}
