abstract class SklopniUredaj(
    val id: String,
    var stanje: StanjeSklopnogUredaja,
    var komanda: KomandaSklopnogUredaja = KomandaSklopnogUredaja.OFF,
) {

    fun toggleStanje(repaint: () -> Unit, wait: Boolean) {
        when (stanje) {
            StanjeSklopnogUredaja.ON -> iskljuci(repaint, wait)
            StanjeSklopnogUredaja.OFF -> ukljuci(repaint, wait)
            StanjeSklopnogUredaja.MIDDLE -> TODO()
            StanjeSklopnogUredaja.ERROR -> TODO()
        }
    }

    fun ukljuci(repaint: () -> Unit, wait: Boolean) {
        if (stanje == StanjeSklopnogUredaja.OFF) {
            komanda = KomandaSklopnogUredaja.ON

            if (wait) {
                stanje = StanjeSklopnogUredaja.MIDDLE
                repaint()

                try {
                    Thread.sleep(1000)
                } catch (ignored: Exception) {}
            }

            stanje = StanjeSklopnogUredaja.ON
            repaint()
        } else {
            println("$id se ne može uključiti. On je u stanju ${stanje.opis}.")
        }
    }

    fun iskljuci(repaint: () -> Unit, wait: Boolean) {
        if (stanje == StanjeSklopnogUredaja.ON) {
            komanda = KomandaSklopnogUredaja.OFF

            if (wait) {
                stanje = StanjeSklopnogUredaja.MIDDLE
                repaint()

                try {
                    Thread.sleep(1000)
                } catch (ignored: Exception) {}
            }

            stanje = StanjeSklopnogUredaja.OFF
            repaint()
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
    MIDDLE("međupoložaj"), OFF("isključen"), ON("uključen"), ERROR("kvar signalizacije")
}

enum class KomandaSklopnogUredaja(val opis: String) {
    ON("uklop"), OFF("isklop")
}
