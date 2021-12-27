package com.example.calculator.presentation.settings

import com.example.calculator.presentation.common.BaseActivity
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.R
import com.example.calculator.databinding.SettingsActivityBinding
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.domain.entity.PrecisionValue
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.domain.entity.VibrationFeedbackValue

class SettingsActivity: BaseActivity() {
    private val viewBinding by viewBinding(SettingsActivityBinding::bind)
    private val viewModel by viewModels<SettingsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SettingsViewModel(SettingsDaoProvider.getDao(this@SettingsActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.settings_activity)

        viewBinding.settingsBack.setOnClickListener {
            finish()
        }

        viewBinding.resultPanel.setOnClickListener {
            viewModel.onResultPanelClick()
        }
        viewModel.resultPanelState.observe(this) { state ->

            viewBinding.resultPanelDescription.text =
                resources.getStringArray(R.array.result_panel_types)[state.ordinal]
        }
        viewModel.openResultPanelAction.observe(this) { type ->
            showDialogResultPanelType(type)
        }

        viewBinding.setPrecision.setOnClickListener {
            viewModel.onPrecisionValueClick()
        }
        viewModel.precisionValue.observe(this) { state ->

            viewBinding.setPrecisionDescription.text =
                resources.getStringArray(R.array.precision_values)[state.ordinal]
        }
        viewModel.openPrecisionValueAction.observe(this) { value ->
            showDialogPrecisionValue(value)
        }

        viewBinding.vibrationFeedback.setOnClickListener {
            viewModel.onVibrationFeedbackValueClick()
        }
        viewModel.vibrationFeedbackValue.observe(this) { state ->

            viewBinding.vibrationFeedbackDescription.text =
                resources.getStringArray(R.array.vibration_feedback_values)[state.ordinal]
        }
        viewModel.openVibrationFeedbackValueAction.observe(this) { type ->
            showDialogVibrationFeedbackValue(type)
        }

    }

    private fun showDialogResultPanelType(type: ResultPanelType) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.result_panel_dialog_title))
            .setSingleChoiceItems(R.array.result_panel_types, type.ordinal){ _, id ->
                viewModel.onResultPanelTypeChanged(ResultPanelType.values()[id])
            }
            .show()
    }

    private fun showDialogPrecisionValue(value: PrecisionValue) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.precision_dialog_title))
            .setSingleChoiceItems(R.array.precision_values, value.ordinal){ _, id ->
                viewModel.onPrecisionValueChanged(PrecisionValue.values()[id])
            }
            .show()
    }

    private fun showDialogVibrationFeedbackValue(type: VibrationFeedbackValue) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.vibration_feedback_dialog_title))
            .setSingleChoiceItems(R.array.vibration_feedback_values, type.ordinal){ _, id ->
                viewModel.onVibrationFeedbackValueChanged(VibrationFeedbackValue.values()[id])
            }
            .show()
    }
}