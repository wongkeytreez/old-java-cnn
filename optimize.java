import java.util.HashMap;
import java.util.Map;

public class optimize {
    static float beta1 = 0.9f;
static float beta2 = 0.999f;

public static Map<String, double[][][]> NNadam(
        double[][][] gradients, 
        double[][][] weights, 
        double[][][] oldMomentum, 
        double[][][] oldVariance, 
        double learningRate, 
        int iteration) {
    
    double[][][] momentum = new double[gradients.length][][];
    double[][][] variance = new double[gradients.length][][];
    double[][][] newWeights=new double[gradients.length][][];

    for (int layer = 0; layer < gradients.length; layer++) {
        momentum[layer] = new double[gradients[layer].length][gradients[layer][0].length];
        variance[layer] = new double[gradients[layer].length][gradients[layer][0].length];
        newWeights[layer]=new double[gradients[layer].length][gradients[layer][0].length];

        for (int inputNode = 0; inputNode < gradients[layer].length; inputNode++) {
            for (int outputNode = 0; outputNode < gradients[layer][inputNode].length; outputNode++) {
                // Momentum update
                momentum[layer][inputNode][outputNode] =
                        beta1 * oldMomentum[layer][inputNode][outputNode] +
                        (1 - beta1) * gradients[layer][inputNode][outputNode];

                // Variance update
                variance[layer][inputNode][outputNode] =
                        beta2 * oldVariance[layer][inputNode][outputNode] +
                        (1 - beta2) * gradients[layer][inputNode][outputNode] *
                        gradients[layer][inputNode][outputNode];

                // Bias correction
                double correctedMomentum = momentum[layer][inputNode][outputNode] / 
                        (1 - Math.pow(beta1, iteration));
                double correctedVariance = variance[layer][inputNode][outputNode] / 
                        (1 - Math.pow(beta2, iteration));

                // Weight update
                newWeights[layer][inputNode][outputNode] =weights[layer][inputNode][outputNode]-
//(learningRate * gradients[layer][inputNode][outputNode]);
                (learningRate * correctedMomentum) / 
                       (Math.sqrt(correctedVariance) + 1e-8);
            }
        }
    }

    // Return results
    Map<String, double[][][]> map = new HashMap<>();
    map.put("newWeights", newWeights);
    map.put("momentum", momentum);
    map.put("variance", variance);
    return map;
}
public static Map<String, double[][][][]> CNNadam(
        double[][][][] gradients, 
        double[][][][] weights, 
        double[][][][] oldMomentum, 
        double[][][][] oldVariance, 
        double learningRate, 
        int iteration) {
    double[][][][] newweights = new double[weights.length][][][];
    double[][][][] momentum = new double[gradients.length][][][];
    double[][][][] variance = new double[gradients.length][][][];


    for (int layer = 0; layer < gradients.length; layer++) {
        newweights[layer] = new double[gradients[layer].length][3][3];
        momentum[layer] = new double[gradients[layer].length][3][3];
        variance[layer] = new double[gradients[layer].length][3][3];
       
    for (int channel = 0; channel < gradients[layer].length; channel++)
        for (int wx = 0; wx < gradients[layer][0].length; wx++) {
            for (int wy = 0; wy < gradients[layer][0][0].length; wy++) {
//System.out.println(layer+" "+channel+" "+wx+" "+wy+" "+oldMomentum.length);
                // Momentum update
                momentum[layer][channel][wx][wy] =
                        beta1 * oldMomentum[layer][channel][wx][wy] +
                        (1 - beta1) * gradients[layer][channel][wx][wy];

                // Variance update
                variance[layer][channel][wx][wy] =
                        beta2 * oldVariance[layer][channel][wx][wy] +
                        (1 - beta2) * gradients[layer][channel][wx][wy] *
                        gradients[layer][channel][wx][wy];

                // Bias correction
                double correctedMomentum = momentum[layer][channel][wx][wy] / 
                        (1 - Math.pow(beta1, iteration));
                double correctedVariance = variance[layer][channel][wx][wy] / 
                        (1 - Math.pow(beta2, iteration));

                // Weight update
                 newweights[layer][channel][wx][wy] =weights[layer][channel][wx][wy]-

                 (learningRate * correctedMomentum) / 
                        (Math.sqrt(correctedVariance) + 1e-8);

            }
        }
    }
  
    // Return results
    Map<String, double[][][][]> map = new HashMap<>();
    map.put("newWeights", newweights);
    map.put("momentum", momentum);
    map.put("variance", variance);
    return map;
}

}
