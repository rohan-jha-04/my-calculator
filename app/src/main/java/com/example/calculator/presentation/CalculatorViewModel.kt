package com.example.calculator.presentation

import androidx.lifecycle.ViewModel
import com.example.calculator.domain.MathEvaluator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val evaluator = MathEvaluator()

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    private var parenthesisCount = 0

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Parenthesis -> enterParenthesis()
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Clear -> performClear()
            is CalculatorAction.Calculate -> performCalculation()
        }
    }

    private fun enterNumber(number: Int) {
        if (_state.value.equation.length >= MAX_EQUATION_LENGTH) return
        
        _state.update {
            val newEq = it.equation + number
            it.copy(
                equation = newEq,
                liveResult = evaluateLive(newEq),
                error = null
            )
        }
    }

    private fun enterOperation(operation: String) {
        if (_state.value.equation.length >= MAX_EQUATION_LENGTH) return

        _state.update { state ->
            val eq = state.equation
            if (eq.isNotEmpty()) {
                val lastChar = eq.last()
                // Prevent duplicate operators
                if (lastChar.toString() in "+-×÷^") {
                    val replaced = eq.dropLast(1) + operation
                    state.copy(equation = replaced, error = null)
                } else {
                    state.copy(equation = eq + operation, error = null)
                }
            } else if (operation == "-") {
                 // Allow starting with negative
                 state.copy(equation = operation, error = null)
            } else {
                state
            }
        }
    }

    private fun enterDecimal() {
        if (_state.value.equation.length >= MAX_EQUATION_LENGTH) return
        
        _state.update { state ->
            val eq = state.equation
            // Simple check: don't allow consecutive decimals. 
            // A perfect check would parse the last active number constraint.
            if (!eq.endsWith(".") && (eq.isEmpty() || eq.last().isDigit())) {
                state.copy(equation = eq + ".")
            } else {
                state
            }
        }
    }

    private fun enterParenthesis() {
        if (_state.value.equation.length >= MAX_EQUATION_LENGTH) return

        _state.update { state ->
            val eq = state.equation
            val lastChar = if (eq.isNotEmpty()) eq.last() else ' '
            
            val newEq = if (parenthesisCount == 0 || lastChar == '(' || lastChar.toString() in "+-×÷^") {
                parenthesisCount++
                "$eq("
            } else {
                parenthesisCount--
                "$eq)"
            }
            state.copy(
                equation = newEq,
                liveResult = evaluateLive(newEq)
            )
        }
    }

    private fun performDeletion() {
        _state.update { state ->
            if (state.equation.isNotBlank()) {
                val lastChar = state.equation.last()
                if (lastChar == '(') parenthesisCount--
                if (lastChar == ')') parenthesisCount++
                
                val newEq = state.equation.dropLast(1)
                
                state.copy(
                    equation = newEq,
                    liveResult = evaluateLive(newEq),
                    error = null
                )
            } else {
                state
            }
        }
    }

    private fun performClear() {
        parenthesisCount = 0
        _state.value = CalculatorState()
    }

    private fun performCalculation() {
        val eq = _state.value.equation
        if (eq.isNotBlank()) {
            var balancedEq = eq
            // Auto-close parentheses before final evaluation
            val openCount = balancedEq.count { it == '(' }
            val closeCount = balancedEq.count { it == ')' }
            repeat(openCount - closeCount) {
                 balancedEq += ")"
            }

            val result = evaluator.evaluate(balancedEq)
            if (result == "Error") {
                _state.update { it.copy(error = "Invalid Expression", liveResult = "") }
            } else {
                 _state.update { it.copy(equation = result, liveResult = "", error = null) }
                 parenthesisCount = 0 // Reset after final calculation string sets
            }
        }
    }

    private fun evaluateLive(equation: String): String {
        if (equation.isBlank() || equation.last().toString() in "+-×÷^(") return ""
        
        var balancedEq = equation
        val openCount = balancedEq.count { it == '(' }
        val closeCount = balancedEq.count { it == ')' }
        repeat(openCount - closeCount) {
             balancedEq += ")"
        }

        val result = evaluator.evaluate(balancedEq)
        return if (result == "Error" || result == equation) "" else result
    }

    companion object {
        const val MAX_EQUATION_LENGTH = 100
    }
}
