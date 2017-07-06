package com.github.landyking.learnJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.List;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/6 16:39
 * note:
 */
public class GsonTest {
    @Test
    public void test1() throws Exception {
        String text = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"lakemob.getRegulationList\",\n" +
                "    \"result\": {\n" +
                "        \"error\": null,\n" +
                "        \"resultStatus\": \"1\",\n" +
                "        \"info\": {\n" +
                "            \"page\": 1,\n" +
                "            \"pageSize\": 20,\n" +
                "            \"totalPage\": 10,\n" +
                "            \"timestamp\": 1499329339193,\n" +
                "            \"dataList\": [\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808080\",\n" +
                "                    \"title\": \"我是一个好孩子0\",\n" +
                "                    \"fileName\": \"testaa0.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808081\",\n" +
                "                    \"title\": \"我是一个好孩子1\",\n" +
                "                    \"fileName\": \"testaa1.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808082\",\n" +
                "                    \"title\": \"我是一个好孩子2\",\n" +
                "                    \"fileName\": \"testaa2.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808083\",\n" +
                "                    \"title\": \"我是一个好孩子3\",\n" +
                "                    \"fileName\": \"testaa3.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808084\",\n" +
                "                    \"title\": \"我是一个好孩子4\",\n" +
                "                    \"fileName\": \"testaa4.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808085\",\n" +
                "                    \"title\": \"我是一个好孩子5\",\n" +
                "                    \"fileName\": \"testaa5.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808086\",\n" +
                "                    \"title\": \"我是一个好孩子6\",\n" +
                "                    \"fileName\": \"testaa6.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808087\",\n" +
                "                    \"title\": \"我是一个好孩子7\",\n" +
                "                    \"fileName\": \"testaa7.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808088\",\n" +
                "                    \"title\": \"我是一个好孩子8\",\n" +
                "                    \"fileName\": \"testaa8.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd8880808089\",\n" +
                "                    \"title\": \"我是一个好孩子9\",\n" +
                "                    \"fileName\": \"testaa9.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080810\",\n" +
                "                    \"title\": \"我是一个好孩子10\",\n" +
                "                    \"fileName\": \"testaa10.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080811\",\n" +
                "                    \"title\": \"我是一个好孩子11\",\n" +
                "                    \"fileName\": \"testaa11.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080812\",\n" +
                "                    \"title\": \"我是一个好孩子12\",\n" +
                "                    \"fileName\": \"testaa12.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080813\",\n" +
                "                    \"title\": \"我是一个好孩子13\",\n" +
                "                    \"fileName\": \"testaa13.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080814\",\n" +
                "                    \"title\": \"我是一个好孩子14\",\n" +
                "                    \"fileName\": \"testaa14.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080815\",\n" +
                "                    \"title\": \"我是一个好孩子15\",\n" +
                "                    \"fileName\": \"testaa15.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080816\",\n" +
                "                    \"title\": \"我是一个好孩子16\",\n" +
                "                    \"fileName\": \"testaa16.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080817\",\n" +
                "                    \"title\": \"我是一个好孩子17\",\n" +
                "                    \"fileName\": \"testaa17.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080818\",\n" +
                "                    \"title\": \"我是一个好孩子18\",\n" +
                "                    \"fileName\": \"testaa18.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fsd88808080819\",\n" +
                "                    \"title\": \"我是一个好孩子19\",\n" +
                "                    \"fileName\": \"testaa19.pdf\",\n" +
                "                    \"fileSize\": 1230234,\n" +
                "                    \"downloadUrl\": \"http://fileserver:8080/getfile/aasdfs\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";
        RootBean<MyBean<ItemBean>> root = new Gson().fromJson(text, new TypeToken<RootBean<MyBean<ItemBean>>>() {
        }.getType());
        System.out.println(root.getResult().getInfo().getClass());
        System.out.println(root.getResult().getInfo().getDataList().get(0).getClass());
    }
    @Test
    public void test2() throws Exception {
        String text = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"lakemob.getRegulationList\",\n" +
                "    \"result\": {\n" +
                "        \"error\": null,\n" +
                "        \"resultStatus\": \"1\",\n" +
                "        \"info\": {\n" +
                "            \n" +
                "        }\n" +
                "    }\n" +
                "}";
        RootBean<MyBean<ItemBean>> root = new Gson().fromJson(text, new TypeToken<RootBean<MyBean<ItemBean>>>() {
        }.getType());
        System.out.println(root.getResult().getInfo().getClass());
        System.out.println(root.getResult().getInfo().getDataList());
    }
}
class ItemBean {
    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
class ResultBean<T> {
    private String error;
    private String resultStatus;
    private T info;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}
class RootBean<T> {
    private String jsonrpc;
    private String method;
    private ResultBean<T> result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ResultBean<T> getResult() {
        return result;
    }

    public void setResult(ResultBean<T> result) {
        this.result = result;
    }
}
class MyBean<T> {
    private int page;
    private int pageSize;
    private int totalPage;
    private List<T> dataList;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
