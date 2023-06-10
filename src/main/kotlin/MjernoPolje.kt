class MjernoPolje(
    id: String,
    x: Int,
    y: Int,
    val sabirnicaIRastavljac: SabirnicaIRastavljac
): Polje(id, x, y) {

    override fun click(clickX: Int, clickY: Int): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        if (inside(sabirnicaIRastavljac.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            sabirnicaIRastavljac.rastavljac.toggleStanje()
        }
        return null
    }

    fun toggleStanje() = sabirnicaIRastavljac.rastavljac.toggleStanje()
    fun ukljuci() = sabirnicaIRastavljac.rastavljac.ukljuci()
    fun iskljuci() = sabirnicaIRastavljac.rastavljac.iskljuci()

    override fun provjeriStanje(): StanjePolja {
        return when(sabirnicaIRastavljac.rastavljac.stanje) {
            StanjeSklopnogUredaja.ON -> StanjePolja.ON
            else -> StanjePolja.OFF
        }
    }
}