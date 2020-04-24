package com.wfb.mutithreadanalysis.service;

import com.wfb.mutithreadanalysis.mapper.FileMapper;
import com.wfb.mutithreadanalysis.model.ClassFile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileService {
    @Resource
    FileMapper fileMapper;

    public void add(ClassFile classFile) {
        fileMapper.add(classFile);
    }
}
