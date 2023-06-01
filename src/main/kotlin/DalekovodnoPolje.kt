class DalekovodnoPolje(
    x: Int,
    y: Int,
    val sabirniceIRastavljaci: List<SabirnicaIRastavljac>,
    val prekidac: Prekidac,
    val izlazniRastavljac: Rastavljac,
    val rastavljacUzemljenja: Rastavljac,
) : Polje(x, y)

class SabirnicaIRastavljac(
    val sabirnica: Sabirnica,
    val rastavljac: Rastavljac,
)