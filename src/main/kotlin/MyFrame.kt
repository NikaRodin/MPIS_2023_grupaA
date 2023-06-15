import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*


class MyFrame internal constructor() : JFrame() {

    var updateSignaliText: (String) -> Unit = {}

    private val panel: MyPanel = MyPanel()
    private val infoBox: JTextField = JTextField("Welcome!")
    private val mouseListener: MouseListener

    private val upravljacDalekovoda: JButton = JButton()
    private val upravljacMjernogPolja: JButton = JButton()
    private val pokaziSveSignale: JButton = JButton()

    init {
        mouseListener = object : MouseListener {
            override fun mouseClicked(p0: MouseEvent?) {
                infoBox.text = panel.mouseClicked(p0)?:""
                upravljacDalekovoda.text = dohvatiTekstUpravljanjaDalekovodom(panel.dalekovod)
                upravljacMjernogPolja.text = dohvatiTekstUpravljanjaPoljem(panel.mjernoPolje)
                revalidate()
                repaint()
            }
            override fun mousePressed(p0: MouseEvent?) {}
            override fun mouseReleased(p0: MouseEvent?) {}
            override fun mouseEntered(p0: MouseEvent?) {}
            override fun mouseExited(p0: MouseEvent?) {}
        }
    }

    fun init() {
        infoBox.setBounds(420, 545, 300, 40)
        infoBox.horizontalAlignment = JTextField.CENTER
        infoBox.isEditable = false

        upravljacDalekovoda.setBounds(10, 500, 200, 40)
        upravljacDalekovoda.text = dohvatiTekstUpravljanjaDalekovodom(panel.dalekovod)
        upravljacDalekovoda.isFocusPainted = false
        upravljacDalekovoda.addActionListener {
            Thread{
                infoBox.text = getLoadingText(panel.dalekovod)
                upravljacDalekovoda.isEnabled = false
                panel.dalekovod.toggleStanje(::repaint, true)
                upravljacDalekovoda.isEnabled = true
                upravljacDalekovoda.text = dohvatiTekstUpravljanjaDalekovodom(panel.dalekovod)
                infoBox.text = "Dalekovod ${panel.dalekovod.provjeriStanje().opis}!"
                repaint()
            }.start()
        }

        upravljacMjernogPolja.setBounds(10, 545, 200, 40)
        upravljacMjernogPolja.text = dohvatiTekstUpravljanjaPoljem(panel.mjernoPolje)
        upravljacMjernogPolja.isFocusPainted = false
        upravljacMjernogPolja.addActionListener {
            panel.mjernoPolje.toggleStanje()
            upravljacMjernogPolja.text = dohvatiTekstUpravljanjaPoljem(panel.mjernoPolje)
            infoBox.text = "Mjerno polje ${panel.mjernoPolje.provjeriStanje().opis}!"
            repaint()

        }

        pokaziSveSignale.setBounds(10, 590, 200, 40)
        pokaziSveSignale.text = "Svi signali"
        pokaziSveSignale.isFocusPainted = false
        pokaziSveSignale.addActionListener {
            updateSignaliText(SignalsProcessor().all(panel))
        }

        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.add(infoBox)
        contentPane.add(upravljacDalekovoda)
        contentPane.add(upravljacMjernogPolja)
        contentPane.add(pokaziSveSignale)
        contentPane.add(panel)
        contentPane.addMouseListener(mouseListener)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = true
    }
}