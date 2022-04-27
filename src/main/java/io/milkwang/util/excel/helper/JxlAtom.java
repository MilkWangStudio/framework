package io.milkwang.util.excel.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JxlAtom {
    String name() default "未知";
    int order();
    AtomType type() default AtomType.NUMBER;
}
