class Dalekovod(
    val dalPolje1: DalekovodnoPolje,
    val dalPolje2: DalekovodnoPolje,
) {

    private val DELAY_MS = 1000L

    fun toggleStanje(repaint: () -> Unit, wait: Boolean) {
        when (provjeriStanje()) {
            StanjeDalekovoda.ON -> iskljuci(repaint, wait)
            StanjeDalekovoda.OFF -> ukljuci(repaint, wait)
        }
    }

    fun ukljuci(repaint: () -> Unit, wait: Boolean) {
        val odabraniSabRast1: Rastavljac =
            dalPolje1.ukljuceniSabirnickiRastavljac() ?: dalPolje1.sabirniceIRastavljaci.first().rastavljac
        val odabraniSabRast2: Rastavljac =
            dalPolje2.ukljuceniSabirnickiRastavljac() ?: dalPolje2.sabirniceIRastavljaci.first().rastavljac


        println("=> Isključiti rastavljače za uzemljenje s obje strane")
        dalPolje1.rastavljacUzemljenja.iskljuci()
        dalPolje2.rastavljacUzemljenja.iskljuci()
        repaint()

        Thread {

            try {
                if (wait) Thread.sleep(DELAY_MS)
            } catch (e: Exception) {}

            println("=> Uključiti sabirničke rastavljače s obje strane")
            dalPolje1.sabirniceIRastavljaci.forEach {
                if (it.rastavljac == odabraniSabRast1) it.rastavljac.ukljuci()
                else it.rastavljac.iskljuci()
            }
            dalPolje2.sabirniceIRastavljaci.forEach {
                if (it.rastavljac == odabraniSabRast2) it.rastavljac.ukljuci()
                else it.rastavljac.iskljuci()
            }
            repaint()


            Thread {

                try {
                    if (wait) Thread.sleep(DELAY_MS)
                } catch (e: Exception) {}

                println("=> Uključiti linijske rastavljače s obje strane")
                dalPolje1.izlazniRastavljac.ukljuci()
                dalPolje2.izlazniRastavljac.ukljuci()
                repaint()


                Thread {

                    try {
                        if (wait) Thread.sleep(DELAY_MS)
                    } catch (e: Exception) {}

                    println("=> Uključiti prekidače s obje strane")
                    dalPolje1.prekidac.ukljuci()
                    dalPolje2.prekidac.ukljuci()
                    repaint()

                }.start()

            }.start()


        }.start()
    }

    fun iskljuci(repaint: () -> Unit, wait: Boolean) {

        Thread {

            try {
                if (wait) Thread.sleep(DELAY_MS)
            } catch (e: Exception) {}

            println("=> Isključiti prekidače s obje strane")
            dalPolje1.prekidac.iskljuci()
            dalPolje2.prekidac.iskljuci()
            repaint()

            Thread {

                try {
                    if (wait) Thread.sleep(DELAY_MS)
                } catch (e: Exception) {}

                println("=> Isključiti sabirničke i linijske rastavljače s obje strane")
                dalPolje1.ukljuceniSabirnickiRastavljac()?.iskljuci()
                dalPolje1.izlazniRastavljac.iskljuci()
                dalPolje2.ukljuceniSabirnickiRastavljac()?.iskljuci()
                dalPolje2.izlazniRastavljac.iskljuci()
                repaint()


                Thread {

                    try {
                        if (wait) Thread.sleep(DELAY_MS)
                    } catch (e: Exception) {}

                    println("=> Uključiti rastavljače za uzemljenje na obje strane")
                    dalPolje1.rastavljacUzemljenja.ukljuci()
                    dalPolje2.rastavljacUzemljenja.ukljuci()
                    repaint()

                }.start()

            }.start()

        }.start()
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