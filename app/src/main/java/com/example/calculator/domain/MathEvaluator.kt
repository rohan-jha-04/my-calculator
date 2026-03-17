package com.example.calculator.domain

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MathEvaluator {

    fun evaluate(expression: String): String {
        return try {
            val tokens = tokenize(expression)
            val postfix = infixToPostfix(tokens)
            val result = evaluatePostfix(postfix)
            formatResult(result)
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        var currentNumber = StringBuilder()

        for (i in expression.indices) {
            val char = expression[i]

            if (char.isDigit() || char == '.') {
                currentNumber.append(char)
            } else if (char == '-' && (i == 0 || expression[i - 1] in "+-×÷/(^")) {
                // Handle negative numbers
                currentNumber.append(char)
            } else {
                if (currentNumber.isNotEmpty()) {
                    tokens.add(currentNumber.toString())
                    currentNumber.clear()
                }
                if (!char.isWhitespace()) {
                    tokens.add(char.toString())
                }
            }
        }
        if (currentNumber.isNotEmpty()) {
            tokens.add(currentNumber.toString())
        }
        return tokens
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operators = ArrayDeque<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "×" to 2, "÷" to 2, "^" to 3)
        val associativity = mapOf("+" to "L", "-" to "L", "×" to "L", "÷" to "L", "^" to "R")

        for (token in tokens) {
            when {
                token.toBigDecimalOrNull() != null -> output.add(token)
                token == "(" -> operators.addLast(token)
                token == ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        output.add(operators.removeLast())
                    }
                    if (operators.isNotEmpty() && operators.last() == "(") {
                        operators.removeLast()
                    }
                }
                precedence.containsKey(token) -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        val topOp = operators.last()
                        val topPrec = precedence[topOp] ?: 0
                        val tokenPrec = precedence[token] ?: 0
                        val isLeftAssoc = associativity[token] == "L"

                        if ((isLeftAssoc && tokenPrec <= topPrec) || (!isLeftAssoc && tokenPrec < topPrec)) {
                            output.add(operators.removeLast())
                        } else {
                            break
                        }
                    }
                    operators.addLast(token)
                }
            }
        }
        while (operators.isNotEmpty()) {
            output.add(operators.removeLast())
        }
        return output
    }

    private fun evaluatePostfix(postfix: List<String>): BigDecimal {
        val stack = ArrayDeque<BigDecimal>()
        val mc = MathContext(10, RoundingMode.HALF_UP)

        for (token in postfix) {
            val number = token.toBigDecimalOrNull()
            if (number != null) {
                stack.addLast(number)
            } else {
                val b = stack.removeLast()
                val a = stack.removeLast()
                val result = when (token) {
                    "+" -> a.add(b, mc)
                    "-" -> a.subtract(b, mc)
                    "×" -> a.multiply(b, mc)
                    "÷" -> {
                        if (b == BigDecimal.ZERO) throw ArithmeticException("Division by zero")
                        a.divide(b, mc)
                    }
                    "^" -> a.pow(b.toInt(), mc)
                    else -> throw IllegalArgumentException("Unknown operator: $token")
                }
                stack.addLast(result)
            }
        }
        return if (stack.isNotEmpty()) stack.last() else BigDecimal.ZERO
    }

    private fun formatResult(result: BigDecimal): String {
        val stripped = result.stripTrailingZeros()
        // Prevent scientific notation for large numbers if possible
        return stripped.toPlainString()
    }
}
