import com.wfb.net.PetriNet;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String CALFUZZER_PATH = "/home/wfb/毕设/calfuzzer/";
    public static void main(String[] args) throws Exception{
//        String className = "wfb.testcases.TestWait1";

//        try {
//            Runtime runtime = Runtime.getRuntime();
//            Process p = null;
//            String ant = "ant -f " + CALFUZZER_PATH + "run.xml -DclassName=" + className;
//            p = runtime.exec(ant);
//            boolean isExit = p.waitFor(100, TimeUnit.SECONDS);
//            if (!isExit) {
//                System.err.println("无法我在100s内完成解析");
//                return;
//            }
//            Scanner sc = new Scanner(p.getErrorStream());
//            while (sc.hasNextLine()) {
//                System.err.println(sc.nextLine());
//            }
//            sc.close();
//            Scanner sc2 = new Scanner(p.getInputStream());
//            while (sc2.hasNextLine()) {
//                System.out.println(sc2.nextLine());
//            }
//            sc2.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String name = "/home/wfb/毕设/calfuzzer/source/MyTest.obj";
//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name));
//        Object object = ois.readObject();
//        PetriNet petriNet = (PetriNet) object;
//        System.out.println(petriNet);

        String name = "/home/wfb/毕设/calfuzzer/src/benchmarks/iidToLine.map";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name));
        Object object = ois.readObject();
        System.out.println(object);
    }
}