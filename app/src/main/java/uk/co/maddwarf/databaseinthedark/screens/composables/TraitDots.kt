package uk.co.maddwarf.databaseinthedark.screens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.InfoDialog

@Composable
fun TraitDots(
    traitName: String,
    traitValue: Int,
    maxValue: Int,
    infoText: String = "",
    onDotClicked: (Int) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(15.dp))
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color.LightGray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$traitName: ")
            for (dot in 1..maxValue) {
                TraitDot(index = dot, rank = traitValue, onDotClicked = onDotClicked)
            }
        }
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            modifier = Modifier
                .padding(5.dp)
                .clickable(
                    onClick = { showDialog.value = true }
                )
        )
        if (showDialog.value) {
            InfoDialog(
                open = showDialog.value,
                onDismiss = { showDialog.value = false },
                title = traitName,
                body = infoText
            )
        }
    }
}//end TraitDots

@Composable
fun TraitDot(
    index: Int,
    rank: Int,
    onDotClicked: (Int) -> Unit
) {
    val image: ImageVector = if (index > rank) {
        Icons.Filled.Clear
    } else {
        Icons.Filled.AddCircle
    }
    Icon(
        imageVector = image,
        contentDescription = null,
        modifier = Modifier
            .size(20.dp)
            .clickable {
                if (rank == 1 && index == 1) {
                    onDotClicked(0)
                } else {
                    onDotClicked(index)
                }
            }
    )
}