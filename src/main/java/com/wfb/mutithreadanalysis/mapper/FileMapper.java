package com.wfb.mutithreadanalysis.mapper;

import com.wfb.mutithreadanalysis.model.ClassFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    List<ClassFile> getClassFile();
    void add(ClassFile classFile);
}
