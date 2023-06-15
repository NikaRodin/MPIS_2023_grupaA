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

enum class TipMjernogUredaja(val mjernaJedinica: String) {
    RADNA_SNAGA("MW"),
    NAPON("kV"),
    FREKVENCIJA("Hz"),
    JALOVA_ENERGIJA("kVArh")
}