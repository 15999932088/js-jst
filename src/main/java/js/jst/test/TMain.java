package js.jst.test;

import js.jst.interfaces.JTScan;
import js.jst.worker.JSScanner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan("js.jst.test")
@JTScan("js.jst.test.test")
public class TMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TMain.class);
    }
}
