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

fun checkAndToggle(glavniUredaj: SklopniUredaj, uvjet: StanjeSklopnogUredaja, vararg ostaliUredaji: SklopniUredaj): String? {
    for (item in ostaliUredaji) {
        if(item.stanje != uvjet) return item.javiGresku()
    }
    glavniUredaj.toggleStanje()
    return null
}