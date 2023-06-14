fun main() {
    println("Starting!")
    val mainFrame = MyFrame()
    val listaSignalaFrame = ListaSignalaFrame()
    mainFrame.updateSignaliText = {
        listaSignalaFrame.updateText(it)
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
