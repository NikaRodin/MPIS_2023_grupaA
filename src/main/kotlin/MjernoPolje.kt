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

    override fun click(clickX: Int, clickY: Int, repaint: () -> Unit, wait: Boolean): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        if (inside(sabirnicaIRastavljac.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            sabirnicaIRastavljac.rastavljac.toggleStanje(repaint, wait)
        }
        return null
    }

    fun toggleStanje(repaint: () -> Unit, wait: Boolean) = sabirnicaIRastavljac.rastavljac.toggleStanje(repaint, wait)
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
        signals.add(Signal(eepId, naponskiNivo.toInt().toString(), id, "mjerni pretvornik", "Napon (kV)", "110"))
        signals.add(Signal(eepId, naponskiNivo.toInt().toString(), id, "mjerni pretvornik", "Frekvencija (Hz)", "50"))
        return signals
    }
}