package js.jst.worker;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class JSScanner {

    //通过包名扫描所有的类文件，并且获取类名
    public static Set<String> scanPackage(String packageName){
        String packageDirName = packageName.replace(".","/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageDirName);
        File packageFile = new File(url.getFile());
        if(!packageFile.exists()||!packageFile.isDirectory()){
            throw new RuntimeException("不存在或者不是文件夹");
        }
        Set<String> className = new HashSet<>();
        getAllClass(packageFile,packageName,className);
        return className;
    }

    private static void getAllClass(File parentFile,String packageName,Set<String> classNames){
        File[] files = parentFile.listFiles();
        for(File classFile:files){
            String path = classFile.getPath();
            if(classFile.isFile()){
                if(path.endsWith(".class")){
                    classNames.add(packageName+"."+path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf(".")));
                }else{
                    packageName = packageName + path.substring(path.lastIndexOf("\\")+1);
                    getAllClass(classFile,packageName,classNames);
                }
            }
        }
    }
}
