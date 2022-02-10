package com.aob.inotify.ui.remind_me_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aob.inotify.R
import com.aob.inotify.data.model.RemindersData
import com.aob.inotify.databinding.RemindersListItemBinding


/// Remind me view recycler view adapter
class RemindMeViewListAdapter(private var reminders: List<RemindersData>) :
    RecyclerView.Adapter<RemindMeViewListAdapter.RemindersViewHolder>() {

    /**
     * To create the ViewHolder class we need a View which is going to hold
     * the reminders_list_item.xml view created
     */
    class RemindersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // use data binding to get ids from the list item view
        private var binding = RemindersListItemBinding.bind(view)

        // To bind the views to the layout (RecyclerView)
        fun bind(reminders: RemindersData) {
            binding.apply {
                mainTextView.text = reminders.name
                descriptionTextView.text = reminders.description
                timeTextView.text = reminders.time
                dateTextView.text = reminders.date
            }
        }
    }

    /**
     * Return the view from the RemindersViewHolder
     * This method creates/uses the class RemindersViewHolder (The Recycler View is the one that        * takes the RemindersViewHolder class when extended as seen in the class declaration).
     * This is a shorter version of the function below do not uncomment
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        return RemindersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.reminders_list_item, parent, false)
        )
    }

    /**
     * To bind the reminders to the layout (RecyclerView)
     */
    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        holder.bind(reminders[position])
    }

    /**
     * returning the size/length/count of list of reminders
     */
    override fun getItemCount(): Int {
        return reminders.size
    }
}