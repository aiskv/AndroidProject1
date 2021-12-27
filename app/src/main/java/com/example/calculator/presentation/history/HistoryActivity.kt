package com.example.calculator.presentation.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.R
import com.example.calculator.databinding.HistoryActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.presentation.common.BaseActivity

class HistoryActivity: BaseActivity() {
    private val viewBinding by viewBinding(HistoryActivityBinding::bind)
    private val viewModel by viewModels<HistoryViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HistoryViewModel(
                    HistoryRepositoryProvider.get(this@HistoryActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)

        val historyAdapter = HistoryAdapter(viewModel::onItemClicked)

        with(viewBinding.historyList) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = historyAdapter
        }

        viewModel.historyItemsState.observe(this) { state ->
            historyAdapter.setData(state)
        }
        viewBinding.historyBack.setOnClickListener {
            finish()
        }

        viewModel.closeWithResult.observe(this) { state ->
            setResult(RESULT_OK, Intent().putExtra(HISTORY_ACTIVITY_KEY, state))
            finish()
        }
    }
    companion object {
        const val HISTORY_ACTIVITY_KEY = "HISTORY_ACTIVITY_KEY"
    }
}