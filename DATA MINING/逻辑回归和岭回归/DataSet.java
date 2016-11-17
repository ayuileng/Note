import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by yajima on 2016/11/15.
 */
public class DataSet {
    private ArrayList<ArrayList<Double>> feature= new ArrayList<>();
    private ArrayList<Integer> label = new ArrayList<>();
    public DataSet(String filename){
        File dataSet = new File(filename);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(dataSet));
            String line;
            String[] strs;
            while ((line = reader.readLine()) != null) {
                ArrayList<Double> p = new ArrayList<>();
                strs = line.split(",");
                for (int i = 0; i < strs.length - 1; ++i) {
                    p.add(Double.parseDouble(strs[i]));
                }
                label.add(Integer.parseInt(strs[strs.length - 1]));
                feature.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Double>> getFeature() {
        return feature;
    }

    public ArrayList<Integer> getLabel() {
        return label;
    }
}
