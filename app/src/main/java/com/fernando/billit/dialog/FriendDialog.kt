package com.fernando.billit.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.R
import com.fernando.billit.extension.validateEmpty
import com.fernando.billit.model.FriendModel
import com.fernando.billit.viewmodel.FriendViewModel
import kotlinx.android.synthetic.main.popup_new_friend.view.*
import java.util.*

class FriendDialog(private var friend: FriendModel?) : DialogFragment() {

    private lateinit var viewModel: FriendViewModel

    init {
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.popup_new_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FriendViewModel::class.java)

        setupView(view)
        setupClickListeners(view)
    }

    private fun setupView(view: View) {

        if (friend == null)
            view.et_name_friend.setText("")
        else {
            // Edit friend
            view.et_name_friend.setText(friend?.name)
        }
    }

    private fun setupClickListeners(view: View) {
        view.bt_cancel_friend.setOnClickListener {
            dismiss()
        }
        view.bt_save_friend.setOnClickListener {
            if (!view.et_name_friend.validateEmpty(R.string.required_name)) {

                // If is a new friend
                if (friend == null)
                    friend = FriendModel(UUID.randomUUID().toString())

                friend?.name = view.et_name_friend.text.toString()
                friend?.amount = 0.0


                viewModel.addFriend(friend!!)

                dismiss()
            }
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