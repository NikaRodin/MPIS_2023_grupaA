import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.*
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultHighlighter


class ListaSignalaFrame : JFrame() {

    private val listeSignalaBox: JTextArea = JTextArea("")
    private val scroll: JScrollPane = JScrollPane(
        listeSignalaBox,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    )

    fun init() {
        this.preferredSize = Dimension(1100, 800)

        listeSignalaBox.setBounds(0, 650, 730, 200)
        listeSignalaBox.isEditable = false
        listeSignalaBox.setSize(400, 400)
        listeSignalaBox.font = Font(Font.MONOSPACED, Font.BOLD, 16)

        scroll.setBounds(100, 100, 200, 200)

        defaultCloseOperation = HIDE_ON_CLOSE
        contentPane.add(scroll)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = false
    }

    fun update(signals: List<Signal>) {
        val prevScrollX = scroll.horizontalScrollBar.value
        val prevScrollY = scroll.verticalScrollBar.value

        listeSignalaBox.text = toString(signals)
        signals.forEachIndexed { index, signal ->
            if (signal.isActive) {
                highlighRow(index, Color.GREEN)
            }
        }

        repaint()
        scrollTo(prevScrollX, prevScrollY)
    }

    private fun highlighRow(row: Int, color: Color) {
        try {
            val startIndex: Int = listeSignalaBox.getLineStartOffset(row)
            val endIndex: Int = listeSignalaBox.getLineEndOffset(row)
            val painter = DefaultHighlighter.DefaultHighlightPainter(color)
            listeSignalaBox.highlighter.addHighlight(startIndex, endIndex, painter)
        } catch (ble: BadLocationException) {
            ble.printStackTrace()
        }
    }

    private fun toString(signals: List<Signal>): String {
        val sb = StringBuilder()
        for (s in signals) {
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