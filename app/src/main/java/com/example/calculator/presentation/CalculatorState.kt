package com.example.calculator.presentation

data class CalculatorState(
    val equation: String = "",
    val liveResult: String = "",
    val error: String? = null
)
