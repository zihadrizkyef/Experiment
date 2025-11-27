package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat.dpToPx
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
                top = insets.top + dpToPx(
                    16f,
                    resources.displayMetrics
                ).toInt()
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
}