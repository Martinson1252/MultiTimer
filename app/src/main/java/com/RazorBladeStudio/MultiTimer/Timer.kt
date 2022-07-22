package com.RazorBladeStudio.MultiTimer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.*
import android.media.MediaPlayer
import android.os.IBinder
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class Timer(timerNumber: Int,_timeM: Int,_timeS: Int,name: String,_timersPanel: TableLayout,mod_wind: TableRow, context: Context ) : Service() {
    enum class MainButtonState{ START,STOP}
    var number = timerNumber
    var context = context
    var alarm = MediaPlayer()
    var time_name_display = TextView(context)
    var time_display = TextView(context)
    var buttonState = MainButtonState.START
    var wholeTimer = LinearLayout(context)
    var timerContent = LinearLayout(context)
    var start_button = ImageButton(context)
    var edit_button = ImageButton(context)
    var passed_time_text = TextView(context)
    var add15s_button = Button(context)
    var delete_button = ImageButton(context)
    var CHANNEL_T_ID: String = "channel"+number.toString()
    var tname = ""
    var timeM = 0
    var timeS = 0
    var M = 0
    var S = 0

    init {
        tname = name
        timeM = _timeM
        timeS = _timeS
        wholeTimer = _timersPanel.getChildAt(number) as TableRow
        val mod_Wind = mod_wind
        val context = context
        timerContent = wholeTimer.getChildAt(1) as LinearLayout
        start_button = timerContent.getChildAt(4) as ImageButton
        edit_button = (wholeTimer.getChildAt(2) as LinearLayout).getChildAt(0) as ImageButton
        add15s_button = (wholeTimer.getChildAt(2) as LinearLayout).getChildAt(4) as Button
        passed_time_text = (wholeTimer.getChildAt(2) as LinearLayout).getChildAt(2) as TextView
        delete_button = (wholeTimer.getChildAt(0) as LinearLayout).getChildAt(0) as ImageButton
        time_name_display = timerContent.getChildAt(0) as TextView
        time_display = timerContent.getChildAt(2) as TextView

        SetTimer(time_display,time_name_display,timeM.toString(),timeS.toString(),tname,context)

        start_button.setOnClickListener()
        {
//            Intent(context,Timer::class.java).also {
//                startService(it)
//            }
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

        add15s_button.setOnClickListener()
        {
            if(timeS+15>=59)
            {
                timeM += 1
                timeS = 59 - timeS + 15
            }else{
                timeS += 15

            }
            time_display.setTextColor(Color.parseColor("#FF03A9F4"))
            time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
            if(alarm.isPlaying) {
                alarm.stop()
                SetupAlarm()}
        }

    }
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            if(buttonState==MainButtonState.START) StartTimer() else Stoptimer()
            return super.onStartCommand(intent, flags, startId)
        }




    fun StartTimer()
    {

        M = timeM
        S = timeS
        var m_passed = 0
        var s_passed = 0
        var intent = Intent(context.getApplicationContext(),Timer::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
        var pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),1,intent,PendingIntent.FLAG_ONE_SHOT)

        var channel1 = NotificationChannel(CHANNEL_T_ID,"Channel 1",NotificationManager.IMPORTANCE_HIGH)
        var notManag: NotificationManager = context.getSystemService(NotificationManager::class.java)
                notManag.createNotificationChannel(channel1)
        var notifyManager = NotificationManagerCompat.from(context)

        start_button.setImageResource(R.drawable.stop_button)
        time_display.setTextColor(Color.parseColor("#FF03A9F4"))
        buttonState = MainButtonState.STOP
        start_button.setBackgroundResource(R.drawable.pause_round_button)
        passed_time_text.visibility = View.VISIBLE
        add15s_button.visibility = View.VISIBLE
        var notifi = SetupNotify(pendingIntent)
        notifyManager.notify(number,notifi)
        SetupAlarm()
        var timer = Thread{
            while(buttonState==MainButtonState.STOP)
            {
                Thread.yield()

                if(s_passed>=59){
                    s_passed = 0
                    m_passed +=1
                }else
                    s_passed +=1

                passed_time_text.text = StringCheck(m_passed.toString())+":"+StringCheck(s_passed.toString())


                if(timeM>=0 && timeS>0)
                {
                    timeS-=1
                    time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
                    var notifi = SetupNotify(pendingIntent)
                    notifyManager.notify(number,notifi)
                }
                else
                    if(timeM>0 && timeS <=0)
                    {
                        timeM -= 1
                        timeS = 59
                        time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
                        var notifi = SetupNotify(pendingIntent)
                        notifyManager.notify(number,notifi)
                    }
                    else
                        if(timeM+timeS <=0 && !alarm.isPlaying)
                        {
                            time_display.setTextColor(Color.parseColor("#ff0000"))
                            alarm.start()
                        }


                    Thread.sleep(1000)



            }
            timeM = M
            timeS = S
            time_display.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
            passed_time_text.text = StringCheck(timeM.toString())+":"+StringCheck(timeS.toString())
            alarm.stop()
            notifyManager.cancel(number)


        }


            timer.start()



    }


    fun SetupNotify(pendingIntent: PendingIntent): Notification
    {
        var notifi: Notification = NotificationCompat.Builder(context,CHANNEL_T_ID)
            .setContentTitle(tname)
            .setSmallIcon(R.drawable.timer_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(time_display.text.toString()+"\n"+passed_time_text.text+" passed"))
            //.setContentText()
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setOngoing(true)
            .build()
        return notifi
    }

    fun SetupAlarm()
    {
        alarm = MediaPlayer.create(context,R.raw.mixkit_alarm_buzzer_992)
        alarm.isLooping = true
    }

    fun Stoptimer()
    {
        time_display.setTextColor(Color.parseColor("#FFFFFFFF"))
        start_button.setBackgroundResource(R.drawable.round_button)
        start_button.setImageResource(R.drawable.start_button)
        buttonState = MainButtonState.START
        passed_time_text.visibility = View.INVISIBLE
        add15s_button.visibility = View.INVISIBLE
//        Intent(context,Timer::class.java).also {
//            stopService(it)
//        }

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

    override fun onBind(p0: Intent?): IBinder? = null


}