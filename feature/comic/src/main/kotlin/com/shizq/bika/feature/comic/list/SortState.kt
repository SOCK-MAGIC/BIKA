package com.shizq.bika.feature.comic.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shizq.bika.core.designsystem.component.Dialog
import com.shizq.bika.core.designsystem.component.DialogSelectRow
import com.shizq.bika.core.network.model.Sort

@Composable
fun SortDialog(onDismiss: () -> Unit) {
    Dialog(title = { Text(text = "排序") }, onDismiss = onDismiss) {
        HorizontalDivider()
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Column(Modifier.selectableGroup()) {
                Sort.entries.forEach { s ->
                    DialogSelectRow(s.description, SortState.sort == s) {
                        SortState.sort = s
                    }
                }
            }
            HorizontalDivider(Modifier.padding(top = 8.dp))
        }
    }
}

internal object SortState {
    var sort by mutableStateOf(Sort.SORT_DEFAULT)
    val flow = snapshotFlow { sort }
}
