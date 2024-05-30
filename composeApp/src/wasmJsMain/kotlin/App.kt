import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import danigutiadanportfolio.composeapp.generated.resources.Res
import danigutiadanportfolio.composeapp.generated.resources.ic_hamburger
import danigutiadanportfolio.composeapp.generated.resources.ic_profile
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App() {
    val windowSizeClass = calculateWindowSizeClass()
    MaterialTheme {
        var borderRadius: Float by remember { mutableStateOf(0f) }
            LazyColumn(
                Modifier.fillMaxSize().background(brush = Brush.radialGradient(colors = listOf(
                    Color(198, 189, 178, 255),
                    Color(123, 89, 69, 255)
                ), radius = borderRadius)).padding(horizontal = 50.dp, vertical = 25.dp).onGloballyPositioned { coordinates ->
                    borderRadius =
                        ((coordinates.size.width.takeIf { it > coordinates.size.height } ?: (coordinates.size.height)) / 2).plus(100).toFloat()
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                topBar(windowSizeClass)
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
                introduction(windowSizeClass)
            }
        }
    }

@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.introduction(windowSizeClass: WindowSizeClass) {
    val nameSize: TextUnit = when(windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 40.sp
        WindowWidthSizeClass.Compact -> 32.sp
        else -> 50.sp
    }
    item {
        AnimatedContent {
            FlowRow(horizontalArrangement = Arrangement.Center, verticalArrangement = Arrangement.Center) {
                Column(modifier = Modifier.padding(horizontal = 50.dp, vertical = 25.dp)) {
                    Text("Daniel Gutiérrez Adán", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = nameSize), modifier = Modifier)
                    Text(modifier = Modifier.padding (top = 20.dp), text = "Desarrollador Android", style = TextStyle(fontSize = 25.sp))
                    Text(
                        modifier = Modifier.widthIn(0.dp, 600.dp).padding(top = 20.dp),
                        text = "Apasionado de las tecnologías móviles y de los nuevos desafios que se me planteen",
                        style = TextStyle(fontSize = 18.sp)
                    )
                    Row(modifier = Modifier.fillMaxWidth().takeIf { windowSizeClass.widthSizeClass != Expanded } ?: Modifier,
                        horizontalArrangement = Arrangement.Center.takeIf { windowSizeClass.widthSizeClass != Expanded } ?: Arrangement.Start) {
                        ViewHelper.socialDataList.forEach { data ->
                            IconButton(
                                modifier = Modifier.size(48.dp).padding(end = 10.dp),
                                onClick = { window.open(data.url) }) { Image(painter = painterResource(data.icon), data.name) }

                        }
                    }
                }
                Image(
                    painter = painterResource(Res.drawable.ic_profile),
                    contentDescription = "avatar",
                    modifier = Modifier.size(200.dp).border(border = BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(100.dp)).clip(
                        RoundedCornerShape(100.dp)
                    )
                )
            }
        }
    }
}

@Composable
private fun AnimatedContent(content: @Composable () -> Unit) {
    val animationState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(animationState, enter = fadeIn(animationSpec = tween(1500)), exit = fadeOut(), content = {content.invoke()})
}

private fun LazyListScope.topBar(windowSizeClass: WindowSizeClass) {
    val topbarTitleSize: TextUnit = when(windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 24.sp
        WindowWidthSizeClass.Compact -> 20.sp
        else -> 30.sp
    }
    item {
        var showHamburger: Boolean by remember { mutableStateOf(false) }
        var showExpandedMenu: Boolean by remember { mutableStateOf(false) }
        var topBarWidth: LayoutCoordinates? by remember { mutableStateOf(null) }
        MeasureUnconstrainedViewWidth(viewToMeasure = { TopbarOptionsMenu() }, content = { toolbarOptionsWidth ->
            AnimatedContent {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            modifier = Modifier.padding(end = 100.dp),
                            text = "Danigutiadan",
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = topbarTitleSize,
                            color = Color.White
                        )
                        Box(modifier = Modifier.weight(1f).onGloballyPositioned { coordinates ->
                            topBarWidth = coordinates
                            showHamburger = coordinates.size.width.dp < toolbarOptionsWidth
                        }, contentAlignment = Alignment.CenterEnd)
                        {
                            if(showHamburger) {
                                IconButton(onClick = {
                                    showExpandedMenu = !showExpandedMenu
                                }) {
                                    Image(modifier = Modifier.size(40.dp),painter = painterResource(Res.drawable.ic_hamburger), contentDescription = null)
                                }

                            } else {
                                TopbarOptionsMenu()
                            }
                        }
                    }
                    AnimatedVisibility(visible = showExpandedMenu) {
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Home".uppercase())
                            Text(text = "About".uppercase())
                            Text(text = "Skills".uppercase())
                            Text(text = "Portfolio".uppercase())
                            Text(text = "Contact".uppercase())
                        }
                    }
                }
            }
        })
    }
}

@Composable
fun TopbarOptionsMenu(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = "Home", style = TextStyle(fontSize = 25.sp, color = Color.White))
        Text(modifier = Modifier.padding(start = 30.dp), text = "About", style = TextStyle(fontSize = 25.sp, color = Color.White))
        Text(modifier = Modifier.padding(start = 30.dp), text = "Skills", style = TextStyle(fontSize = 25.sp, color = Color.White))
        Text(modifier = Modifier.padding(start = 30.dp), text = "Portfolio", style = TextStyle(fontSize = 25.sp, color = Color.White))
        Text(modifier = Modifier.padding(start = 30.dp), text = "Contact", style = TextStyle(fontSize = 25.sp, color = Color.White))
    }
}
@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure).firstOrNull()
            ?.measure(Constraints())?.width?.toDp() ?: 0.dp

        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }.firstOrNull()?.measure(constraints)
        layout(contentPlaceable?.width ?: 0, contentPlaceable?.height ?: 0) {
            contentPlaceable?.place(0, 0)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowCustom(modifier: Modifier, onOverflow: (Boolean) -> Unit, content: @Composable () -> Unit) {
    var height: Int? by remember { mutableStateOf(0) }
    var recomposedForFirstTime: Boolean by remember { mutableStateOf(true) }
    FlowRow(modifier = modifier.onGloballyPositioned { coordinates ->
        if (coordinates.size.height != height && recomposedForFirstTime.not()) {
            height = coordinates.size.height
            onOverflow(true)
        } else {
            onOverflow(false)
            recomposedForFirstTime = false
        }
    }) {
        content()
    }
}

@Composable
internal fun DimensionSubcomposeLayout(
    modifier: Modifier = Modifier,
    mainContent: @Composable () -> Unit,
    dependentContent: @Composable (DpSize) -> Unit
) {

    val density = LocalDensity.current
    SubcomposeLayout(
        modifier = modifier
    ) { constraints: Constraints ->

        // Subcompose(compose only a section) main content and get Placeable
        val mainPlaceable: Placeable = subcompose(SlotsEnum.Main, mainContent)
            .map {
                it.measure(constraints.copy(minWidth = 0, minHeight = 0))
            }.first()


        val dependentPlaceable: Placeable =
            subcompose(SlotsEnum.Dependent) {
                dependentContent(
                    DpSize(
                        density.run { mainPlaceable.width.toDp() },
                        density.run { mainPlaceable.height.toDp() }
                    )
                )
            }
                .map { measurable: Measurable ->
                    measurable.measure(constraints)
                }.first()
        layout(mainPlaceable.width, mainPlaceable.height) {
            dependentPlaceable.placeRelative(0, 0)
        }
    }
}
enum class SlotsEnum { Main, Dependent }


