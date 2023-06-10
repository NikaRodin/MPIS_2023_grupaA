class Dalekovod(
    val dalPolje1: DalekovodnoPolje,
    val dalPolje2: DalekovodnoPolje,
) {

    fun toggleStanje() {
        when (provjeriStanje()) {
            StanjeDalekovoda.ON -> iskljuci()
            StanjeDalekovoda.OFF -> ukljuci()
        }
    }

    fun ukljuci() {
        dalPolje1.rastavljacUzemljenja.iskljuci()
        dalPolje1.sabirniceIRastavljaci[0].rastavljac.ukljuci()
        dalPolje1.sabirniceIRastavljaci[1].rastavljac.iskljuci()
        dalPolje1.izlazniRastavljac.ukljuci()
        dalPolje1.prekidac.ukljuci()

        dalPolje2.rastavljacUzemljenja.iskljuci()
        dalPolje2.sabirniceIRastavljaci[0].rastavljac.ukljuci()
        dalPolje2.izlazniRastavljac.ukljuci()
        dalPolje2.prekidac.ukljuci()
    }

    fun iskljuci() {
        dalPolje1.prekidac.iskljuci()
        dalPolje1.sabirniceIRastavljaci[0].rastavljac.iskljuci()
        dalPolje1.izlazniRastavljac.iskljuci()
        dalPolje1.rastavljacUzemljenja.ukljuci()

        dalPolje2.prekidac.iskljuci()
        dalPolje2.sabirniceIRastavljaci[0].rastavljac.iskljuci()
        dalPolje2.izlazniRastavljac.iskljuci()
        dalPolje2.rastavljacUzemljenja.ukljuci()
    }

    fun provjeriStanje(): StanjeDalekovoda {
        return if (dalPolje1.provjeriStanje() == StanjePolja.ON && dalPolje2.provjeriStanje() == StanjePolja.ON)
            StanjeDalekovoda.ON
        else
            StanjeDalekovoda.OFF
    }
}

enum class StanjeDalekovoda(val opis: String) {
    ON("ukljucen"), OFF("iskljucen")
}