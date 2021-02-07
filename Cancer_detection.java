import java.io.FileInputStream;
import java.util.*;
import java.util.Random;
import javax.swing.JFrame;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.converters.*;
import weka.core.*;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import javax.swing.JTextArea;

public class ClassificationDemo {

	private Instances data;
	private J48 tree;

	public ClassificationDemo(String arrf) throws Exception {
		DataSource source = new DataSource(arrf);
		data = source.getDataSet();
                data.setClassIndex(data.numAttributes() - 1);
		System.out.println(data.numInstances() + " instances loaded!");
		 System.out.println(data.toString());
	}

	public void removeAttribute() throws Exception {
		Remove remove = new Remove();
		String[] opts = new String[] { "-R", "16" };
		remove.setOptions(opts);
		remove.setInputFormat(data);
		data = Filter.useFilter(data, remove);
	}

	public void selectFeatures() throws Exception {
		InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
		Ranker ranker = new Ranker();
		AttributeSelection attSelect = new AttributeSelection();
		attSelect.setEvaluator(evaluator);
		attSelect.setSearch(ranker);
		attSelect.SelectAttributes(data);
		int[] selectedAttributes = attSelect.selectedAttributes();
		System.out.println(Utils.arrayToString(selectedAttributes));
	}

	public void buildDecisionTree() throws Exception {
		tree = new J48();
		String[] options = new String[1];
		options[0] = "-U"; // un-pruned tree option
		tree.setOptions(options);
		tree.buildClassifier(data);
		System.out.println(tree.toString());
	}

	public void visualizeTree() throws Exception {
		TreeVisualizer tv = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
		JFrame frame = new JFrame("Tree Visualizer");
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(tv);
		frame.setVisible(true);
		tv.fitToScreen();
	}

	public String classifyData(double...x) throws Exception {
		double[] vals = new double[x.length];
		/*vals[0] = a;	//rbc count
		vals[1] =  b;	//wbc count
		vals[2] = c;	//blood platelets
		vals[3] = d;	//hemoglobin
		vals[4] = e;	//hematocrit
		vals[5] = f;	//lymphs
		vals[6] = g;	//mono
		vals[7] = h;	//eosino
		vals[8] = i;	//baso
		vals[9] = j;	//neutro
		vals[10] = k;	//lb
		vals[11] = l;	//mb
		vals[12] = m;	//aurods
		vals[13] = n;	//age
		vals[14] = 0;	//family dna*/
                int i=0;
                for(double temp:x)
                {vals[i++]=temp;
                }
    		Instance Cancer = new DenseInstance(1.0, vals);
		Cancer.setDataset(data);
		double result = tree.classifyInstance(Cancer);
		System.out.println(data.classAttribute().value((int)result));//prints out the result
	}


	public void showErrorMetrics() throws Exception {
		Classifier c1 = new J48();
		Evaluation evalRoc = new Evaluation(data);
		evalRoc.crossValidateModel(c1, data, 10, new Random(1), new Object[] {});
		System.out.println(evalRoc.toSummaryString());
		System.out.println(evalRoc.toMatrixString());
	}//shows evaluation and performance of model

	public static void main(String[] args) {
           
            Scanner sc=null;
            try{
         sc = new Scanner(new FileInputStream("C:\\Users\\JAMESBOND\\Desktop\\sa.txt"));// reading the input from text file
        }catch(Exception e){}
            double RBC,WBC,Blood_platelets,Hemoglobin,hematocrit,Lymphocytes,monocytes,basophills,eosinophills,neutrophills,Lymphoblasts,Myeloblasts;
            double age,var1 = 0,var2 = 0;
            RBC = sc.nextDouble();
            WBC = sc.nextDouble();
            Blood_platelets = sc.nextDouble();
            Hemoglobin = sc.nextDouble();
            hematocrit = sc.nextDouble();
            Lymphocytes = sc.nextDouble();
            monocytes = sc.nextDouble();
            basophills = sc.nextDouble();
            eosinophills = sc.nextDouble();
            neutrophills = sc.nextDouble();
            Lymphoblasts = sc.nextDouble();
            Myeloblasts = sc.nextDouble();
            age = sc.nextDouble();
            boolean ar = sc.nextBoolean();
            boolean fd = sc.nextBoolean();
            if (ar == true) { 
               var1 = 1.0;  
           } else if (ar == false) {
               var1 = 0.0;
           }
            if (fd == true) { 
               var2 = 1.0;  
           } else if (fd == false) {
               var2 = 0.0;
           }
		try {
			ClassificationDemo weka = new ClassificationDemo("C:\\Users\\JAMESBOND\\Documents\\files\\REPORTS_CLASSIFICATION_6.csv.arff");
			//weka.removeAttribute();
			//weka.selectFeatures();e
			weka.buildDecisionTree();
			//weka.visualizeTree();
	                String r = weka.classifyData(RBC,WBC,Hemoglobin,hematocrit,Blood_platelets,Lymphocytes,monocytes,eosinophills, basophills,neutrophills,Lymphoblasts,Myeloblasts,age,var1,var2);
			//weka.showErrorMetrics();
                        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}