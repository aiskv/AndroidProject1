package com.example.calculator.domain

import com.fathzer.soft.javaluator.DoubleEvaluator
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.pow

class CalculateExpression {
    private val evaluator = DoubleEvaluator(DoubleEvaluator.getDefaultParameters(), true)
    private val operandRegex = """[0-9]+(?:\.[0-9]*)?(?:E[+-][0-9]+)?""".toRegex()
    /**
     * Рассчитывает значение выражения [expression]
     */
    fun calculateExpression(expression: String, withPrecision: Int): String {
        val mc = MathContext(withPrecision + 1)
        var preparedExpression: String = expression

        if (expression.isBlank()) return ""

        when {
            expression.endsWith("+") -> preparedExpression = "(" + expression.dropLast(1) + ")*2"
            expression.endsWith("-") -> preparedExpression = "0"
            expression.endsWith("*") -> preparedExpression = "(" + expression.dropLast(1) + ")^2"
            expression.endsWith("/") -> preparedExpression = "1"
        }

        val calculation = evaluator.evaluate(preparedExpression)
        return if (calculation.toString() == "Error" || calculation.toString() == "Infinity") {
            calculation.toString()
        } else {
            val result = BigDecimal(calculation, mc)
            //val result = evaluator.evaluate(preparedExpression)

            if (result < BigDecimal.valueOf(abs((10.0).pow(withPrecision)))) {
                String.format("%.${withPrecision}f", result)
            } else {
                result.toString()
            }
        }
    }

    fun zeroLastOperand(expression: String): String {
        when {
            expression.endsWith("+")
                    || expression.endsWith("-")
                    || expression.endsWith("*")
                    || expression.endsWith("/")
                    || expression.endsWith("^") -> return expression.plus("0")
            expression.last().isDigit() -> {
                return expression.replaceRange(getLastOperandRange(expression), "0")
            }
        }
        return expression
    }

    private fun getLastOperandRange(expression: String): IntRange {
        return operandRegex.findAll(expression).last().range
    }

    fun fastEvaluateCheck(operand: String): Double {
        return evaluator.evaluate(operand)
    }
}