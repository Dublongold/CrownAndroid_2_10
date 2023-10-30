package com.game.crownandroid_2_10.views.custom

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.game.crownandroid_2_10.R

@Composable
fun PrivacyPolicyText() {
    val scrollState = remember {
        ScrollState(0)
    }
    BoxWithConstraints {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .border(
                    1.dp,
                    Brush.linearGradient(
                        start = Offset((constraints.minWidth / 2).toFloat(), 0f),
                        end = Offset(
                            (constraints.minWidth / 2).toFloat(),
                            constraints.minHeight.toFloat()
                        ),
                        colors = listOf(
                            colorResource(id = R.color.gold_grad_start),
                            colorResource(id = R.color.gold_grad_end),
                        )
                    ),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_big_radius))
                )
                .background(colorResource(id = R.color.element_fill))
                .padding(dimensionResource(id = R.dimen.privacy_policy_text_padding))
        ) {
            Text(
                text = stringResource(id = R.string.privacy_policy_text),
                style = TextStyle(color = Color.White),
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(scrollState)
            )
        }
    }
}

@Preview(
    heightDp = 500
)
@Composable
fun MyPreview() {
    PrivacyPolicyText()
}