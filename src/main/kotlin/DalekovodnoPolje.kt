import java.util.LinkedList

class DalekovodnoPolje(
    x: Int,
    y: Int,
    val sabirniceIRastavljaci: List<SabirnicaIRastavljac>,
    val prekidac: Prekidac,
    val izlazniRastavljac: Rastavljac,
    val rastavljacUzemljenja: Rastavljac,
) : Polje(x, y), ClickableElement {

    override fun click(clickX: Int, clickY: Int): Boolean {
        val innerClickX = clickX - x
        val innerClickY = clickY - y

        val clickableElements = LinkedList<ClickableElement>().apply {
            addAll(sabirniceIRastavljaci.map { it.rastavljac })
            add(prekidac)
            add(izlazniRastavljac)
            add(rastavljacUzemljenja)
        }

        val clickedOn = clickableElements.firstOrNull { it.click(innerClickX, innerClickY) }

        return clickedOn != null
    }
}

class SabirnicaIRastavljac(
    val sabirnica: Sabirnica,
    val rastavljac: Rastavljac,
)