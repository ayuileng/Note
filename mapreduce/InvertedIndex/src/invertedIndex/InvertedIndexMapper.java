package invertedIndex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.util.StringTokenizer;


/**
 * Created by yj on 16-11-1.
 */
public class InvertedIndexMapper extends Mapper<Object, Text, Text, IntWritable> {
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        FileSplit filesplite = (FileSplit) context.getInputSplit();
        String longFilename = filesplite.getPath().getName();//获取完整的文件名
        String filename = longFilename.substring(0,longFilename.indexOf("."));//截取文件名，去除.txt.segmented后缀名
        String line = value.toString().toLowerCase();
        StringTokenizer itr = new StringTokenizer(line);//分词
        while (itr.hasMoreTokens()) {
            Text word = new Text();
            word.set(itr.nextToken() + "#" + filename);//格式为词语#文件名
            context.write(word, new IntWritable(1));//输出的键值对格式是<词语#文件名，1>
        }
    }


}
