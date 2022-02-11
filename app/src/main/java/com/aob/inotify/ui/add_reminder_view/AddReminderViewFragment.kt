package com.aob.inotify.ui.add_reminder_view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.aob.inotify.R
import com.aob.inotify.database.RemindersDatabase
import com.aob.inotify.databinding.FragmentAddReminderViewBinding
import com.aob.inotify.workers.ReminderWorker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class AddReminderViewFragment : BottomSheetDialogFragment() {

    val TAG = "AddReminderViewFragment"

    lateinit var binding: FragmentAddReminderViewBinding
    lateinit var viewModel: AddReminderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_reminder_view, container, false
        )

        setupUI()

        attachClickListeners()

        return binding.root
    }

    /**
     * Setup the viewmodel and database
     */
    private fun setupUI() {
        val application = requireNotNull(this.activity).application

        val dataSource = RemindersDatabase.getInstance(application).remindersDatabaseDao

        val viewModelFactory = AddReminderViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[AddReminderViewModel::class.java]

    }

    /**
     * Attach the click listeners to the view
     */
    private fun attachClickListeners() {
        binding.chooseDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.chooseTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.addNewReminderButton.setOnClickListener {
            Log.i(this.TAG, "Add reminder button tapped")

            setEditTextAndSaveToDB()

            scheduleNotification()

            this.dismiss()
        }
    }

    /**
     * Schedule notification using the delay (how long before the notification is shown).
     */
    private fun scheduleNotification() {
        // A persistable set of key/value pairs which are used as inputs and outputs for ListenableWorkers.
        // Here we pass in our reminder name and description as key value pairs to be used by our reminder worker class
        // using the "inputData" variable
        val data = Data.Builder()
            .putString("name", binding.editTextReminderName.text.toString())
            .putString("description", binding.editTextTReminderDescription.text.toString())

        // try to get the difference between the reminder set time and current time
        // which sets the delay before the notification comes
        val delay = viewModel.getDelay()

        // schedule one time request using a delay in milliseconds
        val notificationWork = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data.build()).build()

        // get instance of work manager and schedule / enqueue a work
        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueue(notificationWork)

        Log.d(TAG, "Schedule notification delay $delay")
    }

    /**
     * This saves the text from the EditText views to the database.
     */
    private fun setEditTextAndSaveToDB() {
        viewModel.insertNewReminder(
            binding.editTextReminderName.text.toString(),
            binding.editTextTReminderDescription.text.toString()
        )
    }


    /**
     * To show the time picker dialog.
     */
    private fun showTimePickerDialog() {
        val timePicker = TimePickerDialog(
            // pass the Context
            requireContext(),
            // listener to perform task
            // when time is picked
            timePickerDialogListener,
            // default hour when the time picker
            // dialog is opened
            12,
            // default minute when the time picker
            // dialog is opened
            10,
            // 24 hours time picker is
            // false (varies according to the region)
            false
        )
        // then after building the timepicker
        // dialog show the dialog to user
        timePicker.show()
    }

    /**
     * To show the date picker dialog.
     */
    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            // pass the Context
            requireContext(),
            0,
        )
        // set the minimum date to the current date
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            val date = LocalDate.of(year, month, dayOfMonth)
            val text: String = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            /// set the reminder date to parsed date
            viewModel.reminderDate = text

            // set the choose date button text to the date chosen
            binding.chooseDate.text = text
        }
        // then after building the datePicker
        // dialog show the dialog to user
        datePicker.show()
    }

    /**
     * Time picker dialog listener.
     * listens for changes in time selection.
     */
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
            if (timePicker.validateInput()) {
                val time = LocalTime.of(hourOfDay, minute)
                val text = time.format(DateTimeFormatter.ISO_LOCAL_TIME)

                // set reminder time to time chosen
                viewModel.reminderTime = text

                // set the choose time button text to chosen time
                binding.chooseTime.text = text
            }
        }
}