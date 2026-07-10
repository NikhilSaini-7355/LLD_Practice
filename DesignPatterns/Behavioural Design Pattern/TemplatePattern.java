abstract class ModelTrainer{
    protected void load(String path){
        System.out.println("Loading data from path ="+path);
    }
    protected abstract void train();
    protected void preprocess(){
        System.out.println("preprocessing data");
    }
    protected abstract void evaluate();
    protected void save(){
        System.out.println("Saving model result using default way");
    }
    protected final void templateMethod(String path)
    {
        load(path);
        preprocess();
        train();
        evaluate();
        save();
    }
}

class NeuralNetworkModel extends ModelTrainer{
    @Override
    protected void train()
    {
        System.out.println("Training Neural Network Model");
    }
    
    @Override
    protected void evaluate()
    {
        System.out.println("evaluating Neural Network Model");
    }
    
    @Override
    protected void save()
    {
        System.out.println("Saving Neural Network Model result");
    }
}

class SVMModel extends ModelTrainer{
    @Override
    protected void train()
    {
        System.out.println("Training SVM Network Model");
    }
    
    @Override
    protected void evaluate()
    {
        System.out.println("evaluating SVM Network Model");
    }
}

class DecisionTreeModel extends ModelTrainer{
    @Override
    protected void train()
    {
        System.out.println("Training Decision Tree Network Model");
    }
    
    @Override
    protected void evaluate()
    {
        System.out.println("evaluating Decision Tree Network Model");
    }
    
    @Override
    protected void save()
    {
        System.out.println("Saving Decision Tree Network Model result");
    }
}

public class Main{
    public static void main(String[] args)
    {
        ModelTrainer neuralModel = new NeuralNetworkModel();
        ModelTrainer svmModel = new SVMModel();
        ModelTrainer decisionTreeModel = new DecisionTreeModel();
        
        neuralModel.templateMethod("data/neuralData/files");
        svmModel.templateMethod("data/SVMData/files");
        decisionTreeModel.templateMethod("data/decisionTreeData/files");
    }
}
