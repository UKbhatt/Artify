package cromega.studio.web.ui.page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import cromega.studio.web.enums.EditingActions
import cromega.studio.web.models.AreaCoordinates
import cromega.studio.web.models.DrawingCoordinates
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Color
import org.w3c.dom.*
import org.w3c.files.FileReader

class Controller
{
    lateinit var dialog: HTMLDialogElement
    lateinit var canvas: HTMLCanvasElement
    lateinit var pallete: HTMLInputElement
    lateinit var upload: HTMLInputElement
    lateinit var canvasContext: CanvasRenderingContext2D
    val drawingCoordinates: DrawingCoordinates = DrawingCoordinates()
    var areaCoordinates: AreaCoordinates = AreaCoordinates()
    var currentEditingAction: EditingActions = EditingActions.DRAW
    var selectedColor by mutableStateOf(Color.white)
    var fileSelected by mutableStateOf("")
    val fileReader: FileReader = FileReader()
    var drawing by mutableStateOf(false)
    private val canvasStates: SnapshotStateList<ImageData> = mutableStateListOf()

    init {
        fileReader.onload = {
            val image: Image = Image()

            image.onload = {
                canvas.width = image.width
                canvas.height = image.height

                canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D

                canvasContext.drawImage(
                    image = image,
                    dx = 0.0,
                    dy = 0.0,
                    dw = image.width.toDouble(),
                    dh = image.height.toDouble()
                )

                canvasStates.clear()
                saveCanvasState()
            }

            ((it.target as FileReader).result as String).also { result ->  fileSelected = result }

            image.src = fileSelected

            Unit
        }
    }

    fun saveCanvasState() =
        canvasStates.add(canvasContext.getImageData(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble()))

    fun restoreCanvasState() =
        when(canvasStates.size)
        {
            0 -> {}
            1 -> canvasContext.putImageData(canvasStates.last(), 0.0, 0.0)
            else -> canvasContext.putImageData(canvasStates.removeLast(), 0.0, 0.0)
        }

    fun calculateRealCoordinates(mouseX: Double, mouseY: Double) : Pair<Double, Double>
    {
        val x = (mouseX * canvas.width) / canvas.clientWidth
        val y = (mouseY * canvas.height) / canvas.clientHeight

        return x to y
    }

    fun updateDrawingCoordinates(mouseX: Double, mouseY: Double)
    {
        val realCoordinates: Pair<Double, Double> = calculateRealCoordinates(mouseX, mouseY)

        drawingCoordinates.updateCurrentCoordinates(realCoordinates.first, realCoordinates.second)
    }

    fun startAreaCoordinates(mouseX: Double, mouseY: Double)
    {
        val realCoordinates: Pair<Double, Double> = calculateRealCoordinates(mouseX, mouseY)

        areaCoordinates = AreaCoordinates(realCoordinates.first, realCoordinates.second)
    }

    fun updateAreaCoordinates(mouseX: Double, mouseY: Double)
    {
        val realCoordinates: Pair<Double, Double> = calculateRealCoordinates(mouseX, mouseY)

        areaCoordinates.updateEndCoordinates(realCoordinates.first, realCoordinates.second)
    }

    fun drawOnCanvas()
    {
        canvasContext.strokeStyle = selectedColor
        canvasContext.lineWidth = 50.0
        canvasContext.lineCap = CanvasLineCap.ROUND
        canvasContext.lineJoin = CanvasLineJoin.ROUND
        canvasContext.beginPath()
        canvasContext.moveTo(drawingCoordinates.lastX, drawingCoordinates.lastY)
        canvasContext.lineTo(drawingCoordinates.currentX, drawingCoordinates.currentY)
        canvasContext.stroke()
    }

    fun colorAreaOnCanvas()
    {
        canvasContext.fillStyle = selectedColor
        canvasContext.beginPath()
        canvasContext.fillRect(areaCoordinates.startX, areaCoordinates.startY, areaCoordinates.width, areaCoordinates.height)
        canvasContext.stroke()
    }

    fun pixelateAreaOnCanvas()
    {
        val pixelSize = 5
        val temporalCanvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement
        val temporalCanvasContext: CanvasRenderingContext2D = temporalCanvas.getContext("2d") as CanvasRenderingContext2D

        temporalCanvas.width = pixelSize
        temporalCanvas.height = pixelSize

        temporalCanvasContext.imageSmoothingEnabled = false

        temporalCanvasContext.drawImage(
            canvasContext.canvas,
            areaCoordinates.startX,
            areaCoordinates.startY,
            areaCoordinates.width,
            areaCoordinates.height,
            0.0,
            0.0,
            temporalCanvas.width.toDouble(),
            temporalCanvas.height.toDouble()
        )

        canvasContext.clearRect(
            areaCoordinates.startX,
            areaCoordinates.startY,
            areaCoordinates.width,
            areaCoordinates.height
        )

        canvasContext.imageSmoothingEnabled = false

        canvasContext.drawImage(
            temporalCanvasContext.canvas,
            0.0,
            0.0,
            temporalCanvas.width.toDouble(),
            temporalCanvas.height.toDouble(),
            areaCoordinates.startX,
            areaCoordinates.startY,
            areaCoordinates.width,
            areaCoordinates.height
        )

        canvasContext.imageSmoothingEnabled = true
    }

    fun beforeDrawing()
    {
        drawing = true
        saveCanvasState()
    }
    
    fun afterDrawing()
    {
        drawing = false
        areaCoordinates = AreaCoordinates()
    }

    fun downloadCanvasContent()
    {
        if (fileSelected.isBlank()) return

        val export = Image()

        export.onload = {
            val hyperLink: HTMLAnchorElement = document.createElement("a") as HTMLAnchorElement

            hyperLink.href= export.src

            hyperLink.download = "image.png"

            document.body!!.appendChild(hyperLink)

            hyperLink.click()

            document.body!!.removeChild(hyperLink)

            Unit
        }

        export.src = canvas.toDataURL(".png")
    }
}