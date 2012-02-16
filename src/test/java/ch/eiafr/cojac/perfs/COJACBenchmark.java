/*
 * *
 *    Copyright 2011 Baptiste Wicht & Frédéric Bapst
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ch.eiafr.cojac.perfs;

import ch.eiafr.cojac.Arg;
import ch.eiafr.cojac.Args;
import ch.eiafr.cojac.CojacClassLoader;
import ch.eiafr.cojac.perfs.opcodes.*;
import ch.eiafr.cojac.perfs.scimark.SciMark;
import ch.eiafr.cojac.Agent;
import ch.eiafr.cojac.CojacReferences;
import ch.eiafr.cojac.CojacReferences.CojacReferencesBuilder;
import ch.eiafr.cojac.unit.AgentTest;
import com.wicht.benchmark.utils.Benchs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Callable;

public class COJACBenchmark {
    public static void main(String[] args) throws Exception {
        System.out.println("COJAC Benchmark");

        bench(52, "IADD Benchmark", new IADDCallable(), "ch.eiafr.cojac.perfs.opcodes.IADDCallable");
        bench(52, "ISUB Benchmark", new ISUBCallable(), "ch.eiafr.cojac.perfs.opcodes.ISUBCallable");
        bench(52, "IMUL Benchmark", new IMULCallable(), "ch.eiafr.cojac.perfs.opcodes.IMULCallable");
        bench(52, "IDIV Benchmark", new IDIVCallable(), "ch.eiafr.cojac.perfs.opcodes.IDIVCallable");

        bench(52, "FADD Benchmark", new FADDCallable(), "ch.eiafr.cojac.perfs.opcodes.FADDCallable");
        bench(52, "FSUB Benchmark", new FSUBCallable(), "ch.eiafr.cojac.perfs.opcodes.FSUBCallable");
        bench(52, "FMUL Benchmark", new FMULCallable(), "ch.eiafr.cojac.perfs.opcodes.FMULCallable");
        bench(52, "FDIV Benchmark", new FDIVCallable(), "ch.eiafr.cojac.perfs.opcodes.FDIVCallable");
        bench(52, "FREM Benchmark", new FREMCallable(), "ch.eiafr.cojac.perfs.opcodes.FREMCallable");

        bench(52, "LADD Benchmark", new LADDCallable(), "ch.eiafr.cojac.perfs.opcodes.LADDCallable");
        bench(52, "LSUB Benchmark", new LSUBCallable(), "ch.eiafr.cojac.perfs.opcodes.LSUBCallable");
        bench(29, "LMUL Benchmark", new LMULCallable(), "ch.eiafr.cojac.perfs.opcodes.LMULCallable");
        bench(52, "LDIV Benchmark", new LDIVCallable(), "ch.eiafr.cojac.perfs.opcodes.LDIVCallable");

        bench(52, "DADD Benchmark", new DADDCallable(), "ch.eiafr.cojac.perfs.opcodes.DADDCallable");
        bench(52, "DSUB Benchmark", new DSUBCallable(), "ch.eiafr.cojac.perfs.opcodes.DSUBCallable");
        bench(52, "DMUL Benchmark", new DMULCallable(), "ch.eiafr.cojac.perfs.opcodes.DMULCallable");
        bench(52, "DDIV Benchmark", new DDIVCallable(), "ch.eiafr.cojac.perfs.opcodes.DDIVCallable");
        bench(52, "DREM Benchmark", new DREMCallable(), "ch.eiafr.cojac.perfs.opcodes.DREMCallable");
        bench(52, "DCMP Benchmark", new DCMPCallable(), "ch.eiafr.cojac.perfs.opcodes.IADDCallable");

        bench(1, "Rabin Karp", new StringSearchingRunnable(), "ch.eiafr.cojac.perfs.StringSearchingRunnable");
        bench(1, "Sweeping Plane", new SweepingSorterRunnable(), "ch.eiafr.cojac.perfs.SweepingSorterRunnable");
        bench(1, "Traveling Salesman", new TravelingSalesmanRunnable(), "ch.eiafr.cojac.perfs.TravelingSalesmanRunnable");

        BufferedImage image1 = ImageIO.read(COJACBenchmark.class.getResource("/images/matthew2.jpg"));
        benchWithImages(1, "FFT", new FFTRunnable(), "ch.eiafr.cojac.perfs.FFTRunnable", image1);

        BufferedImage image2 = ImageIO.read(COJACBenchmark.class.getResource("/images/alessandra.jpg"));
        benchWithImages(1, "Box Blur", new BoxBlurRunnable(), "ch.eiafr.cojac.perfs.BoxBlurRunnable", image2);

        bench(1, "Linpack", new LinpackRunnable(), "ch.eiafr.cojac.perfs.LinpackRunnable");
        bench(1, "SciMark FFT", new SciMarkFFTRunnable(), "ch.eiafr.cojac.perfs.SciMarkFFTRunnable");
        bench(1, "SciMark LU", new SciMarkLURunnable(), "ch.eiafr.cojac.perfs.SciMarkLURunnable");
        bench(1, "SciMark Monte Carlo", new SciMarkMonteCarloRunnable(), "ch.eiafr.cojac.perfs.SciMarkMonteCarloRunnable");
        bench(1, "SciMark SOR", new SciMarkSORRunnable(), "ch.eiafr.cojac.perfs.SciMarkSORRunnable");
        bench(1, "SciMark Sparse Mat Mult", new SciMarkSparseMatmultRunnable(), "ch.eiafr.cojac.perfs.SciMarkSparseMatmultRunnable");

        benchSum();
        benchSort();

        linpackBenchmark();
        sciMarkBenchmark();
    }

    private static void benchSum() throws Exception {
        int arraySize = 10000000;

        benchWithArrays(1, "Sum (Foreach)", new IntForeachSumCallable(), "ch.eiafr.cojac.perfs.IntForeachSumCallable", arraySize);
        benchWithArrays(1, "Sum (For)", new IntForSumCallable(), "ch.eiafr.cojac.perfs.IntForSumCallable", arraySize);
    }

    private static void benchSort() throws Exception {
        int arraySize = 1000000;

        benchWithArrays(1, "Sort (Java)", new JavaSortRunnable(), "ch.eiafr.cojac.perfs.JavaSortRunnable", arraySize);
        benchWithArrays(1, "Quicksort", new QuickSortRunnable(), "ch.eiafr.cojac.perfs.QuickSortRunnable", arraySize);
        benchWithArrays(1, "Shell Sort", new ShellSortRunnable(), "ch.eiafr.cojac.perfs.ShellSortRunnable", arraySize);
    }

    private static void linpackBenchmark() throws Exception {
        System.out.println("Start linpack not instrumented");
        new Linpack().run_benchmark();

        System.out.println("Start linpack instrumented");
        Object linpack = getFromClassLoader("ch.eiafr.cojac.perfs.Linpack", false);
        Method m = linpack.getClass().getMethod("run_benchmark");

        m.invoke(linpack);
    }

    private static void sciMarkBenchmark() throws Exception {
        System.out.println("Start SciMMark2 not instrumented");
        new SciMark().run(false);

        System.out.println("Start SciMMark2 instrumented");
        Object scimark = getFromClassLoader("ch.eiafr.cojac.perfs.scimark.SciMark", false);
        Method m = scimark.getClass().getMethod("run", Boolean.TYPE);

        m.invoke(scimark, false);
    }

    private static void setArray(int[] array, Object instrumented) throws Exception {
        Class<?> cls = instrumented.getClass();

        Method m = cls.getMethod("setArray", int[].class);

        m.invoke(instrumented, array);
    }

    private static void setImage(BufferedImage image, Object instrumented) throws Exception {
        Class<?> cls = instrumented.getClass();

        Method m = cls.getMethod("setImage", BufferedImage.class);

        m.invoke(instrumented, image);
    }

    private static int[] generateIntRandomArray(int size) {
        int[] array = new int[size];

        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000) - 500;
        }

        return array;
    }

    private static void bench(int actions, String title, Callable<?> runnable, String cls) throws Exception {
        Benchs benchs = new Benchs(title);
        initBench(actions, benchs);
        benchs.bench("Not instrumented", runnable);
        benchs.bench("Instrumented", COJACBenchmark.<Callable<?>>getFromClassLoader(cls, false));
        benchs.bench("WASTE_SIZE", COJACBenchmark.<Callable<?>>getFromClassLoader(cls, true));
        benchAgentVariant("Agent",            benchs, cls, false, false, -1, null);
        benchAgentVariant("Agent WASTE_SIZE", benchs, cls, true, false, -1, null);
        benchs.generateCharts(false);
        benchs.printResults();
    }

    private static void bench(int actions, String title, Runnable runnable, String cls) throws Exception {
        Benchs benchs = new Benchs(title);
        initBench(actions, benchs);
        benchs.bench("Not instrumented", runnable);
        benchs.bench("Instrumented", COJACBenchmark.<Runnable>getFromClassLoader(cls, false));
        benchs.bench("WASTE_SIZE", COJACBenchmark.<Runnable>getFromClassLoader(cls, true));
        benchAgentVariant("Agent",            benchs, cls, false, true, -1, null);
        benchAgentVariant("Agent WASTE_SIZE", benchs, cls, true, true, -1, null);
        benchs.generateCharts(false);
        benchs.printResults();
    }

    private static void benchWithArrays(int actions, String title, Runnable runnable, String cls, int size) throws Exception {
        Benchs benchs = new Benchs(title);
        initBench(actions, benchs);
        benchWithArrays1(benchs, "Not instrumented", runnable, size);
        benchWithArrays1(benchs, "Instrumented", (Runnable)getFromClassLoader(cls, false), size);
        benchWithArrays1(benchs, "WASTE_SIZE", (Runnable)getFromClassLoader(cls, true), size);
        benchAgentVariant("Agent", benchs, cls, false, true, size, null);
        benchAgentVariant("Agent WASTE_SIZE", benchs, cls, true, true, size, null);
        benchs.generateCharts(false);
        benchs.printResults();
    }
    
    private static void benchWithArrays1(Benchs benchs, String item, Runnable runnable, int size) throws Exception {
        setArray(generateIntRandomArray(size), runnable);
        benchs.bench(item, runnable);
    }

    private static void benchWithArrays(int actions, String title, Callable<?> runnable, String cls, int size) throws Exception {
        Benchs benchs = new Benchs(title);
        initBench(actions, benchs);
        benchWithArrays2(benchs, "Not instrumented", runnable, size);
        benchWithArrays2(benchs, "Instrumented", (Callable<?>)getFromClassLoader(cls, false), size);
        benchWithArrays2(benchs, "WASTE_SIZE", (Callable<?>)getFromClassLoader(cls, true), size);
        benchAgentVariant("Agent", benchs, cls, false, false, size, null);
        benchAgentVariant("Agent WASTE_SIZE", benchs, cls, true, false, size, null);
        benchs.generateCharts(false);
        benchs.printResults();
    }

    private static void benchWithArrays2(Benchs benchs, String item, Callable<?>  runnable, int size) throws Exception {
        setArray(generateIntRandomArray(size), runnable);
        benchs.bench(item, runnable);
    }

    private static void benchWithImages(int actions, String title, Runnable runnable, String cls, BufferedImage bufferedImage) throws Exception {
        Benchs benchs = new Benchs(title);
        initBench(actions, benchs);
        benchWithImages1(benchs, "Not instrumented", runnable, bufferedImage);
        benchWithImages1(benchs, "Instrumented", (Runnable)getFromClassLoader(cls, false), bufferedImage);
        benchWithImages1(benchs, "WASTE_SIZE", (Runnable)getFromClassLoader(cls, true), bufferedImage);
        benchAgentVariant("Agent", benchs, cls, false, true, -1, bufferedImage);
        benchAgentVariant("Agent WASTE_SIZE", benchs, cls, true, true, -1, bufferedImage);
        benchs.generateCharts(false);
        benchs.printResults();
    }

    private static void benchWithImages1(Benchs benchs, String item, Runnable runnable, BufferedImage bufferedImage) throws Exception {
        setImage(bufferedImage, runnable);
        benchs.bench(item, runnable);
    }

    private static void initBench(int actions, Benchs benchs) {
        //benchs.getParams().setNumberMeasurements(1);
        benchs.getParams().setNumberActions(actions);
        benchs.setExclusionFactor(1000D);
        benchs.setConsoleResults(false);
        benchs.setGraphDimension(800, 600);

        File graphFolder = new File(System.getProperty("user.dir"), "graphs");

        if (!((graphFolder.exists() && graphFolder.isDirectory()) || graphFolder.mkdirs())) {
            System.err.println("Unable to create the folder for the graph, use \"user.dir\" as graph folder");
            graphFolder = new File(System.getProperty("user.dir"));
        }

        benchs.setFolder(graphFolder.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFromClassLoader(String cls, boolean wasteSize) throws Exception {

        CojacReferencesBuilder builder = new CojacReferencesBuilder(getArgs(wasteSize));
        ClassLoader classLoader = new CojacClassLoader(new URL[0], builder);
        Class<?> instanceClass = classLoader.loadClass(cls);
        return (T) instanceClass.newInstance();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFromAgentClassLoader(String cls) throws Exception {
        Class<?> instanceClass = ClassLoader.getSystemClassLoader().loadClass(cls);
        AgentTest.instrumentation.retransformClasses(instanceClass);
        return (T) instanceClass.newInstance();
    }

    private static Agent benchAgent(boolean wasteSize) throws Exception {
        CojacReferencesBuilder builder = new CojacReferencesBuilder(getArgs(wasteSize));
        builder.setSplitter(new CojacReferences.AgentSplitter());
        Agent agent = new Agent(builder.build());
        AgentTest.instrumentation.addTransformer(agent, true);
        return agent;
    }

    private static void benchAgentVariant(String name, Benchs benchs, String cls,
             boolean wasteSize, 
             boolean runnable, int size, BufferedImage bufImg) throws Exception {
        Agent agent = benchAgent(wasteSize);
        Runnable run=null;
        Callable<?> callable = null;
        Object runnableOrCallable=null;
        if (runnable){
            runnableOrCallable = run = getFromAgentClassLoader(cls);   //COJACBenchmark.<Runnable> getFromAgentClassLoader(cls)
        } else {
            runnableOrCallable = callable = getFromAgentClassLoader(cls);
        }
        if (size>=0) 
            setArray(generateIntRandomArray(size), runnableOrCallable);
        if (bufImg != null) {
            setImage(bufImg, runnableOrCallable);
        }
        if (runnable) {
            benchs.bench(name, run);
        } else {
            benchs.bench(name, callable);
        }
        AgentTest.instrumentation.removeTransformer(agent);
        COJACBenchmark.<Callable<?>> getFromAgentClassLoader(cls); // gets back to an uninstrumented class definition
    }

    private static Args getArgs(boolean wasteSize) {
        Args args = new Args();
        args.specify(Arg.ALL);
        args.specify(Arg.PRINT);
        args.specify(Arg.FILTER);
        //args.specify(Arg.EXCEPTION);

        if (wasteSize) {
            args.specify(Arg.WASTE_SIZE);
        }

        return args;
    }
}