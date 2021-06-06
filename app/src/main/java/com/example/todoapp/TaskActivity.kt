package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    // holds value of date, time and month
    lateinit var myCalender: Calendar

    // Interface invoked when you show the dialog and when you click on any value of dialog
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    val db by lazy {
        Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                DB_NAME
        )
    }

    lateinit var dateEdt: TextInputEditText
    lateinit var timeInputLay: TextInputLayout
    lateinit var timeEdt: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        connectToLayout()

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
    }

    private fun connectToLayout() {
        dateEdt = findViewById(R.id.dateEdt)
        timeInputLay = findViewById(R.id.timeInputLay)
        timeEdt = findViewById(R.id.timeEdt)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.dateEdt -> {
                setListener()
            }
            R.id.timeEdt -> {
                setTimeListener()
            }
        }
    }

    private fun setTimeListener() {
        myCalender = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener { _ : TimePicker, hourOfDay: Int, minute: Int ->
            myCalender.set(Calendar.HOUR, hourOfDay)
            myCalender.set(Calendar.MINUTE, minute)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(this,
                timeSetListener,
                myCalender.get(Calendar.HOUR_OF_DAY),
                myCalender.get(Calendar.MINUTE),
                false)
        timePickerDialog.show()
    }


    private  fun setListener() {
        myCalender = Calendar.getInstance()

        // Fetching Value Via Callback
        dateSetListener = DatePickerDialog.OnDateSetListener { _ : DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        // Opening DatePicker Dialog
        val datePickerDialog = DatePickerDialog(this,
                dateSetListener,
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private  fun updateDate() {

        //m -> min
        //M -> Month
        //Mon, 5,Jun 2021
        val myFormat = "EEE d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)

        dateEdt.setText(sdf.format(myCalender.time))

        timeInputLay.visibility = View.VISIBLE
    }

    private fun updateTime() {
        //3:32 am
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)

        timeEdt.setText(sdf.format(myCalender.time))
    }
}