class Prekidac(
    id: String,
    stanje: StanjeSklopnogUredaja,
    val coordinate: Coordinate,
) : SklopniUredaj(id, stanje) {

    fun getSignals(eep: String, napon: String, polje: String): List<Signal> {

        val prestanak = "prestanak"

        return listOf(
            Signal(eep, napon, polje, id, "komanda", komanda.opis),
            Signal(eep, napon, polje, id, "stanje", stanje.opis),
            Signal(eep, napon, polje, id, "pad tlaka <16 b", prestanak),
            Signal(eep, napon, polje, id, "pad tlaka <14 b", prestanak),
            Signal(eep, napon, polje, id, "pad tlaka <11 b", prestanak),
            Signal(eep, napon, polje, id, "APU blokada", prestanak),
            Signal(eep, napon, polje, id, "nesklad polova - 3P isklop", prestanak),
            Signal(eep, napon, polje, id, "upravljanje", "daljinsko"),
        )
    }
}