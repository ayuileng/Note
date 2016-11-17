import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yajima on 2016/11/11.
 */
public class Ridge {
    //两个向量的点积
    private static double dotMuti(ArrayList<Double> w, ArrayList<Double> x) {
        double sum = 0;
        for (int i = 0; i < w.size(); i++) {
            sum += w.get(i) * x.get(i);
        }
        return sum;
    }

    //计算精确率
    private static double computeAccuracy(ArrayList<ArrayList<Double>> feature, ArrayList<Integer> label, ArrayList<Double> w) {
        double accuracy = 0.0;
        int pre_label;
        for (int i = 0; i < feature.size(); i++) {
            double tmp = dotMuti(feature.get(i),w);
            if (Math.abs(tmp-1)>Math.abs(tmp+1)) {
                pre_label = 1;
            } else {
                pre_label = -1;
            }
            if (pre_label == label.get(i)) {
                accuracy += 1.0;
            }
        }
        return 1 - accuracy / feature.size();
    }

    private static ArrayList<Double> computeW(ArrayList<Double> W, ArrayList<ArrayList<Double>> train_feature, ArrayList<Integer> train_label, int rand, int iterNum) {
        ArrayList<Double> point = train_feature.get(rand);
        ArrayList<Double> newW = new ArrayList<>(W);
        for (int i = 0; i < W.size(); i++) {
            double gredient = 2*(train_label.get(rand)-dotMuti(W,point)*(-point.get(i)))+2*0.001*W.get(i);
            newW.set(i, W.get(i) - 0.001*gredient);
        }
        return newW;
    }


    public static void main(String[] args) {
        DataSet d1 = new DataSet("D:\\IDEAprojects\\质检\\NJUzhijian\\DM4\\file\\covtype-training.txt");
        DataSet d2 = new DataSet("D:\\IDEAprojects\\质检\\NJUzhijian\\DM4\\file\\covtype-testing.txt");
        ArrayList<ArrayList<Double>> train_feature = d1.getFeature();
        ArrayList<Integer> train_label = d1.getLabel();
        ArrayList<ArrayList<Double>> test_feature = d2.getFeature();
        ArrayList<Integer> test_label = d2.getLabel();
        ArrayList<Double> w = new ArrayList<>();
        for (int i = 0; i < train_feature.get(0).size(); i++) {
            w.add(1.0);
        }
        for (int iter = 1; iter <= 500000; iter++) {
            Random r = new Random();
            int rand = r.nextInt(train_label.size()-1) ;
            w = Ridge.computeW(w, train_feature, train_label, rand, iter);
            if (iter % 10000 == 0) {
                System.out.println(Ridge.computeAccuracy(train_feature, train_label, w));
                //System.out.println(Ridge.computeAccuracy(test_feature, test_label, w));
            }
        }
    }
}
