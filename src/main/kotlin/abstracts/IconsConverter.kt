package cromega.studio.web.abstracts

import cromega.studio.web.enums.ActionButtons

abstract class IconsConverter<T>
{
    private val actionButtons = ActionButtons.entries
    protected abstract val convertions: Map<ActionButtons, T>

    fun getIcon(actionButton: ActionButtons): T = convertions[actionButton]!!
}