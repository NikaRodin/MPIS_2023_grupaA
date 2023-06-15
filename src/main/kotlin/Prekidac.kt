class Prekidac(
    id: String,
    stanje: StanjeSklopnogUredaja,
    val coordinate: Coordinate,
) : SklopniUredaj(id, stanje) {

    override fun getSignals(eep: String, napon: String, polje: String): List<Signal> {

        val signals = ArrayList<Signal>()
        signals.addAll(super.getSignals(eep, napon, polje))

        val prorada = "prorada"
        val prestanak = "prestanak"

        signals.addAll(listOf(
            Signal(eep, napon, polje, id, "pad tlaka <16 b", prorada, false),
            Signal(eep, napon, polje, id, "pad tlaka <16 b", prestanak),
            Signal(eep, napon, polje, id, "pad tlaka <14 b", prorada, false),
            Signal(eep, napon, polje, id, "pad tlaka <14 b", prestanak),
            Signal(eep, napon, polje, id, "pad tlaka <11 b", prorada, false),
            Signal(eep, napon, polje, id, "pad tlaka <11 b", prestanak),
            Signal(eep, napon, polje, id, "APU blokada", prorada, false),
            Signal(eep, napon, polje, id, "APU blokada", prestanak),
            Signal(eep, napon, polje, id, "nesklad polova - 3P isklop", prorada, false),
            Signal(eep, napon, polje, id, "nesklad polova - 3P isklop", prestanak),
            Signal(eep, napon, polje, id, "upravljanje", "daljinsko"),
            Signal(eep, napon, polje, id, "upravljanje", "lokalno", false),
        ))

        return signals
    }
}