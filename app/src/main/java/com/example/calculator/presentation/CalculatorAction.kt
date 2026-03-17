package com.example.calculator.presentation

sealed class CalculatorAction {
    data class Number(val number: Int): CalculatorAction()
    data class Operation(val operation: String): CalculatorAction()
    object Calculate: CalculatorAction()
    object Delete: CalculatorAction()
    object Clear: CalculatorAction()
    object Decimal: CalculatorAction()
    object Parenthesis: CalculatorAction()
}
