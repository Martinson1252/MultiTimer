package com.example.multitimer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import androidx.navigation.ui.AppBarConfiguration
import com.example.multitimer.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.Console
import java.sql.Time


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    companion object {
        var timerL =  mutableListOf<Timer>()
        var activeTimerNumber = 0
        fun  deleteTimer() {}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setSupportActionBar(binding.toolbar)
        //var wholeTimer = findViewById<TableRow>(R.id.timer1)
        var timersPanel = findViewById<TableLayout>(R.id.tableLayout)
        //var timerContent = findViewById<LinearLayout>(R.id.linear1)
        var addTimer_Window = findViewById<TableRow>(R.id.add_timer_window)
        var addButton = findViewById<Button>(R.id.add)
        val modify_Timer_window = findViewById<TableRow>(R.id.modify_timer_window)
        var editName =  ( findViewById<EditText>(R.id.timer_content_name_text))
        var editTimeM = ( findViewById<EditText>(R.id.Timer_Input_time_min))
        var editTimeS = ( findViewById<EditText>(R.id.Timer_Input_time_sec))
        val back_button_add_timer_window = findViewById<Button>(R.id.add_timer_back)
        val add_new_timer_button = findViewById<Button>(R.id.add_new_timer)
        var AddTimerTimeName = findViewById<EditText>(R.id.AddTimerTimeName)
        var AddTimerTimeM = findViewById<EditText>(R.id.AddTimerTimeM)
        var AddTimerTimeS = findViewById<EditText>(R.id.AddTimerTimeS)
        var removeTimerCancel = findViewById<Button>(R.id.removeTimerCancel)
        var removeTimerConfirm = findViewById<Button>(R.id.removeTimerConfirm)
        //setContentView(binding.root)
        timerL += Timer(timerL.lastIndex+1,15,0,"timer1",timersPanel,modify_Timer_window,this)
        timerL += Timer(timerL.lastIndex+1,11,0,"timer2",timersPanel,modify_Timer_window,this)
        timerL += Timer(timerL.lastIndex+1,11,0,"timer3",timersPanel,modify_Timer_window,this)



        val back_edit_window = ((modify_Timer_window.getChildAt(0) as LinearLayout).getChildAt(4) as LinearLayout).getChildAt(0) as Button
        val confirm_edit_window = ((modify_Timer_window.getChildAt(0) as LinearLayout).getChildAt(4) as LinearLayout).getChildAt(2) as Button

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        back_edit_window.setOnClickListener()
        {
            modify_Timer_window.visibility = View.INVISIBLE
            addButton.visibility = View.VISIBLE
        }
        confirm_edit_window.setOnClickListener()
        {
            timerL[activeTimerNumber].SetTimer(timerL[activeTimerNumber].time_display,timerL[activeTimerNumber].time_name_display,
                editTimeM.text.toString(), editTimeS.text.toString(), editName.text.toString(),this)
            modify_Timer_window.visibility = View.INVISIBLE
            addButton.visibility = View.VISIBLE
        }



        //var t = TableRow(this)
        //mainPanel.addView(t)


        back_button_add_timer_window.setOnClickListener()
        {
            addTimer_Window.visibility = View.INVISIBLE
            addButton.visibility = View.VISIBLE
        }

        add_new_timer_button.setOnClickListener()
        {
            addButton.visibility = View.VISIBLE
            var view = layoutInflater.inflate(R.layout.timer,null)
            timersPanel.addView(view)
            addTimer_Window.visibility = View.INVISIBLE
            timerL += Timer(timerL.lastIndex+1,Timer.StringNullCheck(AddTimerTimeM.text.toString()).toInt(),Timer.StringNullCheck(AddTimerTimeS.text.toString()).toInt(),AddTimerTimeName.text.toString(),timersPanel,modify_Timer_window,this)
        }

        addButton.setOnClickListener()
        {
            addButton.visibility = View.INVISIBLE
            addTimer_Window.visibility = View.VISIBLE
        }

        removeTimerCancel.setOnClickListener()
        {
            findViewById<TableRow>(R.id.deleteTimerWindow).visibility = View.INVISIBLE
        }

        removeTimerConfirm.setOnClickListener()
        {
            findViewById<TableRow>(R.id.deleteTimerWindow).visibility = View.INVISIBLE
            timerL.removeAt(activeTimerNumber)
            var View = timersPanel.getChildAt(activeTimerNumber) as TableRow
            timersPanel.removeView(View)
            //Log.i("info","${timerL.lastIndex} ")
            for (i in timerL.indices)
            {
                Log.i("info","remain index $i ")
                timerL[i].number = i
            }
        }


    }



}


