package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据父节点id查询类目列表
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategoryListByParentId(
            @RequestParam(value = "id", defaultValue = "0") Long pid) {
        try {
            ContentCategory record = new ContentCategory();
            record.setParentId(pid);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
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
     * 新增节点
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
        try {
            contentCategory.setStatus(1);
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            this.contentCategoryService.save(contentCategory);

            // 判断该节点的父节点的isParent是否为true，如为false，修改为true
            ContentCategory parent = this.contentCategoryService.queryById(contentCategory.getParentId());
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                this.contentCategoryService.update(parent);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(contentCategory);
    }

    /**
     * 重命名
     * 
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(@RequestParam("id") Long id, @RequestParam("name") String name) {
        try {
            ContentCategory record = new ContentCategory();
            record.setId(id);
            record.setName(name);
            this.contentCategoryService.updateSelective(record);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 删除节点以及所有的子节点
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory) {
        try {
            List<Object> ids = new ArrayList<Object>();
            ids.add(contentCategory.getId());

            // 查询所有的子节点，使用递归完成
            findAllSubNode(ids, contentCategory.getId());

            this.contentCategoryService.deleteByIds(ContentCategory.class, "id", ids);

            // 判断该节点是否还存在其他的兄弟节点，如果不存在，修改父节点的isParent为false
            ContentCategory record = new ContentCategory();
            record.setParentId(contentCategory.getParentId());
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (CollectionUtils.isEmpty(list)) {
                // 没有兄弟节点，修改父节点的isParent为false
                ContentCategory parent = new ContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                this.contentCategoryService.updateSelective(parent);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void findAllSubNode(List<Object> ids, Long pid) {
        ContentCategory record = new ContentCategory();
        record.setParentId(pid);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
        if (CollectionUtils.isNotEmpty(list)) {
            for (ContentCategory contentCategory : list) {
                ids.add(contentCategory.getId());
                // 判断该节点是否父节点
                if (contentCategory.getIsParent()) {
                    // 开始递归
                    findAllSubNode(ids, contentCategory.getId());
                }
            }
        }
    }

}
