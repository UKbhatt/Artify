package cromega.studio.web.enums

enum class ActionButtons(
    val text: String
) {
    UPLOAD(text = "Upload Image to Edit"),
    UNDO(text = "Undo Changes on Selected Image"),
    PIXELATE(text = "Pixelate Section of Selected Image"),
    DRAW(text = "Draw on Selected Image"),
    AREA(text = "Coloring Area of Selected Image"),
    PALLETE(text = "Modify Modification Color"),
    DOWNLOAD(text = "Download Edited Image");
}