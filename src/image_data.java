import java.io.IOException;
import java.util.Arrays;

public class image_data {
    public static void core() throws IOException {
        /* given: two images with their grayscale values defined
         this is in the form of two 2-D integer arrays, which we will define as int[][] image1 and int[][] image2
         */
        // int[][] image1 = new int[][]{{23,42},{9,12}};
        int[][] image1 = get_grayscale.getGrayscale("C:\\Users\\vishn\\OneDrive - University of St Andrews\\EMD2\\MNISTExtractor\\Extracted images\\test set\\1\\2.png");
       // int[][] image2 = new int[][]{{44,25},{92,1}};
        int[][] image2 = get_grayscale.getGrayscale("C:\\Users\\vishn\\OneDrive - University of St Andrews\\EMD2\\MNISTExtractor\\Extracted images\\test set\\1\\5.png");
        // normalise them first (to double arrays)
        double[][] imagen1 = new double[image1.length][image1[0].length];
        double[][] imagen2 = new double[image2.length][image2[0].length];
        int image1sum = 0;
        for (int[] i: image1)
        {
            for (int j: i)
                image1sum+=j;
        }
        int image2sum = 0;
        for (int[] i: image2)
        {
            for (int j: i)
                image2sum+=j;
        }
        for (int i = 0; i < image1.length; i++)
        {
            for (int j = 0; j < image1[0].length; j++)
            {
                imagen1[i][j] = (double)image1[i][j]/(double)image1sum;
            }
        }
        for (int i = 0; i < image2.length; i++)
        {
            for (int j = 0; j < image2[0].length; j++)
            {
                imagen2[i][j] = (double)image2[i][j]/(double)image2sum;
            }
        }
        // compare
        double[][] comp = new double[image1.length][image1[0].length];
        int supplynum = 0;
        int demandnum = 0;
        for (int i = 0; i < comp.length; i++)
        {
            for (int j = 0; j < comp[0].length; j++) {
                comp[i][j] = imagen1[i][j] - imagen2[i][j];
                if (comp[i][j] > 0)
                    supplynum++;
                else if (comp[i][j] < 0)
                    demandnum++;
            }
        }
        // now convert to graph
        /*
        Example: if the array at this point is [[-0.4, 0], [0.3, 0.5]], then the distance can be easily computed.
        Remember that the format of supply and demand arrays is (vertex number, how much to supply)
        Let's do that first.
         */
        // define the core array
        int[][] supplyarr = new int[supplynum][2];
        int[][] demandarr = new int[demandnum][2];
        int supplyptr = 0;
        int demandptr = 0;
        double supplydoub = 0;
        double demanddoub = 0;
        for (int i = 0; i < comp.length; i++)
        {
            for (int j = 0; j < comp[0].length; j++)
            {
                if (comp[i][j] > 0)
                {
                    supplyarr[supplyptr][0] = (1000*i + j); // hack for vertex number
                    supplyarr[supplyptr][1] = (int) (1000000*comp[i][j]); // normalise to 1000 for now
                    supplyptr++;
                    supplydoub+=comp[i][j];
                }
                else if (comp[i][j] < 0)
                {
                    demandarr[demandptr][0] = (1000*i + j); // hack for vertex number
                    demandarr[demandptr][1] = (-1)*(int)(1000000*comp[i][j]); // normalise to 1000 for now
                    demandptr++;
                    demanddoub+=comp[i][j];
                }
            }
        }
        int supplysum = 0;
        int demandsum = 0;
        for (int[] ints : supplyarr) supplysum += ints[1];
        for (int[] ints : demandarr) demandsum += ints[1];
        if (supplysum != demandsum)
        {
            // temporary hack to allow the algorithm to run
            supplyarr[supplyarr.length - 1][1] -= (supplysum - demandsum);
        }

        System.out.println("Supplysum = " + supplysum + " and demandsum is " + demandsum + " while supplydoub is " + supplydoub + " and demanddoub is " + demanddoub);
        // next step is to define the vertices themselves and the edge. Complete graph
        int R = comp.length;
        int[] vertices = new int[comp.length * comp[0].length];
        int[][] edges = new int[R*R*(R*R - 1)][2];
        int[] capacity = new int[R*R*(R*R - 1)];
        int vertptr = 0;
        int edgeptr = 0;
        int[] edge_weights = new int[R*R*(R*R - 1)]; // weight of each edge - basically their distance
        for (int i = 0; i < comp.length; i++)
        {
            for (int j = 0; j < comp[0].length; j++)
            {
                int locator = 1000*i + j;
                vertices[vertptr] = locator;
                for (int k = 0; k < R; k++)
                {
                    for (int l = 0; l < R; l++)
                    {
                        if (k == i && l == j)
                            continue;
                        edges[edgeptr][0] = locator;
                        edges[edgeptr][1] = 1000*k + l;
                        edge_weights[edgeptr] = Math.abs(k - i) + Math.abs(l - j);
                        edgeptr++;
                    }
                }
                vertptr++;
            }
        }
        // consider the capacity as infinite
        Arrays.fill(capacity, 999999999);
        // now send it to the minflow function
        System.out.println("final flow = " + Main.getEMD(vertices, edges, edge_weights, supplyarr, demandarr, capacity));
    }
}
