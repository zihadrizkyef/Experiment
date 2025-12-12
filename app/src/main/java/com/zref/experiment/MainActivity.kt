package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.zref.experiment.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val adapter = NameAdapter()
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            var list = listOf<Name>()

            for (i in 0..20) {
                val name = Name(
                    id = i.toLong(),
                    name = "Name $i",
                    color = ContextCompat.getColor(
                        this@MainActivity,
                        listOf(
                            R.color.purple_200,
                            R.color.purple_500,
                            R.color.purple_700,
                            R.color.teal_200,
                            R.color.teal_700,
                            R.color.white,
                        ).random()
                    )
                )
                list = list+name
            }
            adapter.submitList(list)

            delay(5000)

            for (i in 21..40) {
                val name = Name(
                    id = i.toLong(),
                    name = "Name $i",
                    color = ContextCompat.getColor(
                        this@MainActivity,
                        listOf(
                            R.color.purple_200,
                            R.color.purple_500,
                            R.color.purple_700,
                            R.color.teal_200,
                            R.color.teal_700,
                            R.color.white,
                        ).random()
                    )
                )
                list = list+name
            }
            adapter.submitList(list)
            binding.textTitle.isVisible = false
        }
    }
}