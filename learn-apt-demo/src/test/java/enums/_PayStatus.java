package enums;

import enumTool.CodeWithValue;
import enumTool.EnumCodeValue;

@EnumCodeValue({
        @CodeWithValue(name = "INIT", code = 1, value = "默认"),
        @CodeWithValue(name = "SUCCESS", code = 2, value = "成功"),
        @CodeWithValue(name = "FAILURE", code = 3, value = "失败"),
        @CodeWithValue(name = "REFUND", code = 4, value = "退款"),
})
public class _PayStatus {
}
