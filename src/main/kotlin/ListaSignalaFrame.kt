import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class ListaSignalaFrame : JFrame() {

    private val listeSignalaBox: JTextArea = JTextArea("")

    fun init() {
        this.preferredSize = Dimension(1100, 800)

        listeSignalaBox.setBounds(0, 650, 730, 200)
        listeSignalaBox.isEditable = false
        listeSignalaBox.setSize(400, 400)
        listeSignalaBox.font = Font(Font.MONOSPACED, Font.BOLD, 16)

        val scroll = JScrollPane(
            listeSignalaBox,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        )
        scroll.setBounds(100, 100, 200, 200)

        defaultCloseOperation = HIDE_ON_CLOSE
        contentPane.add(scroll)
        pack()
        setLocationRelativeTo(null)
        this.isVisible = false
    }

    fun updateText(text: String) {
        listeSignalaBox.text = text
    }
}