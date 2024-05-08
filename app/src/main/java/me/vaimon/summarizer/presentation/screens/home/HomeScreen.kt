package me.vaimon.summarizer.presentation.screens.home

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.models.SummarizedText
import me.vaimon.summarizer.presentation.navigation.NavigationDestination
import me.vaimon.summarizer.presentation.screens.components.SummarizationResultViewer
import me.vaimon.summarizer.presentation.screens.home.components.InputTextEditor
import me.vaimon.summarizer.presentation.screens.home.components.SummarizationHistoryGrid
import me.vaimon.summarizer.presentation.screens.scanner.ScannerDestination
import me.vaimon.summarizer.presentation.screens.summarization.SummarizationDestination
import me.vaimon.summarizer.presentation.theme.SummarizerTheme

object HomeScreenDestination : NavigationDestination {
    override val route = "home"
    const val SCAN_RESULT_KEY = "scanResult"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val inputText by viewModel.inputText.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val summarizationHistory by viewModel.summarizationHistory.collectAsState()

    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            navController.navigate(ScannerDestination.route)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.permission_camera_denial_warning), Toast.LENGTH_SHORT
            ).show()
        }
    }

    val scanningResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>(HomeScreenDestination.SCAN_RESULT_KEY)?.observeAsState()

    HomeBody(
        inputText = inputText,
        summarizationHistory = summarizationHistory,
        onInputTextChanged = viewModel::onInputTextChanged,
        onBtnSummarizeClick = viewModel::onBtnSummarizeClick,
        onBtnCameraClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                navController.navigate(ScannerDestination.route)
            } else {
                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        },
        onHistoryEntryClick = viewModel::onSummarizationHistoryEntryClick,
        onSummarizationModeSelected = viewModel::onSummarizationModeSelected
    )

    uiState.priorSummarizationDetails?.let {
        ModalBottomSheet(onDismissRequest = { viewModel.onHistoryEntryDetailsShown() }) {
            Text(
                stringResource(R.string.title_details),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(8.dp))
            SummarizationResultViewer(
                inputText = it.originalText,
                processedText = it.summarizedText,
                compressionRate = it.compressionRate
            )
        }
    }

    LaunchedEffect(uiState) {
        uiState.summarizationNavigationArg?.let {
            viewModel.onNavigateToSummarizationHandled()
            navController.navigate(
                SummarizationDestination.getDestinationWithArgs(it.first, it.second),
            )
        }
    }

    LaunchedEffect(scanningResult) {
        scanningResult?.value?.let {
            viewModel.onScanningResultReceived(it)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>(HomeScreenDestination.SCAN_RESULT_KEY)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    inputText: String,
    summarizationHistory: List<SummarizedText>,
    onInputTextChanged: (String) -> Unit,
    onBtnSummarizeClick: () -> Unit,
    onBtnCameraClick: () -> Unit,
    onHistoryEntryClick: (SummarizedText) -> Unit,
    onSummarizationModeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) {
        Column(modifier = modifier.padding(it)) {
            InputTextEditor(
                inputText = inputText,
                onInputTextChanged = onInputTextChanged,
                onBtnSummarizeClick = onBtnSummarizeClick,
                onBtnCameraClick = onBtnCameraClick,
                onSummarizationModeSelected = onSummarizationModeSelected
            )
            SummarizationHistoryGrid(summarizationHistory, onHistoryEntryClick)
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    SummarizerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeBody(
                inputText = "",
                summarizationHistory = emptyList(),
                onInputTextChanged = {},
                onBtnSummarizeClick = {},
                onBtnCameraClick = {},
                onHistoryEntryClick = {},
                onSummarizationModeSelected = {}
            )
        }
    }
}