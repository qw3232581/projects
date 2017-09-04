package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据父节点id查询类目列表
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(
            @RequestParam(value = "id", defaultValue = "0") Long pid) {
        try {
            ItemCat record = new ItemCat();
            record.setParentId(pid);
            List<ItemCat> list = this.itemCatService.queryListByWhere(record);
            if (null == list || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据id查询商品类目数据
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<ItemCat> queryById(@PathVariable("id") Long id) {
        try {
            ItemCat itemCat = this.itemCatService.queryById(id);
            if (null == itemCat) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
