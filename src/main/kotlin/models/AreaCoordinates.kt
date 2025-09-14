package cromega.studio.web.models

data class AreaCoordinates(
    val startX: Double = 0.0,
    val startY: Double = 0.0,
    var endX: Double = 0.0,
    var endY: Double = 0.0
) {
    constructor(initialCoordinateX: Double, initialCoordinateY: Double) :
            this(initialCoordinateX, initialCoordinateY, initialCoordinateX, initialCoordinateY)

    val width
        get() = endX - startX

    val height
        get() = endY - startY

    fun updateEndCoordinates(newEndX: Double, newEndY: Double)
    {
        endX = newEndX
        endY = newEndY
    }
}
