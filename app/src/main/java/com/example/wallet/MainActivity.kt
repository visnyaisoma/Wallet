package com.example.wallet

import android.content.Context
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.example.wallet.databinding.ActivityMainBinding
import com.example.wallet.databinding.SalaryRowBinding
import com.squareup.seismic.ShakeDetector

class MainActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rowBinding: SalaryRowBinding
    private var sum = 0
    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(ShakeDetector.Listener {

            Toast.makeText(this, "Your items are cleared!", Toast.LENGTH_SHORT)
                .show()
            // clear listOfRows
            binding.listOfRows.removeAllViews()
            binding.sumTextview.text = ""

        })


        binding.saveButton.setOnClickListener {
            if (binding.salaryName.text.toString().isEmpty() || binding.salaryAmount.text.toString()
                    .isEmpty()
            ) {
                val contextView = findViewById<View>(R.id.list_of_rows)
                Snackbar.make(contextView, R.string.warn_message, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.expenseOrIncome.isChecked) {
                sum = sum - binding.salaryAmount.text.toString().toInt()
            } else {
                sum = sum + binding.salaryAmount.text.toString().toInt()
            }
            binding.sumTextview.text = sum.toString()


            rowBinding = SalaryRowBinding.inflate(layoutInflater)
            rowBinding.salaryDirectionIcon.setImageResource(if (binding.expenseOrIncome.isChecked) R.drawable.expense else R.drawable.income)
            rowBinding.rowSalaryName.text = binding.salaryName.text.toString()
            rowBinding.rowSalaryAmount.text = binding.salaryAmount.text.toString()
            binding.listOfRows.addView(rowBinding.root)

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                binding.listOfRows.removeAllViews()
                sum = 0
                binding.sumTextview.text = ""
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {

        super.onResume()
        shakeDetector.start(sensorManager)
    }

    override fun onPause() {

        super.onPause()
        shakeDetector.stop()
    }


}
