class Rastavljac(
    id: String,
    stanje: StanjeSklopnogUredaja,
    val coordinate: Coordinate,
) : SklopniUredaj(id, stanje) {

    fun getSignals(eep: String, napon: String, polje: String): List<Signal> {
        return listOf(
            Signal(eep, napon, polje, id, "komanda", komanda.opis),
            Signal(eep, napon, polje, id, "stanje", stanje.opis),
        )
    }
}