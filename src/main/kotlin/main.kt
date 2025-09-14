package cromega.studio.web

import cromega.studio.web.ui.page.Controller
import cromega.studio.web.ui.page.GlobalStyleSheet
import cromega.studio.web.ui.page.Page
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

fun main()
{
    renderComposable(rootElementId = "root")
    {
        Style(GlobalStyleSheet)
        Page(Controller()).Body()
    }
}
