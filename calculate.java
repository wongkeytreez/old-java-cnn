public class calculate {
    public static double leakyReluActivator =0.001;
    public static double dropoutRate=0.25;
    public static double activaterelu(double inp){
     return (inp>0)?inp:(inp*leakyReluActivator);
    }
    // public static double activaterelu(int inp){
    //  return (int)((inp>0)?inp:inp/leakyReluActivator);
    // }
    public static double deactivaterelu(double inp){
     return (inp>0)?inp:(inp/leakyReluActivator);
    }
    public static double BCE(double predictedOutput,boolean trueOutputB){
    int trueOutput = (trueOutputB==false)?0:1;
    predictedOutput = Math.max(1e-10, Math.min(1 - 1e-10,predictedOutput));
    return -(trueOutput*Math.log(predictedOutput)+(1-trueOutput)*Math.log(1-predictedOutput));
    }
    public static double sigmoid(double x){
    return 1 / (1 + Math.exp(-x));
    }
    public static double MSE(double predictedOutput,double trueOutput){
        return(trueOutput-predictedOutput)*(trueOutput-predictedOutput);
    }
    public static double[][][] FlatToCNN(double[] flat, int channels) {
    int size = (int) Math.sqrt(flat.length / channels); // assuming it's a square grid
    double[][][] output = new double[channels][size][size];
    
    // Loop through each channel, x, and y and assign values from the flat array
    for (int channel = 0; channel < channels; channel++) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                output[channel][x][y] = flat[channel * size * size + x * size + y];
            }
        }
    }
    return output;
}

    public static double[] FlatToGrid(double[]flat,int channels){
        int size = (int) Math.sqrt(flat.length / channels);
        double[] output= new double[flat.length];
        for(int channel=0;channel<channels;channel++)
        for(int x=0;x<size;x++) 
        for(int y=0;y<size;y++) output[channel+y*channels+x*channel*channels]= flat[channel*size*size+x*size+y];
        return output;
    }
     public static double[] average2DTo1D(double[][] input2D) {
if(input2D.length==0)return new double[0];
    double[] output1D = new double[input2D[0].length];

    // Loop through each of the 3D arrays in the 4D array
  
   for (int b = 0; b < input2D[0].length; b++) {
   
            double sum = 0;
             for (int a = 0; a < input2D.length; a++) 
                sum += input2D[a][b];
                  
            
            output1D[b] += sum / input2D.length;
        
        }
    return output1D;}
    public static double[][][] average4DTo3D(double[][][][] input4D) {
  if(input4D.length==0)return new double[0][][];
    double[][][] output3D = new double[input4D[0].length][][];

    // Loop through each of the 3D arrays in the 4D array
  
   for (int b = 0; b < input4D[0].length; b++) {
    output3D[b] = new double [input4D[0][b].length][];
        for (int c = 0; c < input4D[0][b].length; c++) {
            output3D[b][c] = new double [input4D[0][b][c].length];
        for (int d = 0; d < input4D[0][b][c].length; d++) {
            double sum = 0;
             for (int a = 0; a < input4D.length; a++) 
                sum += input4D[a][b][c][d];
                  
            
            output3D[b][c][d] += sum / input4D.length;
        
        }}}
    return output3D;}
     public static double[][][][] average5DTo4D(double[][][][][] input5D) {
    if(input5D.length==0)return new double[0][][][];
    double[][][][] output4D = new double[input5D[0].length][][][];

    // Loop through each of the 3D arrays in the 4D array
  
   for (int b = 0; b < input5D[0].length; b++) {
    output4D[b] = new double [input5D[0][b].length][][];
        for (int c = 0; c < input5D[0][b].length; c++) {
            output4D[b][c] = new double [input5D[0][b][c].length][];
        for (int d = 0; d < input5D[0][b][c].length; d++) {
            output4D[b][c][d] = new double [input5D[0][b][c][d].length];
            for (int e=0; e< input5D[0][b][c].length; e++){

            double sum = 0;
             for (int a = 0; a < input5D.length; a++) 
                sum += input5D[a][b][c][d][e];
            output4D[b][c][d][e] += sum / input5D.length;
                  
            }
        
        }}}
        
    
    return output4D;
}
}
