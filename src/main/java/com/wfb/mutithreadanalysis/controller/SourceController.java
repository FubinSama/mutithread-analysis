package com.wfb.mutithreadanalysis.controller;

import com.wfb.mutithreadanalysis.util.Paramters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Scanner;

@Controller
public class SourceController {
    @GetMapping("/src/{path}")
    public void generateHtml(@PathVariable("path") String path, HttpServletResponse response) {
        try (PrintWriter pw = response.getWriter()) {
            String calfuzzerPath = Paramters.CALFUZZER_PATH;
            File file = new File(calfuzzerPath + "source/" + path);
            try (Scanner sc = new Scanner(file)){
                while (sc.hasNextLine()) {
                    pw.println(sc.nextLine());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/srcCode/{path}")
    public void generateSrc(@PathVariable("path") String path, HttpServletResponse response) {
        try (PrintWriter pw = response.getWriter()) {
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("    <meta charset=\"UTF-8\">");
            pw.println("    <link rel=\"stylesheet\" href=\"/css/code.css\" />");
            pw.println("    <script type=\"text/javascript\" src=\"/js/code.js\"></script>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<pre class=\"sourceCode\">");
            String calfuzzerPath = Paramters.CALFUZZER_PATH;
            File file = new File(calfuzzerPath + "test/" + path.replaceAll("_", "/") + ".java");
            try(Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    pw.println(sc.nextLine().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                }
            }
            pw.println("</pre>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
