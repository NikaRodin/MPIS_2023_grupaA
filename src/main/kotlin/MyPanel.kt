import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import javax.swing.JPanel
import kotlin.math.max
import kotlin.math.min

class MyPanel internal constructor() : JPanel() {

    //Image image;
    private val dalPolje1: DalekovodnoPolje

    init {

        //image = new ImageIcon("sky.png").getImage();
        this.preferredSize = Dimension(500, 500)

        addMouseListener(object : MouseListener {
            override fun mouseClicked(p0: MouseEvent?) {
                println("clicked x, y: ${p0?.x}, ${p0?.y}")
            }
            override fun mousePressed(p0: MouseEvent?) {}
            override fun mouseReleased(p0: MouseEvent?) {}
            override fun mouseEntered(p0: MouseEvent?) {}
            override fun mouseExited(p0: MouseEvent?) {}
        })

        dalPolje1 = DalekovodnoPolje(
            0, 0,
            listOf(
                SabirnicaIRastavljac(
                    Sabirnica(0, 0, 250, 10),
                    Rastavljac(Coordinate(50, 75))
                ),
                SabirnicaIRastavljac(
                    Sabirnica(0, 20, 250, 10),
                    Rastavljac(Coordinate(150, 75))
                ),
            ),
            Prekidac(Coordinate(100, 200)),
            Rastavljac(Coordinate(100, 350)),
            Rastavljac(Coordinate(200, 275)),
        )
    }

    override fun paint(g: Graphics) {
        val g2D = g as Graphics2D
        drawDalekovodnoPolje(g2D, dalPolje1)
    }

    private fun drawDalekovodnoPolje(g: Graphics2D, polje: DalekovodnoPolje) {
        val rastavljacSize = 50
        val prekidacSize = 50
        val lineWidth = 10

        g.paint = Color.blue
        for (sabirnicaIRastavljac in polje.sabirniceIRastavljaci) {
            val s = sabirnicaIRastavljac.sabirnica
            g.fillRect(s.x, s.y, s.width, s.height)

            val r = sabirnicaIRastavljac.rastavljac
            g.fillRect(r.coordinate.x, r.coordinate.y, rastavljacSize, rastavljacSize)

            // connection
            val upY = min(s.y, r.coordinate.y)
            val downY = max(s.y, r.coordinate.y)
            val rastavljacMiddle = r.coordinate.x + rastavljacSize/2
            g.fillRect(rastavljacMiddle - lineWidth/2, upY, lineWidth, downY - upY)
        }

        // prekidac
        g.fillOval(polje.prekidac.coordinate.x, polje.prekidac.coordinate.y, prekidacSize, prekidacSize)

        var minMiddleX = Int.MAX_VALUE
        var maxMiddleX = Int.MIN_VALUE
        val rastavljaciConnectionLineY = polje.prekidac.coordinate.y - lineWidth - 25
        for (sabirnicaIRastavljac in polje.sabirniceIRastavljaci) {
            val r = sabirnicaIRastavljac.rastavljac
            val rastavljacMiddle = r.coordinate.x + rastavljacSize/2
            val topY = r.coordinate.y + rastavljacSize
            g.fillRect(rastavljacMiddle - lineWidth/2, topY, lineWidth, rastavljaciConnectionLineY - topY)

            minMiddleX = min(minMiddleX, rastavljacMiddle)
            maxMiddleX = max(maxMiddleX, rastavljacMiddle)
        }

        val left = minMiddleX - lineWidth/2
        val right = maxMiddleX + lineWidth/2
        g.fillRect(left, rastavljaciConnectionLineY, right - left, lineWidth)

        val prekidacMiddleX = polje.prekidac.coordinate.x + prekidacSize/2
        g.fillRect(prekidacMiddleX - lineWidth/2, rastavljaciConnectionLineY+1, lineWidth, polje.prekidac.coordinate.y - rastavljaciConnectionLineY)


        // Izlazni rastavljac
        g.fillRect(polje.izlazniRastavljac.coordinate.x, polje.izlazniRastavljac.coordinate.y, prekidacSize, prekidacSize)

        val prekidacBottom = polje.prekidac.coordinate.y + prekidacSize
        g.fillRect(prekidacMiddleX - lineWidth/2, prekidacBottom, lineWidth, polje.izlazniRastavljac.coordinate.y - prekidacBottom)


        // Rastavljac uzemljenja
        g.fillRect(polje.rastavljacUzemljenja.coordinate.x, polje.rastavljacUzemljenja.coordinate.y, prekidacSize, prekidacSize)

        val rastavljacUzemljenjaMidY = polje.rastavljacUzemljenja.coordinate.y + rastavljacSize/2
        g.fillRect(prekidacMiddleX, rastavljacUzemljenjaMidY - lineWidth/2, polje.rastavljacUzemljenja.coordinate.x - prekidacMiddleX, lineWidth)

        // Dalekovod strelica
        val arrowX = prekidacMiddleX.toDouble()
        val arrowY = polje.izlazniRastavljac.coordinate.y.toDouble()
        val dalekovodArrowLine = Line2D.Double(arrowX, arrowY, arrowX, arrowY + 95.0)
        val dalekovodArrowLine2 = Line2D.Double(arrowX, arrowY, arrowX, arrowY + 100.0)
        g.stroke = BasicStroke(lineWidth.toFloat())
        g.draw(dalekovodArrowLine)
        drawArrowHead(g, dalekovodArrowLine2, lineWidth)
    }

    private fun drawArrowHead(g2d: Graphics2D, line: Line2D.Double, arrowSize: Int) {
        val arrowHead = Polygon()
        arrowHead.addPoint(0, arrowSize)
        arrowHead.addPoint(-arrowSize, -arrowSize)
        arrowHead.addPoint(arrowSize, -arrowSize)

        val tx = AffineTransform()
        tx.setToIdentity()
        val angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1)
        tx.translate(line.x2, line.y2)
        tx.rotate(angle - Math.PI / 2.0)
        val g = g2d.create() as Graphics2D
        g.transform = tx
        g.fill(arrowHead)
        g.dispose()
    }
}