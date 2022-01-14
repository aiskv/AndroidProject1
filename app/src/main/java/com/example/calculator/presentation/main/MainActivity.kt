package com.example.calculator.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.R
import com.example.calculator.databinding.MainActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.domain.entity.VibrationFeedbackValue
import com.example.calculator.presentation.common.BaseActivity
import com.example.calculator.presentation.history.HistoryResult
import com.example.calculator.presentation.settings.SettingsActivity

class MainActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {
    private val viewBinding by viewBinding(MainActivityBinding::bind)
    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(SettingsDaoProvider.getDao(this@MainActivity),
                    HistoryRepositoryProvider.get(this@MainActivity)) as T
            }
        }
    }



    private val resultLauncher = registerForActivityResult(HistoryResult()) { item ->
        viewModel.onHistoryResult(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.main_activity)

        viewBinding.mainActivitySettings.setOnClickListener {

            openSettings()
        }

        viewBinding.mainActivityHistory.setOnClickListener {

            openHistory()
        }

        viewBinding.mainEquals.setOnClickListener {

            viewModel.onResultClicked()
        }

        listOf(
            viewBinding.mainPlus,
            viewBinding.mainMinus,
            viewBinding.mainMultiply,
            viewBinding.mainDivide,
            viewBinding.mainPow
        ).forEach { operationView ->
            operationView.setOnClickListener {

                viewModel.onOperationClicked(operationView.contentDescription.toString())
            }
        }

        viewBinding.mainAllClear.setOnClickListener {

            viewModel.onAllClearClicked()
        }

        listOf(
            viewBinding.mainZero,
            viewBinding.mainOne,
            viewBinding.mainTwo,
            viewBinding.mainThree,
            viewBinding.mainFour,
            viewBinding.mainFive,
            viewBinding.mainSix,
            viewBinding.mainSeven,
            viewBinding.mainEight,
            viewBinding.mainNine
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener {

                viewModel.onNumberClicked(index)
            }
        }

        viewBinding.mainClear.setOnClickListener {

            viewModel.onClearClicked()
        }

        viewBinding.mainPoint.setOnClickListener {

            viewModel.onPointClicked()
        }

        viewBinding.mainPow.setOnLongClickListener {

            showPowMenu()
            return@setOnLongClickListener true
        }

        viewModel.expressionState.observe(this) { state ->
            viewBinding.inputEdit.text = state
        }
        viewModel.resultState.observe(this) { state ->
            viewBinding.outputTextView.text = state
        }

        viewModel.resultPanelState.observe(this) {
            with(viewBinding.outputTextView) {
                gravity = when (it) {
                    ResultPanelType.LEFT -> Gravity.START.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.RIGHT -> Gravity.END.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.HIDE -> gravity
                }
                isVisible = it != ResultPanelType.HIDE
            }
        }


    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
    private fun openHistory() {
        resultLauncher.launch()
    }
    private fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun showPowMenu() {
        PopupMenu(this, viewBinding.mainPow).apply {
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.pow_menu)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.pow2 -> {
                viewModel.onDefaultPowClicked("^2")
                true
            }
            R.id.pow05 -> {
                viewModel.onDefaultPowClicked("^0.5")
                true
            }
            else -> false
        }
    }
}