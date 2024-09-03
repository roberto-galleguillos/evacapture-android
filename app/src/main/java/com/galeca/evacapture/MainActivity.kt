package com.galeca.evacapture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarButton: Button = findViewById(R.id.button_calendar)
        val coursesButton: Button = findViewById(R.id.button_courses)
        val settingsButton: ImageButton = findViewById(R.id.button_settings)

        // Botón para abrir la actividad de Calendario
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // Botón para abrir la actividad de Ramos
        coursesButton.setOnClickListener {
            val intent = Intent(this, RamosActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            // Lógica para abrir la actividad de Ajustes
        }
    }
}
