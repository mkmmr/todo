package com.raisetech.todo.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRestApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void タスクを全件取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("[" +
                "    {" +
                "        \"id\": 1," +
                "        \"done\": false," +
                "        \"task\": \"Readの実装\"," +
                "        \"limitDate\": \"2022-08-10\"" +
                "    }," +
                "    {" +
                "        \"id\": 2," +
                "        \"done\": false," +
                "        \"task\": \"Createの実装\"," +
                "        \"limitDate\": \"2022-08-20\"" +
                "    }," +
                "    {" +
                "        \"id\": 3," +
                "        \"done\": false," +
                "        \"task\": \"Deleteの実装\"," +
                "        \"limitDate\": \"2022-08-30\"" +
                "    }" +
                "]", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void 特定のタスクを１件取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/todos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{" +
                "    \"id\": 1," +
                "    \"done\": false," +
                "    \"task\": \"Readの実装\"," +
                "    \"limitDate\": \"2022-08-10\"" +
                "}", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void 新規作成できること() throws Exception{
        String response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"task\": \"Updateの実装\"," +
                                "    \"limitDate\": \"2022-08-25\"" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().string("Location", "/todos/4"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{" +
                "    \"id\": 4," +
                "    \"done\": false," +
                "    \"task\": \"Updateの実装\"," +
                "    \"limitDate\": \"2022-08-25\"" +
                "}", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void nullをPOSTした時にBadRequestが返ってくること() throws Exception{
        String response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/todos")
                        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String expected = "{" +
                "    \"error\": \"Bad Request\"," +
                "    \"status\": \"400\"," +
                "    \"timestamp\": \"\"," +
                "    \"message\": {" +
                "        \"task\": \"must not be blank\"," +
                "        \"limitDate\": \"must not be null\"" +
                "    }" +
                "}";

        JSONAssert.assertEquals(expected, response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", ((o1, o2) -> true))));
    }

    @Test
    @DataSet(value = "datasets/to_do_list.yml")
    @Transactional
    void limitDateに有効な型以外をPOSTした時にBadRequestが返ってくること() throws Exception{
        String response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/todos")
                        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                        .content("{" +
                                "    \"task\": \"Updateの実装\"," +
                                "    \"limitDate\": \"2022-8-25\"" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String expected = "{" +
                "    \"error\": \"Bad Request\"," +
                "    \"status\": \"400\"," +
                "    \"timestamp\": \"\"," +
                "    \"message\": \"2022-8-25は有効ではありません。limitDateは'yyyy-MM-dd'形式で入力してください。\"" +
                "}";

        JSONAssert.assertEquals(expected, response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", ((o1, o2) -> true))));
    }
}
