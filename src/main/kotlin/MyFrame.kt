import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JButton
import javax.swing.JFrame

class MyFrame internal constructor() : JFrame() {

    private val panel: MyPanel = MyPanel()
    private val upravljacDalekovoda: JButton = JButton()
    private val upravljacMjernogPolja: JButton = JButton()
    private val mouseListener: MouseListener

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

        mouseListener = object : MouseListener {
            override fun mouseClicked(p0: MouseEvent?) {
                panel.mouseListener.mouseClicked(p0)
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
        contentPane.add(upravljacDalekovoda)
        contentPane.add(upravljacMjernogPolja)
        contentPane.add(panel)
        contentPane.addMouseListener(mouseListener)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = true
    }
}