class DalekovodnoPolje(
    id: String,
    eepId: String,
    naponskiNivo: Float,
    x: Int,
    y: Int,
    val sabirniceIRastavljaci: List<SabirnicaIRastavljac>,
    val prekidac: Prekidac,
    val izlazniRastavljac: Rastavljac,
    val rastavljacUzemljenja: Rastavljac,
    tip: TipPolja = TipPolja.DVP
) : Polje(id, eepId, naponskiNivo, x, y, tip) {

    override fun click(clickX: Int, clickY: Int): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y
        val ukljuceniSabRast: Rastavljac? = pronadiUkljuceniSabirnickiRastavljac()

        if (inside(prekidac.coordinate, PREKIDAC_SIZE, innerClickX, innerClickY)) {
            return when (prekidac.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    prekidac.iskljuci()
                    null
                }
                StanjeSklopnogUredaja.OFF -> {
                    when (ukljuceniSabRast) {
                        null -> {
                            val hit = "abirnicki rastavljac mora biti ukljucen!"
                            if (sabirniceIRastavljaci.size > 1) "Barem jedan s$hit"
                            else "S$hit"
                        }
                        else -> checkAndToggle(prekidac, StanjeSklopnogUredaja.ON, izlazniRastavljac)
                    }
                }
                StanjeSklopnogUredaja.MIDDLE -> TODO()
                StanjeSklopnogUredaja.ERROR -> TODO()
            }

        } else if (inside(izlazniRastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (izlazniRastavljac.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    checkAndToggle(izlazniRastavljac, StanjeSklopnogUredaja.OFF, prekidac)
                }
                StanjeSklopnogUredaja.OFF -> {
                    checkAndToggle(izlazniRastavljac, StanjeSklopnogUredaja.OFF, prekidac, rastavljacUzemljenja)
                }
                StanjeSklopnogUredaja.MIDDLE -> TODO()
                StanjeSklopnogUredaja.ERROR -> TODO()
            }

        } else if (inside(rastavljacUzemljenja.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (rastavljacUzemljenja.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    checkAndToggle(rastavljacUzemljenja, StanjeSklopnogUredaja.OFF, prekidac)
                }
                StanjeSklopnogUredaja.OFF -> {
                    when (ukljuceniSabRast) {
                        null -> checkAndToggle(
                            rastavljacUzemljenja,
                            StanjeSklopnogUredaja.OFF,
                            prekidac,
                            izlazniRastavljac
                        )
                        else -> getError(ukljuceniSabRast)
                    }
                }
                StanjeSklopnogUredaja.MIDDLE -> TODO()
                StanjeSklopnogUredaja.ERROR -> TODO()
            }
        } else {
            sabirniceIRastavljaci.forEach {
                if (inside(it.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
                    return when (it.rastavljac.stanje) {
                        StanjeSklopnogUredaja.ON -> {
                            checkAndToggle(it.rastavljac, StanjeSklopnogUredaja.OFF, prekidac)
                        }
                        StanjeSklopnogUredaja.OFF -> {
                            when (ukljuceniSabRast) {
                                null -> checkAndToggle(
                                    it.rastavljac,
                                    StanjeSklopnogUredaja.OFF,
                                    prekidac,
                                    rastavljacUzemljenja
                                )
                                else -> getError(ukljuceniSabRast)
                            }
                        }
                        StanjeSklopnogUredaja.MIDDLE -> TODO()
                        StanjeSklopnogUredaja.ERROR -> TODO()
                    }
                }
            }
        }

        return null
    }

    fun pronadiUkljuceniSabirnickiRastavljac(): Rastavljac? {
        sabirniceIRastavljaci.forEach {
            if (it.rastavljac.stanje == StanjeSklopnogUredaja.ON) return it.rastavljac
        }
        return null
    }

    override fun provjeriStanje(): StanjePolja {
        return when (prekidac.stanje) {
            StanjeSklopnogUredaja.ON -> StanjePolja.ON
            else -> StanjePolja.OFF
        }
    }

    override fun getSignals(): List<Signal> {
        val signals = ArrayList<Signal>()
        signals.addAll(prekidac.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        for (rs in sabirniceIRastavljaci) {
            signals.addAll(rs.rastavljac.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        }
        signals.addAll(izlazniRastavljac.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        signals.addAll(rastavljacUzemljenja.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        // TODO: zastita distantna
        // TODO: APU
        // TODO: zaštita nadstrujna
        // TODO: mjerni pretvornik
        // TODO: brojilo
        return signals
    }
}

class SabirnicaIRastavljac(
    val sabirnica: Sabirnica,
    val rastavljac: Rastavljac,
)