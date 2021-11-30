package com.zqf.customframwork.resources;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class CustomPackageResolver {

    private Set<String>  classNames = new HashSet<>();

    /**
     * 扫描包，获取全限定名
     * @param packageName 包名
     * @return
     */
    public Set<String>  getFullyQualifiedName(String packageName){
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String packagePath = path + packageName.replaceAll("\\.", "/");//  com/zqf/demo

        File file = new File(packagePath);
        File[] files = file.listFiles();
        if(file!=null){
            for (File childFile : files) {
                if(childFile.isDirectory()){
                    //如果还有子目录，则递归扫描
                    getFullyQualifiedName(packageName + "." + childFile.getName());
                }else{
                    String fileName = childFile.getName();
                    if (fileName.endsWith(".class") && !fileName.contains("$")) {
                        classNames.add(packageName + "." + fileName.replace(".class", ""));
                    }
                }
            }
        }
        return classNames;
    }
}
