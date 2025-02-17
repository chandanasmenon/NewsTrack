package com.chandana.newstrack.ui.languagenews

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.databinding.ActivityLanguageDisplayBinding
import com.chandana.newstrack.di.component.DaggerActivityComponent
import com.chandana.newstrack.di.module.ActivityModule
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageDisplayBinding

    @Inject
    lateinit var viewModel: LanguageNewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        injectDependencies()
        lifecycleScope.launch {
            setUpObserver()
        }
    }

    private suspend fun setUpObserver() {
        viewModel.getLanguagesList()
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiStateLanguage.collect {
                when (it) {
                    UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        displayErrorMessage(it.message)
                    }

                    is UiState.Success -> {
                        binding.chipGroup.removeAllViews()
                        binding.progressBar.visibility = View.GONE
                        for (item in it.data) {
                            addChipLanguage(item)
                        }
                    }
                }
            }
        }
    }

    private fun addChipLanguage(code: Code) {
        val chip = Chip(this)
        val language = code.value.capitalizeWords()
        chip.text = language
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        chip.setTypeface(null, Typeface.BOLD)
        chip.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.chipGroup.addView(chip)
        chip.setOnClickListener {
            startActivity(LanguageNewsActivity.getStartIntent(this, code))
        }
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }

}