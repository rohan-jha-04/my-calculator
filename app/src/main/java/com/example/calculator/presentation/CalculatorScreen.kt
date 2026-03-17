package com.example.calculator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val buttonSpacing = 12.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            
            // Display Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = state.equation,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    fontWeight = FontWeight.Light,
                    fontSize = 60.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )

                // Error or Live Result Display
                AnimatedContent(
                    targetState = state.error ?: state.liveResult,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    }, label = "ResultAnimation"
                ) { targetText ->
                    Text(
                        text = targetText,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 32.sp,
                        color = if (state.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Button Keypad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "AC",
                    modifier = Modifier.weight(2f).aspectRatio(2f),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) { onAction(CalculatorAction.Clear) }
                
                CalculatorButton(
                    symbol = "⌫",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) { onAction(CalculatorAction.Delete) }
                
                CalculatorButton(
                    symbol = "÷",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) { onAction(CalculatorAction.Operation("÷")) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "( )", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Parenthesis) }
                CalculatorButton(symbol = "^", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Operation("^")) }
                CalculatorButton(symbol = "x", modifier = Modifier.weight(1f).aspectRatio(1f), color = MaterialTheme.colorScheme.background, textColor = MaterialTheme.colorScheme.background) { } // Placeholder for layout
                CalculatorButton(
                    symbol = "×",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) { onAction(CalculatorAction.Operation("×")) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "7", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(7)) }
                CalculatorButton(symbol = "8", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(8)) }
                CalculatorButton(symbol = "9", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(9)) }
                CalculatorButton(
                    symbol = "-",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) { onAction(CalculatorAction.Operation("-")) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "4", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(4)) }
                CalculatorButton(symbol = "5", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(5)) }
                CalculatorButton(symbol = "6", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(6)) }
                CalculatorButton(
                    symbol = "+",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) { onAction(CalculatorAction.Operation("+")) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "1", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(1)) }
                CalculatorButton(symbol = "2", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(2)) }
                CalculatorButton(symbol = "3", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Number(3)) }
                CalculatorButton(
                    symbol = "=",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                ) { onAction(CalculatorAction.Calculate) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "0", modifier = Modifier.weight(2f).aspectRatio(2f)) { onAction(CalculatorAction.Number(0)) }
                CalculatorButton(symbol = ".", modifier = Modifier.weight(1f).aspectRatio(1f)) { onAction(CalculatorAction.Decimal) }
                // Equal occupies double height, so we leave an empty space or layout tricks depending on grid. Here it's a grid row.
                CalculatorButton(
                    symbol = "=",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                ) { onAction(CalculatorAction.Calculate) }
            }
        }
    }
}
