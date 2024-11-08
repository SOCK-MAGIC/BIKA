package com.shizq.bika.feature.comic.list

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.ui.comicCardItems

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ComicScreen(
    component: ComicListComponent,
    navigationToComicInfo: (String) -> Unit,
) {
    val comicsPagingItems = component.comicFlow.collectAsLazyPagingItems()
    val hobbyUiState by component.hobbyUiState.collectAsStateWithLifecycle()
    var showSealTagDialog by rememberSaveable { mutableStateOf(false) }
    var showSortDialog by rememberSaveable { mutableStateOf(false) }
    ComicContent(
        comicsPagingItems,
        navigationToComicInfo = navigationToComicInfo,
        showSealCategoriesDialog = showSealTagDialog,
        onDismissed = {
            showSealTagDialog = false
            showSortDialog = false
        },
        onActionClickHobbyDialog = { showSealTagDialog = true },
        showSortDialog = showSortDialog,
        onActionClickSortDialog = { showSortDialog = true },
        hobbyUiState = hobbyUiState,
        onChangeHobbiesSelection = component::updateHobbiesSelection,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun ComicContent(
    lazyPagingItems: LazyPagingItems<ComicResource>,
    hobbyUiState: HobbyUiState,
    showSealCategoriesDialog: Boolean,
    showSortDialog: Boolean,
    modifier: Modifier = Modifier,
    onActionClickHobbyDialog: () -> Unit,
    onDismissed: () -> Unit,
    onActionClickSortDialog: () -> Unit,
    navigationToComicInfo: (String) -> Unit,
    onChangeHobbiesSelection: (String, Boolean) -> Unit,
) {
    if (showSealCategoriesDialog) {
        HobbyDialog(hobbyUiState, onChangeHobbiesSelection) { onDismissed() }
    }
    if (showSortDialog) {
        SortDialog { onDismissed() }
    }
    var showTextFieldDialog by remember { mutableStateOf(false) }
    if (showTextFieldDialog) {
        InputDialog({ showTextFieldDialog = false }) {
            PageMetaData.redirectedPage = it
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                {},
                actions = {
                    IconButton(onActionClickSortDialog) {
                        Icon(BikaIcons.Sort, "sort")
                    }
                    IconButton(onActionClickHobbyDialog) {
                        Icon(BikaIcons.HideSource, "hide tag")
                    }
                    val pageMetadata = PageMetaData.metadata
                    if (pageMetadata != null) {
                        Text(
                            "${pageMetadata.currentPage} / ${pageMetadata.totalPages}",
                            modifier = Modifier.clickable { showTextFieldDialog = true },
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            comicCardItems(
                lazyPagingItems,
            ) {
                navigationToComicInfo(it)
            }
        }
    }
}

@Composable
private fun InputDialog(onDismiss: () -> Unit, onClick: (Int?) -> Unit) {
    var index by remember { mutableStateOf<Int?>(null) }
    AlertDialog(
        onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {
            Text(
                text = "确定",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onDismiss()
                        onClick(index)
                    },
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    index?.toString() ?: "",
                    {
                        index = if (it.isDigitsOnly() && it.isNotBlank()) {
                            it.toIntOrNull() ?: 1
                        } else {
                            null
                        }
                    },
                    modifier = Modifier.onKeyEvent {
                        if (it.key == Key.Enter) {
                            onClick(index)
                            true
                        } else {
                            false
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                )
            }
        },
    )
}
