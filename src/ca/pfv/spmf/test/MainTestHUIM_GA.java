package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.HUIM_GA.AlgoHUIM_GA;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;


/**
 * Example of how to use the HUIM-GA algorithm
 * from the source code.
 *
 * @author Jerry Chun-Wei Lin, Lu Yang, Philippe Fournier-Viger, 2016
 */
public class MainTestHUIM_GA
{

    public static void main(String[] arg) throws IOException
    {

        String input = fileToPath("contextHUIM.txt");
        System.out.println(input);

        String output = ".//output.txt";

        int min_utility = 30;  //

        // Applying the huim_bpso algorithm
        AlgoHUIM_GA huim_ga = new AlgoHUIM_GA();
        huim_ga.runAlgorithm(input, output, min_utility);
        huim_ga.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException
    {
        URL url = MainTestHUIM_GA.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
