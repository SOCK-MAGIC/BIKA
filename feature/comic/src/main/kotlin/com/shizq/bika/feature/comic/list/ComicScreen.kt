package com.shizq.bika.feature.comic.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.HideSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.ui.comicCardItems

@Composable
fun ComicScreen(component: ComicListComponent, navigationToComicInfo: (String) -> Unit) {
    val comicsPagingItems = component.comicFlow.collectAsLazyPagingItems()
    val categoryVisibilityUiState by component.categoryVisibilityUiState.collectAsStateWithLifecycle()
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
        onTopAppBarActionClick = { showSealTagDialog = true },
        showSortDialog = showSortDialog,
        onActionSortDialogClick = { showSortDialog = true },
        categoryVisibilityUiState = categoryVisibilityUiState,
        onChangeCategoryState = component::updateCategoryState,
        currentPage = component.currentPage,
        totalPage = component.totalPage,
        setCurrentPage = { component.currentPage = it },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComicContent(
    lazyPagingItems: LazyPagingItems<ComicResource>,
    navigationToComicInfo: (String) -> Unit,
    showSealCategoriesDialog: Boolean,
    onDismissed: () -> Unit,
    onTopAppBarActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    showSortDialog: Boolean,
    onActionSortDialogClick: () -> Unit,
    categoryVisibilityUiState: Map<String, Boolean>,
    onChangeCategoryState: (String, Boolean) -> Unit,
    currentPage: Int,
    totalPage: Int,
    setCurrentPage: (Int) -> Unit,
) {
    if (showSealCategoriesDialog) {
        SettingsDialog(categoryVisibilityUiState, onChangeCategoryState) { onDismissed() }
    }
    if (showSortDialog) {
        SortDialog { onDismissed() }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                {},
                actions = {
                    IconButton(onActionSortDialogClick) {
                        Icon(Icons.AutoMirrored.Rounded.Sort, "sort")
                    }
                    IconButton(onTopAppBarActionClick) {
                        Icon(Icons.Rounded.HideSource, "hide tag")
                    }
                    val focusRequester = remember { FocusRequester.Default }
                    Row(
                        Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { focusRequester.requestFocus() },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BasicTextField(
                            currentPage.toString(),
                            {
                                if (it.isDigitsOnly() && it.isNotBlank()) {
                                    setCurrentPage(it.toIntOrNull() ?: 1)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .focusRequester(focusRequester),
                            singleLine = true,
                        )
                        Text(" / $totalPage")
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            comicCardItems(lazyPagingItems) {
                navigationToComicInfo(it)
            }
        }
    }
}
