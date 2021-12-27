package com.example.calculator.data

import android.content.SharedPreferences
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.PrecisionValue
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.domain.entity.VibrationFeedbackValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsDaoImpl(
    private val preferences: SharedPreferences
): SettingsDao  {
    override suspend fun setResultPanelType(resultPanelType: ResultPanelType) =
        withContext(Dispatchers.IO) {
            preferences.edit().putString(RESULT_PANEL_TYPE_KEY, resultPanelType.name).apply()
        }

    override suspend fun getResultPanelType(): ResultPanelType =
        withContext(Dispatchers.IO) {
            preferences.getString(RESULT_PANEL_TYPE_KEY, null)
                ?.let { ResultPanelType.valueOf(it) } ?: ResultPanelType.LEFT
        }

    override suspend fun setPrecisionValue(precisionValue: PrecisionValue) =
        withContext(Dispatchers.IO) {
            preferences.edit().putString(PRECISION_VALUE_KEY, precisionValue.name).apply()
        }

    override suspend fun getPrecisionValue(): PrecisionValue =
        withContext(Dispatchers.IO) {
            preferences.getString(PRECISION_VALUE_KEY, null)?.let { PrecisionValue.valueOf(it) } ?: PrecisionValue.THREE
        }

    override suspend fun setVibrationFeedback(vibrationFeedbackValue: VibrationFeedbackValue) =
        withContext(Dispatchers.IO) {
            preferences.edit().putString(VIBRATION_FEEDBACK_VALUE_KEY,
                vibrationFeedbackValue.name).apply()
        }

    override suspend fun getVibrationFeedback(): VibrationFeedbackValue =
        withContext(Dispatchers.IO) {
            preferences.getString(VIBRATION_FEEDBACK_VALUE_KEY, null)
                ?.let { VibrationFeedbackValue.valueOf(it) } ?: VibrationFeedbackValue.MEDIUM
        }

    companion object {
        private const val RESULT_PANEL_TYPE_KEY = "RESULT_PANEL_TYPE_KEY"
        private const val PRECISION_VALUE_KEY = "PRECISION_VALUE_KEY"
        private const val VIBRATION_FEEDBACK_VALUE_KEY = "VIBRATION_FEEDBACK_VALUE_KEY"
    }
}