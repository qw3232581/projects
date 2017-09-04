package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.SearchService;

@RequestMapping("search")
@Controller
public class SearchController {

    private static final Integer ROWS = 32;

    @Autowired
    private SearchService searchService;

    /**
     * 搜索商品
     * 
     * @param keyWords
     * @param page
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWords,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        try {
            keyWords = new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        SearchResult searchResult = null;
        try {
            searchResult = this.searchService.search(keyWords, page, ROWS);
        } catch (Exception e) {
            searchResult = new SearchResult(0L, null);
            e.printStackTrace();
        }

        // 搜索关键字
        mv.addObject("query", keyWords);

        // 商品的集合
        mv.addObject("itemList", searchResult.getList());

        // 当前页数
        mv.addObject("page", page);

        // 总页数
        int total = searchResult.getTotal().intValue();
        int pages = total % ROWS == 0 ? total / ROWS : total / ROWS + 1;
        mv.addObject("pages", pages);

        return mv;
    }

}
