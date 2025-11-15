    import javax.imageio.ImageIO;
    import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class UI {

static JFrame frame = new JFrame("Image Losses");
    public static JScrollPane createScrollablePanel(YOLO yolo) {
    frame.setLayout(null);
   ArrayList<double[][]> outputs= new ArrayList<>();//=yolo.forwardPass(yolo.trainingImages.get(0).get(1));
   ArrayList<BufferedImage> images = new ArrayList<>();
   ArrayList<Double> lossesList = new ArrayList<>();
   int highestLossOutput=-1;
   for(int objectName =0;objectName<yolo.trainingImages.size();objectName++)
   for(int i=0;i<yolo.trainingImages.get(objectName).size();i++){
      double[][]  forwardpass = yolo.forwardPass(yolo.trainingImages.get(objectName).get(i));
   
    {outputs.add(yolo.trueBoundingBoxes.get(objectName).get(i));
      images.add(file.createImageFromDoubleArray(yolo.trainingImages.get(objectName).get(i)));
    double[][] largeLoss= new double[forwardpass.length][yolo.trueBoundingBoxes.get(objectName).get(i).length];
      for(int j=0;j<largeLoss.length;j++)
      largeLoss[j] = YOLO.getLoss(yolo.trueBoundingBoxes.get(objectName).get(i)[j], forwardpass[j]);
      double loss=calculate.average2DTo1D(largeLoss)[0]
      +calculate.average2DTo1D(largeLoss)[1]+calculate.average2DTo1D(largeLoss)[2]
      +calculate.average2DTo1D(largeLoss)[3]+calculate.average2DTo1D(largeLoss)[4];
      lossesList.add(loss);
    if(highestLossOutput==-1||lossesList.get(highestLossOutput)<loss)
     highestLossOutput=i;
    }
    }
BufferedImage[] bufferedImages=images.toArray(new BufferedImage[0]); 
double[][][] boundingBoxes=outputs.toArray(new double[0][][]);
double[] losses = lossesList.stream().mapToDouble(Double::doubleValue).toArray();
    
       
    // Create a panel to hold everything
JPanel mainPanel = new JPanel();
JPanel sidePanel = new JPanel();
if(highestLossOutput!=-1){
System.out.println(highestLossOutput);
     JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BorderLayout());
    
    // Create a label for the image
    JLabel imageLabel = new JLabel(new ImageIcon(file.drawBoundingBoxes(bufferedImages[highestLossOutput], boundingBoxes[highestLossOutput])));
    imagePanel.add(imageLabel, BorderLayout.CENTER);

    // Set the preferred and maximum size for the image panel
    imagePanel.setPreferredSize(new Dimension(200, 400));  // Use correct width/height
    imagePanel.setMaximumSize(new Dimension(200, 400));
    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setValue((int) (losses[highestLossOutput] * 100));  // Assuming loss is between 0 and 1
    progressBar.setStringPainted(true);
    progressBar.setString(String.format("%.2f%%", losses[highestLossOutput] * 100));  // Display percentage

    // Add the progress bar below the image
    imagePanel.add(progressBar, BorderLayout.SOUTH);
    sidePanel.add(imagePanel);
}
mainPanel.setLayout(new GridLayout(0, 3, 10, 10));  // Stack panels vertically

// Create a panel for each image and progress bar
for (int i = 0; i < bufferedImages.length; i++) {
    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BorderLayout());
    
    // Create a label for the image
    JLabel imageLabel = new JLabel(new ImageIcon(file.drawBoundingBoxes(bufferedImages[i], boundingBoxes[i])));
    imagePanel.add(imageLabel, BorderLayout.CENTER);

    // Set the preferred and maximum size for the image panel
    imagePanel.setPreferredSize(new Dimension(200, 300));  // Use correct width/height


    // Create a progress bar
    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setValue((int) (losses[i] * 100));  // Assuming loss is between 0 and 1
    progressBar.setStringPainted(true);
    progressBar.setString(String.format("%.2f%%", losses[i] * 100));  // Display percentage

    // Add the progress bar below the image
    imagePanel.add(progressBar, BorderLayout.SOUTH);

    // Add the image panel to the main panel
    mainPanel.add(imagePanel);
}

// Make the main panel scrollable
JScrollPane scrollPane = new JScrollPane(mainPanel);

scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollPane.setBounds(0,0,800,800);
sidePanel.setBounds(800,0,400,800);
// Create and display the frame

frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.add(scrollPane);
frame.add(sidePanel);
frame.setSize(1200, 800);
frame.setVisible(true);

        return scrollPane;
    }
 

   
}


