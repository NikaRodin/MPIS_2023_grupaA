abstract class SklopniUredaj(
    val id: String,
    var stanje: StanjeSklopnogUredaja,
    var komanda: KomandaSklopnogUredaja = KomandaSklopnogUredaja.OFF,
) {

    fun toggleStanje() {
        when (stanje) {
            StanjeSklopnogUredaja.ON -> iskljuci()
            StanjeSklopnogUredaja.OFF -> ukljuci()
        }
    }

    fun ukljuci() {
        if (stanje == StanjeSklopnogUredaja.OFF) {
            stanje = StanjeSklopnogUredaja.ON
            komanda = KomandaSklopnogUredaja.ON
        } else {
            println("$id je vec ukljucen.")
        }
    }

    fun iskljuci() {
        if (stanje == StanjeSklopnogUredaja.ON) {
            stanje = StanjeSklopnogUredaja.OFF
            komanda = KomandaSklopnogUredaja.OFF
        } else {
            println("$id je vec iskljucen.")
        }
    }
}

enum class StanjeSklopnogUredaja(val opis: String) {
    ON("ukljucen"), OFF("iskljucen")
}

enum class KomandaSklopnogUredaja(val opis: String) {
    ON("uklop"), OFF("isklop")
}
