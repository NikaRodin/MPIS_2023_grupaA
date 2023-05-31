import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JPanel

class MyPanel internal constructor() : JPanel() {

    //Image image;

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
    }

    override fun paint(g: Graphics) {
        val g2D = g as Graphics2D

//        g2D.drawImage(image, 0, 0, null);
        g2D.paint = Color.blue
        g2D.stroke = BasicStroke(5f)
        g2D.drawLine(0, 0, 500, 500)

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
    }



}