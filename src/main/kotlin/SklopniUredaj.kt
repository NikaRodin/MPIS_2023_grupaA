abstract class SklopniUredaj(
    val id: String,
    var stanje: StanjeSklopnogUredaja,
    var komanda: KomandaSklopnogUredaja = KomandaSklopnogUredaja.OFF,
) {

    fun toggleStanje() {
        when (stanje) {
            StanjeSklopnogUredaja.ON -> iskljuci()
            StanjeSklopnogUredaja.OFF -> ukljuci()
            StanjeSklopnogUredaja.MIDDLE -> TODO()
            StanjeSklopnogUredaja.ERROR -> TODO()
        }
    }

    fun ukljuci() {
        if (stanje == StanjeSklopnogUredaja.OFF) {
            stanje = StanjeSklopnogUredaja.ON
            komanda = KomandaSklopnogUredaja.ON
        } else {
            println("$id se ne može uključiti. On je u stanju ${stanje.opis}.")
        }
    }

    fun iskljuci() {
        if (stanje == StanjeSklopnogUredaja.ON) {
            stanje = StanjeSklopnogUredaja.OFF
            komanda = KomandaSklopnogUredaja.OFF
        } else {
            println("$id se ne može isključiti. On je u stanju ${stanje.opis}.")
        }
    }

    open fun getSignals(eep: String, napon: String, polje: String): List<Signal> {

        val signals = ArrayList<Signal>()

        for (kom in KomandaSklopnogUredaja.values()) {
            signals.add(Signal(eep, napon, polje, id, "komanda", kom.opis, kom == komanda))
        }

        for (st in StanjeSklopnogUredaja.values()) {
            signals.add(Signal(eep, napon, polje, id, "stanje", st.opis, st == stanje))
        }

        return signals
    }
}

enum class StanjeSklopnogUredaja(val opis: String) {
    MIDDLE("međupoložaj"), OFF("iskljucen"), ON("ukljucen"), ERROR("kvar signalizacije")
}

enum class KomandaSklopnogUredaja(val opis: String) {
    ON("uklop"), OFF("isklop")
}
