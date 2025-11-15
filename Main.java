import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

public class Main {
    
   public static void main(String[] args) {

      YOLO yolo = new YOLO(new int[]{100,80,80,80,80,40,40,40,20,20,20,10,10,10}, new int[]{1,2,4,8,16,32,64,128,256,512,1024,1024,1024,1024}, 2, "D:/Documents/ai/cnn/nnweights.dat","D:/Documents/ai/cnn/cnnweights.dat","D:/Documents/ai/cnn/training",10);
   //yolo.addTrainingData("D:/Documents/ai/cnn/training/human/1000066986.jpg", new double[]{38,16,30,40}, 0);
  // yolo.addTrainingData("D:/Documents/ai/cnn/training/human/7290070.jpg", new double[]{28,5,44,60}, 0);
   //yolo.addTrainingData("D:/Documents/ai/cnn/training/human/1000067024.jpg", new double[]{32,15,30,44}, 0);

  yolo.addObjectName("bus");
  yolo.addObjectName("car");
//yolo.addTrainingData("./training/bus/1.webp",new double[]{28,30,50,19},"bus");
 yolo.train(10);
//    try {
//        String ImagePath ="C:/Users/user/Downloads/1000067021.jpg";
//        JFrame frame = new JFrame("Image Click Tracker");
//         selectImageWindow panel =new selectImageWindow(
//       file.createImageFromDoubleArray(file.poolImage(file.readImage(file.makeSquare(ImageIO.read(new File(ImagePath)))),500))
//       ,(e->{
//        yolo.addTrainingData(ImagePath, new double[]{
//          Math.min((double)e[0],(double)e[2])/5,
//          Math.min((double)e[1],(double)e[3])/5,
//          Math.abs((double)e[0]-(double)e[2])/5,
//          Math.abs((double)e[1]-(double)e[3])/5
//       }, (String)e[4]);
//       frame.dispose();

//       }),yolo.outputNames.toArray(new String[0]));
//         frame.add(panel);
//       // panel.confirmButton.addActionListener(new ActionListener() {
//       //    @Override
//       //    public void actionPerformed(ActionEvent e){
//       //       if(!panel.firstClickBool)System.out.print("panel");
//       //    }
//       // });
//         // Set frame properties
//         frame.setSize(800, 600);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setVisible(true);
//    } catch (Exception e) {
//     System.out.println(e);
//       // TODO: handle exception
//    }
      

//       ArrayList<cnnArchitecture> architecture= new ArrayList<cnnArchitecture>();
//    architecture.add(new cnnArchitecture(1,208));
//    architecture.add(new cnnArchitecture(2,104));
//    architecture.add(new cnnArchitecture(4,52));
//    architecture.add(new cnnArchitecture(8,52));
//    architecture.add(new cnnArchitecture(16,26));
//    architecture.add(new cnnArchitecture(32,26));
//    architecture.add(new cnnArchitecture(64,26));
//    architecture.add(new cnnArchitecture(128,13));
//    architecture.add(new cnnArchitecture(256,13));
//    architecture.add(new cnnArchitecture(512,13));
//   // architecture.add(new cnnArchitecture(1024,13));
//   // architecture.add(new cnnArchitecture(2048,13));
//    //architecture.add(new cnnArchitecture(2048,13));

//    String[]objects =file.readFolderFolders("D:/Documents/ai/newcnn/training");
//    NeuralNetwork[] nn= new NeuralNetwork[architecture.get(architecture.size()-1).layerSize*architecture.get(architecture.size()-1).layerSize];
//    double[][][] cnninputs = new double[][][]{file.readImage(file.makeSquare(ImageIO.read(new File("training/human/1000066986.jpg"))))};
//    double[][] trueboundingboxes=YOLO.createTrainingData(new double[][]{{37,20,30,38,0}},13,objects.length);
//    //file.read2DArrayFromFile("D:/Documents/ai/newcnn/boundingboxes.txt");
  
//    CNN cnn = new CNN();
//    for(int i=0;i<nn.length;i++)nn[i]=new NeuralNetwork(new int[]{architecture.get(architecture.size()-1).layerChannels,100,50,4+objects.length});
    
//    File cnnweightsFile=new File("D:/Documents/ai/newcnn/cnnweights.dat");
//    File nnweightsFile=new File("D:/Documents/ai/newcnn/nnweights.dat");
    
//    if(cnnweightsFile.exists()){
//    cnn.SetWeights(file.readArrayFromFile4d("D:/Documents/ai/newcnn/cnnweights.dat"));
//   //ystem.out.println(file.readArrayFromFile4d("D:/Documents/ai/newcnn/cnnweights.dat")[0][0][0][0]);  
//   } else
//    cnn.SetWeights(null);

//    if(nnweightsFile.exists())
//    for(int i=0;i<nn.length;i++)nn[i].SetWeights(file.readArrayFromFile3d("D:/Documents/ai/newcnn/nnweights.dat"));
//    else
//    for(int i=0;i<nn.length;i++)nn[i].SetWeights(null);



   

//    double[][] predictedBoundingBoxes =new double[trueboundingboxes.length][];
//    double[][][]NNmomentum = new double[nn[0].weights.length][][];
//    double[][][]NNvariance = new double[NNmomentum.length][][];
//    for(int layer =0;layer<nn[0].weights.length;layer++)
//    { NNmomentum[layer]=new double[nn[0].weights[layer].length][nn[0].weights[layer][0].length];
//     NNvariance[layer]=new double[nn[0].weights[layer].length][nn[0].weights[layer][0].length];
//    }
//    double[][][][]CNNmomentum = new double[cnn.weights.length][][][];
//    double[][][][]CNNvariance = new double[CNNmomentum.length][][][];
//    for(int layer =0;layer<cnn.weights.length;layer++)
//    { CNNmomentum[layer]=new double[cnn.weights[layer].length][cnn.weights[layer][0].length][cnn.weights[layer][0].length];
//     CNNvariance[layer]=new double[cnn.weights[layer].length][cnn.weights[layer][0].length][cnn.weights[layer][0].length];
//    }

//    for(int iteration=0;iteration<300;iteration++){
//    cnn.forwardPass(cnninputs[0]);

//      int gridsize = cnn.network[cnn.network.length-1].length;
//     int amountOfGrids = cnn.network[cnn.network.length-1][0].length*cnn.network[cnn.network.length-1][0].length;
//     double[][] gridInputs = new double[amountOfGrids][gridsize]; 

//     for(int gridindex=0;gridindex<amountOfGrids;gridindex++)
//     for(int inputindex=0;inputindex<gridsize;inputindex++){
//     gridInputs[gridindex][inputindex] = cnn.network[cnn.network.length-1][inputindex][(int)(gridindex/Math.sqrt(amountOfGrids))][(int)(gridindex%Math.sqrt(amountOfGrids))];

//     }
//     predictedBoundingBoxes = new double[gridInputs.length][nn[0].network[nn[0].network.length-1].length];
    
 
//     for (int i = 0; i < nn.length; i++){ 
//         nn[i].run(gridInputs[i]);
    

//         // System.out.println(Arrays.toString(gridInputs[gridCol+gridRow*(int)Math.sqrt(gridInputs.length)]));
 
    
//     predictedBoundingBoxes[i]=nn[i].network[nn[0].network.length-1];
//     }

//    List<Map<String, double[][][]>> backPropagatedNNs =new ArrayList<Map<String, double[][][]>>();
//    for(int i=0;i<nn.length;i++) {backPropagatedNNs.add(nn[i].calculateGradients(YOLO.getLoss(trueboundingboxes[i], predictedBoundingBoxes[i])));
//   //  Map<String, double[][][]> thisNNBP = nn[i].calculateGradients(trueboundingboxes[i], predictedBoundingBoxes[i]);
//   //  backPropagatedNNs.get(i).put("gradients", thisNNBP.get("gradients"));
//   //  backPropagatedNNs.get(i).put("deltas", thisNNBP.get("deltas"));
//   //System.out.print(" "+i);
//   }
//   // System.exit(amountOfGrids);
//    double[][][] CNNCalculateGradientsInputs = new double[cnn.network[cnn.network.length-1].length][cnn.network[cnn.network.length-1][0].length][cnn.network[cnn.network.length-1][0].length];
//    for(int channel=0;channel<CNNCalculateGradientsInputs.length;channel++)
//    for(int x=0;x<CNNCalculateGradientsInputs[0].length;x++)
//    for(int y=0;y<CNNCalculateGradientsInputs[0].length;y++){

//    CNNCalculateGradientsInputs[channel][x][y]= backPropagatedNNs.get((int)(y+x*Math.sqrt(nn.length))).get("deltas")[0][channel][0];
//    }
//   //System.out.println(Arrays.toString( cnn.calculateGradients( CNNCalculateGradientsInputs)[0][0][0]));
   
//    double[][][][] CNNGradients= cnn.calculateGradients( CNNCalculateGradientsInputs);
//  // System.out.println(Arrays.toString(CNNCalculateGradientsInputs[0][0]));
   
//    double[][][][]NNGradients = new double[backPropagatedNNs.size()][][][];
//    for(int i=0;i<NNGradients.length;i++) NNGradients[i]= backPropagatedNNs.get(i).get("gradients");
//    double[][][] AvragedNNGradients= calculate.average4DTo3D(NNGradients);
   
//    Map<String, double[][][][]>CNNoptimizer = optimize.CNNadam(CNNGradients,cnn.weights, CNNmomentum, CNNvariance, 0.0001,iteration+1);
//    Map<String, double[][][]>NNoptimizer = optimize.NNadam(AvragedNNGradients,nn[0].weights,NNmomentum,NNvariance,0.0001,iteration+1);
   
//    NNmomentum=NNoptimizer.get("momentum");
//    NNvariance=NNoptimizer.get("variance");
//    CNNmomentum=CNNoptimizer.get("momentum");
//    CNNvariance=CNNoptimizer.get("variance");

//    cnn.SetWeights(CNNoptimizer.get("newWeights"));
//    for(int i=0;i<nn.length;i++) nn[i].SetWeights (NNoptimizer.get("newWeights"));
//    //System.out.println(Arrays.toString(predictedBoundingBoxes[84]));
// }

  
   
//    File outputimage = new File("D:/Documents/ai/newcnn/output.png");
// cnn.forwardPass(file.readImage(file.makeSquare(ImageIO.read(new File("C:/Users/user/Downloads/7290070.jpg")))));
//  int gridsize = cnn.network[cnn.network.length-1].length;
//     int amountOfGrids = cnn.network[cnn.network.length-1][0].length*cnn.network[cnn.network.length-1][0].length;
//     double[][] gridInputs = new double[amountOfGrids][gridsize]; 

//     for(int gridindex=0;gridindex<amountOfGrids;gridindex++)
//     for(int inputindex=0;inputindex<gridsize;inputindex++){
//     gridInputs[gridindex][inputindex] = cnn.network[cnn.network.length-1][inputindex][(int)(gridindex/Math.sqrt(amountOfGrids))][(int)(gridindex%Math.sqrt(amountOfGrids))];

//     }
//     predictedBoundingBoxes = new double[gridInputs.length][nn[0].network[nn[0].network.length-1].length];
    
 
//     for (int i = 0; i < nn.length; i++){ 
//         nn[i].run(gridInputs[i]);
    

//         // System.out.println(Arrays.toString(gridInputs[gridCol+gridRow*(int)Math.sqrt(gridInputs.length)]));
 
    
//     predictedBoundingBoxes[i]=nn[i].network[nn[0].network.length-1];
//     }
//     try {

//        double imagesize=200;
//     int[][] classlessboundingbox = new int[predictedBoundingBoxes.length][4];
//     for(int i =0;i<classlessboundingbox.length;i++){
//       int thisclass =-1;
//       for(int j=4;j<predictedBoundingBoxes[0].length&&thisclass==-1;j++){
//       if(predictedBoundingBoxes[i][j]>0.5) thisclass=j;
//       }
//       if(thisclass!=-1){
//      classlessboundingbox[i] = new int[]{(int)((imagesize/13)*(i/13+predictedBoundingBoxes[i][0])),(int)((imagesize/13)*(i%13+predictedBoundingBoxes[i][1])),(int)(predictedBoundingBoxes[i][2]*imagesize),(int)(predictedBoundingBoxes[i][3]*imagesize)};
//       System.out.println(Arrays.toString(predictedBoundingBoxes[i]));
//    }
//    }
   
//     ImageIO.write(file.drawBoundingBoxes(file.poolImage(file.readImage(file.makeSquare(ImageIO.read(new File("training/human/1000066986.jpg")))),(int)imagesize), classlessboundingbox), "png", outputimage);
//    } catch (Exception e) {

//    }
    
//     file.writeArrayToFile("D:/Documents/ai/newcnn/cnnweights.dat",cnn.weights);
//    file.writeArrayToFile("D:/Documents/ai/newcnn/nnweights.dat",nn[0].weights);
 }

     
}