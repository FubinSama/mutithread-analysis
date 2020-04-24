import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String CALFUZZER_PATH = "/home/wfb/毕设/calfuzzer/";
    public static void main(String[] args) {
        String className = "wfb.testcases.Test";

        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = null;
            String ant = "ant -f " + CALFUZZER_PATH + "run.xml -DclassName=" + className;
            p = runtime.exec(ant);
            boolean isExit = p.waitFor(100, TimeUnit.SECONDS);
            if (!isExit) {
                System.err.println("无法我在100s内完成解析");
                return;
            }
            Scanner sc = new Scanner(p.getErrorStream());
            while (sc.hasNextLine()) {
                System.err.println(sc.nextLine());
            }
            sc.close();
            Scanner sc2 = new Scanner(p.getInputStream());
            while (sc2.hasNextLine()) {
                System.out.println(sc2.nextLine());
            }
            sc2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}