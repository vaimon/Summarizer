package me.vaimon.summarizer.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

@Composable
fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

@Composable
fun Modifier.conditional(
    condition: Boolean,
    ifTrue: @Composable Modifier.() -> Modifier,
    ifFalse: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else {
        then(ifFalse(Modifier))
    }
}
