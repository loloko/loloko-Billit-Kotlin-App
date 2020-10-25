package com.fernando.billit.adapter

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.fernando.billit.R
import com.fernando.billit.extension.TAG
import com.fernando.billit.extension.inflate
import com.fernando.billit.model.FriendModel
import kotlinx.android.synthetic.main.item_friend.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FriendAdapter @Inject constructor() : RecyclerView.Adapter<FriendAdapter.MyViewHolder>(), Filterable {

    private var friendList: MutableList<FriendModel> = ArrayList()
    private var friendFilterList: MutableList<FriendModel> = ArrayList()
    private lateinit var context: Context

    private val isFriendScreen = true
    //val mItemClickListener: ItemClickListener? = null

    private var mTotalAmount = 0.0

    // Used to filter when search by name


    interface ItemClickListener {
        fun onRecyclerItemClick(position: Int)
    }

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = parent.inflate(R.layout.item_friend)
        context = parent.context

        return MyViewHolder(itemView)
    }

    fun setFriendsList(friends: List<FriendModel>?) {
        friendList.clear()
        friendFilterList.clear()

        if (friends != null)
            friendList.addAll(friends)

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.apply {
                //show last row as TOTAL
                if (!isFriendScreen && friendFilterList.size == position) {
                    mFriendName.text = ""
                    mPaid.text = context.getString(R.string.total)
                    mAmount.text = "%.2f".format(mTotalAmount)

                    return
                } else
                    mPaid.text = context.getString(R.string.paid)

                if (position == 0)
                    mTotalAmount = 0.0


                mFriendName.text = friendFilterList[position].name


//                tracker?.let {
//                    itemView.isActivated = it.isSelected(position.toLong())
//                }

                if (isFriendScreen)
                    mLayout.isVisible = false
                else {
                    mLayout.isVisible = true
                    mAmount.text = "%.2f".format(friendFilterList[position].amount)
                    mTotalAmount += friendFilterList[position].amount
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder: ${e.message}")
        }
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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mFriendName: TextView = itemView.friend_item_name
        var mAmount: TextView = itemView.tv_amount
        var mPaid: TextView = itemView.tv_paid
        val mLayout: LinearLayout = itemView.layout_amount

        init {
//            if (mItemClickListener != null)
//                itemView.setOnClickListener {
//                    mItemClickListener.onRecyclerItemClick(adapterPosition)
//                }
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