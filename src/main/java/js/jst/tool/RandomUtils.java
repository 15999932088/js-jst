package js.jst.tool;

import java.util.Random;

public class RandomUtils {

    public static Integer getRandom(Integer randomSize){
        if(randomSize<0){
            randomSize = 0;
        }
        return new Random().nextInt(randomSize);
    }
}
