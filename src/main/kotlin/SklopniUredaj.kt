abstract class SklopniUredaj(
    val id: String,
    var stanje: StanjeSklopnogUredaja
) {

    fun toggleStanje() {
        stanje = when (stanje) {
            StanjeSklopnogUredaja.ON -> StanjeSklopnogUredaja.OFF
            StanjeSklopnogUredaja.OFF -> StanjeSklopnogUredaja.ON
        }
    }

    fun ukljuci() {
        if (stanje == StanjeSklopnogUredaja.OFF) {
            stanje = StanjeSklopnogUredaja.ON
        } else {
            println("$id je vec ukljucen.")
        }
    }

    fun iskljuci() {
        if (stanje == StanjeSklopnogUredaja.ON) {
            stanje = StanjeSklopnogUredaja.OFF
        } else {
            println("$id je vec iskljucen.")
        }
    }
}

enum class StanjeSklopnogUredaja(val opis: String) {
ON("ukljucen"), OFF("iskljucen")
}

/*
enum class TipSklopnogUredaja(val opis: String) {
    PREKIDAC("Prekidac"), RASTAVLJAC("Rastavljac")
}
*/
