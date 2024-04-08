package me.vaimon.summarizer.presentation.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.navigation.NavigationDestination
import me.vaimon.summarizer.presentation.screens.summarization.SummarizationDestination
import me.vaimon.summarizer.presentation.theme.OnSurfaceSecondary
import me.vaimon.summarizer.presentation.theme.SummarizerTheme
import me.vaimon.summarizer.presentation.theme.secondaryBackground
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object HomeScreenDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val inputText by viewModel.inputText.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    HomeBody(
        inputText = inputText,
        onInputTextChanged = viewModel::onInputTextChanged,
        onBtnSummarizeClick = viewModel::onBtnSummarizeClick
    )

    LaunchedEffect(key1 = uiState) {
        uiState.summarizationNavigationArg?.let {
            viewModel.onNavigateToSummarizationHandled()
            navController.navigate(
                SummarizationDestination.getDestinationWithArg(it)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    inputText: String,
    onInputTextChanged: (String) -> Unit,
    onBtnSummarizeClick: () -> Unit,
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
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                val clipboardManager: ClipboardManager = LocalClipboardManager.current

                TextField(
                    value = inputText,
                    onValueChange = onInputTextChanged,
                    placeholder = { Text(stringResource(R.string.placeholder_input_text)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryBackground
                    ),
                    minLines = 4,
                    maxLines = 8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    SecondaryActionButton(
                        icon = R.drawable.ic_paste,
                        enabled = clipboardManager.hasText(),
                        onClick = {
                            clipboardManager.getText()?.text?.let(onInputTextChanged)
                        })
                    PrimaryActionButton(
                        icon = R.drawable.ic_compress,
                        onClick = onBtnSummarizeClick,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    SecondaryActionButton(icon = R.drawable.ic_camera, onClick = { /*TODO*/ })
                }
            }
            SummarizationHistoryGrid()
        }
    }
}

@Composable
fun PrimaryActionButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        colors = IconButtonDefaults.filledIconButtonColors(),
        onClick = onClick,
        modifier = modifier.size(56.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Composable
fun SecondaryActionButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryBackground
        ),
        enabled = enabled,
        onClick = onClick,
        modifier = modifier.size(42.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Composable
fun SummarizationHistoryGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.title_summarization_history),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            modifier = Modifier
        ) {
            //Fixme
            items(18) {
                SummarizedItem(
                    timestamp = LocalDateTime.now(),
                    textResult = stringResource(R.string.lorem_ipsum_dolor_sit_amet)
                )
            }
        }
    }
}

@Composable
fun SummarizedItem(
    timestamp: LocalDateTime,
    textResult: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.width(256.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Text(
                text = timestamp.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm")),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = textResult,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceSecondary,
                overflow = TextOverflow.Ellipsis
            )
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
                onInputTextChanged = {},
                onBtnSummarizeClick = {}
            )
        }
    }
}