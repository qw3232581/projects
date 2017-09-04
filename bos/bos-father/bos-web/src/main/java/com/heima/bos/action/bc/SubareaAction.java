package com.heima.bos.action.bc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.bc.Region;
import com.heima.bos.domain.bc.Subarea;
import com.heima.bos.utils.DownloadUtils;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class SubareaAction extends BaseAction<Subarea> {

    @Action(value = "subareaAction_download")
    public String download() throws Exception {

        List<Subarea> list = facadeService.getSubareaService().findAll(getSpecification());

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        HSSFRow firstRow = sheet.createRow(0);
        firstRow.createCell(0).setCellValue("分区编号");
        firstRow.createCell(1).setCellValue("区域编号");
        firstRow.createCell(2).setCellValue("关键字");
        firstRow.createCell(3).setCellValue("起始号码");
        firstRow.createCell(4).setCellValue("结束号");
        firstRow.createCell(5).setCellValue("单双号");
        firstRow.createCell(6).setCellValue("位置信息");

        for (Subarea s : list) {
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(s.getId());
            row.createCell(1).setCellValue(s.getRegion().getId());
            row.createCell(2).setCellValue(s.getAddresskey());
            row.createCell(3).setCellValue(s.getStartnum());
            row.createCell(4).setCellValue(s.getEndnum());
            row.createCell(5).setCellValue(s.getSingle() + "");
            row.createCell(6).setCellValue(s.getPosition());
        }

        HttpServletResponse response = getResponse();

        String downloadname = new Date(System.currentTimeMillis()) + "分区数据.xls";
        ServletContext context = ServletActionContext.getServletContext();
        String mimeType = context.getMimeType(downloadname);
        response.setContentType(mimeType);
        DownloadUtils.getAttachmentFileName(downloadname, getRequest().getHeader("user-agent"));
        response.setHeader("Context-Disposition", "attachment:filename=" + downloadname);
        workbook.write(response.getOutputStream());

        return NONE;
    }

    @Action(value = "subareaAction_saveSubarea", results = {
            @Result(name = "saveSubarea", location = "/WEB-INF/pages/base/subarea.jsp")})
    public String saveSubarea() {
        try {
            facadeService.getSubareaService().saveSubarea(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "saveSubarea";
    }

    @Action(value = "subareaAction_pageQuery")
    public String pageQuery() {
        Page<Subarea> pageData = facadeService.getSubareaService().pageQuery(getPageRequest(), getSpecification());
        setPageDatas(pageData);
        return "pageQuery";
    }


    @Action(value = "subareaAction_update", 
    		results = {@Result(name = "update", type = "fastJson")})
    public String delregion() {
        try {
            Region region = new Region();
            region.setId(getParameter("region[id]"));
            model.setRegion(region);

            facadeService.getSubareaService().saveSubarea(model);
            push(true);
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "update";
    }

    protected Specification getSpecification() {
        Specification<Subarea> spec = new Specification<Subarea>() {
            public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (model.getRegion() != null) {
                    Join<Subarea, Region> regionJoin = root.join(root.getModel().getSingularAttribute("region", Region.class), JoinType.LEFT);
                    if (StringUtils.isNotBlank(model.getRegion().getProvince())) {
                        Predicate p1 = cb.like(regionJoin.get("province").as(String.class), "%" + model.getRegion().getProvince() + "%");
                        list.add(p1);
                    }
                    if (StringUtils.isNotBlank(model.getRegion().getCity())) {
                        Predicate p2 = cb.like(regionJoin.get("city").as(String.class), "%" + model.getRegion().getCity() + "%");
                        list.add(p2);
                    }
                    if (StringUtils.isNotBlank(model.getRegion().getDistrict())) {
                        Predicate p3 = cb.like(regionJoin.get("district").as(String.class), "%" + model.getRegion().getDistrict() + "%");
                        list.add(p3);
                    }
                }

                if (model.getDecidedzone() != null && StringUtils.isNotBlank(model.getDecidedzone().getId())) {
                    Predicate p4 = cb.equal(root.get("decidedzone").as(Decidedzone.class), model.getDecidedzone());
                    list.add(p4);
                }
                if (StringUtils.isNotBlank(model.getAddresskey())) {
                    Predicate p5 = cb.like(root.get("addresskey").as(String.class), "%" + model.getAddresskey() + "%");
                    list.add(p5);
                }

                Predicate ps[] = new Predicate[list.size()];
                return cb.and(list.toArray(ps));
            }
        };

        return spec;
    }

    @Action(value = "subareaAction_noAssociationList", results = {
            @Result(name = "noAssociationList", type = "fastJson")})
    public String noAssociationList() {
        List<Subarea> Subareas = facadeService.getSubareaService().noAssociationList();
        push(Subareas);
        return "noAssociationList";
    }
}
