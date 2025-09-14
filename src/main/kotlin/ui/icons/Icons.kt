package cromega.studio.web.ui.icons

import cromega.studio.web.abstracts.IconsConverter
import cromega.studio.web.enums.ActionButtons

object Icons : IconsConverter<String>()
{
    override val convertions: Map<ActionButtons, String> =
        mapOf(
            ActionButtons.AREA to "icons/area.svg",
            ActionButtons.DOWNLOAD to "icons/download.svg",
            ActionButtons.DRAW to "icons/draw.svg",
            ActionButtons.PALLETE to "icons/pallete.svg",
            ActionButtons.PIXELATE to "icons/pixelate.svg",
            ActionButtons.UNDO to "icons/undo.svg",
            ActionButtons.UPLOAD to "icons/upload.svg",
        )
}