import java.awt.*
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import javax.swing.JPanel
import javax.swing.JTextField
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

class MyPanel : JPanel() {

    var onRepaint: () -> Unit = {}

    val dalPolje1: DalekovodnoPolje
    val dalPolje2: DalekovodnoPolje
    val mjernoPolje: MjernoPolje
    val dalekovod: Dalekovod

    init {

        this.preferredSize = Dimension(730, 685)

        dalPolje1 = DalekovodnoPolje(
            "DV A2",
            "TS A1",
            110f,
            0, 0,
            listOf(
                SabirnicaIRastavljac(
                    Sabirnica(0, 0, 250, 10),
                    Rastavljac("Rastavljač S1", StanjeSklopnogUredaja.OFF, Coordinate(50, 75))
                ),
                SabirnicaIRastavljac(
                    Sabirnica(0, 20, 250, 10),
                    Rastavljac("Rastavljač S2", StanjeSklopnogUredaja.OFF, Coordinate(150, 75))
                ),
            ),
            Prekidac("Prekidač", StanjeSklopnogUredaja.OFF, Coordinate(100, 200)),
            Rastavljac("Izlazni rastavljač", StanjeSklopnogUredaja.OFF, Coordinate(100, 325)),
            Rastavljac("Rastavljač uzemljenja", StanjeSklopnogUredaja.OFF, Coordinate(200, 400)),
            listOf(
                MjerniUredaj("Mjerni pretvornik", TipMjernogUredaja.RADNA_SNAGA, 20F),
                MjerniUredaj("Brojilo", TipMjernogUredaja.JALOVA_ENERGIJA, 5F)
            )
        )

        dalPolje2 = DalekovodnoPolje(
            "DV A1",
            "TS A2",
            110f,
            350, 0,
            listOf(
                SabirnicaIRastavljac(
                    Sabirnica(0, 0, 250, 10),
                    Rastavljac("Rastavljač S", StanjeSklopnogUredaja.OFF, Coordinate(100, 75))
                ),
            ),
            Prekidac("Prekidač", StanjeSklopnogUredaja.OFF, Coordinate(100, 200)),
            Rastavljac("Izlazni rastavljač", StanjeSklopnogUredaja.OFF, Coordinate(100, 325)),
            Rastavljac("Rastavljač uzemljenja", StanjeSklopnogUredaja.OFF, Coordinate(200, 400)),
            listOf(
                MjerniUredaj("Mjerni pretvornik", TipMjernogUredaja.RADNA_SNAGA, 20F),
                MjerniUredaj("Brojilo", TipMjernogUredaja.JALOVA_ENERGIJA, 5F)
            )
        )

        mjernoPolje = MjernoPolje(
            "MP",
            "TS A2",
            110f,
            350, 0,
            SabirnicaIRastavljac(
                Sabirnica(250, 0, 125, 10),
                Rastavljac("Rastavljač mjernog polja", StanjeSklopnogUredaja.OFF, Coordinate(285, 75))
            ),
            listOf(
                MjerniUredaj("Mjerni pretvornik", TipMjernogUredaja.NAPON, 110F),
                MjerniUredaj("Mjerni pretvornik", TipMjernogUredaja.FREKVENCIJA, 50F)
            )
        )

        dalekovod = Dalekovod(dalPolje1, dalPolje2)

        dalekovod.ukljuci(::repaint, false)
        mjernoPolje.ukljuciImmediate()
    }

    fun mouseClicked(p0: MouseEvent?, infoBox: JTextField): String? {
        p0?.let {
            println("clicked x, y: ${p0.x}, ${p0.y}")
            val polja = listOf(dalPolje1, dalPolje2, mjernoPolje)
            val clickError = polja.firstNotNullOfOrNull { it.click(p0.x, p0.y, ::repaint, true, infoBox) }
            if (clickError != null)
                return clickError

            revalidate()
            repaint()
        }
        return null
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
        drawPolje(g2d, mjernoPolje)
    }

    private fun drawPolje(g2d: Graphics2D, polje: Polje): String? {
        val tx = AffineTransform()
        tx.setToIdentity()
        tx.translate(polje.x.toDouble(), polje.y.toDouble())

        val g = g2d.create() as Graphics2D
        g.transform = tx
        when (polje) {
            is DalekovodnoPolje -> drawDalekovodnoPolje(g, polje)
            is MjernoPolje -> drawMjernoPolje(g, polje)
            else -> return "Pokušaj crtanja nepodržanog polja."
        }
        g.dispose()
        return null
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
            g.fillOval(r.coordinate.x, r.coordinate.y, RASTAVLJAC_SIZE, RASTAVLJAC_SIZE)
            g.color = defaultColor

            // connection
            val upY = min(s.y, r.coordinate.y)
            val downY = max(s.y, r.coordinate.y)
            val rastavljacMiddle = r.coordinate.x + RASTAVLJAC_SIZE / 2
            g.fillRect(rastavljacMiddle - LINE_WIDTH / 2, upY, LINE_WIDTH, downY - upY)
        }

        // Prekidac
        g.color = getColor(polje.prekidac.stanje)
        g.fillRect(polje.prekidac.coordinate.x, polje.prekidac.coordinate.y, PREKIDAC_SIZE, PREKIDAC_SIZE)
        g.color = defaultColor

