package com.aob.inotify.ui.remind_me_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aob.inotify.R
import com.aob.inotify.database.RemindersDatabase
import com.aob.inotify.databinding.FragmentRemindMeViewBinding
import com.aob.inotify.ui.add_reminder_view.AddReminderViewFragment

class RemindMeViewFragment : Fragment() {
    /// Add the remindViewModel
    private lateinit var remindMeViewModel: RemindMeViewModel
    lateinit var binding: FragmentRemindMeViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R
                .layout.fragment_remind_me_view, container, false
        )

        setupUI()

        setupListView()

        attachClickListeners()

        return binding.root
    }

    /**
     * Setup the viewmodel and database
     */
    private fun setupUI() {
        val application = requireNotNull(this.activity).application

        val dataSource = RemindersDatabase.getInstance(application).remindersDatabaseDao

        val viewModelFactory = RemindMeViewModelFactory(dataSource, application)

        remindMeViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[RemindMeViewModel::class.java]

    }

    /**
     * This observes/listens to changes in the database of reminders and since we're
     * observing the list of reminders we can actively update our recycler view by using
     * the adapter's constructor where we pass the list of reminders.
     */
    private fun setupListView() {

        // observe the reminders list to get updates and use it to populate the recycler view
        remindMeViewModel.reminders.observe(viewLifecycleOwner) { list ->
            val adapter = RemindMeViewListAdapter(list)
            binding.remindersRecyclerView.adapter = adapter
        }

        // this creates a linear layout manager since our list view is in a linear form
        binding.remindersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * This shows the "AddReminderViewFragment" bottom sheet
     */
    private fun showBottomSheetDialog() {
        val addReminderFragment = AddReminderViewFragment()
        addReminderFragment.show(parentFragmentManager, addReminderFragment.tag)
    }

    /**
     * Attach the click listeners to the view
     */
    private fun attachClickListeners() {
        binding.addRemindersButton.setOnClickListener {
            showBottomSheetDialog()
        }
    }
}