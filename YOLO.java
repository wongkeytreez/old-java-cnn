
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

public class YOLO {
  public ArrayList<ArrayList<double[][]>> trainingImages = new ArrayList<>();
  public ArrayList<ArrayList<double[][]>> trueBoundingBoxes = new ArrayList<>();
  public ArrayList<String> outputNames = new ArrayList<>();
  CNN CNNTemplate;
  NeuralNetwork NNTemplate;
  int amountOfGridsInRow;
  String CNNSaveFile;
  String NNSaveFile;
  String trainingfolder;
  public YOLO(int[]CNNLayerSizes,int[]CNNLayerChannels,int hiddenNNLayers,String NNWeightsFile,String CNNWeightsFile,String trainingfolder,int inputAmountOfGridsInRow){
  try {
    trainingImages =file.readArrayListFromBinaryFile(trainingfolder+"/images.dat");
  trueBoundingBoxes =file.readArrayListFromBinaryFile(trainingfolder+"/boundingboxes.dat");
  outputNames = file.readArrayListFromBinaryFile2d(trainingfolder+"/names.dat");

} catch (Exception e) {
trainingImages = new ArrayList<>();
trueBoundingBoxes = new ArrayList<>();
  outputNames = new ArrayList<>();
  file.writeArrayListToBinaryFile(trainingfolder+"/images.dat", trainingImages);
   file.writeArrayListToBinaryFile(trainingfolder+"/boundingboxes.dat", trueBoundingBoxes);
   file.writeArrayListToBinaryFile2d(trainingfolder+"/names.dat", outputNames);
   
  System.out.println(e);
  }
    CNNTemplate= new CNN(CNNLayerSizes,CNNLayerChannels);
  int[] NNArcitecture = new int[2+hiddenNNLayers];
  NNArcitecture[0] = CNNLayerChannels[CNNLayerChannels.length-1];
  for(int i=0;i<hiddenNNLayers;i++) NNArcitecture[i+1] = 100;
  NNArcitecture[NNArcitecture.length-1] =4+outputNames.size();

  NNTemplate = new NeuralNetwork(NNArcitecture);
  

  amountOfGridsInRow=inputAmountOfGridsInRow;
  CNNSaveFile=CNNWeightsFile;
  NNSaveFile=NNWeightsFile;
    
    File cnnweightsFile=new File(CNNWeightsFile);
    File nnweightsFile=new File(NNWeightsFile);
    
   if(cnnweightsFile.exists())
   CNNTemplate.SetWeights(file.readArrayFromFile4d(CNNWeightsFile));

  else
  CNNTemplate.SetWeights(null);

  if(nnweightsFile.exists())
 NNTemplate.SetWeights(file.readArrayFromFile3d(NNWeightsFile));   
 else{
  NNTemplate.SetWeights(null);
}

  this.trainingfolder=trainingfolder;
  
  
  }
  public void addObjectName(String ObjectName){
  int objectInt =-1;
    for (int i=0;i<outputNames.size();i++) {
          if(outputNames.get(i).equals(ObjectName)) objectInt = i;

      }
      if(objectInt==-1){
        Random random = new Random();
    outputNames.add(ObjectName);
    trainingImages.add(new ArrayList<>());
    trueBoundingBoxes.add(new ArrayList<>());
    for(int i=0;i<trueBoundingBoxes.size();i++)
    for(int j=0;j<trueBoundingBoxes.get(i).size();j++)
    for(int k=0;j<trueBoundingBoxes.get(i).get(0).length;k++)
    {
      double[] newBB = new double[trueBoundingBoxes.get(i).get(0)[k].length+1];
      for(int l=0;l<trueBoundingBoxes.get(i).get(0)[k].length;l++)
      newBB[l] = trueBoundingBoxes.get(i).get(0)[k][l];

    }
    
    
  
      file.writeArrayListToBinaryFile(trainingfolder+"/images.dat",trainingImages);
  file.writeArrayListToBinaryFile(trainingfolder+"/boundingboxes.dat",trueBoundingBoxes);
  file.writeArrayListToBinaryFile2d(trainingfolder+"/names.dat", outputNames);
  double[][] newLastWeights;

  newLastWeights= new double[NNTemplate.weights[NNTemplate.weights.length-1].length][NNTemplate.weights[NNTemplate.weights.length-1][0].length+1];
  for(int weight=0;weight<NNTemplate.weights[NNTemplate.weights.length-1].length;weight++){
   for(int nextnode =0;nextnode<NNTemplate.weights[NNTemplate.weights.length-1][weight].length;nextnode++)
   newLastWeights[weight][nextnode] = NNTemplate.weights[NNTemplate.weights.length-1][weight][nextnode];
   newLastWeights[weight][NNTemplate.weights[NNTemplate.weights.length-1][weight].length]=
   Math.sqrt(2.0/newLastWeights.length)*random.nextGaussian();
  
  }
  double[] newLastNetwork = new double[NNTemplate.network[NNTemplate.network.length-1].length+1];
  NNTemplate.weights[NNTemplate.weights.length-1]= newLastWeights;
  NNTemplate.network[NNTemplate.network.length-1]=newLastNetwork;
  
}
    else System.out.println("object alreay added! : "+ objectInt);
  }
  public void addTrainingData(String image,double[]RawBoundingBoxes,String objectClass){
int objectInt =-1;
    for (int i=0;i<outputNames.size();i++) {
          if(outputNames.get(i).equals(objectClass)) objectInt = i;

      }
     
      if(objectInt!=-1)
    try{
    

  double[][] Image2d=file.poolImage(file.readImage(file.makeSquare(ImageIO.read(new File(image)))),CNNTemplate.network[0][0].length);
  trainingImages.get(objectInt).add(Image2d);

  int placeInGridX = (int)((RawBoundingBoxes[0]/100)*amountOfGridsInRow);
  int placeInGridY = (int)((RawBoundingBoxes[1]/100)*amountOfGridsInRow);
//add the real bounding boxes, not fixed yet
  

  double[][] boundingBoxes = new double[amountOfGridsInRow*amountOfGridsInRow][NNTemplate.network[NNTemplate.network.length-1].length];


  boundingBoxes[placeInGridX*amountOfGridsInRow+placeInGridY][0]= ((RawBoundingBoxes[0]/100)*amountOfGridsInRow)-(double)placeInGridX;
  boundingBoxes[placeInGridX*amountOfGridsInRow+placeInGridY][1]= ((RawBoundingBoxes[1]/100)*amountOfGridsInRow)-(double)placeInGridY;
  boundingBoxes[placeInGridX*amountOfGridsInRow+placeInGridY][2]= (RawBoundingBoxes[2]/100);
  boundingBoxes[placeInGridX*amountOfGridsInRow+placeInGridY][3]= (RawBoundingBoxes[3]/100);
  boundingBoxes[placeInGridX*amountOfGridsInRow+placeInGridY][3+outputNames.size()]= 1;

  trueBoundingBoxes.get(objectInt).add(boundingBoxes);
  
  }catch(Exception e) {System.out.println(e+" e");}
  else{
    System.out.print("object not found!");
    return;
  }
  file.writeArrayListToBinaryFile(trainingfolder+"/images.dat",trainingImages);
  file.writeArrayListToBinaryFile(trainingfolder+"/boundingboxes.dat",trueBoundingBoxes);

}
class FindGradients implements Callable<Object[]> {

