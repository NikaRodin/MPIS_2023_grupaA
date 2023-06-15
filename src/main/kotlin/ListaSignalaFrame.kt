import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.DefaultHighlighter

enum class FilterMode(
    val buttonText: String,
    val highlightActive: Boolean,
) {
    ALL("Svi signali", true), ACTIVE("Trenutni signali", false)
}

fun FilterMode.toggle(): FilterMode {
    return when (this) {
        FilterMode.ALL -> FilterMode.ACTIVE
        FilterMode.ACTIVE -> FilterMode.ALL
    }
}

class ListaSignalaFrame : JFrame() {

    private val WIDTH = 1100
    private val HEIGHT = 800
    private val TOP_PADDING = 170

    private var filterMode: FilterMode = FilterMode.ALL

    private val listeSignalaBox: JTextArea = JTextArea("")
    private val scroll: JScrollPane = JScrollPane(
        listeSignalaBox,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    )

    private var allSignals: List<Signal> = listOf()

    private val allSignalsToggle: JButton = JButton()

    private val poljeFilterLabel: JTextField = JTextField("Polje:")
    private val poljeFilter: JTextField = JTextField("")
    private val uredajFilterLabel: JTextField = JTextField("Uređaj:")
    private val uredajFilter: JTextField = JTextField("")
    private val varijablaFilterLabel: JTextField = JTextField("Varijabla:")
    private val varijablaFilter: JTextField = JTextField("")

    fun init() {
        this.preferredSize = Dimension(WIDTH, HEIGHT)

        initFilters()

        listeSignalaBox.isEditable = false
        listeSignalaBox.font = Font(Font.MONOSPACED, Font.BOLD, 16)

        scroll.setBounds(0, TOP_PADDING, WIDTH-10, HEIGHT-TOP_PADDING)

        defaultCloseOperation = HIDE_ON_CLOSE
        contentPane.add(allSignalsToggle)
        contentPane.add(poljeFilterLabel)
        contentPane.add(poljeFilter)
        contentPane.add(uredajFilterLabel)
        contentPane.add(uredajFilter)
        contentPane.add(varijablaFilterLabel)
        contentPane.add(varijablaFilter)
        contentPane.add(scroll)
        contentPane.add(object : JPanel() {
            init {
                this.preferredSize = Dimension(WIDTH, HEIGHT)
            }
        })
        pack()
        setLocationRelativeTo(null)
        this.isVisible = false
    }

    fun update(allSignals: List<Signal>) {
        this.allSignals = allSignals

        val prevScrollX = scroll.horizontalScrollBar.value
        val prevScrollY = scroll.verticalScrollBar.value

        val filteredSignals = filterSignals(allSignals)

        listeSignalaBox.text = toString(filteredSignals)
        highlighRow(0, Color.YELLOW)

        if (filterMode.highlightActive) {
            filteredSignals.forEachIndexed { index, signal ->
                if (signal.isActive) {
                    highlighRow(index+1, Color.GREEN)
                }
            }
        }

        repaint()
        scrollTo(prevScrollX, prevScrollY)
    }

    private fun initFilters() {
        allSignalsToggle.setBounds(10, 10, 200, 40)
        allSignalsToggle.text = filterMode.buttonText
        allSignalsToggle.isFocusPainted = false
        allSignalsToggle.addActionListener {
            filterMode = filterMode.toggle()
            allSignalsToggle.text = filterMode.buttonText
            update(allSignals)
        }

        val updateOnChange = object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) {
                update(allSignals)
            }

            override fun removeUpdate(e: DocumentEvent?) {
                update(allSignals)
            }

            override fun insertUpdate(e: DocumentEvent?) {
                update(allSignals)
            }
        }

        poljeFilterLabel.setBounds(10, 60, 100, 25)
        poljeFilterLabel.horizontalAlignment = JTextField.LEFT
        poljeFilterLabel.isEditable = false
        poljeFilterLabel.isFocusable = false

        poljeFilter.setBounds(120, 60, 100, 25)
        poljeFilter.horizontalAlignment = JTextField.LEFT
        poljeFilter.isEditable = true
        poljeFilter.document.addDocumentListener(updateOnChange)


        uredajFilterLabel.setBounds(10, 95, 100, 25)
        uredajFilterLabel.horizontalAlignment = JTextField.LEFT
        uredajFilterLabel.isEditable = false
        uredajFilterLabel.isFocusable = false

        uredajFilter.setBounds(120, 95, 100, 25)
        uredajFilter.horizontalAlignment = JTextField.LEFT
        uredajFilter.isEditable = true
        uredajFilter.document.addDocumentListener(updateOnChange)


        varijablaFilterLabel.setBounds(10, 130, 100, 25)
        varijablaFilterLabel.horizontalAlignment = JTextField.LEFT
        varijablaFilterLabel.isEditable = false
        varijablaFilterLabel.isFocusable = false

        varijablaFilter.setBounds(120, 130, 100, 25)
        varijablaFilter.horizontalAlignment = JTextField.LEFT
        varijablaFilter.isEditable = true
        varijablaFilter.document.addDocumentListener(updateOnChange)
    }

    private fun filterSignals(signals: List<Signal>): List<Signal> {
        return when (filterMode) {
            FilterMode.ALL -> signals
            FilterMode.ACTIVE -> signals.filter { it.isActive }
        }.filter { signal ->
            signal.polje.contains(poljeFilter.text, ignoreCase = true) &&
            signal.nazivUredaja.contains(uredajFilter.text, ignoreCase = true) &&
            signal.varijabla.contains(varijablaFilter.text, ignoreCase = true)
        }
    }

    private fun highlighRow(row: Int, color: Color) {
        try {
            val startIndex: Int = listeSignalaBox.getLineStartOffset(row)
            val endIndex: Int = listeSignalaBox.getLineEndOffset(row)
            val painter = DefaultHighlighter.DefaultHighlightPainter(color)
            listeSignalaBox.highlighter.addHighlight(startIndex, endIndex, painter)
        } catch (ignored: Exception) {

        }
    }

    private fun toString(signals: List<Signal>): String {
        val headerAndSignals = ArrayList(signals)
        headerAndSignals.add(0, Signal("EEP", "Napon", "Polje", "Uređaj", "Varijabla", "Stanje", false))

        val sb = StringBuilder()
        for (s in headerAndSignals) {
            sb.append(s.toString())
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun scrollTo(xpos: Int, ypos: Int) {
        SwingUtilities.invokeLater {
            scroll.horizontalScrollBar.value = xpos
            scroll.verticalScrollBar.value = ypos
        }
    }
}