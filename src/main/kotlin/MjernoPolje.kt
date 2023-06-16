import javax.swing.JTextField

class MjernoPolje(
    id: String,
    eepId: String,
    naponskiNivo: Float,
    x: Int,
    y: Int,
    val sabirnicaIRastavljac: SabirnicaIRastavljac,
    val mjerniUredaji: List<MjerniUredaj>,
    tip: TipPolja = TipPolja.MP
): Polje(id, eepId, naponskiNivo, x, y, tip) {

    override fun click(clickX: Int, clickY: Int, repaint: () -> Unit, wait: Boolean, infoBox: JTextField): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        if (inside(sabirnicaIRastavljac.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            if(wait) infoBox.text = getLoadingText(sabirnicaIRastavljac.rastavljac)
            sabirnicaIRastavljac.rastavljac.toggleStanje(repaint, wait)
            when(provjeriStanje()) {
                StanjePolja.ON ->  mjerniUredaji.forEach { it.measurment = it.initialMeasurment }
                StanjePolja.OFF -> mjerniUredaji.forEach { it.measurment = 0F }
            }
            infoBox.text = "${sabirnicaIRastavljac.rastavljac.id} ${sabirnicaIRastavljac.rastavljac.stanje.opis}!"
        }
        return null
    }

    fun toggleStanje(repaint: () -> Unit, wait: Boolean){
        sabirnicaIRastavljac.rastavljac.toggleStanje(repaint, wait)
        when(provjeriStanje()) {
            StanjePolja.ON ->  mjerniUredaji.forEach { it.measurment = it.initialMeasurment }
            StanjePolja.OFF -> mjerniUredaji.forEach { it.measurment = 0F }
        }
    }
    fun ukljuciImmediate() = sabirnicaIRastavljac.rastavljac.ukljuci({}, false)
    fun iskljuciImmediate() = sabirnicaIRastavljac.rastavljac.iskljuci({}, false)

    override fun provjeriStanje(): StanjePolja {
        return when(sabirnicaIRastavljac.rastavljac.stanje) {
            StanjeSklopnogUredaja.ON -> StanjePolja.ON
            else -> StanjePolja.OFF
        }
    }

    override fun getSignals(): List<Signal> {
        val signals = ArrayList<Signal>()
        signals.addAll(sabirnicaIRastavljac.rastavljac.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        for (mu in mjerniUredaji) {
            signals.addAll(mu.getSignals(eepId, naponskiNivo.toInt().toString(), id))
        }
        return signals
    }
}