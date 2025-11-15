import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NeuralNetwork implements Serializable {
    double [][]network;
    double [][][]weights;
    public NeuralNetwork(int[] layers){
        network = new double[layers.length][];
    for(int layer =0;layer< layers.length;layer++)
    network[layer]= new double[layers[layer]];
    }
  public void SetWeights(double[][][] inputweights) {
    
   
    if (inputweights == null) { 
        inputweights= new double[network.length-1][][];
      Random random = new Random();
for (int layer = 0; layer < inputweights.length; layer++) {
    inputweights[layer] = new double[network[layer].length][network[layer + 1].length];
    for (int input = 0; input < inputweights[layer].length; input++) {
        for (int output = 0; output < inputweights[layer][input].length; output++) {
            double fanIn = network[layer].length;
            double stddev = Math.sqrt(2.0 / fanIn);
            inputweights[layer][input][output] = random.nextGaussian() * stddev; // Correct He initialization
        }
    }
}
    }
    
    weights = inputweights;
}

    public void run(double[] input){
    
    for(int layer =0;layer< network.length;layer++)
    network[layer]= new double[network[layer].length];
        network[0] = input;
        for(int layer =0; layer< weights.length;layer++)
        for(int inputNode =0;inputNode<weights[layer].length;inputNode++){
   
         network[layer][inputNode]= calculate.activaterelu(network[layer][inputNode]);
        for(int outputNode =0;outputNode<weights[layer][0].length;outputNode++)
        network[layer+1][outputNode]+=network[layer][inputNode]*weights[layer][inputNode][outputNode];
        }

       // for(int output=0;output<network[network.length-1].length;output++)
      //  network[weights.length][output]= calculate.sigmoid(network[weights.length][output]);
}
public void TrainingRun(double[] input){
    
    for(int layer =0;layer< network.length;layer++)
    network[layer]= new double[network[layer].length];
        network[0] = input;
        for(int layer =0; layer< weights.length;layer++)
        for(int inputNode =0;inputNode<weights[layer].length;inputNode++){
            if(Math.random()<calculate.dropoutRate)continue;
            
         network[layer][inputNode]= calculate.activaterelu(network[layer][inputNode])*(1/(1-calculate.dropoutRate));
        for(int outputNode =0;outputNode<weights[layer][0].length;outputNode++)
        network[layer+1][outputNode]+=network[layer][inputNode]*weights[layer][inputNode][outputNode];
        }

       // for(int output=0;output<network[network.length-1].length;output++)
      //  network[weights.length][output]= calculate.sigmoid(network[weights.length][output]);
}
    public Map<String, double[][][]> calculateGradients(double[]losses1d){
         double[][]losses = new double[losses1d.length][1];
        for(int loss=0;loss<losses1d.length;loss++)
        losses[loss] = new double[]{losses1d[loss]};
  
        if(losses.length!=network[network.length-1].length) {
            System.out.print("sigma");return null;}
        double[][][] gradients = new double[weights.length][][];
        double[][][] delta = new double[network.length][][];
        delta[network.length-1] = losses;
        
        for(int layer=weights.length-1;layer>=0;layer--){
           
            gradients[layer]=new double[weights[layer].length][weights[layer][0].length];
            delta[layer]=new double[weights[layer].length][1];
            for(int inputnode =0;inputnode<weights[layer].length;inputnode++){
                
                for(int outputnode =0;outputnode<weights[layer][0].length;outputnode++){
                gradients[layer][inputnode][outputnode]= delta[layer+1][outputnode][0]*(network[layer][inputnode]);
                delta[layer][inputnode][0]+=delta[layer+1][outputnode][0]*weights[layer][inputnode][outputnode];}
               delta[layer][inputnode][0] *= (network[layer][inputnode] > 0) ? 1.0 : calculate.leakyReluActivator;

            }
            //System.out.println(Arrays.toString(delta[layer]));
        }
        Map<String, double[][][]> map = new HashMap<>();
        map.put("gradients", gradients);
        map.put("deltas", delta);
        return map;
    }
  public NeuralNetwork clone() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this); // Serialize the current object
            out.flush();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);

            return (NeuralNetwork) in.readObject(); // Deserialize to create a deep copy
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Deep copy failed", e);
        }
    }
}
