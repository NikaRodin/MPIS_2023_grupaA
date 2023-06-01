import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import javax.swing.JPanel
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

class MyPanel internal constructor() : JPanel() {

    val dalPolje1: DalekovodnoPolje
    val dalPolje2: DalekovodnoPolje

    init {

        //image = new ImageIcon("sky.png").getImage();
        this.preferredSize = Dimension(650, 500)

        dalPolje1 = DalekovodnoPolje(
            0, 0,
            listOf(
                SabirnicaIRastavljac(
                    Sabirnica(0, 0, 250, 10),
                    Rastavljac("Rastavljac S1", StanjeSklopnogUredaja.OFF, Coordinate(50, 75))
                ),
                SabirnicaIRastavljac(
                    Sabirnica(0, 20, 250, 10),
                    Rastavljac("Rastavljac S2", StanjeSklopnogUredaja.OFF, Coordinate(150, 75))
                ),
            ),
            Prekidac("Prekidac", StanjeSklopnogUredaja.OFF, Coordinate(100, 200)),
            Rastavljac("Izlazni rastavljac", StanjeSklopnogUredaja.OFF, Coordinate(100, 350)),
            Rastavljac("Rastavljac uzemljenja", StanjeSklopnogUredaja.OFF, Coordinate(200, 275)),
        )

        dalPolje2 = DalekovodnoPolje(
            350, 0,
            listOf(
                SabirnicaIRastavljac(
                    Sabirnica(0, 0, 250, 10),
                    Rastavljac("Rastavljac S", StanjeSklopnogUredaja.OFF, Coordinate(100, 75))
                ),
            ),
            Prekidac("Prekidac", StanjeSklopnogUredaja.OFF, Coordinate(100, 200)),
            Rastavljac("Izlazni rastavljac", StanjeSklopnogUredaja.OFF, Coordinate(100, 350)),
            Rastavljac("Rastavljac uzemljenja", StanjeSklopnogUredaja.OFF, Coordinate(200, 275)),
        )

        addMouseListener(object : MouseListener {
            override fun mouseClicked(p0: MouseEvent?) {
                p0?.let {
//                    println("clicked x, y: ${p0.x}, ${p0.y}")
                    val polja = listOf(dalPolje1, dalPolje2)
                    val clickError = polja.firstNotNullOfOrNull { it.click(p0.x, p0.y) }
                    if (clickError == null) {
                        revalidate()
                        repaint()
                    } else {
                        println("Greška: $clickError")
                    }
                }
            }
            override fun mousePressed(p0: MouseEvent?) {}
            override fun mouseReleased(p0: MouseEvent?) {}
            override fun mouseEntered(p0: MouseEvent?) {}
            override fun mouseExited(p0: MouseEvent?) {}
        })
    }

    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.clearRect(g2d.clipBounds.x, g2d.clipBounds.y, g2d.clipBounds.width, g2d.clipBounds.height)
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        drawPolje(g2d, dalPolje1)
        drawPolje(g2d, dalPolje2)
    }

    private fun drawPolje(g2d: Graphics2D, polje: DalekovodnoPolje) {
        val tx = AffineTransform()
        tx.setToIdentity()
        tx.translate(polje.x.toDouble(), polje.y.toDouble())

        val g = g2d.create() as Graphics2D
        g.transform = tx
        drawDalekovodnoPolje(g, polje)
        g.dispose()
    }

    private fun drawDalekovodnoPolje(g: Graphics2D, polje: DalekovodnoPolje) {
        val defaultColor = Color.black

        // Sabirnice i rastavljaci
        g.color = defaultColor
        for (sabirnicaIRastavljac in polje.sabirniceIRastavljaci) {
            val s = sabirnicaIRastavljac.sabirnica
            g.fillRect(s.x, s.y, s.width, s.height)

            val r = sabirnicaIRastavljac.rastavljac
            g.color = getColor(r.stanje)
            g.fillRect(r.coordinate.x, r.coordinate.y, RASTAVLJAC_SIZE, RASTAVLJAC_SIZE)
            g.color = defaultColor

            // connection
            val upY = min(s.y, r.coordinate.y)
            val downY = max(s.y, r.coordinate.y)
            val rastavljacMiddle = r.coordinate.x + RASTAVLJAC_SIZE/2
            g.fillRect(rastavljacMiddle - LINE_WIDTH /2, upY, LINE_WIDTH, downY - upY)
        }

        // prekidac
        g.color = getColor(polje.prekidac.stanje)
        g.fillOval(polje.prekidac.coordinate.x, polje.prekidac.coordinate.y, PREKIDAC_SIZE, PREKIDAC_SIZE)
        g.color = defaultColor

        var minMiddleX = Int.MAX_VALUE
        var maxMiddleX = Int.MIN_VALUE
        val rastavljaciConnectionLineY = polje.prekidac.coordinate.y - LINE_WIDTH - 25
        for (sabirnicaIRastavljac in polje.sabirniceIRastavljaci) {
            val r = sabirnicaIRastavljac.rastavljac
            val rastavljacMiddle = r.coordinate.x + RASTAVLJAC_SIZE/2
            val topY = r.coordinate.y + RASTAVLJAC_SIZE
            g.fillRect(rastavljacMiddle - LINE_WIDTH/2, topY, LINE_WIDTH, rastavljaciConnectionLineY - topY)

            minMiddleX = min(minMiddleX, rastavljacMiddle)
            maxMiddleX = max(maxMiddleX, rastavljacMiddle)
        }

        val left = minMiddleX - LINE_WIDTH/2
        val right = maxMiddleX + LINE_WIDTH/2
        g.fillRect(left, rastavljaciConnectionLineY, right - left, LINE_WIDTH)

        val prekidacMiddleX = polje.prekidac.coordinate.x + PREKIDAC_SIZE/2
        g.fillRect(prekidacMiddleX - LINE_WIDTH/2, rastavljaciConnectionLineY+1, LINE_WIDTH, polje.prekidac.coordinate.y - rastavljaciConnectionLineY)


        // Izlazni rastavljac
        g.color = getColor(polje.izlazniRastavljac.stanje)
        g.fillRect(polje.izlazniRastavljac.coordinate.x, polje.izlazniRastavljac.coordinate.y, PREKIDAC_SIZE, PREKIDAC_SIZE)
        g.color = defaultColor

        val prekidacBottom = polje.prekidac.coordinate.y + PREKIDAC_SIZE
        g.fillRect(prekidacMiddleX - LINE_WIDTH/2, prekidacBottom, LINE_WIDTH, polje.izlazniRastavljac.coordinate.y - prekidacBottom)


        // Rastavljac uzemljenja
        g.color = getColor(polje.rastavljacUzemljenja.stanje)
        g.fillRect(polje.rastavljacUzemljenja.coordinate.x, polje.rastavljacUzemljenja.coordinate.y, RASTAVLJAC_SIZE, RASTAVLJAC_SIZE)
        g.color = defaultColor

        val rastavljacUzemljenjaMidY = polje.rastavljacUzemljenja.coordinate.y + RASTAVLJAC_SIZE/2
        g.fillRect(prekidacMiddleX, rastavljacUzemljenjaMidY - LINE_WIDTH/2, polje.rastavljacUzemljenja.coordinate.x - prekidacMiddleX, LINE_WIDTH)

        // Dalekovod strelica
        val arrowX = prekidacMiddleX.toDouble()
        val arrowY = polje.izlazniRastavljac.coordinate.y.toDouble() + RASTAVLJAC_SIZE
        val dalekovodArrowLine = Line2D.Double(arrowX, arrowY-1, arrowX, arrowY + 60.0)
        val dalekovodArrowLine2 = Line2D.Double(arrowX, arrowY, arrowX, arrowY + 60.0 + LINE_WIDTH)
        g.stroke = BasicStroke(LINE_WIDTH.toFloat(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL)
        g.draw(dalekovodArrowLine)
        drawArrowHead(g, dalekovodArrowLine2, LINE_WIDTH)
    }

    private fun drawArrowHead(g2d: Graphics2D, line: Line2D.Double, arrowSize: Int) {
        val arrowHead = Polygon()
        arrowHead.addPoint(0, arrowSize)
        arrowHead.addPoint(-arrowSize, -arrowSize)
        arrowHead.addPoint(arrowSize, -arrowSize)

        val tx = AffineTransform(g2d.transform)
        val angle = atan2(line.y2 - line.y1, line.x2 - line.x1)
        tx.translate(line.x2, line.y2)
        tx.rotate(angle - Math.PI / 2.0)
        val g = g2d.create() as Graphics2D
        g.transform = tx
        g.fill(arrowHead)
        g.dispose()
    }
}