class MjerniUredaj(
    val id: String,
    val tip: TipMjernogUredaja,
    var measurment: Float,
    val coordinate: Coordinate,
) {
    fun getMeasurmentString(): String {
        return "$measurment ${tip.mjernaJedinica}"
    }
}

enum class TipMjernogUredaja(val mjernaJedinica: String, val iconFileName: String) {
    RADNA_SNAGA("MW", "radna_snaga.png"),
    NAPON("kV", "napon.png"),
    FREKVENCIJA("Hz", "frekvencija.png"),
    JALOVA_ENERGIJA("kVArh", "jalova_energija.png")
}