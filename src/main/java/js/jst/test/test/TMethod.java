package js.jst.test.test;

import com.alibaba.fastjson.JSON;
import js.jst.interfaces.JCTest;
import js.jst.test.param.BannerEntity;
import js.jst.test.param.JTParam;
import js.jst.test.param.UserEntity;

public class TMethod {

//    @JCTest
//    public void test(String test){
//        System.out.println(test);
//    }

    @JCTest(entity = JTParam.class)
    public void test2(UserEntity param){
        System.out.println("******************");
        System.out.println(JSON.toJSONString(param));
    }

//    @JCTest(entity = BannerEntity.class)
//    public void test3(BannerEntity bannerEntity){
//        System.out.println("111111111111111");
//        System.out.println(JSON.toJSONString(bannerEntity));
//    }
}
