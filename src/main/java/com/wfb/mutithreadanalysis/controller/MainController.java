package com.wfb.mutithreadanalysis.controller;

import com.wfb.analysis.ConflictAnalysis;
import com.wfb.base.TransitionNode;
import com.wfb.mutithreadanalysis.model.ClassFile;
import com.wfb.mutithreadanalysis.service.MainService;
import com.wfb.mutithreadanalysis.util.Paramters;
import com.wfb.net.PetriNet;
import com.wfb.utils.PetriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    @RequestMapping("/data/{className}")
    public Map<Long, List<TransitionNode>> getVarTransitionNodes(@PathVariable("className") String cur, HttpSession session) {
        return getPetriNet(cur, session).varTransitionNodes;
    }

    @Autowired
    MainService mainService;

    @RequestMapping("/getChildren")
    public List<ClassFile> getChildren() {
        return mainService.getChildren();
    }

    @RequestMapping("/getVar")
    public Collection<Long> getVar(String cur, HttpSession session) {
        PetriNet petriNet = getPetriNet(cur, session);
        System.out.println(cur + ":" + petriNet);
        return mainService.getVar(petriNet);
    }

    @RequestMapping("/getRead")
    public Collection<String> getRead(String cur, long variable, HttpSession session) {
        PetriNet petriNet = getPetriNet(cur, session);
        return mainService.getRead(variable, petriNet);
    }

    @RequestMapping("/getWrite")
    public Collection<String> getWrite(String cur, long variable, HttpSession session) {
        PetriNet petriNet = getPetriNet(cur, session);
        return mainService.getWrite(variable, petriNet);
    }

    @RequestMapping("/getReadLine")
    public Collection<Integer> getReadLine(String cur, long variable, HttpSession session) {
        PetriNet petriNet = getPetriNet(cur, session);
        return mainService.getReadLine(variable, petriNet);
    }

    @RequestMapping("/getWriteLine")
    public Collection<Integer> getWriteLine(String cur, long variable, HttpSession session) {
        PetriNet petriNet = getPetriNet(cur, session);
        return mainService.getWriteLine(variable, petriNet);
    }

    @RequestMapping("/isConcurrent")
    public boolean isConcurrent(String cur, String t1, String t2, HttpSession session) {
        ConflictAnalysis conflictAnalysis = getConflictAnalysis(cur, session);
        return mainService.isConcurrent(conflictAnalysis, t1, t2);
    }

    @RequestMapping("/isConcurrent2")
    public boolean isConcurrent2(String cur, int l1, int l2, HttpSession session) {
        ConflictAnalysis conflictAnalysis = getConflictAnalysis(cur, session);
        PetriNet petriNet = getPetriNet(cur, session);
        Collection<TransitionNode> read = mainService.getTransitionByLine(l1, petriNet);
        Collection<TransitionNode> write = mainService.getTransitionByLine(l2, petriNet);
        for (TransitionNode t1: read) {
            for (TransitionNode t2: write) {
                if (t1 == t2) continue;
                if (mainService.isConcurrent(conflictAnalysis, t1.getName(), t2.getName())) return true;
            }
        }
        return false;
    }

    private ConflictAnalysis getConflictAnalysis(String cur, HttpSession session) {
        Object conflict = session.getAttribute(cur+"conflict");
        if (conflict == null) {
            try {
                PetriNet petriNet = getPetriNet(cur, session);
                ConflictAnalysis conflictAnalysis = new ConflictAnalysis(Paramters.CALFUZZER_PATH + "source/" + cur + ".xml",
                        petriNet);
                session.setAttribute(cur+"conflict", conflictAnalysis);
                return conflictAnalysis;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (ConflictAnalysis) conflict;
    }

    private PetriNet getPetriNet(String cur, HttpSession session) {
        Object o = session.getAttribute(cur);
        if (o == null) {
            try {
                o = PetriUtil.readPetriNet(Paramters.CALFUZZER_PATH + "source/" + cur + ".obj");
                session.setAttribute(cur, o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (PetriNet) o;
    }
}
