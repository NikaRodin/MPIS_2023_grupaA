abstract class Polje(
    val x: Int,
    val y: Int,
) {
    // Returns an error or null of no error
    abstract fun click(clickX: Int, clickY: Int): String?
}