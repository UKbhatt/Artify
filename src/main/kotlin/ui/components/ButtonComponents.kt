package cromega.studio.web.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import cromega.studio.web.ui.page.GlobalStyleSheet
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.EventTarget

@Composable
fun IconButton(
    buttonAttrs: AttrBuilderContext<HTMLButtonElement> = {},
    iconAttrs: AttrBuilderContext<HTMLImageElement> = {},
    icon: String,
    alt: String = "",
    onClick: (SyntheticEvent<EventTarget>) -> Unit,
    contentBuilder: @Composable (ElementScope<HTMLButtonElement>.() -> Unit) = {}
) =
    Button(
        attrs = {
            classes(GlobalStyleSheet.iconButton)
            apply(buttonAttrs)
            onClick(onClick)
            onTouchStart(onClick)
        },
        content = {
            contentBuilder()
            Img(
                src = icon,
                alt = alt,
                attrs = {
                    apply(iconAttrs)
                    classes(GlobalStyleSheet.icon)
                }
            )
        }
    )
