package com.example.calculator.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.CalculateExpression
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.domain.entity.PrecisionValue
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.domain.entity.VibrationFeedbackValue
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository
): ViewModel() {
    private var expression: String = ""
    private var inputNumber: String = ""
    private val _expressionState = MutableLiveData<String>()
    val expressionState: LiveData<String> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _precisionValue = MutableLiveData<Int>()
    private val precisionValue: LiveData<Int> = _precisionValue

    private val _vibrationFeedbackValue = MutableLiveData<VibrationFeedbackValue>()
    val vibrationFeedbackValue: LiveData<VibrationFeedbackValue> = _vibrationFeedbackValue

    private val calculator = CalculateExpression()

    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionValue.value = when (settingsDao.getPrecisionValue()) {
                PrecisionValue.THREE -> 3
                PrecisionValue.FOUR -> 4
                PrecisionValue.FIVE -> 5
                PrecisionValue.SIX -> 6
            }
            _vibrationFeedbackValue.value = settingsDao.getVibrationFeedback()
        }
    }

    fun onNumberClicked(number: Int) {
        if (expression != "Infinity") {
            if (number.toDouble() ==
                calculator.fastEvaluateCheck(inputNumber + number.toString())) {
                if (inputNumber.isNotBlank() && !inputNumber.contains(".")) {
                    expression = expression.dropLast(1)
                }
                if (number == 0)
                    inputNumber += number.toString()
                else
                    inputNumber = number.toString()
            } else {
                inputNumber += number.toString()
            }
            expression += number.toString()
        } else {
            inputNumber = number.toString()
            expression = number.toString()
            _resultState.value = ""
        }
        _expressionState.value = expression
    }

    fun onResultClicked() {
        val result = calculator.calculateExpression(expression, precisionValue.value ?: 3)
        if (("" != expression || "0" != expression) && "0" != result) {
            viewModelScope.launch {
                historyRepository.add(HistoryItem(expression, result))
            }
        }
        expression = result
        _expressionState.value = expression
        _resultState.value = expression
        inputNumber = result
    }

    fun onAllClearClicked() {
        _resultState.value = ""
        expression = ""
        _expressionState.value = expression
        inputNumber = ""
    }

    fun onOperationClicked(operation: String) {
        if (expression.isNotBlank() && (expression.last().isDigit() || expression.endsWith("."))) {
            _resultState.value = calculator.calculateExpression(expression, precisionValue.value ?: 3)
            expression += operation
            _expressionState.value = expression
        }
        inputNumber = ""
    }

    fun onClearClicked() {
        _resultState.value = ""
        expression = calculator.zeroLastOperand(expression)
        _expressionState.value = expression
        inputNumber = "0"
    }

    fun onPointClicked() {
        if (expression.isNotBlank() && expression.last().isDigit()) {
            inputNumber += "."
            expression += "."
            _expressionState.value = expression
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "onCleared")
    }

    fun onStart() {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionValue.value = when(settingsDao.getPrecisionValue()) {
                PrecisionValue.THREE -> 3
                PrecisionValue.FOUR -> 4
                PrecisionValue.FIVE -> 5
                PrecisionValue.SIX -> 6
            }
            _vibrationFeedbackValue.value = settingsDao.getVibrationFeedback()
            _resultState.value = ""
        }
    }

    fun onHistoryResult(item: HistoryItem?) {
        if (item != null) {
            expression = item.expression
            _expressionState.value = expression
            _resultState.value = item.result
        }
    }

    fun onDefaultPowClicked(suffix: String) {
        if (expression.isNotBlank() && (expression.last().isDigit() || expression.endsWith("."))) {
            expression = "($expression)$suffix"
            _expressionState.value = expression
        }
        inputNumber = suffix.drop(1)
    }
}