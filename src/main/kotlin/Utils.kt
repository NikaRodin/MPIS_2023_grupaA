import java.awt.Color

fun inside(coord: Coordinate, size: Int, x: Int, y: Int): Boolean {
    return inside(coord, size, size, x, y)
}

fun inside(coord: Coordinate, width: Int, height: Int, x: Int, y: Int): Boolean {
    return x > coord.x && y > coord.y && x < coord.x + width && y < coord.y + height
}

fun getColor(stanje: StanjeSklopnogUredaja): Color {
    return when (stanje) {
        StanjeSklopnogUredaja.ON -> Color(0xFF00AA00.toInt())
        StanjeSklopnogUredaja.OFF -> Color.BLACK
        StanjeSklopnogUredaja.MIDDLE -> TODO()
        StanjeSklopnogUredaja.ERROR -> TODO()
    }
}

fun getError(uredaj: SklopniUredaj): String{
    return "${uredaj.id} mora biti " + when(uredaj.stanje) {
        StanjeSklopnogUredaja.ON -> "${StanjeSklopnogUredaja.OFF.opis}!"
        StanjeSklopnogUredaja.OFF -> "${StanjeSklopnogUredaja.ON.opis}!"
        StanjeSklopnogUredaja.MIDDLE -> TODO()
        StanjeSklopnogUredaja.ERROR -> TODO()
    }
}

fun checkAndToggle(
    odabraniUredaj: SklopniUredaj,
    uvjet: StanjeSklopnogUredaja,
    vararg ostaliUredaji: SklopniUredaj
): String? {
    for (uredaj in ostaliUredaji) {
        if (uredaj.stanje != uvjet) return getError(uredaj)
    }
    odabraniUredaj.toggleStanje()
    return null
}

fun dohvatiTekstUpravljanjaDalekovodom(dalekovod: Dalekovod): String {
    return when (dalekovod.provjeriStanje()) {
        StanjeDalekovoda.ON -> "Iskljuci dalekovod"
        StanjeDalekovoda.OFF -> "Ukljuci dalekovod"
    }
}

fun dohvatiTekstUpravljanjaPoljem(polje: Polje): String {
    return when (polje.provjeriStanje()) {
        StanjePolja.ON -> "Iskljuci ${polje.tip.opis} polje"
        StanjePolja.OFF -> "Ukljuci ${polje.tip.opis} polje"
    }
}

fun getLoadingText(dalekovod: Dalekovod): String {
    return when (dalekovod.provjeriStanje()) {
        StanjeDalekovoda.ON -> "Iskljucivanje u tijeku..."
        StanjeDalekovoda.OFF -> "Ukljucivanje u tijeku..."
    }
}