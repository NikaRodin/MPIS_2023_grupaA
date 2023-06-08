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
            TODO("error?")
        }
    }

    fun iskljuci() {
        if (stanje == StanjeSklopnogUredaja.ON) {
            stanje = StanjeSklopnogUredaja.OFF
        } else {
            TODO("error?")
        }
    }

    fun javiGresku(): String{
        return "$id mora biti " + when(stanje) {
            StanjeSklopnogUredaja.ON -> "${StanjeSklopnogUredaja.OFF.opis}!"
            StanjeSklopnogUredaja.OFF -> "${StanjeSklopnogUredaja.ON.opis}!"
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
