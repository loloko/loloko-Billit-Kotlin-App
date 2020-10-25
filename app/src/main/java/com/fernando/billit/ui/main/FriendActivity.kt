package com.fernando.billit.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fernando.billit.BaseActivity
import com.fernando.billit.R
import com.fernando.billit.adapter.FriendAdapter
import com.fernando.billit.databinding.ActivityFriendBinding
import com.fernando.billit.extension.*
import com.fernando.billit.model.FriendModel
import com.fernando.billit.util.Resource.Status.*
import com.fernando.billit.viewmodel.FriendViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import java.util.*
import javax.inject.Inject

class FriendActivity : BaseActivity() {

    private lateinit var viewModel: FriendViewModel
    private lateinit var binding: ActivityFriendBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var adapter: FriendAdapter

    private lateinit var loadingPopup: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(FriendViewModel::class.java)

        // Create loading dialog
        loadingPopup = createLoadingPopup()


        // Toolbar
        binding.toolbar.tbOptions.title = ""
        setSupportActionBar(binding.toolbar.tbOptions)
        binding.toolbar.toolbarTitle.setText(R.string.friends)

        init()
        observers()

        viewModel.getAllFriends()
    }

    private fun init() {
        // Call popup to add new friend
        binding.fabAddFriend.setOnClickListener {
            createNewFriendPopup()
        }

        // Init the recycler
        binding.recyclerFriends.layoutManager = LinearLayoutManager(this)
        binding.recyclerFriends.adapter = adapter
    }

    private fun observers() {
        viewModel.friendResultObserver().observe(this, { data ->
            if (data != null) {
                when (data.status) {
                    LOADING -> {
                        loadingPopup.show()
                    }
                    SUCCESS -> {
                        loadingPopup.dismiss()

                        setFriendsList(data.data)
                    }
                    ERROR -> {
                        toastMessage(data.message, isWarning = true)
                        loadingPopup.dismiss()
                    }
                }
            }
        })
    }

    //
    private fun setFriendsList(data: List<FriendModel>?) {
        // Scroll recycler view to the top
        binding.recyclerFriends.smoothScrollToPosition(0)
        // Update adapter
        adapter.setFriendsList(data)

        // If exist friends, hide message
        if (data != null && data.isNotEmpty())
            binding.tvNoFriendFound.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // Show popup to insert or edit a friend
    private fun createNewFriendPopup(friendModel: FriendModel? = null) {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val view: View = inflate(R.layout.popup_new_friend)

        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val mFriendName = view.findViewById<EditText>(R.id.et_name_friend)

        friendModel?.let {
            mFriendName.setText(it.name)
        }

        // Button cancel action click
        val btCancel = view.findViewById<Button>(R.id.bt_cancel_friend)
        btCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Button save action click
        val btSave = view.findViewById<Button>(R.id.bt_save_friend)
        btSave.setOnClickListener {

            // Validation
            if (!mFriendName.validateEmpty(R.string.required_name)) {

                // Set a new UUID if it is a new friend
                val uuid = friendModel?.id ?: UUID.randomUUID().toString()

                // Insert into firebase
                val friend = FriendModel(uuid, mFriendName.text.toString())

                viewModel.addFriend(friend)

                dialog.dismiss()
            }
        }
    }

}
