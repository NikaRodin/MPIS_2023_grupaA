fun main() {
    println("Starting!")
    val mainFrame = MyFrame()
    val listaSignalaFrame = ListaSignalaFrame()
    mainFrame.updateSignals = {
        listaSignalaFrame.update(it)
    }
    mainFrame.showSignals = {
        listaSignalaFrame.isVisible = true
    }
    Thread {
        mainFrame.init()
    }.start()
    Thread {
        listaSignalaFrame.init()
    }.start()
    println("Done!")
}
