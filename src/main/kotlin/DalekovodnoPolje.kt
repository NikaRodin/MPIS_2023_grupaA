import javax.swing.JTextField

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
    val mjerniUredaji: List<MjerniUredaj>,
    tip: TipPolja = TipPolja.DVP
) : Polje(id, eepId, naponskiNivo, x, y, tip) {

    override fun click(clickX: Int, clickY: Int, repaint: () -> Unit, wait: Boolean, infoBox: JTextField): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y
        val ukljuceniSabRast: Rastavljac? = pronadiUkljuceniSabirnickiRastavljac()
        val medjupolozajSabRast: Rastavljac? = pronadiSabirnickiRastavljacUMedjupolozaju()

        if (inside(prekidac.coordinate, PREKIDAC_SIZE, innerClickX, innerClickY)) {
            return when (prekidac.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    if (wait) infoBox.text = getLoadingText(prekidac)
                    prekidac.iskljuci(repaint, wait)
                    mjerniUredaji.forEach { it.measurment = 0F }
                    infoBox.text = "${prekidac.id} isključen!"
                    null
                }
                StanjeSklopnogUredaja.OFF -> {
                    when (ukljuceniSabRast) {
                        null -> {
                            val hit = "abirnički rastavljač mora biti uključen!"
                            if (sabirniceIRastavljaci.size > 1) "Barem jedan s$hit"
                            else "S$hit"
                        }
                        else -> {
                            val feedback = checkAndToggle(
                                repaint,
                                wait,
                                infoBox,
                                prekidac,
                                StanjeSklopnogUredaja.ON,
                                izlazniRastavljac
                            )
                            if (feedback == null) mjerniUredaji.forEach { it.measurment = it.initialMeasurment }
                            feedback
                        }
                    }
                }
                StanjeSklopnogUredaja.MIDDLE -> "Pričekajte. Uređaj je u međupoložaju."
                StanjeSklopnogUredaja.ERROR -> "Uređaj je u stanju greške!"
            }

        } else if (inside(izlazniRastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (izlazniRastavljac.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    checkAndToggle(repaint, wait, infoBox, izlazniRastavljac, StanjeSklopnogUredaja.OFF, prekidac)
                }
                StanjeSklopnogUredaja.OFF -> {
                    checkAndToggle(
                        repaint,
                        wait,
                        infoBox,
                        izlazniRastavljac,
                        StanjeSklopnogUredaja.OFF,
                        prekidac,
                        rastavljacUzemljenja
                    )
                }
                StanjeSklopnogUredaja.MIDDLE -> "Pričekajte. Uređaj je u međupoložaju."
                StanjeSklopnogUredaja.ERROR -> "Uređaj je u stanju greške!"
            }

        } else if (inside(rastavljacUzemljenja.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (rastavljacUzemljenja.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    checkAndToggle(repaint, wait, infoBox, rastavljacUzemljenja, StanjeSklopnogUredaja.OFF, prekidac)
                }
                StanjeSklopnogUredaja.OFF -> {
                    when (ukljuceniSabRast) {
                        null -> {
                            if (medjupolozajSabRast == null) {
                                checkAndToggle(
                                    repaint, wait, infoBox,
                                    rastavljacUzemljenja,
                                    StanjeSklopnogUredaja.OFF,
                                    prekidac,
                                    izlazniRastavljac
                                )
                            } else {
                                getError(medjupolozajSabRast)
                            }
                        }
                        else -> getError(ukljuceniSabRast)
                    }
                }
                StanjeSklopnogUredaja.MIDDLE -> "Pričekajte. Uređaj je u međupoložaju."
                StanjeSklopnogUredaja.ERROR -> "Uređaj je u stanju greške!"
            }

        } else {
            sabirniceIRastavljaci.forEach {
                if (inside(it.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
                    return when (it.rastavljac.stanje) {
                        StanjeSklopnogUredaja.ON -> {
                            checkAndToggle(repaint, wait, infoBox, it.rastavljac, StanjeSklopnogUredaja.OFF, prekidac)
                        }
                        StanjeSklopnogUredaja.OFF -> {
                            when (ukljuceniSabRast) {
                                null -> {
                                    if (medjupolozajSabRast == null) {
                                        checkAndToggle(
                                            repaint, wait, infoBox,
                                            it.rastavljac,
                                            StanjeSklopnogUredaja.OFF,
                                            prekidac,
                                            rastavljacUzemljenja
                                        )
                                    } else {
                                        getError(medjupolozajSabRast)
                                    }
                                }
                                else -> getError(ukljuceniSabRast)
                            }
                        }
                        StanjeSklopnogUredaja.MIDDLE -> "Pričekajte. Uređaj je u međupoložaju."
                        StanjeSklopnogUredaja.ERROR -> "Uređaj je u stanju greške!"
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

    fun pronadiSabirnickiRastavljacUMedjupolozaju(): Rastavljac? {
        sabirniceIRastavljaci.forEach {
            if (it.rastavljac.stanje == StanjeSklopnogUredaja.MIDDLE) return it.rastavljac
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
        for (sr in sabirniceIRastavljaci) {
            signals.addAll(sr.rastavljac.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        }
        signals.addAll(izlazniRastavljac.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        signals.addAll(rastavljacUzemljenja.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        for (mu in mjerniUredaji) {
            signals.addAll(mu.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        }
        // TODO: zastita distantna
        // TODO: APU
        // TODO: zaštita nadstrujna
        return signals
    }
}

class SabirnicaIRastavljac(
    val sabirnica: Sabirnica,
    val rastavljac: Rastavljac,
)