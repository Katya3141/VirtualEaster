package eggbonk.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

class PadDraw extends JComponent{
    Image image;
    //this is gonna be your image that you draw on
    Graphics2D graphics2D;
    //this is what we'll be using to draw on
    int currentX, currentY, oldX, oldY;
    //these are gonna hold our mouse coordinates

    //Now for the constructors
    public PadDraw(){
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                oldX = e.getX();
                oldY = e.getY() - 50;
            }
        });
        //if the mouse is pressed it sets the oldX & oldY
        //coordinates as the mouses x & y coordinates
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                currentX = e.getX();
                currentY = e.getY() - 50;
                if(graphics2D != null)
                    graphics2D.drawLine(oldX, oldY, currentX, currentY);
                repaint();
                oldX = currentX;
                oldY = currentY;
            }

        });
        //while the mouse is dragged it sets currentX & currentY as the mouses x and y
        //then it draws a line at the coordinates
        //it repaints it and sets oldX and oldY as currentX and currentY
    }

    @Override
    public void paintComponent(Graphics g){
        if(image == null){
            try {
                image = ImageIO.read(new File("default-egg.png"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setStroke(new BasicStroke(10));
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //clear();

        }
        g.drawImage(image, 0, 50, null);
    }
    //this is the painting bit
    //if it has nothing on it then
    //it creates an image the size of the window
    //sets the value of Graphics as the image
    //sets the rendering
    //runs the clear() method
    //then it draws the image

    //public void clear(){
    //    graphics2D.setPaint(Color.white);
    //    graphics2D.fillRect(0, 0, getSize().width, getSize().height);
    //    graphics2D.setPaint(Color.black);
    //    repaint();
    //}
    //this is the clear
    //it sets the colors as white
    //then it fills the window with white
    //thin it sets the color back to black
    public void red(){
        graphics2D.setPaint(Color.red);
        repaint();
    }
    //this is the red paint
    public void black(){
        graphics2D.setPaint(Color.black);
        repaint();
    }
    //black paint
    public void magenta(){
        graphics2D.setPaint(Color.magenta);
        repaint();
    }
    //magenta paint
    public void lightBlue(){
        graphics2D.setPaint(new Color(138, 239, 255));
        repaint();
    }
    //blue paint
    public void lightGreen(){
        graphics2D.setPaint(new Color(202, 255, 138));
        repaint();
    }
    //green paint 
    
    public void paleYellow(){
        graphics2D.setPaint(new Color(255, 240, 122));
        repaint();
    }
    //yellow paint
    
    public void pink(){
        graphics2D.setPaint(new Color(255, 171, 255));
        repaint();
    }
    //pink paint
    
    public void blue(){
        graphics2D.setPaint(Color.blue);
        repaint();
    }
    //blue paint
    
    public void green(){
        graphics2D.setPaint(Color.green);
        repaint();
    }
    //green paint
    
    public Image getImage() {
        return image;
    }
}