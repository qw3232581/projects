package com.baba.solr.service;

import com.baba.dao.ProductDAO;
import com.baba.dao.SKUDAO;
import com.baba.pojo.Product;
import com.baba.pojo.Sku;
import com.baba.pojo.SuperPojo;
import com.baba.service.SolrService;
import com.baba.utils.PageHelper;
import com.github.abel533.entity.Example;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Jamayette
 *         Created on  2017/8/18
 */
@Service("solrService")
@Transactional
public class SolrServiceImpl implements SolrService {

    @Autowired
    private HttpSolrServer solrServer;
    @Autowired
    private SKUDAO skuDAO;
    @Autowired
    private ProductDAO productDAO;

    @Override
    public PageHelper.Page<SuperPojo> findProductByKeyWord(
            String keyword, String sort, Integer pageNum, Integer pageSize,Long brandId, Float pa, Float pb)
            throws SolrServerException {

        SolrQuery solrQuery = new SolrQuery();

        if (keyword!=null&&keyword.trim().length()>0){
            solrQuery.set("q","name_ik:"+keyword);
        }else {
            solrQuery.set("q","name_ik:*");
        }

        // 设置过滤条件
        if (brandId != null) {
            solrQuery.addFilterQuery("brandId:" + brandId);// 品牌
        }

        // 价格
        if (pa != null && pb != null) {
            if (pb == -1) {
                solrQuery.addFilterQuery("price:[" + pa + " TO *]");
            } else {
                solrQuery.addFilterQuery("price:[" + pa + " TO " + pb + "]");
            }
        }

        //开始分页设置
        PageHelper.Page<SuperPojo> page = new PageHelper.Page<>(pageNum, pageSize);

        solrQuery.setStart(page.getStartRow());
        solrQuery.setRows(page.getPageSize());

        // 设置排序
        // solrQuery.setSort("price", ORDER.asc);
        if (sort != null && sort.trim().length() > 0) {
            solrQuery.setSort(new SolrQuery.SortClause(sort.split(" ")[0], sort.split(" ")[1]));
        }

        // 设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("name_ik");
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");

        // 开始查询
        QueryResponse response = solrServer.query(solrQuery);

        // 获得高亮数据集合
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        // 获得结果集
        SolrDocumentList results = response.getResults();

        // 获得总数量
        long total  = results.getNumFound();
        page.setTotal(total);

        // 将结果集中的信息封装到商品对象中
        // 由于原商品对象中并没有价格属性，而价格属性本应该是在商品对象的子对象库存对象中，
        // 而本次设计并不打算使用类似于hibernate的在pojo中做对象的相应关联，
        // 所以这里，我们使用万能对象来装载数据
        // 一个万能对象就可以等同于从数据库查询（包括连接查询）出的结果表中的一条数据

        // 创建商品对象（万能对象）集合
        List<SuperPojo> superProducts = new ArrayList<>();

        for (SolrDocument solrDocument : results) {
            // 创建商品对象
            SuperPojo superProduct = new SuperPojo();

            // 商品id
            String id = (String) solrDocument.get("id");
            superProduct.setProperty("id", id);

            // 商品名称
            //String name = (String) solrDocument.get("name_ik");
            //superProduct.setProperty("name", name);

            // 取得高亮数据集合中的商品名称
            Map<String, List<String>> map = highlighting.get(id);
            String string = map.get("name_ik").get(0);
            superProduct.setProperty("name", string);

            // 图片地址
            String imgUrl = (String) solrDocument.get("url");
            superProduct.setProperty("imgUrl", imgUrl);

            // 商品最低价格
            Float price = (Float) solrDocument.get("price");
            superProduct.setProperty("price", price);

            // 品牌id
            String brandId2 = (String) solrDocument.get("brandId");
            superProduct.setProperty("brandId", brandId2);

            // 将万能商品对象添加到集合中
            superProducts.add(superProduct);
        }

        //将结果集添加到分页对象中
        page.setResult(superProducts);

        return page;
    }

    @Override
    public void addProduct(String ids) throws SolrServerException, IOException {

        Example example = new Example(Product.class);

        // 将ids的字符串转成list集合
        List<Object> list = new ArrayList<>();
        String[] split = ids.split(",");
        list.addAll(Arrays.asList(split));

        // 设置批量查询的id条件
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",list);

        // 查询ids中的所有商品
        List<Product> products = productDAO.selectByExample(example);

        // 遍历查询出来的商品集合
        for (Product product2 : products) {

            // 将商品的各个信息，添加到文档对象中
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", product2.getId());
            doc.addField("name_ik", product2.getName());
            doc.addField("url", product2.getImgUrl().split(",")[0]);
            doc.addField("brandId", product2.getBrandId());

            // 查询出某商品库存中的最低价格
            // SELECT price from bbs_sku WHERE bbs_sku.product_id = 449
            // ORDER BY price ASC LIMIT 1

            Example example2 = new Example(Sku.class);
            // 某商品的库存
            example2.createCriteria().andEqualTo("productId", product2.getId());
            example2.setOrderByClause("price asc");// 价格升序

            // 开始分页 limit
            PageHelper.startPage(1, 1);
            List<Sku> skus = skuDAO.selectByExample(example2);

            // 结束分页
            PageHelper.endPage();

            doc.addField("price", skus.get(0).getPrice());

            // 将文档对象添加到solr服务器中
            solrServer.add(doc);

            // 提交
            solrServer.commit();
        }
    }


}
