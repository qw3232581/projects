package com.taotao.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Content;

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    @Value("${INDEX_AD1}")
    private String INDEX_AD1;

    @Value("${INDEX_ITEMAD_LIST}")
    private String INDEX_ITEMAD_LIST;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public String queryIndexAD1() {
        String url = TAOTAO_MANAGE_URL + INDEX_AD1;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
            List<Object> result = new ArrayList<Object>();
            for (Content content : (List<Content>) easyUIResult.getRows()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("srcB", content.getPic());
                map.put("height", 240);
                map.put("alt", content.getTitle());
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 550);
                map.put("href", content.getUrl());
                map.put("heightB", 240);

                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryIndexItemADList() {
        // 查询大家电，id=52
        // 查询大家电new，id=53
        try {
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            queryItemAdData(result, 52L);
            queryItemAdData(result, 53L);
            queryItemAdData(result, 54L);
            queryItemAdData(result, 57L);
            queryItemAdData(result, 58L);
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void queryItemAdData(Map<String, Object> result, Long cid) throws ClientProtocolException,
            IOException {
        String url = TAOTAO_MANAGE_URL + StringUtils.replace(INDEX_ITEMAD_LIST, "{cid}", String.valueOf(cid));
        String jsonData = this.apiService.doGet(url);
        if (StringUtils.isEmpty(jsonData)) {
            return;
        }
        EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
        List<Content> list = (List<Content>) easyUIResult.getRows();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        int index = 1;
        for (Content content : list) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();

            map.put("d", content.getPic());
            map.put("e", "6");
            map.put("c", content.getSubTitle());
            map.put("a", content.getTitleDesc());
            map.put("b", content.getTitle());
            map.put("f", 4);

            data.put(index + "", map);
            index++;
        }
        result.put("" + cid, data);
    }

    // public String queryIndexAD1() {
    // String url = TAOTAO_MANAGE_URL + INDEX_AD1;
    // try {
    // String jsonData = this.apiService.doGet(url);
    // if (StringUtils.isEmpty(jsonData)) {
    // return null;
    // }
    // JsonNode jsonNode = MAPPER.readTree(jsonData);
    // ArrayNode rows = (ArrayNode) jsonNode.get("rows");
    // List<Object> result = new ArrayList<Object>();
    // for (JsonNode row : rows) {
    // Map<String, Object> map = new LinkedHashMap<String, Object>();
    // map.put("srcB", row.get("pic").asText());
    // map.put("height", 240);
    // map.put("alt", row.get("title").asText());
    // map.put("width", 670);
    // map.put("src", map.get("srcB"));
    // map.put("widthB", 550);
    // map.put("href", row.get("url").asText());
    // map.put("heightB", 240);
    //
    // result.add(map);
    // }
    // return MAPPER.writeValueAsString(result);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

}