        var minMiddleX = Int.MAX_VALUE
        var maxMiddleX = Int.MIN_VALUE
        val rastavljaciConnectionLineY = polje.prekidac.coordinate.y - LINE_WIDTH - 25
        for (sabirnicaIRastavljac in polje.sabirniceIRastavljaci) {
            val r = sabirnicaIRastavljac.rastavljac
            val rastavljacMiddle = r.coordinate.x + RASTAVLJAC_SIZE / 2
            val topY = r.coordinate.y + RASTAVLJAC_SIZE
            g.fillRect(rastavljacMiddle - LINE_WIDTH / 2, topY, LINE_WIDTH, rastavljaciConnectionLineY - topY)

            minMiddleX = min(minMiddleX, rastavljacMiddle)
            maxMiddleX = max(maxMiddleX, rastavljacMiddle)
        }

        val left = minMiddleX - LINE_WIDTH / 2
        val right = maxMiddleX + LINE_WIDTH / 2
        g.fillRect(left, rastavljaciConnectionLineY, right - left, LINE_WIDTH)

        val prekidacMiddleX = polje.prekidac.coordinate.x + PREKIDAC_SIZE / 2
        g.fillRect(
            prekidacMiddleX - LINE_WIDTH / 2,
            rastavljaciConnectionLineY + 1,
            LINE_WIDTH,
            polje.prekidac.coordinate.y - rastavljaciConnectionLineY
        )

        // Izlazni rastavljac
        g.color = getColor(polje.izlazniRastavljac.stanje)
        g.fillOval(
            polje.izlazniRastavljac.coordinate.x,
            polje.izlazniRastavljac.coordinate.y,
            RASTAVLJAC_SIZE,
            RASTAVLJAC_SIZE
        )
        g.color = defaultColor

        val prekidacBottom = polje.prekidac.coordinate.y + PREKIDAC_SIZE
        g.fillRect(
            prekidacMiddleX - LINE_WIDTH / 2,
            prekidacBottom,
            LINE_WIDTH,
            polje.izlazniRastavljac.coordinate.y - prekidacBottom
        )

        // Rastavljac uzemljenja
        g.color = getColor(polje.rastavljacUzemljenja.stanje)
        g.fillOval(
            polje.rastavljacUzemljenja.coordinate.x,
            polje.rastavljacUzemljenja.coordinate.y,
            RASTAVLJAC_SIZE,
            RASTAVLJAC_SIZE
        )
        g.color = defaultColor

        val rastavljacUzemljenjaMidY = polje.rastavljacUzemljenja.coordinate.y + RASTAVLJAC_SIZE / 2
        g.fillRect(
            prekidacMiddleX,
            rastavljacUzemljenjaMidY - LINE_WIDTH / 2,
            polje.rastavljacUzemljenja.coordinate.x - prekidacMiddleX,
            LINE_WIDTH
        )

        // Oznaka uzemljenja
        drawGroundSymbol(
            g, polje.rastavljacUzemljenja.coordinate.x + RASTAVLJAC_SIZE,
            polje.rastavljacUzemljenja.coordinate.y + RASTAVLJAC_SIZE / 2 - LINE_WIDTH / 2
        )

        // Dalekovod strelica
        val arrowX = prekidacMiddleX.toDouble()
        val arrowY = polje.izlazniRastavljac.coordinate.y.toDouble() + RASTAVLJAC_SIZE
        val dalekovodArrowLine = Line2D.Double(arrowX, arrowY - 1, arrowX, arrowY + 100.0)
        val dalekovodArrowLine2 = Line2D.Double(arrowX, arrowY, arrowX, arrowY + 100.0 + LINE_WIDTH)
        g.stroke = BasicStroke(LINE_WIDTH.toFloat(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL)
        g.draw(dalekovodArrowLine)
        drawArrowHead(g, dalekovodArrowLine2, LINE_WIDTH)
    }

    private fun drawMjernoPolje(g: Graphics2D, polje: MjernoPolje) {
        val defaultColor = Color.black

        // Sabirnica i rastavljac
        g.color = defaultColor

        val s = polje.sabirnicaIRastavljac.sabirnica
        g.fillRect(s.x, s.y, s.width, s.height)

        val r = polje.sabirnicaIRastavljac.rastavljac
        g.color = getColor(r.stanje)
        g.fillOval(r.coordinate.x, r.coordinate.y, RASTAVLJAC_SIZE, RASTAVLJAC_SIZE)
        g.color = defaultColor

        // connection
        val upY = min(s.y, r.coordinate.y)
        val downY = max(s.y, r.coordinate.y)
        val rastavljacMiddle = r.coordinate.x + RASTAVLJAC_SIZE / 2
        g.fillRect(rastavljacMiddle - LINE_WIDTH / 2, upY, LINE_WIDTH, downY - upY)
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

    private fun drawGroundSymbol(g: Graphics2D, x: Int, y: Int) {
        val lw = LINE_WIDTH
        g.fillRect(x, y, lw, lw)
        g.fillRect(x + lw, y - lw, lw / 2, 3 * lw)
        g.fillRect(x + 2 * lw, y - lw + 4, lw / 2, lw + 2 * (lw - 4))
        g.fillRect(x + 3 * lw, y - lw + 8, lw / 2, lw + 2 * (lw - 8))
    }

    override fun repaint() {
        super.repaint()
        onRepaint?.invoke()
    }
}