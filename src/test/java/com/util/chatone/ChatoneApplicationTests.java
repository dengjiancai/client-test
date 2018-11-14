package com.util.chatone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class ChatoneApplicationTests {

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        //mvc = MockMvcBuilders.standaloneSetup(new HController()).build();
        mvc = MockMvcBuilders.standaloneSetup(new UserController()).build();//切换至不同控制类
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello World")));
    }

    @Test
    public void testUserController() throws Exception {
        RequestBuilder requestBuilder = null;

        requestBuilder = MockMvcRequestBuilders.get("/users/");
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
        // 2、post提交一个user
        requestBuilder = MockMvcRequestBuilders.post("/users/")
                .param("id", "1")
                .param("name", "测试大师")
                .param("age", "20");
        mvc.perform(requestBuilder)
                .andExpect(content().string(equalTo("success")));

        // 3、get获取user列表，应该有刚才插入的数据
        requestBuilder = MockMvcRequestBuilders.get("/users/");
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"测试大师\",\"age\":20}]")));

        // 4、put修改id为1的user
        requestBuilder = MockMvcRequestBuilders.put("/users/1")
                .param("name", "测试终极大师")
                .param("age", "30");
        mvc.perform(requestBuilder)
                .andExpect(content().string(equalTo("success")));

        // 5、get一个id为1的user
        requestBuilder = MockMvcRequestBuilders.get("/users/1");
        mvc.perform(requestBuilder)
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"测试终极大师\",\"age\":30}")));

        // 6、del删除id为1的user
        requestBuilder = MockMvcRequestBuilders.delete("/users/1");
        mvc.perform(requestBuilder)
                .andExpect(content().string(equalTo("success")));

        // 7、get查一下user列表，应该为空
        requestBuilder = MockMvcRequestBuilders.get("/users/");
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    }

}
