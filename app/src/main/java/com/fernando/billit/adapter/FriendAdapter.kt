package com.fernando.billit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.fernando.billit.R
import com.fernando.billit.databinding.ItemFriendBinding
import com.fernando.billit.extension.TAG
import com.fernando.billit.model.FriendModel
import com.fernando.billit.util.RxBus
import com.fernando.billit.util.RxEvent
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FriendAdapter @Inject constructor() : RecyclerView.Adapter<FriendAdapter.MyViewHolder>(), Filterable {

    private var friendList: MutableList<FriendModel> = ArrayList()
    private var friendFilterList: MutableList<FriendModel> = ArrayList()
    private lateinit var context: Context

    var friendListFromBill: List<FriendModel>? = null

    private var isFriendScreen = true

    private var mTotalAmount = 0.0

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return MyViewHolder(binding)
    }

    fun isFriendScreen(isFriendScreen: Boolean) {
        this.isFriendScreen = isFriendScreen
    }

    fun setFriendsList(friends: List<FriendModel>?) {
        friendList.clear()
        friendFilterList.clear()

        friends?.forEachIndexed { index, friend ->

            // Will occurs when friend List is sent from BillDetailsActivity to FriendActivity, just to keep the friend info (amount paid)
            // and after send back to BillDetailsActivity
            // + It will keep the friend selected when was in the previus screen
            if (friendListFromBill != null)
                for (fm in friendListFromBill!!.filter { it.id == friend.id }) {
                    tracker?.select(index.toLong())
                    friend.amount = fm.amount
                    friend.hasChanged = fm.hasChanged
                }

            friendList.add(friend)
        }
        friendFilterList.addAll(friendList)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (isFriendScreen)
            return friendFilterList.size

        if (friendFilterList.isNotEmpty())
            return friendFilterList.size + 1

        return friendFilterList.size
    }

    fun getFriendAtPosition(position: Int): FriendModel {
        return friendFilterList[position]
    }

    fun deleteFriendAtPosition(position: Int) {
        friendFilterList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList: MutableList<FriendModel> = ArrayList()

                val charSearch = constraint.toString()

                friendFilterList = if (charSearch.isEmpty()) {
                    friendList
                } else {

                    val friends = friendList.map { it.copy(it.id, it.name, it.amount) }

                    for (row in friends)
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT)))
                            resultList.add(row)

                    resultList
                }

                val filterResults = FilterResults()
                filterResults.values = friendFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                friendFilterList = results?.values as MutableList<FriendModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class MyViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            try {
                binding.apply {

                    if (!isFriendScreen && friendFilterList.size == position) {
                        tvFriendName.text = ""
                        tvPaid.text = context.getString(R.string.total)
                        tvAmount.text = "%.2f".format(mTotalAmount)

                        return
                    } else
                        tvPaid.text = context.getString(R.string.paid)

                    if (position == 0)
                        mTotalAmount = 0.0


                    val friend = friendFilterList[position]

                    tvFriendName.text = friend.name


                    tracker?.let {
                        itemView.isActivated = it.isSelected(position.toLong())
                    }

                    if (isFriendScreen)
                        layoutAmount.isVisible = false
                    else {
                        layoutAmount.isVisible = true
                        tvAmount.text = "%.2f".format(friend.amount)
                        mTotalAmount += friendFilterList[position].amount
                    }

                    // Click will happen just when its in the BillDetailsActivity - recycler view
                    if (!isFriendScreen)
                        itemView.setOnClickListener {
                            RxBus.publish(RxEvent.EventOpenDialogPayment(friend))
                        }

                }


            } catch (e: Exception) {
                Log.e(TAG, "bind: ${e.message}")
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }
}

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)

        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as FriendAdapter.MyViewHolder)
                .getItemDetails()
        }

        return null
    }
}