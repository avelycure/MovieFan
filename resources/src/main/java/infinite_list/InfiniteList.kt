package infinite_list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect


@Composable
fun LazyListState.OnBottomReached(
    buffer: Int = 0,
    loadMore: () -> Unit
) {
    require(buffer >= 0) { "buffer cannot be negative" }

    val shouldLoadMore = remember {
        derivedStateOf {

            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1 - buffer
        }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) loadMore() }
    }
}