package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {

    // @Autowired
    // private ItemCatMapper itemCatMapper;

    // public List<ItemCat> queryItemCatListByParentId(Long pid) {
    // ItemCat record = new ItemCat();
    // record.setParentId(pid);
    // return this.itemCatMapper.select(record);
    // }

    // @Override
    // public Mapper<ItemCat> getMapper() {
    // return this.itemCatMapper;
    // }

    @Autowired
    private RedisService redisService;

    private static final Integer REDIS_TIME = 60 * 60 * 24 * 30 * 6;

    private static final String REDIS_KEY = "TAOTAO_MANAGE_ITEM_CAT_ALL"; // 项目名_模块名_业务名

    /**
     * 查询数据库中的所有类目数据，按照前端所要求的结构进行封装
     * 
     * @return
     */
    public ItemCatResult queryItemCatAll() {

        try {
            // 从缓存中命中，如果命中则返回
            ItemCatResult itemCatResult = this.redisService.get(REDIS_KEY, ItemCatResult.class);
            if (itemCatResult != null) {
                // 命中
                return itemCatResult;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setName(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }

        try {
            this.redisService.set(REDIS_KEY, result, REDIS_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
