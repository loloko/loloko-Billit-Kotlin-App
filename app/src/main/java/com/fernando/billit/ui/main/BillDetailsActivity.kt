package com.fernando.billit.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fernando.billit.BaseActivity
import com.fernando.billit.R
import com.fernando.billit.adapter.FriendAdapter
import com.fernando.billit.databinding.ActivityBillDetailsBinding
import com.fernando.billit.dialog.PayBillDialog
import com.fernando.billit.helper.MyButton
import com.fernando.billit.helper.MyButtonClickListener
import com.fernando.billit.helper.MySwipeHelper
import com.fernando.billit.model.FriendModel
import com.fernando.billit.util.MarginItemDecoration
import com.fernando.billit.util.RxBus
import com.fernando.billit.util.RxEvent
import com.fernando.billit.viewmodel.BillDetailsViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import io.reactivex.disposables.Disposable
import java.io.Serializable
import javax.inject.Inject

class BillDetailsActivity : BaseActivity() {

    private lateinit var viewModel: BillDetailsViewModel
    private lateinit var binding: ActivityBillDetailsBinding
    private lateinit var friendDisposable: Disposable

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var adapter: FriendAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityBillDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(BillDetailsViewModel::class.java)

        // Toolbar
        binding.toolbar.tbOptions.title = ""
        setSupportActionBar(binding.toolbar.tbOptions)
        binding.toolbar.toolbarTitle.setText(R.string.bill_details)


        initVariables()
        subscribeObservers()
    }


    private fun initVariables() {
        // Button add friend
        binding.btAddFriends.setOnClickListener {
            val intent = Intent(this, FriendActivity::class.java)
            if (viewModel.friendListResult.value != null)
                intent.putExtra("friendList", viewModel.friendListResult.value as Serializable)

            startActivityForResult(intent, 1)
        }

        // Init the recycler
        binding.recyclerFriend.layoutManager = LinearLayoutManager(this)
        binding.recyclerFriend.adapter = adapter
        binding.recyclerFriend.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.recycler)))
        adapter.isFriendScreen(false)

        // Add swipe for recycler view Delete
        object : MySwipeHelper(this, binding.recyclerFriend, 250) {
            override fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<MyButton>) {

                // The last row "Total" can not have option to delete
                if (viewHolder.adapterPosition != (adapter.itemCount - 1)) {
                    // Button delete
                    buffer.add(MyButton(this@BillDetailsActivity, getString(R.string.delete), 40, 0, getColor(R.color.billit_red), object : MyButtonClickListener {
                        override fun onClick(pos: Int) {

                            adapter.deleteFriendAtPosition(pos)
                        }
                    }))
                }
            }
        }
    }

    // Receive a list of FriendModel selected in the FriendActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                val friendList = data?.getSerializableExtra("friendList") as List<FriendModel>

                viewModel.friendListResult.value = friendList
            }
    }

    private fun subscribeObservers() {
        // Listener when user click in the friend recycler row
        friendDisposable = RxBus.listen(RxEvent.EventOpenDialogPayment::class.java).subscribe {
            if (it != null)
                PayBillDialog(it.friend).show(supportFragmentManager, "PayBillDialog")
        }

        viewModel.friendListObserver().observe(this) { data ->
            // Show a message in case does not have any friend in the list
            if (data.isEmpty())
                binding.tvNoFriendsSelected.visibility = View.VISIBLE
            else
                binding.tvNoFriendsSelected.visibility = View.GONE

            adapter.setFriendsList(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy listener so wont leak memory
        if (!friendDisposable.isDisposed)
            friendDisposable.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_bill_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_attach -> {

//                startActivity(Intent(this, AttachActivity::class.java))
                false
            }
            R.id.menu_save -> {
                viewModel.saveBill()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}