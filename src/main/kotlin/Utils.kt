import java.awt.Color

fun inside(coord: Coordinate, size: Int, x: Int, y: Int): Boolean {
    return inside(coord, size, size, x, y)
}

fun inside(coord: Coordinate, width: Int, height: Int, x: Int, y: Int): Boolean {
    return x > coord.x && y > coord.y && x < coord.x + width && y < coord.y + height
}

fun getColor(stanje: StanjeSklopnogUredaja): Color {
    return when (stanje) {
        StanjeSklopnogUredaja.ON -> Color.GREEN
        StanjeSklopnogUredaja.OFF -> Color.RED
    }
}

fun getError(uredaj: SklopniUredaj): String{
    return "${uredaj.id} mora biti " + when(uredaj.stanje) {
        StanjeSklopnogUredaja.ON -> "${StanjeSklopnogUredaja.OFF.opis}!"
        StanjeSklopnogUredaja.OFF -> "${StanjeSklopnogUredaja.ON.opis}!"
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

fun tekstUpravljanjaDalekovodom(dalekovod: Dalekovod): String {
    return when (dalekovod.provjeriStanje()) {
        StanjeDalekovoda.ON -> "Iskljuci dalekovod"
        StanjeDalekovoda.OFF -> "Ukljuci dalekovod"
    }
}

fun tekstUpravljanjaPoljem(polje: Polje): String {
    return when (polje.provjeriStanje()) {
        StanjePolja.ON -> "Iskljuci ${polje.id}"
        StanjePolja.OFF -> "Ukljuci ${polje.id}"
    }
}