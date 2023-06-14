abstract class Polje(
    val id: String,
    val eepId: String,
    val naponskiNivo: Float,
    val x: Int,
    val y: Int,
) {
    // Returns an error or null of no error
    abstract fun click(clickX: Int, clickY: Int): String?
    abstract fun provjeriStanje(): StanjePolja

    abstract fun getSignals(): List<Signal>
}