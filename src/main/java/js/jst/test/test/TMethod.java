package js.jst.test.test;

import js.jst.interfaces.JCTest;
import js.jst.test.param.JTParam;

public class TMethod {

//    @JCTest
//    public void test(String test){
//        System.out.println(test);
//    }

    @JCTest(entity = JTParam.class)
    public void test2(JTParam param){
        System.out.println("******************");
        System.out.println(param.getParam1());
        System.out.println(param.getParam2());
    }
}
