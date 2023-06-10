class MjernoPolje(
    x: Int,
    y: Int,
    val sabirnicaIRastavljac: SabirnicaIRastavljac
): Polje(x, y) {
    override fun click(clickX: Int, clickY: Int): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        if (inside(sabirnicaIRastavljac.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            sabirnicaIRastavljac.rastavljac.toggleStanje()
        }
        return null
    }
}