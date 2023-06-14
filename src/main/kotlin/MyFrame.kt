import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextField

class MyFrame internal constructor() : JFrame() {

    private val panel: MyPanel = MyPanel()
    private val infoBox: JTextField = JTextField("Welcome!")
    private val upravljacDalekovoda: JButton = JButton()
    private val upravljacMjernogPolja: JButton = JButton()
    private val mouseListener: MouseListener

    init {
        infoBox.setBounds(420, 545, 300, 40)
        infoBox.horizontalAlignment = JTextField.CENTER
        infoBox.isEditable = false

        upravljacDalekovoda.setBounds(10, 500, 200, 40)
        upravljacDalekovoda.text = tekstUpravljanjaDalekovodom(panel.dalekovod)
        upravljacDalekovoda.isFocusPainted = false
        upravljacDalekovoda.addActionListener {
            panel.dalekovod.toggleStanje(::repaint, true)
            upravljacDalekovoda.text = tekstUpravljanjaDalekovodom(panel.dalekovod)
            infoBox.text = "Dalekovod ${panel.dalekovod.provjeriStanje().opis}!"
            repaint()
        }

        upravljacMjernogPolja.setBounds(10, 545, 200, 40)
        upravljacMjernogPolja.text = tekstUpravljanjaPoljem(panel.mjernoPolje)
        upravljacMjernogPolja.isFocusPainted = false
        upravljacMjernogPolja.addActionListener {
            panel.mjernoPolje.toggleStanje()
            upravljacMjernogPolja.text = tekstUpravljanjaPoljem(panel.mjernoPolje)
            infoBox.text = "Mjerno polje ${panel.mjernoPolje.provjeriStanje().opis}!"
            repaint()
        }

        mouseListener = object : MouseListener {
            override fun mouseClicked(p0: MouseEvent?) {
                infoBox.text = panel.mouseClicked(p0)?:""
                upravljacDalekovoda.text = tekstUpravljanjaDalekovodom(panel.dalekovod)
                upravljacMjernogPolja.text = tekstUpravljanjaPoljem(panel.mjernoPolje)
                revalidate()
                repaint()
            }
            override fun mousePressed(p0: MouseEvent?) {}
            override fun mouseReleased(p0: MouseEvent?) {}
            override fun mouseEntered(p0: MouseEvent?) {}
            override fun mouseExited(p0: MouseEvent?) {}
        }

        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(infoBox)
        contentPane.add(upravljacDalekovoda)
        contentPane.add(upravljacMjernogPolja)
        contentPane.add(panel)
        contentPane.addMouseListener(mouseListener)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = true
    }
}