package com.shizq.bika.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen(component: SearchComponent, onBackClick: () -> Unit) {
    val recentSearchQueriesUiState by component.recentSearchQueriesUiState.collectAsStateWithLifecycle()
    // val searchResultUiState by component.searchResultUiState.collectAsStateWithLifecycle()
    val searchQuery by component.searchQuery.collectAsStateWithLifecycle()
    SearchContent(
        modifier = Modifier,
        searchQuery = searchQuery,
        recentSearchesUiState = RecentSearchQueriesUiState.Loading,
        searchResultUiState = SearchResultUiState.Loading,
        onSearchQueryChanged = component::onSearchQueryChanged,
        onSearchTriggered = component::onSearchTriggered,
        onClearRecentSearches = {},
        onBackClick = onBackClick,
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    recentSearchesUiState: RecentSearchQueriesUiState = RecentSearchQueriesUiState.Loading,
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    onClearRecentSearches: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        SearchToolbar(
            onBackClick = onBackClick,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
        )
    }
    when (searchResultUiState) {
        SearchResultUiState.Loading,
        SearchResultUiState.LoadFailed,
        -> Unit

        SearchResultUiState.SearchNotReady -> Unit // SearchNotReadyBody()
        SearchResultUiState.EmptyQuery,
        -> {
            if (recentSearchesUiState is RecentSearchQueriesUiState.Success) {
                // RecentSearchesBody(
                //     onClearRecentSearches = onClearRecentSearches,
                //     onRecentSearchClicked = {
                //         onSearchQueryChanged(it)
                //         onSearchTriggered(it)
                //     },
                //     recentSearchQueries = recentSearchesUiState.recentQueries.map { it.query },
                // )
            }
        }

        is SearchResultUiState.Success -> {
            // if (searchResultUiState.isEmpty()) {
            // EmptySearchResultBody(
            //     searchQuery = searchQuery,
            //     onInterestsClick = onInterestsClick,
            // )
            if (recentSearchesUiState is RecentSearchQueriesUiState.Success) {
                // RecentSearchesBody(
                //     onClearRecentSearches = onClearRecentSearches,
                //     onRecentSearchClicked = {
                //         onSearchQueryChanged(it)
                //         onSearchTriggered(it)
                //     },
                //     recentSearchQueries = recentSearchesUiState.recentQueries.map { it.query },
                // )
            }
            // } else {
            // SearchResultBody(
            //     searchQuery = searchQuery,
            //     topics = searchResultUiState.topics,
            //     newsResources = searchResultUiState.newsResources,
            //     onSearchTriggered = onSearchTriggered,
            //     onTopicClick = onTopicClick,
            //     onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged,
            //     onNewsResourceViewed = onNewsResourceViewed,
            //     onFollowButtonClick = onFollowButtonClick,
            // )
            // }
        }
    }
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
}

@Composable
private fun SearchToolbar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "back",
            )
        }
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
        )
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear search text",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
}
