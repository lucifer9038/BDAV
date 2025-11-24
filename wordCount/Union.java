package trial;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Union {

  private static Text emptyWord = new Text("");

  public static class UnionMapper
       extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
      context.write(value, emptyWord);
    }
  }

  public static class UnionReducer
       extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
      context.write(key, key);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Union of files");
    job.setJarByClass(Union.class);
    job.setMapperClass(UnionMapper.class);
    job.setCombinerClass(UnionReducer.class);
    job.setReducerClass(UnionReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
