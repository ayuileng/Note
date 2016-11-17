import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yajima on 2016/11/11.
 */
public class Logistic {
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
            if (0.5 < 1 / (1 + Math.pow(Math.E, -dotMuti(w, feature.get(i))))) {
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
            double gredient = Math.pow(Math.E, -train_label.get(rand) * dotMuti(W, point)) * (-train_label.get(rand)) * point.get(i) / (1 + Math.pow(Math.E, -train_label.get(rand) * dotMuti(W, point))) + 0.01 * Math.signum(W.get(i));
            newW.set(i, W.get(i) - 0.001*gredient);
        }
        return newW;
    }

    public static void main(String[] args) {
        DataSet d1 = new DataSet("D:\\IDEAprojects\\质检\\NJUzhijian\\DM4\\file\\dataset1-a9a-training.txt");
        DataSet d2 = new DataSet("D:\\IDEAprojects\\质检\\NJUzhijian\\DM4\\file\\dataset1-a9a-testing.txt");
        ArrayList<ArrayList<Double>> train_feature = d1.getFeature();
        ArrayList<Integer> train_label = d1.getLabel();
        ArrayList<ArrayList<Double>> test_feature = d2.getFeature();
        ArrayList<Integer> test_label = d2.getLabel();
        ArrayList<Double> w = new ArrayList<>();
        for (int i = 0; i < train_feature.get(0).size(); i++) {
            w.add(1.0);
        }
        for (int iter = 1; iter <= 50000; iter++) {
            Random r = new Random();
            int rand = r.nextInt(train_label.size()-1) ;
            w = Logistic.computeW(w, train_feature, train_label, rand, iter);
            if (iter % 1000 == 0) {
                //System.out.println(Logistic.computeAccuracy(train_feature, train_label, w));
                System.out.println(Logistic.computeAccuracy(test_feature, test_label, w));
            }
        }
    }
}
