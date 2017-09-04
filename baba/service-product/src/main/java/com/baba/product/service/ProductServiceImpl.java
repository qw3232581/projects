package com.baba.product.service;

import com.baba.dao.ColorDAO;
import com.baba.dao.ProductDAO;
import com.baba.dao.SKUDAO;
import com.baba.pojo.Color;
import com.baba.pojo.Product;
import com.baba.pojo.Sku;
import com.baba.pojo.SuperPojo;
import com.baba.service.ProductService;
import com.baba.utils.PageHelper;
import com.github.abel533.entity.Example;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 品牌服务类
 * @author Jamayette
 * Created on  2017/8/16
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private ColorDAO colorDAO;
    @Autowired
    private SKUDAO skuDao;
    @Autowired
    private Jedis jedis;
    @Autowired
    private HttpSolrServer solrServer;
    @Autowired
    private JmsTemplate jmsTemplate;


    @Override
    public PageHelper.Page<Product> findByExample(Product product, Integer pageNum, Integer pageSize) {

        if (product.getName()==null){
            product.setName("");
        }

        Example example = new Example(Product.class);
        example.setOrderByClause("createTime desc");
        example.createCriteria().andLike("name","%"+ product.getName()+"%");
        PageHelper.startPage(pageNum,pageSize);
        productDAO.selectByExample(example);
        return PageHelper.endPage();

    }

    @Override
    public List<Color> findEnableColors() {

        Example example = new Example(Color.class);
        example.createCriteria().andEqualTo("parentId",0+"");
        List<Color> colors = colorDAO.selectByExample(example);

        return colors;
    }

    @Override
    public void addProduct(Product product) {

        if (product.getCreateTime()==null){
            product.setCreateTime(new Date());
        }
        if (product.getIsShow()==null){
            product.setIsShow(0);
        }
        if (product.getIsCommend()==null){
            product.setIsCommend(0);
        }
        if (product.getIsHot()==null){
            product.setIsHot(0);
        }
        if (product.getIsNew()==null){
            product.setIsNew(0);
        }
        if (product.getIsDel()==null){
            product.setIsDel(0);
        }

        Long pno = jedis.incr("pno");
        product.setId(pno);

        productDAO.insert(product);

        String[] colors = product.getColors().split(",");
        String[] sizes = product.getSizes().split(",");

        for (String color : colors) {
            for (String size : sizes) {
                Sku sku = new Sku();

                sku.setProductId(product.getId());
                sku.setColorId(Long.valueOf(color));
                sku.setSize(size);
                sku.setMarketPrice(899F);
                sku.setPrice(699F);
                sku.setDeliveFee(20F);
                sku.setStock(0);
                sku.setUpperLimit(100);
                sku.setCreateTime(new Date());

                skuDao.insert(sku);
            }
        }


    }

    @Override
    public void updateProduct(Product product, final String ids) throws IOException, SolrServerException {

        Example example = new Example(Product.class);

        // 将ids的字符串转成list集合
        List<Object> list = new ArrayList<>();
        String[] split = ids.split(",");
        for (String s : split) {
            list.add(s);
        }

        // 设置批量修改的id条件
        example.createCriteria().andIn("id",list);

        // 进行批量，选择性属性修改
        productDAO.updateByExampleSelective(product,example);

        // 如果是商品上架，将商品信息添加到solr服务器中
        // 需要保存的信息有：商品id、商品名称、图片地址、售价、品牌id
        if(product.getIsShow()==1){
            // 采用消息服务模式
            // 发送消息ids到ActiveMQ中
            // 1.将商品信息添加到solr服务器中
            // 2.CMS-生成静态商品信息页面功能
            jmsTemplate.send("productIds", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    //使用session创建文本消息
                    return session.createTextMessage(ids);
                }
            });
        }
    }

    @Override
    public SuperPojo findProductById(Long productId) {

        Product product = productDAO.selectByPrimaryKey(productId);
        Sku sku = new Sku();
        sku.setProductId(productId);

        //此时的Sku因为里面与颜色信息，所以可以用万能POJO对象来装载
        List<SuperPojo> skus = skuDao.selectSkuAndColorByProductId(productId);

        SuperPojo superPojo = new SuperPojo();
        superPojo.setProperty("product",product);
        superPojo.setProperty("skus",skus);
        return superPojo;
    }


}
