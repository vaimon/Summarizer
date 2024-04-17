package me.vaimon.summarizer.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints

@Composable
fun Modifier.swapAxes() =
    graphicsLayer {
        rotationZ = 270f
        transformOrigin = TransformOrigin(0f, 0f)
    }
        .layout { measurable, constraints ->
            val placeable = measurable.measure(
                Constraints(
                    minWidth = constraints.minHeight,
                    maxWidth = constraints.maxHeight,
                    minHeight = constraints.minWidth,
                    maxHeight = constraints.maxHeight,
                )
            )
            layout(placeable.height, placeable.width) {
                placeable.place(-placeable.width, 0)
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
