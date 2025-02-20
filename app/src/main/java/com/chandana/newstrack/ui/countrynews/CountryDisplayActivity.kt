package com.chandana.newstrack.ui.countrynews

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.databinding.ActivityCountryDisplayBinding
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryDisplayActivity : AppCompatActivity() {

    private lateinit var viewModel: CountryNewsViewModel

    private lateinit var binding: ActivityCountryDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CountryNewsViewModel::class.java]
        lifecycleScope.launch {
            setUpObserver()
        }
    }

    private suspend fun setUpObserver() {
        viewModel.getCountriesList()
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiStateCountry.collect {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        displayErrorMessage(it.message)
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.chipGroup.removeAllViews()
                        for (item in it.data) {
                            addChipCountry(item)
                        }
                    }
                }
            }
        }
    }

    private fun addChipCountry(code: Code) {
        val chip = Chip(this)
        val country = code.value.capitalizeWords()
        chip.text = country
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        chip.setTypeface(null, Typeface.BOLD)
        chip.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.chipGroup.addView(chip)
        chip.setOnClickListener {
            startActivity(CountryNewsActivity.getStartIntent(this, code))
        }
    }

}