package com.manil.dailywise.scenario;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manil.dailywise.repository.*;
import com.manil.dailywise.util.TestUtil;
import com.manil.dailywise.dto.CreateTokenReqDTO;
import com.manil.dailywise.dto.common.BaseResponse;
import com.manil.dailywise.dto.common.SingleItemResponse;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public abstract class BaseTestController {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    @Transactional
    public void init() {
        userRepository.deleteAll();
    }

    @BeforeAll
    static void setup () {

    }

    @Test
    public void garbage() {

    }

    protected String generateEmailFromIndex(int i) {
        return i+"@"+i+".com";
    }

    protected HashMap<String, String> createUser(int numberOfUser) {
        HashMap<String, String> userTokens = new HashMap<>();
        IntStream.range(1, numberOfUser+1)
                .forEach(i -> {
                    try {
                        CreateTokenReqDTO vo = new CreateTokenReqDTO();
                        vo.setEmail(generateEmailFromIndex(i));
                        ObjectMapper mapper = new ObjectMapper();
                        String body = TestUtil.convertObjectToString(vo);
                        MvcResult result = mockMvc.perform(
                                MockMvcRequestBuilders.post("/token")
                                        .content(body)
                                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();;
                        String token = result.getResponse().getContentAsString();
                        userTokens.put(generateEmailFromIndex(i), token);

                    } catch (Exception e) {

                    }
                });
        return userTokens;
    }

    protected int getResponseCode(MvcResult result, Class<? extends BaseResponse> myClass) throws Exception{
        String response = result.getResponse().getContentAsString();
        BaseResponse errorResponse = TestUtil.convertStringToBaseResponse(response, myClass);
        return errorResponse.getErrorCode();
    }

    protected <T> SingleItemResponse<T> getSingleItemResponse(MvcResult result, TypeReference<SingleItemResponse<T>> myClass) throws Exception{
        String response = result.getResponse().getContentAsString();
        return TestUtil.convertStringToSingleItem(response, myClass);
    }

    protected MvcResult executePost(String url, String body, String token, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .header("Authorization", "Bearer " + token)
                .content(body==null ? "": body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        if(params != null) {
            params.keySet().stream()
                    .forEach(key -> {
                        builder.queryParam(key, params.get(key));
                    });
        }

        MvcResult result = mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result;
    }

    protected MvcResult executeMultiPart(
            String url,
            Map<String, String> formData,
            String token,
            Map<String, String> params) throws Exception{
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(url);

        formData.keySet().stream()
                .forEach(key -> {
                    MockMultipartFile file = new MockMultipartFile(key,
                            "",
                            "text/plain",
                            formData.get(key).getBytes());
                    builder.file(file);
                });

        builder.header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        if(params != null) {
            params.keySet().stream()
                    .forEach(key -> {
                        builder.queryParam(key, params.get(key));
                    });
        }

        MvcResult result = mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result;
    }

    protected MvcResult executePatchMultiPart(
            String url,
            Map<String, String> formData,
            String token,
            Map<String, String> params) throws Exception{
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(url);

        formData.keySet().stream()
                .forEach(key -> {
                    MockMultipartFile file = new MockMultipartFile(key,
                            "",
                            "text/plain",
                            formData.get(key).getBytes());
                    builder.file(file);
                });

        builder.header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        if(params != null) {
            params.keySet().stream()
                    .forEach(key -> {
                        builder.queryParam(key, params.get(key));
                    });
        }

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        MvcResult result = mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result;
    }

    protected MvcResult executePatch(String url, String body, String token, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch(url)
                .header("Authorization", "Bearer " + token)
                .content(body==null ? "": body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        if(params != null) {
            params.keySet().stream()
                    .forEach(key -> {
                        builder.queryParam(key, params.get(key));
                    });
        }

        MvcResult result = mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result;
    }

    protected MvcResult executeGet(String url, String token, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        if(params != null) {
            params.keySet().stream()
                    .forEach(key -> {
                        builder.queryParam(key, params.get(key));
                    });
        }

        MvcResult result = mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result;
    }

    protected MvcResult executeDelete(String url, String token, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        if(params != null) {
            params.keySet().stream()
                    .forEach(key -> {
                        builder.queryParam(key, params.get(key));
                    });
        }

        MvcResult result = mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result;
    }
}
