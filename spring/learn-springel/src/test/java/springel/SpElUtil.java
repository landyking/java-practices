package springel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by landy on 2018/7/5.
 */
public class SpElUtil {
    private String name;
    private SpelTestInnerClass innerClass;
    private Date2 time;
    private Map<String, Object> maps = new HashMap<String, Object>();
    private List<Integer> numbers;

    public SpElUtil() {
        maps.put("key", Arrays.asList(1, 3, 4));
        maps.put("1", "string1");
        this.time = new Date2(111, 1, 2);
        innerClass = new SpelTestInnerClass();
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> integers) {
        this.numbers=integers;
    }

    public SpelTestInnerClass getInnerClass() {
        return innerClass;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, Object> maps) {
        this.maps = maps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTime(Date2 time) {
        this.time = time;
    }

    public Date2 getTime() {
        return time;
    }

    public static int len(String name) {
        return name.length();
    }

    public void setInnerClass(SpelTestInnerClass innerClass) {
        this.innerClass = innerClass;
    }
}
