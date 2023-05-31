import javax.swing.JFrame

class MyFrame internal constructor() : JFrame() {

    private val panel: MyPanel

    init {
        panel = MyPanel()
        defaultCloseOperation = EXIT_ON_CLOSE
        this.add(panel)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = true
    }
}