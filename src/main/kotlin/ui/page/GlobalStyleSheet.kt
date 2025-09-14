package cromega.studio.web.ui.page

import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.gridArea
import org.jetbrains.compose.web.css.gridTemplateAreas
import org.jetbrains.compose.web.css.gridTemplateColumns
import org.jetbrains.compose.web.css.gridTemplateRows
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.media
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.overflowX
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.vmax
import org.jetbrains.compose.web.css.vmin
import org.jetbrains.compose.web.css.width

object GlobalStyleSheet : StyleSheet()
{
    init {
        "body" style {
            backgroundColor(Color.black)
        }
    }

    val bodyContainer by style {
        top(0.px)
        left(0.px)
        display(DisplayStyle.Grid)
        gridTemplateColumns("repeat(1, 100%)")
        gridTemplateRows("14.3vh 69.33vh 14.3vh")
        gridTemplateAreas(
            "h",
            "m",
            "f"
        )
        height(100.percent)
        width(100.percent)
        padding(0.px)
        overflowX("hidden")
    }

    val header by style {
        gridArea("h")
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.SpaceBetween)
        alignItems(AlignItems.Center)
        padding(2.5.vmin)
    }

    val main by style {
        gridArea("m")
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)
        padding(2.5.vmin)
    }

    val footer by style {
        gridArea("f")
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Row)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)
        padding(2.5.vmin)
        gap(2.vmin)
    }

    val canvas by style {
        height(95.percent)
        width(auto)
        padding(2.vmin)
        property("box-shadow", "0 0 5px white")

        media("(orientation: portrait)") {
            self style {
                height(auto)
                width(95.percent)
            }
        }
    }

    val iconButton by style {
        width(5.vmax)
        height(5.vmax)
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)
    }

    val icon by style {
        width(4.vmax)
        height(4.vmax)
    }

    val dialog by style {
        backgroundColor(Color.white)
        gap(2.5.vmin)
        padding(2.5.vmin)
        overflowX("hidden")
        overflowY("scroll")

        self + "::backdrop" style {
            backgroundColor(Color.black)
            opacity(80.percent)
        }

        self + "> p" style {
            width(100.percent)
            textAlign("justify")
        }
    }
}