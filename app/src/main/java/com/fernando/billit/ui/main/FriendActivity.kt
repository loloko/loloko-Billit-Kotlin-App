package com.fernando.billit.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fernando.billit.BaseActivity
import com.fernando.billit.R
import com.fernando.billit.adapter.FriendAdapter
import com.fernando.billit.adapter.MyItemDetailsLookup
import com.fernando.billit.databinding.ActivityFriendBinding
import com.fernando.billit.dialog.FriendDialog
import com.fernando.billit.extension.TAG
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.helper.MyButton
import com.fernando.billit.helper.MyButtonClickListener
import com.fernando.billit.helper.MyItemKeyProvider
import com.fernando.billit.helper.MySwipeHelper
import com.fernando.billit.model.FriendModel
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.MarginItemDecoration
import com.fernando.billit.util.ResultResource.*
import com.fernando.billit.viewmodel.FriendViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_friend.*
import java.io.Serializable
import javax.inject.Inject


class FriendActivity : BaseActivity() {

    private lateinit var viewModel: FriendViewModel
    private lateinit var binding: ActivityFriendBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var adapter: FriendAdapter

    // Use to select the friends by rows
    private var tracker: SelectionTracker<Long>? = null

    private val loadingPopup by lazy {
        createLoadingPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(FriendViewModel::class.java)

        // Toolbar
        binding.toolbar.tbOptions.title = ""
        setSupportActionBar(binding.toolbar.tbOptions)
        binding.toolbar.toolbarTitle.setText(R.string.friends)


        initVariables()
        subscribeObservers()
        getExtras()

        // Get all Friend from Firebase
        viewModel.getAllFriends()
    }

    private fun getExtras(){
        // Just will happen if there is a friend list send by BillDetailslActivity, so it will be used to make these friends be selected in the recycler
        if (intent.extras != null) {
            val friendList = intent.getSerializableExtra("friendList") as List<FriendModel>
            adapter.friendListFromBill = friendList
        }
    }

    private fun initVariables() {
        // Call popup to add new friend
        binding.fabAddFriend.setOnClickListener {
            FriendDialog(null).show(supportFragmentManager, "FriendDialog")
        }

        // Init the recycler
        binding.recyclerFriends.layoutManager = LinearLayoutManager(this)
        binding.recyclerFriends.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.recycler)))
        binding.recyclerFriends.adapter = adapter

        // For recycler selection friend row
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.recyclerFriends,
            MyItemKeyProvider(binding.recyclerFriends),
            MyItemDetailsLookup(binding.recyclerFriends),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()
        adapter.tracker = tracker

        // Listener for searching friend by name (Searching View)
        binding.searchView.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Add swipe for recycler view (Edit and Delete)
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

    private fun subscribeObservers() {
        viewModel.friendResultObserver().observe(this) { data ->
            when (data) {
                is Loading -> {
                    if (!loadingPopup.isShowing)
                        loadingPopup.show()
                }
                is Success -> {
                    loadingPopup.dismiss()

                    setFriendsList(data.data)
                }
                is Error -> {
                    toastMessage(data.msg, isWarning = true)
                    loadingPopup.dismiss()
                }
            }
        }
    }

    private fun setFriendsList(data: List<FriendModel>?) {
        // Scroll recycler view to the top
        binding.recyclerFriends.smoothScrollToPosition(0)
        // Update adapter
        adapter.setFriendsList(data)

        // If exist friends, hide message
        if (data != null && data.isNotEmpty())
            binding.tvNoFriendFound.visibility = View.GONE
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

    // Get friends selected in the recycler
    private fun getFriendsSelected() {
        try {
            val list = tracker?.selection!!.map {
                adapter.getFriendAtPosition(it.toInt())
            }.toList()

            // Send friend selected to the BillDetailsActivity
            val intent = Intent();
            intent.putExtra("friendList", list as Serializable)
            setResult(RESULT_OK, intent);

        } catch (e: Exception) {
            Log.e(TAG, "getFriendsSelected: ${e.message}")
        }

        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        getFriendsSelected()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.friend_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_done -> {
                getFriendsSelected()
                false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}



