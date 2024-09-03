package com.galeca.evacapture

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RamosActivity : AppCompatActivity() {

    private lateinit var ramosList: LinearLayout
    private lateinit var fabAddRamo: FloatingActionButton
    private lateinit var backButton: ImageButton
    private val sharedPreferences by lazy {
        getSharedPreferences("RamosPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ramos)

        ramosList = findViewById(R.id.ramos_list)
        fabAddRamo = findViewById(R.id.fab_add_ramo)
        backButton = findViewById(R.id.button_back)

        loadRamosList()

        fabAddRamo.setOnClickListener {
            showAddRamoDialog()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista de ramos cuando la actividad vuelve a primer plano
        loadRamosList()
    }

    private fun showAddRamoDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_ramo, null)
        val editText = dialogView.findViewById<EditText>(R.id.edit_text_ramo_name)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group_ramo_type)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Introduce el nombre del ramo")
            .setPositiveButton("Agregar") { _, _ ->
                val ramoName = editText.text.toString()
                if (ramoName.isNotEmpty()) {
                    val selectedType = when (radioGroup.checkedRadioButtonId) {
                        R.id.radio_theory_only -> "Solo Teoría"
                        R.id.radio_theory_and_lab -> "Teoría y Laboratorio"
                        else -> "Solo Teoría"
                    }
                    addRamoButton(ramoName, selectedType)
                    saveRamoToList(ramoName, selectedType)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun addRamoButton(ramoName: String, ramoType: String) {
        val button = Button(this).apply {
            text = ramoName
            setTextColor(Color.WHITE)
            setBackgroundColor(ContextCompat.getColor(this@RamosActivity, R.color.colorAccent))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }

            minHeight = 0
            setPadding(16, 32, 16, 32)
            textSize = 16f

            setOnClickListener {
                val intent = Intent(this@RamosActivity, NotasActivity::class.java).apply {
                    putExtra("ramo_name", ramoName)
                    putExtra("ramo_type", ramoType)
                }
                startActivity(intent)
            }
        }

        ramosList.addView(button)
    }

    private fun clearRamosList() {
        ramosList.removeAllViews()
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun saveRamoToList(ramoName: String, ramoType: String) {
        val editor = sharedPreferences.edit()
        val ramos = sharedPreferences.getStringSet("ramos_set", mutableSetOf())?.toMutableSet()
        ramos?.add("$ramoName,$ramoType")
        editor.putStringSet("ramos_set", ramos)
        editor.apply()
    }

    private fun loadRamosList() {
        ramosList.removeAllViews() // Limpiar la lista antes de cargarla nuevamente
        val ramos = sharedPreferences.getStringSet("ramos_set", mutableSetOf())
        ramos?.forEach { ramo ->
            val parts = ramo.split(",")
            if (parts.size == 2) {
                addRamoButton(parts[0], parts[1])
            } else {
                addRamoButton(parts[0], "Solo Teoría")
            }
        }
    }
}
