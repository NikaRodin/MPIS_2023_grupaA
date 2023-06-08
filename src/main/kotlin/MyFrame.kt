import javax.swing.JFrame

class MyFrame internal constructor() : JFrame() {

    private val panel: MyPanel

    init {
        panel = MyPanel()
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(panel)
        contentPane.addMouseListener(panel.mouseListener)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = true
    }
}