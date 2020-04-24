package com.wfb.mutithreadanalysis.service;

import com.wfb.analysis.ConflictAnalysis;
import com.wfb.base.TransitionNode;
import com.wfb.mutithreadanalysis.mapper.FileMapper;
import com.wfb.mutithreadanalysis.model.ClassFile;
import com.wfb.net.PetriNet;
import com.wfb.transition.WriteTransitionNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainService {
    @Resource
    FileMapper fileMapper;

    public List<ClassFile> getChildren(){
        return fileMapper.getClassFile();
    }

    public Collection<Long> getVar(PetriNet petriNet) {
        return petriNet.varTransitionNodes.keySet();
    }

    public Collection<String> getRead(long var, PetriNet petriNet) {
        return petriNet.varTransitionNodes.get(var).stream().map(t -> t.getName()).collect(Collectors.toSet());
    }

    public Collection<String> getWrite(long var, PetriNet petriNet) {
        return petriNet.varTransitionNodes.get(var).stream().filter(t -> t instanceof WriteTransitionNode).map(t -> t.getName()).collect(Collectors.toSet());
    }

    public Collection<Integer> getReadLine(long var, PetriNet petriNet) {
        return petriNet.varTransitionNodes.get(var).stream().map(t -> t.getLineNumber()).collect(Collectors.toSet());
    }

    public Collection<Integer> getWriteLine(long var, PetriNet petriNet) {
        return petriNet.varTransitionNodes.get(var).stream().filter(t -> t instanceof WriteTransitionNode).map(t -> t.getLineNumber()).collect(Collectors.toSet());
    }

    public Collection<TransitionNode> getTransitionByLine(int i, PetriNet petriNet) {
        return petriNet.varTransitionNodes.values().stream().flatMap(l->l.stream()).filter(t -> t.getLineNumber() == i).collect(Collectors.toSet());
    }

    public Collection<TransitionNode> getWriteTransition(Collection<TransitionNode> collection) {
        return collection.stream().filter(t -> t instanceof WriteTransitionNode).collect(Collectors.toSet());
    }

    public boolean isConcurrent(ConflictAnalysis conflictAnalysis, String t1, String t2) {
        if (t1 == null || t2 == null) return false;
        return conflictAnalysis.isConcurrency(t1, t2);
    }
}
