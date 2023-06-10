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
        val odabraniSabRast1: Rastavljac =
            dalPolje1.ukljuceniSabirnickiRastavljac() ?: dalPolje1.sabirniceIRastavljaci.first().rastavljac
        val odabraniSabRast2: Rastavljac =
            dalPolje2.ukljuceniSabirnickiRastavljac() ?: dalPolje1.sabirniceIRastavljaci.first().rastavljac

        dalPolje1.rastavljacUzemljenja.iskljuci()
        dalPolje1.sabirniceIRastavljaci.forEach {
            if (it.rastavljac == odabraniSabRast1)
                it.rastavljac.ukljuci()
            else
                it.rastavljac.iskljuci()
        }
        dalPolje1.izlazniRastavljac.ukljuci()
        dalPolje1.prekidac.ukljuci()

        dalPolje2.rastavljacUzemljenja.iskljuci()
        dalPolje2.sabirniceIRastavljaci.forEach {
            if (it.rastavljac == odabraniSabRast2)
                it.rastavljac.ukljuci()
            else
                it.rastavljac.iskljuci()
        }
        dalPolje2.izlazniRastavljac.ukljuci()
        dalPolje2.prekidac.ukljuci()
    }

    fun iskljuci() {
        dalPolje1.prekidac.iskljuci()
        dalPolje1.ukljuceniSabirnickiRastavljac()?.iskljuci()
        dalPolje1.izlazniRastavljac.iskljuci()
        dalPolje1.rastavljacUzemljenja.ukljuci()

        dalPolje2.prekidac.iskljuci()
        dalPolje2.ukljuceniSabirnickiRastavljac()?.iskljuci()
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