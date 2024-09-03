package com.galeca.evacapture

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class NotasActivity : AppCompatActivity() {

    private lateinit var layoutLab: LinearLayout
    private lateinit var layoutTheory: LinearLayout
    private lateinit var theoryPercentageInput: TextInputEditText
    private lateinit var labPercentageInput: TextInputEditText
    private lateinit var currentGradeText: TextView
    private lateinit var backButton: ImageButton
    private lateinit var notasTitle: TextView
    private lateinit var deleteButton: Button
    private var isUpdating = false  // Flag to prevent infinite loop

    private val sharedPreferences by lazy {
        getSharedPreferences("RamosPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        layoutLab = findViewById(R.id.layout_lab)
        layoutTheory = findViewById(R.id.layout_theory)
        theoryPercentageInput = findViewById(R.id.input_theory_percentage)
        labPercentageInput = findViewById(R.id.input_lab_percentage)
        currentGradeText = findViewById(R.id.text_current_grade)
        backButton = findViewById(R.id.button_back)
        notasTitle = findViewById(R.id.text_notas_title)
        deleteButton = findViewById(R.id.button_eliminar_ramo)

        // Limitar a 3 dígitos el máximo de cifras
        val maxLengthFilter = InputFilter.LengthFilter(3)
        theoryPercentageInput.filters = arrayOf(maxLengthFilter)
        labPercentageInput.filters = arrayOf(maxLengthFilter)

        val ramoType = intent.getStringExtra("ramo_type") ?: "Solo Teoría"
        val ramoName = intent.getStringExtra("ramo_name") ?: ""

        notasTitle.text = ramoName  // Mostrar el nombre del ramo en el título

        // Verifica el contenido de los SharedPreferences
        val ramos = sharedPreferences.getStringSet("ramos_set", mutableSetOf())
        Log.d("NotasActivity", "Ramos almacenados: $ramos")

        if (ramoType == "Solo Teoría") {
            layoutTheory.visibility = LinearLayout.VISIBLE
            layoutLab.visibility = LinearLayout.GONE
            theoryPercentageInput.setText("100")
            theoryPercentageInput.isEnabled = false
        } else {
            layoutLab.visibility = LinearLayout.VISIBLE

            // Cargar los valores guardados, si existen, y sólo actualizar si no están definidos
            val savedTheoryPercentage = sharedPreferences.getInt("${ramoName}_theory", -1)
            val savedLabPercentage = sharedPreferences.getInt("${ramoName}_lab", -1)

            if (savedTheoryPercentage != -1) {
                theoryPercentageInput.setText(savedTheoryPercentage.toString())
            } else {
                theoryPercentageInput.setText("70")
            }

            if (savedLabPercentage != -1) {
                labPercentageInput.setText(savedLabPercentage.toString())
            } else {
                labPercentageInput.setText("30")
            }

            theoryPercentageInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!isUpdating) {
                        updateLabPercentage()
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    // Mantener el cursor al final del texto
                    theoryPercentageInput.setSelection(s?.length ?: 0)
                }
            })

            labPercentageInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!isUpdating) {
                        updateTheoryPercentage()
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    // Mantener el cursor al final del texto
                    labPercentageInput.setSelection(s?.length ?: 0)
                }
            })
        }

        backButton.setOnClickListener {
            savePercentages(ramoName)
            finish()
        }
        deleteButton.setOnClickListener {
            deleteRamo(ramoName)
            finish()
        }
    }

    private fun updateLabPercentage() {
        val theoryText = theoryPercentageInput.text.toString()
        var theoryPercentage = theoryText.toIntOrNull() ?: 0

        // Limitar el porcentaje a 100 si es mayor
        if (theoryPercentage > 100) {
            theoryPercentage = 100
            theoryPercentageInput.setText("100")
        }

        isUpdating = true
        if (theoryText.isEmpty() || theoryPercentage == 100) {
            labPercentageInput.setText("0")
        } else {
            labPercentageInput.setText((100 - theoryPercentage).toString())
        }
        isUpdating = false
    }

    private fun updateTheoryPercentage() {
        val labText = labPercentageInput.text.toString()
        var labPercentage = labText.toIntOrNull() ?: 0

        // Limitar el porcentaje a 100 si es mayor
        if (labPercentage > 100) {
            labPercentage = 100
            labPercentageInput.setText("100")
        }

        isUpdating = true
        if (labText.isEmpty() || labPercentage == 100) {
            theoryPercentageInput.setText("0")
        } else {
            theoryPercentageInput.setText((100 - labPercentage).toString())
        }
        isUpdating = false
    }

    private fun savePercentages(ramoName: String) {
        val theoryPercentage = theoryPercentageInput.text.toString().toIntOrNull() ?: 100
        val labPercentage = labPercentageInput.text.toString().toIntOrNull() ?: 100

        val editor = sharedPreferences.edit()
        editor.putInt("${ramoName}_theory", theoryPercentage)
        editor.putInt("${ramoName}_lab", labPercentage)
        editor.apply()
    }

    private fun deleteRamo(ramoName: String) {
        val editor = sharedPreferences.edit()
        val ramos = sharedPreferences.getStringSet("ramos_set", mutableSetOf())?.toMutableSet()
        Log.d("NotasActivity", "Ramos almacenados antes de eliminar: $ramos")
        if (ramos?.removeIf { it.startsWith(ramoName) } == true) {
            Log.d("NotasActivity", "Ramo eliminado: $ramoName")
            editor.putStringSet("ramos_set", ramos)
            editor.remove("${ramoName}_theory")
            editor.remove("${ramoName}_lab")
            editor.apply()
        } else {
            Log.d("NotasActivity", "Ramo no encontrado: $ramoName")
        }
        Log.d("NotasActivity", "Ramos almacenados después de eliminar: ${sharedPreferences.getStringSet("ramos_set", mutableSetOf())}")
    }
}
