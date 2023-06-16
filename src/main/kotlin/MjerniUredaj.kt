class MjerniUredaj(
    val id: String,
    val tip: TipMjernogUredaja,
    var measurment: Float,
    val initialMeasurment: Float = measurment
) {
    fun getDescriptionString(): String {
        return "${tip.opis} (${tip.mjernaJedinica})"
    }

    fun getMeasurmentString(): String {
        return "$measurment ${tip.mjernaJedinica}"
    }

    fun getSignals(eep: String, napon: String, polje: String): List<Signal> {

        val signals = ArrayList<Signal>()

        signals.add(Signal(eep, napon, polje, id, getDescriptionString(), getMeasurmentString()))

        if(tip == TipMjernogUredaja.JALOVA_ENERGIJA){
            signals.add(Signal(eep, napon, polje, id, "alarm", "prorada", false))
            signals.add(Signal(eep, napon, polje, id, "alarm", "prestanak"))
        }

        return signals
    }
}

enum class TipMjernogUredaja(val opis: String, val mjernaJedinica: String) {
    RADNA_SNAGA("radna snaga","MW"),
    NAPON("napon","kV"),
    FREKVENCIJA("frekvencija","Hz"),
    JALOVA_ENERGIJA("jalova energija","kVArh")
}