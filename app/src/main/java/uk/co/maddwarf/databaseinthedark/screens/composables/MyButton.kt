package uk.co.maddwarf.databaseinthedark.screens.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MyButton(
    onClick: () -> Unit,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    text: String,
) {

    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {

        if (leadingIcon!=null) {
            Icon(imageVector = leadingIcon, contentDescription = "")
        }
    Text(
        text = text,
        color = Color.LightGray
    )
        if (trailingIcon != null) {
            Icon(imageVector = trailingIcon, contentDescription = "")
        }
    }
}