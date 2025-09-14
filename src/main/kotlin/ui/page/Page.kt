package cromega.studio.web.ui.page

import androidx.compose.runtime.Composable
import cromega.studio.web.enums.ActionButtons
import cromega.studio.web.enums.EditingActions
import cromega.studio.web.enums.ResourcesCredits
import cromega.studio.web.ui.components.IconButton
import cromega.studio.web.ui.elements.Dialog
import cromega.studio.web.ui.icons.Icons
import kotlinx.browser.document
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.accept
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.*
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.MouseEventInit
import org.w3c.files.get

class Page(val controller: Controller)
{
    init {
        document.addEventListener(
            "DOMContentLoaded",
            EventListener {
                controller.dialog = document.getElementById("dialog") as HTMLDialogElement
                controller.canvas = document.getElementById("canvas") as HTMLCanvasElement
                controller.pallete = document.getElementById("pallete") as HTMLInputElement
                controller.upload = document.getElementById("upload") as HTMLInputElement

                controller.canvasContext = controller.canvas.getContext("2d") as CanvasRenderingContext2D
            }
        )
    }

    @Composable
    fun Body() =
        Div(
            attrs = {
                classes(GlobalStyleSheet.bodyContainer)
            }
        ) {
            PageHeader()
            PageMain()
            PageFooter()
            PageAside()
        }

    @Composable
    fun PageHeader() =
        Header(
            attrs = {
                classes(GlobalStyleSheet.header)
            }
        ) {
            A(
                href = "https://cromega08.github.io/Portfolio/",
                attrs = {
                    target(ATarget.Blank)
                }
            ) {
                Img(
                    "images/cromega_logo_transparent.webp",
                    attrs = {
                        classes(GlobalStyleSheet.icon)
                    }
                )
            }

            IconButton(
                icon = "icons/help.svg",
                alt = "Help",
                onClick = { controller.dialog.showModal() }
            )
        }

    @Composable
    fun PageMain() =
        Main(
            attrs = {
                classes(GlobalStyleSheet.main)
            }
        ) {
            H1(
                attrs = {
                    style {
                        color(controller.selectedColor)
                        display(
                            if (controller.fileSelected.isBlank()) DisplayStyle.Block
                            else DisplayStyle.None
                        )
                        property("text-shadow", "0 0 2.5px white")
                    }
                }
            ) { Text("No Image") }
            Canvas(
                attrs = {
                    id("canvas")
                    classes(GlobalStyleSheet.canvas)
                    style {
                        border {
                            style(LineStyle.Solid)
                            color(controller.selectedColor)
                            width(5.px)
                        }
                        display(
                            if (controller.fileSelected.isNotBlank()) DisplayStyle.Block
                            else DisplayStyle.None
                        )
                    }
                    onMouseDown {
                        controller.beforeDrawing()
                        when(controller.currentEditingAction)
                        {
                            EditingActions.DRAW -> controller.updateDrawingCoordinates(it.offsetX, it.offsetY)
                            else -> controller.startAreaCoordinates(it.offsetX, it.offsetY)
                        }
                    }
                    onTouchStart {
                        it.preventDefault()
                        it.stopPropagation()
                        val touch = it.touches.asList().last()
                        val mouseEvent = MouseEvent(
                            type = "mousedown",
                            MouseEventInit(
                                clientX = touch.clientX,
                                clientY = touch.clientY
                            )
                        )
                        controller.canvas.dispatchEvent(mouseEvent)
                    }
                    onMouseMove {
                        if (controller.drawing)
                        {
                            when(controller.currentEditingAction)
                            {
                                EditingActions.DRAW ->
                                    {
                                        controller.updateDrawingCoordinates(it.offsetX, it.offsetY)
                                        controller.drawOnCanvas()
                                    }
                                else -> controller.updateAreaCoordinates(it.offsetX, it.offsetY)
                            }
                        }
                    }
                    onTouchMove {
                        it.preventDefault()
                        it.stopPropagation()
                        val touch = it.touches.asList().last()
                        val mouseEvent = MouseEvent(
                            type = "mousemove",
                            MouseEventInit(
                                clientX = touch.clientX,
                                clientY = touch.clientY
                            )
                        )
                        controller.canvas.dispatchEvent(mouseEvent)
                    }
                    onMouseUp {
                        when(controller.currentEditingAction)
                        {
                            EditingActions.PIXELATE -> controller.pixelateAreaOnCanvas()
                            EditingActions.DRAW -> {}
                            EditingActions.AREA -> controller.colorAreaOnCanvas()
                        }
                        controller.afterDrawing()
                    }
                    onMouseOut {
                        when(controller.currentEditingAction)
                        {
                            EditingActions.PIXELATE -> controller.pixelateAreaOnCanvas()
                            EditingActions.DRAW -> {}
                            EditingActions.AREA -> controller.colorAreaOnCanvas()
                        }
                        controller.afterDrawing()
                    }
                    onTouchEnd {
                        it.preventDefault()
                        it.stopPropagation()
                        val mouseEvent = MouseEvent(type = "mouseup")
                        controller.canvas.dispatchEvent(mouseEvent)
                    }
                },
                content = {}
            )
        }

