package springel;

import java.util.List;

/**
 * Created by landy on 2018/7/5.
 */
public class SpelTestInnerClass {
    private int age;
    private String name="innerClass";

    public SpelTestInnerClass() {
        this.age = 29;
    }

    public SpelTestInnerClass(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public boolean isGt30ForAge() {
        return this.age > 30;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
