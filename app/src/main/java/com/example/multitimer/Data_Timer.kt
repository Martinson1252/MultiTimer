package com.example.multitimer
import android.content.Context
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import java.io.*
import kotlin.collections.*

class Data_Timer(TimerName: String,TimerTimeMin: String, TimerTimeSec: String) {

    var TimerName = TimerName
    var TimerTimeMin = TimerTimeMin
    var TimerTimeSec = TimerTimeSec

companion object{
    fun SaveTimer(path: File, timer: List<Data_Timer>)
    {
        val file = File(path,"data.txt")
        file.createNewFile()
        var writer = BufferedWriter(FileWriter(file,false))
        for (a in timer)
        {
            writer.write(a.TimerName+"\n")
            writer.write(a.TimerTimeMin+"\n")
            writer.write(a.TimerTimeSec+"\n")
            //Log.i("info",a.toString())
        }
        writer.close()


    }

    fun ReadTimer(path: File)
    {

        var i = 1
        var tName = ""
        var tM = ""
        var tS = ""
        val file = File(path,"data.txt")
        var reader = BufferedReader(FileReader(file))
        var line = reader.readLine()
        while(!line.isNullOrEmpty())
        {

            when(i){
                1 -> tName = line //.replace("\n","")
                2 -> tM = line //.replace("\n","")
                3 -> {
                    tS = line //.replace("\n","")

                        MainActivity.data_Timer += Data_Timer(tName,tM,tS)
                    i = 0
                }

            }
            i += 1
            Log.i("info", line)
            line = reader.readLine()
        }

        reader.close()

    }
}

}