    @OptIn(ExperimentalComposeWebApi::class)
    @Composable
    fun PageFooter() =
        Footer(
            attrs = {
                classes(GlobalStyleSheet.footer)
            }
        ) {
            ActionButtons.entries.forEach { actionButton ->
                IconButton(
                    buttonAttrs = {
                        title(actionButton.text)
                        style {
                            if (actionButton == ActionButtons.PALLETE)
                            {
                                backgroundColor(controller.selectedColor)
                            }
                        }
                    },
                    iconAttrs = {
                        style {
                            if (actionButton == ActionButtons.PALLETE)
                            {
                                filter {
                                    dropShadow(0.px, 0.px, 2.px, Color.white)
                                }
                            }
                        }
                    },
                    icon = Icons.getIcon(actionButton = actionButton),
                    alt = actionButton.text,
                    onClick = {
                        when(actionButton) {
                            ActionButtons.UPLOAD -> controller.upload.click()
                            ActionButtons.UNDO -> controller.restoreCanvasState()
                            ActionButtons.PIXELATE -> controller.currentEditingAction = EditingActions.PIXELATE
                            ActionButtons.DRAW -> controller.currentEditingAction = EditingActions.DRAW
                            ActionButtons.AREA -> controller.currentEditingAction = EditingActions.AREA
                            ActionButtons.PALLETE -> controller.pallete.click()
                            ActionButtons.DOWNLOAD -> controller.downloadCanvasContent()
                        }
                    }
                ) {
                    if (actionButton == ActionButtons.UPLOAD)
                    {
                        FileInput(
                            attrs = {
                                id("upload")
                                accept("image/,.png,.jpg,.jpeg,.webp")
                                style {
                                    display(DisplayStyle.Unset)
                                    visibility(VisibilityStyle.Hidden)
                                    position(Position.Absolute)
                                }
                                onChange {
                                    it.target.files!![0]?.let { file ->
                                        controller.fileReader.readAsDataURL(file)
                                    }
                                }
                            }
                        )
                    }

                    if (actionButton == ActionButtons.PALLETE)
                    {
                        Input(InputType.Color) {
                            id("pallete")
                            style {
                                display(DisplayStyle.Unset)
                                visibility(VisibilityStyle.Hidden)
                                position(Position.Absolute)
                            }
                            onInput {
                                val numbers = it.value.removePrefix("#")
                                val red: Short = numbers.substring(0 .. 1).toShort(16)
                                val green: Short = numbers.substring(2 .. 3).toShort(16)
                                val blue: Short = numbers.substring(4 .. 5).toShort(16)
                                controller.selectedColor = rgb(r = red, g = green, b = blue)
                            }
                        }
                    }
                }
            }
        }

    @Composable
    fun PageAside() =
        Dialog(
            attrs = {
                id("dialog")
                classes(GlobalStyleSheet.dialog)
            }
        ) {
            Div(
                attrs = {
                    style {
                        width(100.percent)
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Row)
                        justifyContent(JustifyContent.SpaceBetween)
                        alignItems(AlignItems.Center)
                    }
                }
            ) {
                H1 { Text("Credits") }

                Button(
                    attrs = {
                        onClick {
                            controller.dialog.close("")
                        }
                    }
                ) {
                    Text("X")
                }
            }

            P {
                Text("Current website was developed by ")
                A(
                    href = "https://github.com/Cromega08",
                    attrs = {
                        target(ATarget.Blank)
                    }
                ) { Text("Cromega08") }
                Text(" and under the ")
                A(
                    href = "https://spdx.org/licenses/Apache-2.0.html",
                    attrs = {
                        target(ATarget.Blank)
                    }
                ) { Text("Apache License v2.0") }
                Text(" (inherited from the ")
                A(
                    href = "https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials#html",
                    attrs = {
                        target(ATarget.Blank)
                    }
                ) { Text("Compose HTML Library") }
                Text(").")
            }
            P {
                Text("Also, many graphic resources from ")
                A(
                    href = "https://www.flaticon.com",
                    attrs = { target(ATarget.Blank) }
                ) {
                    Text("Flaticon")
                }
                Text(" were used, and the credits for this ones are:")
            }
            Ul {
                ResourcesCredits.entries.forEach { credits ->
                    Li {
                        A(
                            href = credits.href,
                            attrs = {
                                target(ATarget.Blank)
                            }
                        ) {
                            Text(credits.text)
                        }
                    }
                }
            }
        }
}