package com.chandana.newstrack.ui.categorynews

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
import com.chandana.newstrack.databinding.ActivityCategoryDisplayBinding
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDisplayActivity : AppCompatActivity() {

    private lateinit var viewModel: CategoryNewsViewModel

    private lateinit var binding: ActivityCategoryDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CategoryNewsViewModel::class.java]
        lifecycleScope.launch {
            setUpObserver()
        }
    }

    private suspend fun setUpObserver() {
        viewModel.getCategories()
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiStateCategory.collect {
                when (it) {
                    UiState.Loading -> {
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
                            val category = item.capitalizeWords()
                            addChipCategory(category)
                        }
                    }
                }
            }
        }
    }

    private fun addChipCategory(category: String) {
        val chip = Chip(this)
        chip.text = category
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        chip.setTypeface(null, Typeface.BOLD)
        chip.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.chipGroup.addView(chip)
        chip.setOnClickListener {
            startActivity(CategoryNewsActivity.getStartIntent(this, category))
        }
    }

}