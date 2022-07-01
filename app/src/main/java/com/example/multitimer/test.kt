package com.example.multitimer

import android.content.Context
import android.util.Log
import java.io.*

class test (path: File){
    var path = path
    init {
    Write()
    }

    fun Write()
    {
        val file = File(path,"data.txt")
        file.createNewFile()
        var writer = BufferedWriter(FileWriter(file,false))
        writer.write("dsa"+" \n")
        writer.write("45dxcz6"+ "\n")
        writer.close()

        var reader = BufferedReader(FileReader(file))

        var line = reader.readLine()
        while(line!="")
        {
            Log.i("info",line)
            line = reader.readText()
        }
        reader.close()
    }

}