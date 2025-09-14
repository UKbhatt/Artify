package cromega.studio.web.models

data class DrawingCoordinates(
    var lastX: Double = 0.0,
    var lastY: Double = 0.0,
    var currentX: Double = 0.0,
    var currentY: Double = 0.0
) {
    fun updateCurrentCoordinates(
        newX: Double,
        newY: Double
    ) {
        lastX = currentX
        lastY = currentY
        currentX = newX
        currentY = newY
    }
}
