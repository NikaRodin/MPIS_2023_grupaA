import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame

class MyFrame internal constructor() : JFrame() {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(500, 500)
        setLocationRelativeTo(null)
        isVisible = true
    }

    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.drawLine(0, 0, 500, 500)
    }
}