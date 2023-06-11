package com.example.justtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date

class MainActivity : AppCompatActivity() {
    public var lastTime: LocalDateTime = LocalDateTime.now();

    lateinit var mainHandler: Handler;

    lateinit var d: TextView
    lateinit var h: TextView
    lateinit var m: TextView
    lateinit var s: TextView

    public val updateTime = object : Runnable {
        override fun run() {
            var delta = Duration.between(lastTime, LocalDateTime.now())
            s.text = delta.toSecondsPart().toString()
            m.text = delta.toMinutesPart().toString()
            h.text = delta.toHoursPart().toString()
            d.text = delta.toDaysPart().toString()
            mainHandler.postDelayed(this, 250);

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        d = findViewById(R.id.dayTV)
        h = findViewById(R.id.HourTV)
        m = findViewById(R.id.MinTV)
        s = findViewById(R.id.SecTV)
        loadTime()
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTime)

    }

    fun loadTime() {
        var f = File(filesDir, "time.txt")
        if (!f.exists())
            return
        var a = FileInputStream(f).bufferedReader().use { it.readText() }
        lastTime = LocalDateTime.parse(a)
    }

    fun readTextFile(inputStream: InputStream): String {
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
        }
        return outputStream.toString()
    }

    fun onNewTime(view: View) {
        lastTime = LocalDateTime.now()
        var f = File(filesDir, "time.txt")
        Files.deleteIfExists(f.toPath())
        var nextStr = lastTime.toString()
        f.appendText(nextStr)
    }


}