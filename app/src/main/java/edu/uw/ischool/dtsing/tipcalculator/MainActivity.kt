package edu.uw.ischool.dtsing.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import java.text.NumberFormat
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    // Set up variables for later initialization
    private lateinit var tipButton: Button
    private lateinit var serviceCharge: EditText
    private lateinit var tipSpinner: Spinner
    private var tipPercent = 0.15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        serviceCharge = findViewById(R.id.serviceCharge)
        tipButton = findViewById(R.id.tipButton)
        tipButton.isEnabled = false // Disable button upon launching application
        tipSpinner = findViewById(R.id.tipSpinner)

        tipSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                updateTipPercentage(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        // Text input listener
        serviceCharge.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                formatCurrency(this, s)
            }
        })

        // Calculate tip and display in a Toast message
        tipButton.setOnClickListener {
            val amountString = serviceCharge.text.toString().replace("$", "")
            val amount = amountString.toDouble()
            val tip = amount * tipPercent
            val formattedTip = NumberFormat.getCurrencyInstance().format(tip)

            Toast.makeText(this, formattedTip, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTipPercentage(position: Int) {
        when (position) {
            0 -> tipPercent = 0.10
            1 -> tipPercent = 0.15
            2 -> tipPercent = 0.18
            3 -> tipPercent = 0.20
        }
    }

    private fun formatCurrency(watcher: TextWatcher, s: Editable?) {
        val originalSelection = serviceCharge.selectionStart
        val amountString = s.toString().replace("$", "")
        val amount = amountString.toDoubleOrNull() ?: 0.0

        tipButton.isEnabled = amount >= 0.0

        serviceCharge.removeTextChangedListener(watcher)

        val formatted =
            if (amount == 0.0) {
                "$0.00"
            } else {
                // Remove any "$", ",", or "."
                val cleanString = amountString.replace("""[$,.]""".toRegex(), "")
                val parsed = cleanString.toDouble()
                NumberFormat.getCurrencyInstance().format(parsed / 100)
            }

        serviceCharge.setText(formatted)

        // Adjust cursor location
        val newSelection = originalSelection + formatted.length - s.toString().length
        serviceCharge.setSelection(newSelection)

        serviceCharge.addTextChangedListener(watcher)
    }
}