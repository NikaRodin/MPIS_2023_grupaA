import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
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

        dalPolje1 = DalekovodnoPolje(0, 0, listOf(
            SabirnicaIRastavljac(
                Sabirnica(0, 0, 250, 10),
                Rastavljac(Coordinate(50, 75))
            ),
            SabirnicaIRastavljac(
                Sabirnica(0, 20, 250, 10),
                Rastavljac(Coordinate(150, 75))
            ),
        ))
    }

    override fun paint(g: Graphics) {
        val g2D = g as Graphics2D

//        g2D.drawImage(image, 0, 0, null);
//        g2D.paint = Color.blue
//        g2D.stroke = BasicStroke(5f)
//        g2D.drawLine(0, 0, 500, 500)

//        g2D.setPaint(Color.pink);
//        g2D.drawRect(0, 0, 100, 200);
//        g2D.fillRect(0, 0, 100, 200);

//        g2D.setPaint(Color.orange);
//        g2D.drawOval(0, 0, 100, 100);
//        g2D.fillOval(0, 0, 100, 100);

//        g2D.setPaint(Color.red);
//        g2D.drawArc(0, 0, 100, 100, 0, 180);
//        g2D.fillArc(0, 0, 100, 100, 0, 180);
//        g2D.setPaint(Color.white);
//        g2D.fillArc(0, 0, 100, 100, 180, 180);

//        int[] xPoints = {150,250,350};
//        int[] yPoints = {300,150,300};
//        g2D.setPaint(Color.yellow);
//        g2D.drawPolygon(xPoints, yPoints, 3);
//        g2D.fillPolygon(xPoints, yPoints, 3);

//        g2D.setPaint(Color.magenta);
//        g2D.setFont(Font("Ink Free",Font.BOLD,50));
//        g2D.drawString("U R A WINNER! :D", 50, 50);


        drawDalekovodnoPolje(g2D, dalPolje1)
    }

    private fun drawDalekovodnoPolje(g: Graphics2D, polje: DalekovodnoPolje) {
        g.paint = Color.blue
        for (sabirnicaIRastavljac in polje.sabirniceIRastavljaci) {
            val s = sabirnicaIRastavljac.sabirnica
            g.fillRect(s.x, s.y, s.width, s.height)

            val r = sabirnicaIRastavljac.rastavljac
            val rastavljacSize = 50
            g.fillRect(r.coordinate.x, r.coordinate.y, rastavljacSize, rastavljacSize)

            // connection
            val upY = min(s.y, r.coordinate.y)
            val downY = max(s.y, r.coordinate.y)
            val lineWidth = 20
            val rastavljacMiddle = r.coordinate.x + rastavljacSize/2
            g.fillRect(rastavljacMiddle - lineWidth/2, upY, lineWidth, downY - upY)
        }
    }
}