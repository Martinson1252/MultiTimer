package com.RazorBladeStudio.MultiTimer

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.*
import android.media.MediaPlayer
import androidx.coordinatorlayout.widget.CoordinatorLayout


class Timer(timerNumber: Int,_timeM: Int,_timeS: Int,name: String,_timersPanel: TableLayout,mod_wind: TableRow, context: Context )  {
    enum class MainButtonState{ START,STOP}
    var context = context
    var time_name_display = TextView(context)
    var time_display = TextView(context)
    var buttonState = MainButtonState.START
    var wholeTimer = LinearLayout(context)
    var timerContent = LinearLayout(context)
    var start_button = Button(context)
    var edit_button = ImageButton(context)
    var delete_button = ImageButton(context)
    var tname = ""
    var timeM = 0
    var timeS = 0
    var M = 0
    var S = 0

    var number = timerNumber
    init {
        tname = name
        timeM = _timeM
        timeS = _timeS
        var remainTimeM: Int = timeM
        var remainTimeS: Int = timeS
        wholeTimer = _timersPanel.getChildAt(number) as TableRow
        val mod_Wind = mod_wind
        val context = context
        timerContent = wholeTimer.getChildAt(1) as LinearLayout
        start_button = timerContent.getChildAt(4) as Button
        edit_button = (wholeTimer.getChildAt(2) as LinearLayout).getChildAt(0) as ImageButton
        delete_button = (wholeTimer.getChildAt(0) as LinearLayout).getChildAt(0) as ImageButton
        time_name_display = timerContent.getChildAt(0) as TextView
        time_display = timerContent.getChildAt(2) as TextView
        Log.i("info","$number")

        SetTimer(time_display,time_name_display,timeM.toString(),timeS.toString(),tname,context)

        start_button.setOnClickListener()
        {
            if(buttonState==MainButtonState.START) StartTimer() else Stoptimer()
        }

        edit_button.setOnClickListener()
        {
            var editName =  (mod_Wind.getChildAt(0) as LinearLayout).getChildAt(1) as EditText
            var editTimeM = (mod_Wind.getChildAt(0) as LinearLayout).getChildAt(2) as EditText
            var editTimeS = (mod_Wind.getChildAt(0) as LinearLayout).getChildAt(3) as EditText
            mod_Wind.visibility = View.VISIBLE
            var button = ((_timersPanel.parent as ScrollView).parent as CoordinatorLayout).getChildAt(3) as Button
            button.visibility = View.INVISIBLE
            context@MainActivity.activeTimerNumber = number
            editName.setText(tname)
            editTimeM.setText(timeM.toString())
            editTimeS.setText(timeS.toString())
        }

        delete_button.setOnClickListener()
        {
            var deleteWindow = ((_timersPanel.parent as ScrollView).parent as CoordinatorLayout).getChildAt(4) as TableRow
            deleteWindow.visibility = View.VISIBLE
            context@MainActivity.activeTimerNumber = number
            Log.i("info","Active index $number")
        }


    }




    fun StartTimer()
    {
        M = timeM
        S = timeS
        SetSTARTbutton(MainButtonState.STOP)

        var alarm: MediaPlayer
        alarm = MediaPlayer.create(context,R.raw.mixkit_alarm_buzzer_992)
        alarm.isLooping = true


        var timer = Thread{
            while(buttonState==MainButtonState.STOP)
            {

                if(timeM>=0 && timeS>0)
                {
                    timeS-=1
                    time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
                }
                else
                    if(timeM>0 && timeS <=0)
                    {
                        timeM -= 1
                        timeS = 59
                        time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
                    }
                    else
                        if(timeM<=0 && timeS <=0 && !alarm.isPlaying)
                        {
                            time_display.setTextColor(Color.parseColor("#ff0000"))
                            alarm.start()
                        }

                if(timeM>0||timeS>0)
                Thread.sleep(1_000)
            }
            timeM = M
            timeS = S
            time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
            alarm.stop()
        }

        timer.start()
    }

    fun Stoptimer()
    {
        time_display.setTextColor(Color.parseColor("#FFFFFFFF"))
        SetSTARTbutton(MainButtonState.START)
    }

    fun SetSTARTbutton(state: MainButtonState)
    {
        buttonState = state
        start_button.text = state.toString()
    }

    fun SetTimer(time_display: TextView, time_name_display: TextView, timeM: String, timeS: String, name_: String, context: Context)
    {

        this.timeM = StringNullCheck(timeM).toInt()
        this.timeS = StringNullCheck(timeS).toInt()
        this.time_display.text =  StringCheck(this.timeM.toString())+":"+StringCheck(this.timeS.toString())
        this.tname = when(name_){ "" -> tname else -> name_ }
        time_name_display.text =  this.tname

    }

    fun StringCheck(w: String):String{
        return if(w.length<2) ("0$w") else w
    }

    companion object {
        fun StringNullCheck(w: String): String {
            if (w == "") return "0"
            else return w
        }
    }


}