package enumTool;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({})
public @interface CodeWithValue {
    String name();

    int code();

    String value();
}