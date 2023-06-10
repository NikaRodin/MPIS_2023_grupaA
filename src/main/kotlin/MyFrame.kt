import javax.swing.JButton
import javax.swing.JFrame

class MyFrame internal constructor() : JFrame() {

    private val panel: MyPanel = MyPanel()
    val upravljacDalekovoda: JButton = JButton()
    val upravljacMjernogPolja: JButton = JButton()

    init {

        upravljacDalekovoda.setBounds(10, 500, 200, 40)
        upravljacDalekovoda.text = tekstUpravljanjaDalekovodom(panel.dalekovod)
        upravljacDalekovoda.isFocusPainted = false
        upravljacDalekovoda.addActionListener {
            panel.dalekovod.toggleStanje()
            upravljacDalekovoda.text = tekstUpravljanjaDalekovodom(panel.dalekovod)
            repaint()
        }

        upravljacMjernogPolja.setBounds(10, 545, 200, 40)
        upravljacMjernogPolja.text = tekstUpravljanjaPoljem(panel.mjernoPolje)
        upravljacMjernogPolja.isFocusPainted = false
        upravljacMjernogPolja.addActionListener {
            panel.mjernoPolje.toggleStanje()
            upravljacMjernogPolja.text = tekstUpravljanjaPoljem(panel.mjernoPolje)
            repaint()
        }

        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(upravljacDalekovoda)
        contentPane.add(upravljacMjernogPolja)
        contentPane.add(panel)
        contentPane.addMouseListener(panel.mouseListener)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = true

        panel.dalekovod.iskljuci()
        panel.mjernoPolje.iskljuci()
    }
}