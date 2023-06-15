class Rastavljac(
    id: String,
    stanje: StanjeSklopnogUredaja,
    val coordinate: Coordinate,
) : SklopniUredaj(id, stanje) {

    override fun getSignals(eep: String, napon: String, polje: String): List<Signal> {
        return super.getSignals(eep, napon, polje)
    }
}