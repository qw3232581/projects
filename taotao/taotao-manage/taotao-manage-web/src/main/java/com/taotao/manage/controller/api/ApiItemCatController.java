package com.taotao.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对外提供接口服务，查询所有的类目数据
     * 
     * @return
     */
    // @RequestMapping(method = RequestMethod.GET)
    // public ResponseEntity<String> queryItemCatAll(
    // @RequestParam(value = "callback", required = false) String callback) {
    // try {
    // ItemCatResult itemCatResult = this.itemCatService.queryItemCatAll();
    // if (null == itemCatResult) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    // }
    // String json = MAPPER.writeValueAsString(itemCatResult);
    // if (StringUtils.isNotEmpty(callback)) {
    // json = callback + "(" + json + ")";
    // }
    // return ResponseEntity.ok(json);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }

    /**
     * 对外提供接口服务，查询所有的类目数据
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCatAll(
            @RequestParam(value = "callback", required = false) String callback) {
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryItemCatAll();
            if (null == itemCatResult) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
