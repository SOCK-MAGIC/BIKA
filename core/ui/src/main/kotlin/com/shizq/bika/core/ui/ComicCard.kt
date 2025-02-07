package com.shizq.bika.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.model.ComicResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ComicCard(
    comicResource: ComicResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Card(
                onClick = onClick,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .sharedBounds(
                        rememberSharedContentState(
                            ComicCardSharedElementKey(
                                comicResource.id,
                                ComicCardSharedElementType.Bounds,
                            ),
                        ),
                        animatedVisibilityScope,
                        boundsTransform = BikaDetailBoundsTransform,
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                    )
                    .animateEnterExit(
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                    ),
            ) {
                Row(Modifier.wrapContentSize()) {
                    DynamicAsyncImage(
                        comicResource.imageUrl,
                        null,
                        Modifier
                            .size(120.dp, 180.dp)
                            .sharedElement(
                                rememberSharedContentState(
                                    ComicCardSharedElementKey(
                                        comicResource.id,
                                        ComicCardSharedElementType.Image,
                                    ),
                                ),
                                animatedVisibilityScope,
                            ),
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                    ) {
                        Text(
                            comicResource.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            color = if (comicResource.finished) MaterialTheme.colorScheme.primary else Color.Unspecified,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(
                                    ComicCardSharedElementKey(
                                        comicResource.id,
                                        ComicCardSharedElementType.Title,
                                    ),
                                ),
                                animatedVisibilityScope,
                            ),
                        )
                        Text(
                            "${comicResource.epsCount}E/${comicResource.pagesCount}P",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                        Text(
                            comicResource.author,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .sharedElement(
                                    rememberSharedContentState(
                                        ComicCardSharedElementKey(
                                            comicResource.id,
                                            ComicCardSharedElementType.Author,
                                        ),
                                    ),
                                    animatedVisibilityScope,
                                ),
                        )
                        Text(
                            comicResource.categories.fastJoinToString(" "),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(
                                    ComicCardSharedElementKey(
                                        comicResource.id,
                                        ComicCardSharedElementType.Tagline,
                                    ),
                                ),
                                animatedVisibilityScope,
                            ),
                        )
                        Spacer(Modifier.weight(1f))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Image(Icons.Default.Favorite, null, Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(comicResource.likeCount.toString())
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@DevicePreviews
@Composable
private fun ComicCardPreview() {
    SharedTransitionLayout {
        AnimatedContent("dddddddddd") {
            ComicCard(
                ComicResource(
                    id = it,
                    imageUrl = "",
                    title = "petentiumdddddddddddddddddddddddddddddddddddddddddddddddddd",
                    author = "etiam",
                    categories = listOf("tractatos", "aractatos"),
                    finished = false,
                    epsCount = 7864,
                    pagesCount = 2924,
                    likeCount = 4109,
                ),
                onClick = {},
            )
        }
    }
}
