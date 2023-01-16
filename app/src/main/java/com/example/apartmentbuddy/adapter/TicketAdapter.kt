package com.example.apartmentbuddy.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.Complain
import com.google.firebase.firestore.FirebaseFirestore

// References : https://www.youtube.com/watch?v=Az4gXQAP-a4

class TicketAdapter(
    private val c: Context,
    private val userList: MutableList<Complain>,
    private val statusAdapter: ArrayAdapter<String>,

    ) : RecyclerView.Adapter<TicketAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_tickets, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: TicketAdapter.MyViewHolder, position: Int) {
        val list: Complain = userList[position]
        holder.firstName.text = list.firstname
        holder.date.text = list.date
        holder.status.text = list.status
        holder.llrow.setOnLongClickListener {
            //References: https://stackoverflow.com/questions/52076779/kotlin-custom-dialog-in-android
            val dialog = Dialog(c)
            dialog.setContentView(R.layout.update_layout)
            val status = dialog.findViewById<TextView>(R.id.updateStatus)
            status.text = userList[position].status
            print(userList[position].status)
            val buttonAction = dialog.findViewById<Button>(R.id.button)
            val dialogAutoCompleteTextView =
                dialog.findViewById<AutoCompleteTextView>(R.id.autoComplete)
            dialogAutoCompleteTextView.setAdapter(statusAdapter)
            var selectedValue: String? = null
            dialogAutoCompleteTextView.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, position, _ ->
                    selectedValue = statusAdapter.getItem(position)
                }

            buttonAction.setOnClickListener {
                var output = "";
                var result = ""
                output = statusAdapter.getPosition(selectedValue).toString()
                when (output) {
                    "0" -> {
                        result = "Not responded"
                    }
                    "1" -> {
                        result = "Open";
                    }
                    else -> {
                        result = "Close";
                    }

                }

                when (output) {
                    "-1" -> {
                        Toast.makeText(c, "Please select a value", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> {

                        userList.set(
                            position,
                            Complain(
                                firstname = list.firstname,
                                status = result,
                                date = list.date,
                                documentid = list.documentid
                            )
                        )
                        notifyItemChanged(position)
                        // Updates fields in the firestore database
                        val db = FirebaseFirestore.getInstance()
                        val ticketCollection = db.collection("complain")
                        //References : https://stackoverflow.com/questions/56608046/update-a-document-in-firestore
                        list.documentid?.let { it1 ->
                            ticketCollection.document(it1).update(
                                "firstname", list.firstname,
                                "status" , result,
                                "date", list.date
                            )
                            statusAdapter.notifyDataSetChanged()
                        }
                    }
                }
                dialog.dismiss()

            }
            dialog.show()
            true
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.tvFirstName)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val date: TextView = itemView.findViewById(R.id.tvTime)
        val llrow = itemView.findViewById<LinearLayout>(R.id.llrow)


    }
}

