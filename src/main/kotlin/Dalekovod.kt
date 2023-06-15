class Dalekovod(
    private val dalPolje1: DalekovodnoPolje,
    private val dalPolje2: DalekovodnoPolje,
    private val DELAY_MS: Long = 1000L
) {
    fun toggleStanje(repaint: () -> Unit, wait: Boolean) {
        when (provjeriStanje()) {
            StanjeDalekovoda.ON -> iskljuci(repaint, wait)
            StanjeDalekovoda.OFF -> ukljuci(repaint, wait)
        }
    }

    fun ukljuci(repaint: () -> Unit, wait: Boolean) {
        val odabraniSabRast1: Rastavljac =
            dalPolje1.pronadiUkljuceniSabirnickiRastavljac() ?: dalPolje1.sabirniceIRastavljaci.first().rastavljac
        val odabraniSabRast2: Rastavljac =
            dalPolje2.pronadiUkljuceniSabirnickiRastavljac() ?: dalPolje2.sabirniceIRastavljaci.first().rastavljac

        var doneCounter = 0

        if (wait) sleep(DELAY_MS)

        println("=> Isključiti rastavljače za uzemljenje s obje strane")
        doneCounter = 0
        Thread { dalPolje1.rastavljacUzemljenja.iskljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.rastavljacUzemljenja.iskljuci(repaint, wait); doneCounter++ }.start()
        while (doneCounter != 2) sleep(10)
        repaint()

        if (wait) sleep(DELAY_MS)

        println("=> Uključiti sabirničke rastavljače s obje strane")
        doneCounter = 0
        Thread {
            dalPolje1.sabirniceIRastavljaci.forEach {
                if (it.rastavljac == odabraniSabRast1) it.rastavljac.ukljuci(repaint, wait)
                else it.rastavljac.iskljuci(repaint, wait)
            }
            doneCounter++
        }.start()
        Thread {
            dalPolje2.sabirniceIRastavljaci.forEach {
                if (it.rastavljac == odabraniSabRast2) it.rastavljac.ukljuci(repaint, wait)
                else it.rastavljac.iskljuci(repaint, wait)
            }
            doneCounter++
        }.start()
        while (doneCounter != 2) sleep(10)
        repaint()

        if (wait) sleep(DELAY_MS)

        println("=> Uključiti linijske rastavljače s obje strane")
        doneCounter = 0
        Thread { dalPolje1.izlazniRastavljac.ukljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.izlazniRastavljac.ukljuci(repaint, wait); doneCounter++ }.start()
        while (doneCounter != 2) sleep(10)
        repaint()

        if (wait) sleep(DELAY_MS)

        println("=> Uključiti prekidače s obje strane")
        doneCounter = 0
        Thread { dalPolje1.prekidac.ukljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.prekidac.ukljuci(repaint, wait); doneCounter++ }.start()
        while (doneCounter != 2) sleep(10)

        repaint()
    }

    fun iskljuci(repaint: () -> Unit, wait: Boolean) {

        var doneCounter = 0

        if (wait) sleep(DELAY_MS)

        println("=> Isključiti prekidače s obje strane")
        doneCounter = 0
        Thread { dalPolje1.prekidac.iskljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.prekidac.iskljuci(repaint, wait); doneCounter++ }.start()
        while (doneCounter != 2) sleep(10)
        repaint()

        if (wait) sleep(DELAY_MS)

        println("=> Isključiti sabirničke i linijske rastavljače s obje strane")
        doneCounter = 0
        Thread { dalPolje1.pronadiUkljuceniSabirnickiRastavljac()?.iskljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.pronadiUkljuceniSabirnickiRastavljac()?.iskljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje1.izlazniRastavljac.iskljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.izlazniRastavljac.iskljuci(repaint, wait); doneCounter++ }.start()
        while (doneCounter != 4) sleep(10)
        repaint()

        if (wait) sleep(DELAY_MS)

        println("=> Uključiti rastavljače za uzemljenje na obje strane")
        doneCounter = 0
        Thread { dalPolje1.rastavljacUzemljenja.ukljuci(repaint, wait); doneCounter++ }.start()
        Thread { dalPolje2.rastavljacUzemljenja.ukljuci(repaint, wait); doneCounter++ }.start()
        while (doneCounter != 2) sleep(10)
        repaint()
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