package com.heima.bos.action.bc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.bc.Region;
import com.heima.bos.utils.PinYin4jUtils;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class RegionAction extends BaseAction<Region> {
    private File upload;

    public void setUpload(File upload) {
        this.upload = upload;
    }
    
    @Action(value = "regionAction_importData", results = {
            @Result(name = "importData", type = "fastJson")})
    public String importData() {

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
            HSSFSheet sheet = workbook.getSheetAt(0);
            ArrayList<Region> regions = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                if (StringUtils.isNotBlank(row.getCell(0).getStringCellValue())) {
                    Region region = new Region();

                    String province = row.getCell(1).getStringCellValue();
                    String city = row.getCell(2).getStringCellValue();
                    String district = row.getCell(3).getStringCellValue();

                    region.setId(row.getCell(0).getStringCellValue());
                    region.setProvince(province);
                    region.setCity(city);
                    region.setDistrict(district);
                    region.setPostcode(row.getCell(4).getStringCellValue());

                    city = city.substring(0, city.length() - 1);
                    region.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));
                    province = province.substring(0, province.length() - 1);
                    district = district.substring(0, district.length() - 1);

                    String[] strings;
                    if (province.equalsIgnoreCase(city)) {
                        strings = PinYin4jUtils.getHeadByString(province + district);
                    } else {
                        strings = PinYin4jUtils.getHeadByString(province + city + district);
                    }
                    String shortcode = PinYin4jUtils.getHeadFromArray(strings);
                    region.setShortcode(shortcode); // 省市区 每一个中文字首字母大写组成
                    regions.add(region);
                }
                facadeService.getRegionService().importData(regions);
            }
            push(true);
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "importData";
    }

    @Action(value = "regionAction_saveRegion", results = {
            @Result(name = "saveRegion", location = "/WEB-INF/pages/base/region.jsp")})
    public String saveRegion() {
        try {
            facadeService.getRegionService().saveRegion(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "saveRegion";
    }

    @Action(value = "regionAction_pageQuery")
    public String pageQuery() {
    	Specification<Region> spec = getSpecification();
    	
//        if (StringUtils.isNotBlank(model.getProvince())||StringUtils.isNotBlank(model.getCity())
//        		||StringUtils.isNotBlank(model.getDistrict())||StringUtils.isNotBlank(model.getPostcode())
//        		||StringUtils.isNotBlank(model.getShortcode())||StringUtils.isNotBlank(model.getCitycode())) {
        	Page<Region> pageData = facadeService.getRegionService().pageQuery(getPageRequest(), spec);
        	setPageDatas(pageData);
        	return "pageQuery";
//		}
        
//        String string = facadeService.getRegionService().pageQueryByRedis(getPageRequest());
//        
//        try {
//			HttpServletResponse response = getResponse();
//			response.setContentType("text/json;charset=utf-8");
//			response.getWriter().println(string);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return NONE;
	}

	private Specification<Region> getSpecification() {
		Specification<Region> spec = new Specification<Region>() {
        	List<Predicate> predicates = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(model.getProvince())) {
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    predicates.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCity())) {
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    predicates.add(p2);
                }
                if (StringUtils.isNotBlank(model.getDistrict())) {
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    predicates.add(p3);
                }
                if (StringUtils.isNotBlank(model.getPostcode())) {
                    Predicate p4 = cb.like(root.get("postcode").as(String.class), "%" + model.getPostcode() + "%");
                    predicates.add(p4);
                }
                if (StringUtils.isNotBlank(model.getShortcode())) {
                    Predicate p5 = cb.like(root.get("shortcode").as(String.class), "%" + model.getShortcode() + "%");
                    predicates.add(p5);
                }
                if (StringUtils.isNotBlank(model.getCitycode())) {
                    Predicate p6 = cb.like(root.get("citycode").as(String.class), "%" + model.getCitycode() + "%");
                    predicates.add(p6);
                }
                Predicate ps[] = new Predicate[predicates.size()];
                return cb.and(predicates.toArray(ps));
            }
        };
		return spec;
	}

    @Action(value = "regionAction_validateRegionIdUnique", results = {
            @Result(name = "getRegionById", type = "json")})
    public String getRegionById() {
        try {
            Region region = facadeService.getRegionService().findRegionById(model.getId());
            if (region == null) {
                push(true);
            } else {
                push(false);
            }
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "getRegionById";
    }

    @Action(value = "regionAction_ajaxList", results = {
            @Result(name = "ajaxList", type = "fastJson", params = {"includeProperties", "id,name"})})
    public String ajaxList() {
        String params = getParameter("q");
        List<Region> regions = facadeService.getRegionService().findAllRegions(params);
        push(regions);
        return "ajaxList";
    }

}
