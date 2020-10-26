package com.fernando.billit.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fernando.billit.BaseActivity
import com.fernando.billit.R
import com.fernando.billit.adapter.FriendAdapter
import com.fernando.billit.databinding.ActivityFriendBinding
import com.fernando.billit.dialog.FriendDialog
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.helper.MyButton
import com.fernando.billit.helper.MyButtonClickListener
import com.fernando.billit.helper.MySwipeHelper
import com.fernando.billit.model.FriendModel
import com.fernando.billit.util.Resource.Status.*
import com.fernando.billit.viewmodel.FriendViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_friend.*
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

        // Get all Friend from Firebase
        viewModel.getAllFriends()
    }

    private fun init() {
        // Call popup to add new friend
        binding.fabAddFriend.setOnClickListener {
            FriendDialog(null).show(supportFragmentManager, "FriendDialog")
        }

        // Init the recycler
        binding.recyclerFriends.layoutManager = LinearLayoutManager(this)
        binding.recyclerFriends.adapter = adapter

        // Listener for searching friend by name
        binding.searchView.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Add swipe for recycler view
        object : MySwipeHelper(this, recycler_friends, 250) {
            override fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<MyButton>) {

                //button delete
                buffer.add(MyButton(this@FriendActivity, getString(R.string.delete), 40, 0, getColor(R.color.billit_red), object : MyButtonClickListener {
                    override fun onClick(pos: Int) {

                        deleteFriendDialog(adapter.getFriendAtPosition(pos))

                    }
                }))

                //button edit
                buffer.add(MyButton(this@FriendActivity, getString(R.string.edit), 40, 0, getColor(R.color.billit_blue), object : MyButtonClickListener {
                    override fun onClick(pos: Int) {

                        FriendDialog(adapter.getFriendAtPosition(pos)).show(supportFragmentManager, "FriendDialog")

                    }
                }))
            }
        }
    }

    private fun observers() {
        viewModel.friendResultObserver().observe(this, { data ->
            if (data != null) {
                when (data.status) {
                    LOADING -> {
                        if (!loadingPopup.isShowing)
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

    private fun deleteFriendDialog(friend: FriendModel) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
        builder.setTitle(R.string.attention)
        builder.setMessage(R.string.delete_firebase)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.cancel) { dialog, _ -> dialog?.dismiss() }
        builder.setNegativeButton(R.string.delete) { dialog, _ ->

            viewModel.deleteFriend(friend.id)

            dialog.dismiss()
        }

        builder.create().show()
    }

}



