package com.baba.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车对象类，里面包括用户选择的多个库存商品，以及结算等相关信息
 * @author Jamayette
 *         Created on  2017/8/21
 */
public class Cart implements Serializable{

    // 购物项的集合
    private List<Item> items = new ArrayList<Item>();

    //自定义添加item到cart中方法，如果item的skuId（id）一样，则item的购买数量加上amount
    public void addItem(Item item) {
        for (Item item2 : items) {
            if (item.getSkuId().equals(item2.getSkuId())) {
                item2.setAmount(item2.getAmount() + item.getAmount());
                return;
            }
        }
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    //获得商品总数量
    @JsonIgnore
    public Integer getProductAmount(){
        Integer result = 0;
        for (Item item : items){
            result = result + item.getAmount();
        }
        return result;
    }

    //获得商品总金额
    @JsonIgnore
    public Float getProductPrice() {
        Float result = 0f;

        for (Item item : items) {
            result = result + item.getAmount()
                    * Float.parseFloat(item.getSku().get("skuPrice").toString());
        }
        return result;
    }

    //计算运费
    @JsonIgnore
    public Float getFee() {
        Float result = 0f;
        // 如果商品总金额小于79就收10
        if (this.getProductPrice() < 79) {
            result = 10f;
        }
        return result;
    }

    //计算总价格
    @JsonIgnore
    public Float getTotalPrice() {
        return this.getProductPrice() + this.getFee();
    }

}