    private double[][]CNNInput;

    private double[][] TrueboundingBoxes;
    // Constructor to accept parameters from outside the task
    public FindGradients(double[][]CNNInput,double[][] TrueboundingBoxes) {
  this.CNNInput=CNNInput;this.TrueboundingBoxes= TrueboundingBoxes;
    }

    @Override
    public Object[] call() throws Exception {
      
       CNN cnn = CNNTemplate.clone();
      
 
    cnn.forwardTrainingPass(CNNInput);
    int gridsize = cnn.network[cnn.network.length-1].length;
    int amountOfGrids = cnn.network[cnn.network.length-1][0].length*cnn.network[cnn.network.length-1][0].length;
    double[][] gridInputs = new double[amountOfGrids][gridsize]; 

    for(int gridindex=0;gridindex<amountOfGrids;gridindex++)
    for(int inputindex=0;inputindex<gridsize;inputindex++){
    gridInputs[gridindex][inputindex] = cnn.network[cnn.network.length-1][inputindex][(int)(gridindex/Math.sqrt(amountOfGrids))][(int)(gridindex%Math.sqrt(amountOfGrids))];

    }      
    
    NeuralNetwork[] nn= new NeuralNetwork[amountOfGrids];
    for(int i =0;i<nn.length;i++){
     nn[i]=NNTemplate.clone();
    }
    double[][]predictedBoundingBoxes = new double[gridInputs.length][];
    

    for (int i = 0; i < nn.length; i++){ 
    nn[i].TrainingRun(gridInputs[i]);
    predictedBoundingBoxes[i]=nn[i].network[nn[0].network.length-1];
    }
// System.out.println(NNTemplate.network[NNTemplate.network.length-1].length);

   List<Map<String, double[][][]>> backPropagatedNNs =new ArrayList<Map<String, double[][][]>>();
   for(int i=0;i<nn.length;i++) {backPropagatedNNs.add(nn[i].calculateGradients(YOLO.getTrainingLoss(TrueboundingBoxes[i], predictedBoundingBoxes[i])));
  }

   double[][][] CNNCalculateGradientsInputs = new double[cnn.network[cnn.network.length-1].length][cnn.network[cnn.network.length-1][0].length][cnn.network[cnn.network.length-1][0].length];
   for(int channel=0;channel<CNNCalculateGradientsInputs.length;channel++)
   for(int x=0;x<CNNCalculateGradientsInputs[0].length;x++)
   for(int y=0;y<CNNCalculateGradientsInputs[0].length;y++){

   CNNCalculateGradientsInputs[channel][x][y]= backPropagatedNNs.get((int)(y+x*Math.sqrt(nn.length))).get("deltas")[0][channel][0];
   }
  //System.out.println(Arrays.toString( cnn.calculateGradients( CNNCalculateGradientsInputs)[0][0][0]));
   
   double[][][][] CNNGradients= cnn.calculateGradients( CNNCalculateGradientsInputs);
 // System.out.println(Arrays.toString(CNNCalculateGradientsInputs[0][0]));
   
   double[][][][]NNGradients = new double[backPropagatedNNs.size()][][][];
   for(int i=0;i<NNGradients.length;i++) NNGradients[i]= backPropagatedNNs.get(i).get("gradients");
   double[][][] AvragedNNGradients= calculate.average4DTo3D(NNGradients);
   

      return new Object[]{CNNGradients,AvragedNNGradients,predictedBoundingBoxes};

    }}
 public double[][] forwardPass(double[][]input){

  CNN cnn = CNNTemplate.clone();
      
 
    cnn.forwardPass(input);
    int gridsize = cnn.network[cnn.network.length-1].length;
    int amountOfGrids = cnn.network[cnn.network.length-1][0].length*cnn.network[cnn.network.length-1][0].length;
    double[][] gridInputs = new double[amountOfGrids][gridsize]; 

    for(int gridindex=0;gridindex<amountOfGrids;gridindex++)
    for(int inputindex=0;inputindex<gridsize;inputindex++){
    gridInputs[gridindex][inputindex] = cnn.network[cnn.network.length-1][inputindex][(int)(gridindex/Math.sqrt(amountOfGrids))][(int)(gridindex%Math.sqrt(amountOfGrids))];

    }      
    
    NeuralNetwork[] nn= new NeuralNetwork[amountOfGrids];
    for(int i =0;i<nn.length;i++){
     nn[i]=NNTemplate.clone();
    }
    double[][]predictedBoundingBoxes = new double[gridInputs.length][];
    

    for (int i = 0; i < nn.length; i++){ 
    nn[i].run(gridInputs[i]);
    predictedBoundingBoxes[i]=nn[i].network[nn[0].network.length-1];
    }
return predictedBoundingBoxes;
  
 }
 public void train(int Epochs){
  

   double[][][]NNmomentum = new double[NNTemplate.weights.length][][];
   double[][][]NNvariance = new double[NNmomentum.length][][];
   for(int layer =0;layer<NNTemplate.weights.length;layer++)
   { NNmomentum[layer]=new double[NNTemplate.weights[layer].length][NNTemplate.weights[layer][0].length];
    NNvariance[layer]=new double[NNTemplate.weights[layer].length][NNTemplate.weights[layer][0].length];
   }
   double[][][][]CNNmomentum = new double[CNNTemplate.weights.length][][][];
   double[][][][]CNNvariance = new double[CNNmomentum.length][][][];
   for(int layer =0;layer<CNNTemplate.weights.length;layer++)
   { CNNmomentum[layer]=new double[CNNTemplate.weights[layer].length][CNNTemplate.weights[layer][0].length][CNNTemplate.weights[layer][0].length];
    CNNvariance[layer]=new double[CNNTemplate.weights[layer].length][CNNTemplate.weights[layer][0].length][CNNTemplate.weights[layer][0].length];
   }

  for(int epoch=0;epoch<Epochs;epoch++){
    ExecutorService executorService = Executors.newFixedThreadPool(8);
    List<Callable<Object[]>> tasks = new ArrayList<>();
    for(int objectClass=0;objectClass<trainingImages.size();objectClass++)
    for(int image=0;image<trainingImages.get(objectClass).size();image++){
    //  System.out.println(Arrays.toString(trueBoundingBoxes.get(objectClass).get(image)[0]));
    tasks.add(new FindGradients(
      trainingImages.get(objectClass).get(image),
    trueBoundingBoxes.get(objectClass).get(image)));
    }
    if(tasks.size()==0){System.out.println("no training data");
      break;}
    List<Future<Object[]>> results;
    try{
     
     results= executorService.invokeAll(tasks);


    }catch(Exception e) {System.out.println(e); results=null;}

    double[][][][][] listofcnngradients = new double[results.size()][][][][];
    double[][][][] listofnngradients = new double[results.size()][][][];
    try{
    for(int i=0;i<results.size();i++){
      Object[] result =results.get(i).get();
     listofcnngradients[i] =(double[][][][]) result[0];
     listofnngradients[i] =(double[][][]) result[1];

    }
  
  }catch(Exception e){System.out.println(e);}

   double[][][][] FinalAvgCNNGradients = calculate.average5DTo4D(listofcnngradients);
    double[][][] FinalAvgNNGradients =calculate.average4DTo3D(listofnngradients);
        // Shutdown the executor service
    executorService.shutdown();
      if(epoch%(Epochs/10)==0)
  {UI.createScrollablePanel(this);
     file.writeArrayToFile(CNNSaveFile,CNNTemplate.weights);
    file.writeArrayToFile(NNSaveFile,NNTemplate.weights);
  }
        Map<String, double[][][][]>CNNoptimizer = optimize.CNNadam(FinalAvgCNNGradients,CNNTemplate.weights, CNNmomentum, CNNvariance, 0.001,epoch+1);
    Map<String, double[][][]>NNoptimizer = optimize.NNadam(FinalAvgNNGradients,NNTemplate.weights,NNmomentum,NNvariance,0.001,epoch+1);
   
    NNmomentum=NNoptimizer.get("momentum");
    NNvariance=NNoptimizer.get("variance");
   CNNmomentum=CNNoptimizer.get("momentum");
  CNNvariance=CNNoptimizer.get("variance");

  CNNTemplate.SetWeights(CNNoptimizer.get("newWeights"));
  NNTemplate.SetWeights (NNoptimizer.get("newWeights"));

 }}

