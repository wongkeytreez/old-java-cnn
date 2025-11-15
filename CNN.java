import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class CNN implements Serializable{
public double [][][][]network;
public double [][][][] weights;
    
    public CNN(int[]CNNLayerSize,int[]CNNLayerChannels){
        network = new double[CNNLayerSize.length][][][];
        for(int layer = 0; layer <CNNLayerSize.length;layer++)
           network[layer] =new double [CNNLayerChannels[layer]][CNNLayerSize[layer]][CNNLayerSize[layer]];
    
    }

    public void SetWeights(double[][][][] inputweights) {
System.out.println("sfd");
    if (inputweights == null) {
        inputweights = new double[network.length - 1][][][];
         // Initialize for each layer
        for (int layer = 0; layer < inputweights.length; layer++) {
            inputweights[layer] = new double[network[layer + 1].length][][]; // For each channel in the next layer
            for (int channel = 0; channel < inputweights[layer].length; channel++) {
                inputweights[layer][channel] = new double[3][3]; // Assuming 3x3 filter size (can be adjusted)
                for (int i = 0; i < inputweights[layer][channel].length; i++) {
                    for (int j = 0; j < inputweights[layer][channel][i].length; j++) {
                        // He initialization
                        double fanIn = network[layer].length; // Number of input channels to this layer
                        double stddev = Math.sqrt(2.0 / fanIn); // Standard deviation for He initialization
                        double randomValue = Math.random(); // Generate a random number between 0 and 1
                        // Apply He initialization: scale it between -1 and 1, then multiply by stddev
                        inputweights[layer][channel][i][j] = stddev * (randomValue * 2 - 1); 
                    }
                }
            }
        }
    }
    
    weights=inputweights;

}

    public void forwardPass (double[][] input){
    
     
     network[0][0] =file.poolImage(input, network[0][0].length);

     for(int layer =0; layer<weights.length;layer++){
        for(int channel =0; channel < weights[layer].length;channel++){
            int inputCannel = (int)(channel *((double)network[layer].length/weights[layer].length));
             
            double[][] beforePooledImage = new double[network[layer][inputCannel].length][network[layer][inputCannel][0].length];
            for(int x =0; x<network[layer][inputCannel].length;x++){
                for(int y =0;y<network[layer][inputCannel][x].length;y++){
                //  if(network[layer][inputCannel][x][y]!=0) System.out.println(network[layer][inputCannel][x][y]+" "+channel+" "+layer);
                for(int fx=-1;fx<2;fx++)if(x+fx>=0&&x+fx<network[layer][inputCannel].length)
                for(int fy=-1;fy<2;fy++)if(y+fy>=0&&y+fy<network[layer][inputCannel][0].length){
                    beforePooledImage[x][y]+=network[layer][inputCannel][x+fx][y+fy]* weights[layer][channel][fx+1][fy+1];
                   beforePooledImage[x][y] = calculate.activaterelu(beforePooledImage[x][y]);
   }}}

   
        network[layer+1][channel]= file.poolImage(beforePooledImage, network[layer+1][channel].length);
 }}


    
    }
    public void forwardTrainingPass (double[][] input){
    
     
     network[0][0] =file.poolImage(input, network[0][0].length);

     for(int layer =0; layer<weights.length;layer++){
        for(int channel =0; channel < weights[layer].length;channel++){
            int inputCannel = (int)(channel *((double)network[layer].length/weights[layer].length));
             
            double[][] beforePooledImage = new double[network[layer][inputCannel].length][network[layer][inputCannel][0].length];
            for(int x =0; x<network[layer][inputCannel].length;x++){
                for(int y =0;y<network[layer][inputCannel][x].length;y++){
                if(Math.random()<calculate.dropoutRate) continue;
                //  if(network[layer][inputCannel][x][y]!=0) System.out.println(network[layer][inputCannel][x][y]+" "+channel+" "+layer);
                for(int fx=-1;fx<2;fx++)if(x+fx>=0&&x+fx<network[layer][inputCannel].length)
                for(int fy=-1;fy<2;fy++)if(y+fy>=0&&y+fy<network[layer][inputCannel][0].length){
                    beforePooledImage[x][y]+=network[layer][inputCannel][x+fx][y+fy]* weights[layer][channel][fx+1][fy+1];
                   beforePooledImage[x][y] = calculate.activaterelu(beforePooledImage[x][y])*(1/(1-calculate.dropoutRate));
   }}}

   
        network[layer+1][channel]= file.poolImage(beforePooledImage, network[layer+1][channel].length);
 }}


    
    }
   public double[][][][] calculateGradients(double[][][]outputDeltas){
     double[][][][] gradients = new double[weights.length][][][];
     double[][][][] deltas = new double[network.length][][][];
      for(int channel=0;channel< outputDeltas.length;channel++)
     for(int x=0;x<outputDeltas[0].length;x++)
      for(int y=0;y<outputDeltas[0][0].length;y++)
     if(network[network.length-1][channel][x][y]<0) outputDeltas[channel][x][y]*=calculate.leakyReluActivator;
     deltas[weights.length]= outputDeltas;
     int kernalSize = (int)(weights[0][0].length/2);
    
     for(int layer=weights.length-1;layer>=0;layer--){
             gradients[layer] = new double[weights[layer].length][3][3];
        deltas[layer] = new double[network[layer].length][network[layer][0].length][network[layer][0].length];
        for(int channel=0;channel<weights[layer].length;channel++){
        int inputChannel=(int)(channel*((double)network[layer].length/network[layer+1].length));
        double[][]nextLayerDelta=
        file.poolImage(deltas[layer+1][channel], network[layer][inputChannel].length);
        
        for(int x=0;x<network[layer][inputChannel].length;x++)
        for(int y=0;y<network[layer][inputChannel].length;y++){
        
        for(int fx=-kernalSize;fx<=kernalSize;fx++)
        if(fx+x>=0&&fx+x<network[layer][inputChannel].length)
        for(int fy=-kernalSize;fy<=kernalSize;fy++)
        if(fy+y>=0&&fy+y<network[layer][inputChannel].length){
            
        gradients[layer][channel][fx+kernalSize][fy+kernalSize] += 
        nextLayerDelta[x][y]*
        network[layer][inputChannel][x+fx][y+fy];
        
        deltas[layer][inputChannel][x+fx][y+fy] += 
        nextLayerDelta[x][y]*
        weights[layer][channel][fx+kernalSize][fy+kernalSize];

    }
}
        
    }
     for(int channel=0;channel< deltas[layer].length;channel++)
     for(int x=0;x<deltas[layer][0].length;x++)
      for(int y=0;y<deltas[layer][0][0].length;y++)
     if(network[layer][channel][x][y]<0) deltas[layer][channel][x][y]*=calculate.leakyReluActivator;

    }
     return gradients;
   }
   public CNN clone() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this); // Serialize the current object
            out.flush();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);

            return (CNN) in.readObject(); // Deserialize to create a deep copy
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Deep copy failed", e);
        }
    }
}
