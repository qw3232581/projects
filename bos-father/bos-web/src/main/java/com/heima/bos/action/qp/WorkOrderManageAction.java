package com.heima.bos.action.qp;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.domain.qp.NoticeBill;
import com.heima.bos.domain.qp.WorkOrderManage;
import com.heima.bos.domain.user.User;
import com.heima.bos.utils.DownloadUtils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Date;
import java.util.List;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class WorkOrderManageAction extends BaseAction<WorkOrderManage> {

    @Action(value = "workOrderManageAction_pdfExport",
            results = {@Result(name = "pdfExport",location = "/WEB-INF/pages/qupai/quickworkorder.jsp") })
    public String pdfExport() {

        // 工作单数据
        List<WorkOrderManage> list = facadeService.getWorkOrderManageService().findAll();
        // pdf 下载...
        // itext 报表 下载
        try {
            Document document = new Document();
            // response
            HttpServletResponse response = ServletActionContext.getResponse();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setEncryption("itcast".getBytes(), "heima01".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
            // 浏览器下载 ...两个头
            String filename = new Date(System.currentTimeMillis()).toLocaleString() + "_工作单报表.pdf";
            response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));// mime 类型
            response.setHeader("Content-Disposition", "attachment;filename=" + DownloadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
            // 打开文档
            document.open();
            Table table = new Table(5, list.size() + 1);// 5列 行号 0 开始
            table.setBorderWidth(1f);
            table.setAlignment(1);// // 其中1为居中对齐，2为右对齐，3为左对齐
            // table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            // 设置表格字体
            BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);

            // 表头
            // table.addCell(buildCell("工作单编号", font));
            table.addCell(buildCell("到达地", font));
            table.addCell(buildCell("货物", font));
            table.addCell(buildCell("数量", font));
            table.addCell(buildCell("重量", font));
            table.addCell(buildCell("配载要求", font));

            // 表格数据
            for (WorkOrderManage workOrderManage : list) {
                // table.addCell(buildCell(workOrderManage.getId(), font));
                table.addCell(buildCell(workOrderManage.getArrivecity(), font));
                table.addCell(buildCell(workOrderManage.getProduct(), font));
                table.addCell(buildCell(workOrderManage.getNum().toString(), font));
                table.addCell(buildCell(workOrderManage.getWeight().toString(), font));
                table.addCell(buildCell(workOrderManage.getFloadreqr(), font));
            }

            // 向文档添加表格
            document.add(table);
            document.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return NONE;

    }

    private Cell buildCell(String content, Font font) throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        Cell cell = new Cell(phrase);
        // 设置垂直居中
        cell.setVerticalAlignment(1);
        // 设置水平居中
        cell.setHorizontalAlignment(1);
        return cell;
    }


}
