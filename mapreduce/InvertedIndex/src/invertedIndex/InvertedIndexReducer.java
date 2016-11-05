package invertedIndex;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yj on 16-11-1.
 */
public class InvertedIndexReducer extends Reducer<Text, IntWritable, Text, Text> {
    private Text word1 = new Text();
    private Text word2 = new Text();
    static Text CurrentItem = new Text(" ");
    static List<String> postingList = new ArrayList<>();
    private String article;

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        word1.set(key.toString().split("#")[0]);//单词
        article = key.toString().split("#")[1];//所在文档
        for (IntWritable val : values) {
            sum += val.get();
        }
        word2.set(article + ":" + sum);
        if (!CurrentItem.equals(word1) && !CurrentItem.equals(" ")) {
            StringBuilder sb = new StringBuilder();
            long count = 0;
            long inArticles = postingList.size();
            for (String p : postingList) {
                sb.append(p).append(";");
                count += Long.parseLong(p.substring(p.indexOf(":") + 1));
            }
            //相等	金庸04天龙八部:1;金庸07鹿鼎记:2;金庸12倚天屠龙记:6;金庸13碧血剑:3;<total,12>.
            //sb.insert(0," "+count/inArticles+", ");
            try {
                double l = (count + 0.0) / inArticles;
                sb.insert(0, " " + l + ",");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (count > 0) {
                context.write(CurrentItem, new Text(sb.toString()));
            }
            postingList = new ArrayList<>();
        }
        CurrentItem = new Text(word1);
        postingList.add(word2.toString());

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        long count = 0;
        long inArticles = postingList.size();
        for (String p : postingList) {
            sb.append(p).append(";");
            count += Long.parseLong(p.substring(p.indexOf(":") + 1));
        }
        double l = (count + 0.0) / inArticles;
        sb.insert(0, " " + l + ",");
        if (count > 0) {
            context.write(CurrentItem, new Text(sb.toString()));
        }
    }
}
