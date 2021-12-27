package com.example.calculator.domain

import com.example.calculator.domain.entity.PrecisionValue
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.domain.entity.VibrationFeedbackValue

interface SettingsDao {
    /**
     * получает тип отображения панели результата
     */
    suspend fun getResultPanelType(): ResultPanelType

    /**
     * устанавливает тип отображения панели результата
     * */
    suspend fun setResultPanelType(resultPanelType: ResultPanelType)

    /**
     * получает точность вычисления (в знаках после запятой)
     * */
    suspend fun getPrecisionValue(): PrecisionValue

    /**
     * устанавливает точность вычисления (в знаках после запятой)
     * */
    suspend fun setPrecisionValue(precisionValue: PrecisionValue)

    /**
     * получает значение величины виброотклика
     * */
    suspend fun getVibrationFeedback(): VibrationFeedbackValue

    /**
     * устанавливает величину виброотклика
     * */
    suspend fun setVibrationFeedback(vibrationFeedbackValue: VibrationFeedbackValue)
}