package com.fernando.billit.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.databinding.PopupPayBillBinding
import com.fernando.billit.model.FriendModel
import com.fernando.billit.viewmodel.BillDetailsViewModel

class PayBillDialog(private var friend: FriendModel) : DialogFragment() {

    private lateinit var viewModel: BillDetailsViewModel
    private lateinit var binding: PopupPayBillBinding

    init {
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = PopupPayBillBinding.inflate(LayoutInflater.from(context), container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BillDetailsViewModel::class.java)

        setupView()
        setupClickListeners()
    }

    private fun setupView() {

        binding.tvNameFriend.text = friend.name
        binding.etAmount.setText(friend.amount.toString())

    }

    private fun setupClickListeners() {
        binding.btCancelBillPay.setOnClickListener {
            dismiss()
        }
        binding.btPayBill.setOnClickListener {

            friend.hasChanged = true
            friend.amount = binding.etAmount.text.toString().toDouble()

            viewModel.changeUserAmountPaidAtPosition(friend)

            dismiss()
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}