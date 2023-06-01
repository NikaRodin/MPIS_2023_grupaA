class Prekidac(
    id: String,
    stanje: StanjeSklopnogUredaja,
    val coordinate: Coordinate,
) : SklopniUredaj(id, stanje) {

    override fun click(clickX: Int, clickY: Int): Boolean {
        if (inside(coordinate, PREKIDAC_SIZE, clickX, clickY)) {
            toggleStanje()
            return true
        } else {
            return false
        }
    }
}