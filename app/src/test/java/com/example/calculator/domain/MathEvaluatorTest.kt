package com.example.calculator.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class MathEvaluatorTest {
    private val evaluator = MathEvaluator()

    @Test
    fun testSimpleAddition() {
        assertEquals("5", evaluator.evaluate("2+3"))
    }

    @Test
    fun testOrderOfOperations() {
        assertEquals("14", evaluator.evaluate("2+3×4"))
    }

    @Test
    fun testParentheses() {
        assertEquals("20", evaluator.evaluate("(2+3)×4"))
    }

    @Test
    fun testDivisionAndDecimals() {
        assertEquals("2.5", evaluator.evaluate("5÷2"))
    }

    @Test
    fun testFloatingPointPrecision() {
        // Typical Float issue: 0.1 + 0.2 = 0.30000000000000004
        assertEquals("0.3", evaluator.evaluate("0.1+0.2"))
    }

    @Test
    fun testDivisionByZero() {
        assertEquals("Error", evaluator.evaluate("5÷0"))
    }

    @Test
    fun testNegativeNumbers() {
        assertEquals("-1", evaluator.evaluate("2-3"))
        assertEquals("-5", evaluator.evaluate("-2-3"))
    }
    
    @Test
    fun testComplexExpression() {
        assertEquals("1", evaluator.evaluate("(10-2)÷8"))
    }
}
