package com.fecrin.eroxia.presentation.screens.viewer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fecrin.eroxia.presentation.screens.viewer.ViewerViewModel
import com.patrykandpatrick.vico.compose.cartesian.AutoScrollCondition
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TelemetryChart(viewModel: ViewerViewModel) {
    val timeFmt = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    val xFmt = remember(viewModel.xSecondKey) {
        CartesianValueFormatter { context, x, _ ->
            val ts = context.model.extraStore.getOrNull(viewModel.xSecondKey)
                ?: return@CartesianValueFormatter ""
            timeFmt.format(Date(ts[x.toInt().coerceIn(ts.indices)]))
        }
    }

    val yFmt = remember {
        CartesianValueFormatter { _, v, _ ->
            when {
                v >= 1000 -> "${"%.1f".format(v / 1000)}K"
                v <= -1000 -> "-${"%.1f".format(-v / 1000)}K"
                else -> v.toInt().toString()
            }
        }
    }

    val lines = listOf(
        buildLine(Color(0xFF4FC3F7)),
        buildLine(Color(0xFF81C784)),
        buildLine(Color(0xFFFFB74D))
    )

    val scroll = rememberVicoScrollState(
        initialScroll = Scroll.Absolute.End,
        autoScrollCondition = AutoScrollCondition.OnModelGrowth
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(lines),
                ),
                startAxis = VerticalAxis.rememberStart(valueFormatter = yFmt),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = xFmt,
                    itemPlacer = HorizontalAxis.ItemPlacer.aligned(spacing = { 10 })
                )
            ),
            modelProducer = viewModel.modelProducer,
            scrollState = scroll,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun buildLine(color: Color) = LineCartesianLayer.rememberLine(
    fill = LineCartesianLayer.LineFill.single(Fill(color)),
    areaFill = LineCartesianLayer.AreaFill.single(Fill(color.copy(alpha = 0.15f))),
    stroke = LineCartesianLayer.LineStroke.Continuous(thickness = 2.dp),
    interpolator = LineCartesianLayer.Interpolator.cubic(curvature = 0.5f)
)