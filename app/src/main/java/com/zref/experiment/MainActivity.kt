package com.zref.experiment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.textTitle.updatePadding(
                top = insets.top + 16.dpToPx()
            )
            binding.recyclerView.updatePadding(
                bottom = 100.dpToPx()
            )
            WindowInsetsCompat.CONSUMED
        }

        binding.textInfo.text =
            binding.textInfo.text.toString() + "\nTotal user = ${UserDatabase.users.size}"

        val adapter = UserAdapter()
        binding.recyclerView.adapter = adapter

        binding.inputSearch.doOnTextChanged { text, _, _, _ ->
            adapter.searchName(text.toString())
        }
    }

    fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}