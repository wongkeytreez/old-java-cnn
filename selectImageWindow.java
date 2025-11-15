import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class selectImageWindow extends JPanel {


     Point firstClickPoint = null;
     Point secondClickPoint = null;
     Boolean firstClickBool= false;
     public selectImageWindow(BufferedImage image,Consumer<Object[]> callback,String[]options){
     
        JButton confirmButton = new JButton("confirm");
     this.setLayout(null);
     ImageCLass ImagePanel = new ImageCLass(image);
     ImagePanel.setBounds(0,0,image.getWidth(),image.getHeight());
     confirmButton.setBounds(image.getWidth()-100,image.getHeight(),100,30);
     this.add(confirmButton);
     JComboBox<String> comboBox = new JComboBox<>(options);
     comboBox.setBounds(20,image.getHeight(),100,30);
     this.add(comboBox);

    this.add(ImagePanel);

     confirmButton.addActionListener(new ActionListener() {
         @Override
            public void actionPerformed(ActionEvent e) {
                if(firstClickBool==false&&firstClickPoint!=null)
                callback.accept(new Object[]{
                    firstClickPoint.getX(),
                firstClickPoint.getY(),
            secondClickPoint.getX(),
                secondClickPoint.getY(),
            comboBox.getSelectedItem()});
           // Call the callback function
            }});

     }
     JButton confirmButton = new JButton("confirm");
    class ImageCLass extends JPanel{
             BufferedImage image;
    public ImageCLass(BufferedImage image) {
        
        this.image = image;

        // Add mouse listener to track clicks
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getPoint().getX()<image.getWidth()&&e.getPoint().getY()<image.getHeight())
                // If it's the first click, save the position
                if (!firstClickBool ) {
                    firstClickPoint = e.getPoint();
                    firstClickBool=true;
                }
                else  {
                    secondClickPoint = e.getPoint();
                    firstClickBool=false;
                      // Trigger repaint after second click
                }
                repaint();
            }
        });
       
        
this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // If the first click is set, draw a square from the first click to the current mouse position
                if (firstClickPoint != null ) {
                    repaint(); // Trigger repaint while moving the mouse
                }
            }
        });
        // Add mouse motion listener to track the mouse between clicks
       
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the image on the panel
        g.drawImage(image, 0, 0, this);

        // If the first click exists and the second doesn't, draw a square from the first click to the mouse position
        if (firstClickPoint != null && firstClickBool == true) {
            Point mousePos = getMousePosition();  // Get current mouse position
            if (mousePos != null) {
                //int size = 20; // Set size of the square
                int x = Math.min(firstClickPoint.x, mousePos.x);
                int y = Math.min(firstClickPoint.y, mousePos.y);
                int width = Math.abs(firstClickPoint.x - mousePos.x);
                int height = Math.abs(firstClickPoint.y - mousePos.y);
                g.setColor(Color.RED);
                g.drawRect(x, y, width, height);
            }
        }

        // If the second click exists, draw a square between first and second clicks
        if (firstClickPoint != null && firstClickBool == false) {
            g.setColor(Color.GREEN);
            int x = Math.min(firstClickPoint.x, secondClickPoint.x);
            int y = Math.min(firstClickPoint.y, secondClickPoint.y);
            int width = Math.abs(firstClickPoint.x - secondClickPoint.x);
            int height = Math.abs(firstClickPoint.y - secondClickPoint.y);
            g.drawRect(x, y, width, height);
        }
    }}

   
}
