package com.fernando.billit.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.BaseActivity
import com.fernando.billit.R
import com.fernando.billit.databinding.ActivityFriendBinding
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.inflate
import com.fernando.billit.extension.validateEmpty
import com.fernando.billit.model.FriendModel
import com.fernando.billit.viewmodel.FriendViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import java.util.*
import javax.inject.Inject

class FriendActivity : BaseActivity() {

    private lateinit var viewModel: FriendViewModel
    private lateinit var binding: ActivityFriendBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

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
    }

    private fun init() {
        binding.fabAddFriend.setOnClickListener {

        }


    }

    private fun observers() {
        viewModel.friendResultObserver().observe(this, { friends ->
            if (friends != null) {
               
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // Show popup to insert or edit a friend
    private fun createNewFriendPopup(friendModel: FriendModel?) {
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


                dialog.dismiss()
            }
        }
    }

}
