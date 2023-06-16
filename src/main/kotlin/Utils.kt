import java.awt.Color
import javax.swing.JTextField

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
        StanjeSklopnogUredaja.MIDDLE -> Color.ORANGE
        StanjeSklopnogUredaja.ERROR -> TODO()
    }
}

fun getError(uredaj: SklopniUredaj): String{
    return "${uredaj.id} mora biti " + when(uredaj.stanje) {
        StanjeSklopnogUredaja.ON -> "${StanjeSklopnogUredaja.OFF.opis}!"
        StanjeSklopnogUredaja.OFF -> "${StanjeSklopnogUredaja.ON.opis}!"
        StanjeSklopnogUredaja.MIDDLE -> "uključen ili isključen!"
        StanjeSklopnogUredaja.ERROR -> TODO()
    }
}

fun checkAndToggle(
    repaint: () -> Unit,
    wait: Boolean,
    infoBox: JTextField,
    odabraniUredaj: SklopniUredaj,
    uvjet: StanjeSklopnogUredaja,
    vararg ostaliUredaji: SklopniUredaj,
): String? {
    for (uredaj in ostaliUredaji) {
        if (uredaj.stanje != uvjet) return getError(uredaj)
    }
    if(wait) infoBox.text = getLoadingText(odabraniUredaj)
    odabraniUredaj.toggleStanje(repaint, wait)
    infoBox.text = "${odabraniUredaj.id} ${odabraniUredaj.stanje.opis}!"
    return null
}

fun dohvatiTekstUpravljanja(dalekovod: Dalekovod): String {
    return when (dalekovod.provjeriStanje()) {
        StanjeDalekovoda.ON -> "Isključi dalekovod"
        StanjeDalekovoda.OFF -> "Uključi dalekovod"
    }
}

fun dohvatiTekstUpravljanja(polje: Polje): String {
    return when (polje.provjeriStanje()) {
        StanjePolja.ON -> "Isključi ${polje.tip.opis} polje"
        StanjePolja.OFF -> "Uključi ${polje.tip.opis} polje"
    }
}

fun getLoadingText(dalekovod: Dalekovod): String {
    return when (dalekovod.provjeriStanje()) {
        StanjeDalekovoda.ON -> "Isključivanje dalekovoda u tijeku..."
        StanjeDalekovoda.OFF -> "Uključivanje dalekovoda u tijeku..."
    }
}

fun getLoadingText(polje: Polje): String {
    return when (polje.provjeriStanje()) {
        StanjePolja.ON -> "Isključivanje polja u tijeku..."
        StanjePolja.OFF -> "Uključivanje polja u tijeku..."
    }
}

fun getLoadingText(uredaj: SklopniUredaj): String {
    return when (uredaj.stanje) {
        StanjeSklopnogUredaja.ON -> "Isključivanje uređaja u tijeku..."
        StanjeSklopnogUredaja.OFF -> "Uključivanje uređaja u tijeku..."
        StanjeSklopnogUredaja.MIDDLE -> "Pričekajte. Uređaj je u međupoložaju."
        StanjeSklopnogUredaja.ERROR -> "Uređaj je u stanju greške!"
    }
}

fun sleep(delayMs: Long) {
    try {
        Thread.sleep(delayMs)
    } catch (e: Exception) {
        println(e.message)
    }
}