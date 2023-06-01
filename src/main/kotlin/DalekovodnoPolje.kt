class DalekovodnoPolje(
    x: Int,
    y: Int,
    val sabirniceIRastavljaci: List<SabirnicaIRastavljac>,
    val prekidac: Prekidac,
    val izlazniRastavljac: Rastavljac,
    val rastavljacUzemljenja: Rastavljac,
) : Polje(x, y) {

    // Returns an error or null of no error
    fun click(clickX: Int, clickY: Int): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        if (inside(prekidac.coordinate, PREKIDAC_SIZE, innerClickX, innerClickY)) {
            // TODO: provjera
            prekidac.toggleStanje()
            return null
        } else if (inside(izlazniRastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            // TODO: provjera
            izlazniRastavljac.toggleStanje()
            return null
        } else if (inside(rastavljacUzemljenja.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (rastavljacUzemljenja.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    rastavljacUzemljenja.iskljuci()
                    null
                }
                StanjeSklopnogUredaja.OFF -> {
                    if (prekidac.stanje == StanjeSklopnogUredaja.ON) {
                        "Prekidac mora biti iskljucen!"
                    } else {
                        rastavljacUzemljenja.ukljuci()
                        null
                    }
                }
            }
        } else {
            for (sabIRast in sabirniceIRastavljaci) {
                if (inside(sabIRast.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
                    // TODO: provjera
                    sabIRast.rastavljac.toggleStanje()
                    return null
                }
            }
        }

        return null
    }
}

class SabirnicaIRastavljac(
    val sabirnica: Sabirnica,
    val rastavljac: Rastavljac,
)