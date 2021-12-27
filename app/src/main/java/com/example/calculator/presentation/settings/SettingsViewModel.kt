package com.example.calculator.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.PrecisionValue
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.domain.entity.VibrationFeedbackValue
import com.example.calculator.presentation.common.SingleLiveEvent
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDao: SettingsDao
    ): ViewModel() {
    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _openResultPanelAction = SingleLiveEvent<ResultPanelType>()
    val openResultPanelAction: LiveData<ResultPanelType> = _openResultPanelAction

    private val _precisionValue = MutableLiveData<PrecisionValue>()
    val precisionValue: LiveData<PrecisionValue> = _precisionValue

    private val _openPrecisionValueAction = SingleLiveEvent<PrecisionValue>()
    val openPrecisionValueAction: LiveData<PrecisionValue> = _openPrecisionValueAction

    private val _vibrationFeedbackValue = MutableLiveData<VibrationFeedbackValue>()
    val vibrationFeedbackValue: LiveData<VibrationFeedbackValue> = _vibrationFeedbackValue

    private val _openVibrationFeedbackValueAction = SingleLiveEvent<VibrationFeedbackValue>()
    val openVibrationFeedbackValueAction: LiveData<VibrationFeedbackValue> = _openVibrationFeedbackValueAction


    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionValue.value = settingsDao.getPrecisionValue()
            _vibrationFeedbackValue.value = settingsDao.getVibrationFeedback()
        }
    }

    fun onResultPanelTypeChanged(resultPanelType: ResultPanelType) {
        _resultPanelState.value = resultPanelType
        viewModelScope.launch {
            settingsDao.setResultPanelType(resultPanelType)
        }

    }

    fun onResultPanelClick() {
        _openResultPanelAction.value = _resultPanelState.value
    }

    fun onPrecisionValueChanged(precisionValue: PrecisionValue) {
        _precisionValue.value = precisionValue
        viewModelScope.launch {
            settingsDao.setPrecisionValue(precisionValue)
        }

    }

    fun onPrecisionValueClick() {
        _openPrecisionValueAction.value = _precisionValue.value
    }

    fun onVibrationFeedbackValueChanged(vibrationFeedbackValue: VibrationFeedbackValue) {
        _vibrationFeedbackValue.value = vibrationFeedbackValue
        viewModelScope.launch {
            settingsDao.setVibrationFeedback(vibrationFeedbackValue)
        }

    }

    fun onVibrationFeedbackValueClick() {
        _openVibrationFeedbackValueAction.value = _vibrationFeedbackValue.value
    }
}