  public static double[] getTrainingLoss(double[]trueboundingboxes,double[] predictedBoundingBoxes){
    //unusable
     double[]returnData = new double[trueboundingboxes.length];
    int object=-1;

    for(int i=4;i<trueboundingboxes.length&&object==-1;i++)
      if(trueboundingboxes[i]==1) {
      object =i;

      }
    for(int i=4;i<trueboundingboxes.length;i++){
    returnData[i] =-(trueboundingboxes[i]-predictedBoundingBoxes[i])+((1-trueboundingboxes[i])/(1-predictedBoundingBoxes[i]));
    if(trueboundingboxes[i]==0)returnData[i]*=0.5;
  }
    if(object!=-1)
      for(int i=0;i<4;i++)
      returnData[i] =-2*( trueboundingboxes[i] - predictedBoundingBoxes[i]);
return returnData;
  }
  public static double[] getLoss(double[]trueboundingboxes,double[] predictedBoundingBoxes){
    //unusable
     double[]returnData = new double[trueboundingboxes.length];
    int object=-1;

    for(int i=4;i<trueboundingboxes.length&&object==-1;i++)
      if(trueboundingboxes[i]==1) {
      object =i;

      }
    for(int i=4;i<trueboundingboxes.length;i++){
    returnData[i] =calculate.BCE(predictedBoundingBoxes[i],trueboundingboxes[i]==1?true:false);

  }
    if(object!=-1)
      for(int i=0;i<4;i++)
      returnData[i] =Math.pow( trueboundingboxes[i] - predictedBoundingBoxes[i],2);
return returnData;
  }

}
