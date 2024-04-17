package me.vaimon.summarizer.presentation.screens.summarization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.navigation.NavigationDestinationWithArg
import me.vaimon.summarizer.presentation.screens.components.SummarizationResultViewer
import me.vaimon.summarizer.presentation.theme.SummarizerTheme
import me.vaimon.summarizer.util.PreviewData

object SummarizationDestination : NavigationDestinationWithArg<String>() {
    override val routeBase = "summarization"
    override val argName = "inputFilePath"
}

@Composable
fun SummarizationScreen(
    navController: NavController,
    viewModel: SummarizationViewModel = hiltViewModel()
) {
    val isProcessing by viewModel.processingState.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val processedText by viewModel.processedText.collectAsState()
    val compressionRate by viewModel.compressionRate.collectAsState()

    SummarizationBody(
        isProcessing = isProcessing,
        inputText = inputText,
        processedText = processedText,
        compressionRate = compressionRate,
        navigateUp = navController::navigateUp
    )
}

@Composable
private fun SummarizationBody(
    inputText: String,
    processedText: String?,
    compressionRate: Int?,
    isProcessing: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SummarizationAppBar(navigateUp = navigateUp)
        },
    ) {
        if (isProcessing) {
            Box(modifier = modifier.fillMaxSize()) {
                ProcessingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            SummarizationResultViewer(
                inputText = inputText,
                processedText = processedText ?: "",
                compressionRate = compressionRate,
                modifier = modifier.padding(it)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizationAppBar(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.title_summarization)) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.desc_back),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Composable
fun ProcessingIndicator(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_processing))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewSummarization() {
    SummarizerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SummarizationBody(
                navigateUp = {},
                isProcessing = false,
                inputText = PreviewData.getLoremIpsum(500),
                processedText = PreviewData.getLoremIpsum(100),
                compressionRate = 57
            )
        }
    }
}