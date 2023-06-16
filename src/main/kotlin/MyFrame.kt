import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*

class MyFrame internal constructor() : JFrame() {

    var updateSignals: (List<Signal>) -> Unit = {}
    var showSignals: () -> Unit = {}

    private val panel: MyPanel = MyPanel()
    private val infoBox: JTextField = JTextField("Dobrodošli!")
    private val mouseListener: MouseListener

    private val upravljacDalekovoda: JButton = JButton()
    private val upravljacMjernogPolja: JButton = JButton()
    private val pokaziSveSignale: JButton = JButton()

    init {
        mouseListener = object : MouseListener {
            override fun mouseClicked(p0: MouseEvent?) {
                Thread {
                    val error = panel.mouseClicked(p0, infoBox)
                    if(error != null) infoBox.text = error
                    upravljacDalekovoda.text = dohvatiTekstUpravljanja(panel.dalekovod)
                    upravljacMjernogPolja.text = dohvatiTekstUpravljanja(panel.mjernoPolje)
                    revalidate()
                    repaint()
                }.start()
            }
            override fun mousePressed(p0: MouseEvent?) {}
            override fun mouseReleased(p0: MouseEvent?) {}
            override fun mouseEntered(p0: MouseEvent?) {}
            override fun mouseExited(p0: MouseEvent?) {}
        }
    }

    fun init() {
        infoBox.setBounds(420, 585, 300, 40)
        infoBox.horizontalAlignment = JTextField.CENTER
        infoBox.isEditable = false

        upravljacDalekovoda.setBounds(10, 540, 200, 40)
        upravljacDalekovoda.text = dohvatiTekstUpravljanja(panel.dalekovod)
        upravljacDalekovoda.isFocusPainted = false
        upravljacDalekovoda.addActionListener {
            Thread{
                infoBox.text = getLoadingText(panel.dalekovod)
                upravljacDalekovoda.isEnabled = false
                panel.dalekovod.toggleStanje(::repaint, true)
                upravljacDalekovoda.isEnabled = true
                upravljacDalekovoda.text = dohvatiTekstUpravljanja(panel.dalekovod)
                infoBox.text = "Dalekovod ${panel.dalekovod.provjeriStanje().opis}!"
                repaint()
            }.start()
        }

        upravljacMjernogPolja.setBounds(10, 585, 200, 40)
        upravljacMjernogPolja.text = dohvatiTekstUpravljanja(panel.mjernoPolje)
        upravljacMjernogPolja.isFocusPainted = false
        upravljacMjernogPolja.addActionListener {
            Thread{
                infoBox.text = getLoadingText(panel.mjernoPolje)
                upravljacMjernogPolja.isEnabled = false
                panel.mjernoPolje.toggleStanje(::repaint, true)
                upravljacMjernogPolja.isEnabled = true
                upravljacMjernogPolja.text = dohvatiTekstUpravljanja(panel.mjernoPolje)
                infoBox.text = "Mjerno polje ${panel.mjernoPolje.provjeriStanje().opis}!"
                repaint()
            }.start()
        }

        pokaziSveSignale.setBounds(10, 630, 200, 40)
        pokaziSveSignale.text = "Liste signala"
        pokaziSveSignale.isFocusPainted = false
        pokaziSveSignale.addActionListener {
            updateSignals()
            showSignals()
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

        panel.onRepaint = { repaint() }
    }

    override fun repaint() {
        super.repaint()
        updateSignals()
    }

    private fun updateSignals() {
        updateSignals(SignalsProcessor().getAllSignals(panel))
    }
}