package com.example.apartmentbuddy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.fragments.ItemController
import com.example.apartmentbuddy.model.EditClickListener
import com.example.apartmentbuddy.model.FirebaseAuthUser
import com.example.apartmentbuddy.model.Item
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import me.relex.circleindicator.CircleIndicator

/**
 * Credits for circle indicator: https://medium.com/@mandvi2346verma/image-slider-with-dot-indicators-using-viewpager-firebase-kotlin-android-735968da76f6
 */
class ListItemAdvRecyclerViewAdapter(
    private val listings: MutableList<Item>,
    private val bottomNavValue: String,
    val listener: EditClickListener
) : RecyclerView.Adapter<ListItemAdvRecyclerViewAdapter.ViewHolder>() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ImageSliderViewPagerAdapter
    lateinit var context: Context
    lateinit var indicator: CircleIndicator
    lateinit var bookmark: FloatingActionButton
    lateinit var bookmarkRemove: FloatingActionButton
    lateinit var delete: FloatingActionButton
    lateinit var edit: FloatingActionButton

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_items, parent, false)

        bookmark = view.findViewById(R.id.bookmark)
        bookmarkRemove = view.findViewById(R.id.bookmark_remove)
        edit = view.findViewById(R.id.edit)

        delete = view.findViewById(R.id.delete)
        if (bottomNavValue == "myPosts") {
            view.findViewById<FloatingActionButton>(R.id.edit)?.visibility = View.VISIBLE
            view.findViewById<FloatingActionButton>(R.id.delete)?.visibility = View.VISIBLE
            view.findViewById<FloatingActionButton>(R.id.bookmark)?.visibility = View.INVISIBLE
        }

        viewPager = view.findViewById(R.id.idViewPager)
        indicator = view.findViewById(R.id.indicator)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val advertisementItem = listings[position]

        viewPagerAdapter = ImageSliderViewPagerAdapter(context, advertisementItem.photos)
        viewPager.adapter = viewPagerAdapter
        indicator.setViewPager(viewPager)

        holder.title.text = advertisementItem.title
        holder.description.text = advertisementItem.description
        holder.price.text = advertisementItem.price.toString()
        holder.condition.text = advertisementItem.condition
        holder.category.text = advertisementItem.category
        holder.address.text = advertisementItem.address
        holder.contact.text = advertisementItem.contact

        //Display bookmark-remove action button if the ad is bookmarked by the logged in user
        val loggedInUser = FirebaseAuthUser.getUserId()
        if (advertisementItem.bookmarkUserList?.map { string ->
                string.replace("[", "").replace("]", "")
            }?.contains(loggedInUser) == true) {
            holder.bookmarkRemove.visibility = View.VISIBLE
        }

        //Bookmark the post
        bookmark.setOnClickListener {
            Snackbar.make(it, "Post Saved For Later", 2000).show()
            if (loggedInUser != null) {
                ItemController().addUserToBookmarkList(
                    advertisementItem.documentId,
                    loggedInUser
                )
            }
            holder.bookmarkRemove.visibility = View.VISIBLE
            notifyDataSetChanged()
        }

        //Remove Bookmark
        bookmarkRemove.setOnClickListener {
            Snackbar.make(it, "Bookmark Removed", 2000).show()
            if (loggedInUser != null) {
                ItemController().removeUserFromBookmarkList(
                    advertisementItem.documentId,
                    loggedInUser
                )
            }
            holder.bookmarkRemove.visibility = View.INVISIBLE
            if (bottomNavValue == "bookmark") {
                listings.remove(advertisementItem)
            }
            notifyDataSetChanged()
        }

        //Delete the post from MyPosts tab
        delete.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete Item Listing?")
                .setMessage("Are you sure you want to delete this listing?")

                .setNegativeButton("Cancel") { _, _ ->
                }
                .setPositiveButton("Delete") { dialog, which ->
                    FirebaseFirestore.getInstance().collection("items")
                        .document(advertisementItem.documentId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Listing deleted!", Toast.LENGTH_SHORT)
                                .show()
                            listings.remove(advertisementItem)
                            notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Unable to delete listing! Try again",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                }
                .show()
        }

        //Edit the post from MyPosts tab
        edit.setOnClickListener {
            listener.onAdvertisementEditClick(advertisementItem)
        }
    }

    override fun getItemCount(): Int {
        return listings.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val description: TextView = itemView.findViewById(R.id.itemDescription)
        val price: TextView = itemView.findViewById(R.id.itemPrice)
        val condition: TextView = itemView.findViewById(R.id.itemCondition)
        val category: TextView = itemView.findViewById(R.id.itemCategory)
        val address: TextView = itemView.findViewById(R.id.itemAddress)
        val contact: TextView = itemView.findViewById(R.id.itemContact)
        val bookmarkRemove: FloatingActionButton = itemView.findViewById(R.id.bookmark_remove)
    }
}