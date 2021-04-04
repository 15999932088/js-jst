package js.jst.interfaces;

import js.jst.worker.JSWorker;
import js.jst.worker.JSWorkerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扫描package
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(JSWorkerRegistrar.class)
public @interface JTScan {

    String[] value();
}
