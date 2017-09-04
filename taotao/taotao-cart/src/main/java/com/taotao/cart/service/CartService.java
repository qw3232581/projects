package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.sso.bean.User;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId) {
        // 判断该商品是否存在购物车中，如果存在，数量相加，不如不存在直接添加
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if (null == cart) {
            // 不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setNum(1); // TODO 暂时默认为1
            cart.setUserId(user.getId());

            Item item = this.itemService.queryItemById(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            String[] images = StringUtils.split(item.getImage(), ',');
            if (images != null && images.length > 0) {
                cart.setItemImage(images[0]);
            }
            this.cartMapper.insert(cart);
        } else {
            // 存在
            cart.setNum(cart.getNum() + 1); // TODO 先默认为1，后续实现
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    /**
     * 根据创建时间倒序排序
     * 
     * @return
     */
    public List<Cart> queryCartList() {
        User user = UserThreadLocal.get();
        return this.queryCartList(user.getId());
    }

    /**
     * 根据创建时间倒序排序
     * 
     * @return
     */
    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");

        example.createCriteria().andEqualTo("userId", userId);

        return this.cartMapper.selectByExample(example);
    }

    public void updateNum(Long itemId, Integer num) {
        // 更新的数据
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());

        // 更新数据的条件
        User user = UserThreadLocal.get();
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", user.getId()).andEqualTo("itemId", itemId);

        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void deleteItem(Long itemId) {
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());

        this.cartMapper.delete(record);
    }

}
