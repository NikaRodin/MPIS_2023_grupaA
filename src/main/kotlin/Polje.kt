abstract class Polje(
    val id: String,
    val eepId: String,
    val naponskiNivo: Float,
    val x: Int,
    val y: Int,
    val tip: TipPolja
) {
    // Returns an error or null if there is no error
    abstract fun click(clickX: Int, clickY: Int): String?
    abstract fun provjeriStanje(): StanjePolja
    abstract fun getSignals(): List<Signal>
}

enum class StanjePolja(val opis: String) {
    ON("ukljuceno"), OFF("iskljuceno")
}

enum class TipPolja(val opis: String) {
    DVP("dalekovodno"), MP("mjerno")
}
