package cromega.studio.web.ui.elements

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLDialogElement

@Composable
fun Dialog(
    attrs: AttrBuilderContext<HTMLDialogElement> = {},
    contentBuilder: ContentBuilder<HTMLDialogElement>?
) =
    TagElement<HTMLDialogElement>(
        tagName = "dialog",
        applyAttrs = attrs,
        content = contentBuilder
    )
