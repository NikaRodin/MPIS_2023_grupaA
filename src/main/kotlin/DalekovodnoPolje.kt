class DalekovodnoPolje(
    x: Int,
    y: Int,
    val sabirniceIRastavljaci: List<SabirnicaIRastavljac>,
    val prekidac: Prekidac,
    val izlazniRastavljac: Rastavljac,
    val rastavljacUzemljenja: Rastavljac,
) : Polje(x, y) {

    fun ukljucenSabRast(): SklopniUredaj? {
        sabirniceIRastavljaci.forEach {
            if (it.rastavljac.stanje == StanjeSklopnogUredaja.ON) return it.rastavljac
        }
        return null
    }

    override fun click(clickX: Int, clickY: Int): String? {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        val sabRast:SklopniUredaj? = ukljucenSabRast()

        if (inside(prekidac.coordinate, PREKIDAC_SIZE, innerClickX, innerClickY)) {
            return when (prekidac.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    prekidac.iskljuci()
                    null
                }
                StanjeSklopnogUredaja.OFF -> {
                    when(sabRast){
                        null -> "Barem jedan sabirnicki rastavljac mora biti ukljucen!"
                        else -> checkAndToggle(prekidac, StanjeSklopnogUredaja.ON, izlazniRastavljac)
                    }
                }
            }

        } else if (inside(izlazniRastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (izlazniRastavljac.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    checkAndToggle(izlazniRastavljac, StanjeSklopnogUredaja.OFF, prekidac)
                }
                StanjeSklopnogUredaja.OFF -> {
                     checkAndToggle(izlazniRastavljac, StanjeSklopnogUredaja.OFF, prekidac, rastavljacUzemljenja)
                }
            }

        } else if (inside(rastavljacUzemljenja.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
            return when (rastavljacUzemljenja.stanje) {
                StanjeSklopnogUredaja.ON -> {
                    checkAndToggle(rastavljacUzemljenja, StanjeSklopnogUredaja.OFF, prekidac)
                }
                StanjeSklopnogUredaja.OFF -> {
                    sabRast?.javiGresku()
                        ?: checkAndToggle(rastavljacUzemljenja, StanjeSklopnogUredaja.OFF, prekidac, izlazniRastavljac)
                }
            }
        } else {
            sabirniceIRastavljaci.forEach {
                if (inside(it.rastavljac.coordinate, RASTAVLJAC_SIZE, innerClickX, innerClickY)) {
                    return when (it.rastavljac.stanje) {
                        StanjeSklopnogUredaja.ON -> {
                            checkAndToggle(it.rastavljac, StanjeSklopnogUredaja.OFF, prekidac)
                        }
                        StanjeSklopnogUredaja.OFF -> {
                            sabRast?.javiGresku()
                                ?: checkAndToggle(it.rastavljac, StanjeSklopnogUredaja.OFF, prekidac, rastavljacUzemljenja)
                        }
                    }
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