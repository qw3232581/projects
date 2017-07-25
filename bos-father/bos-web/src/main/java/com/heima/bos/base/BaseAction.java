package com.heima.bos.base;

import com.heima.bos.service.facade.FacadeService;
import com.heima.bos.utils.DownloadUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    protected T model;
    @Autowired
    protected FacadeService facadeService;
    // 分页请求属性驱动
    protected int page; // 页码
    protected int rows; // 每页 记录数
    private Page<T> pageDatas;

    public BaseAction() {
        // 对model进行实例化， 通过子类 类声明的泛型
        Type superclass = this.getClass().getGenericSuperclass();
        // 转化为参数化类型
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        // 获取一个泛型参数
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getModel() {
        return model;
    }

    public String getParameter(String name) {
        return ServletActionContext.getRequest().getParameter(name);
    }

    // 获取Session Attribute
    public Object getSessionAttribute(String name) {
        return ServletActionContext.getRequest().getSession().getAttribute(name);
    }

    // 向session保存属性
    public void setSessionAttribute(String name, Object value) {
        ServletActionContext.getRequest().getSession().setAttribute(name, value);
    }

    // 向session移除对象
    public void removeSessionAttribute(String name) {
        ServletActionContext.getRequest().getSession().removeAttribute(name);
    }

    // json序列化 ....

    // 值栈操作 后续子类actions操作
    public void push(Object obj) {
        ActionContext.getContext().getValueStack().push(obj);// root
    }

    public void set(String key, Object obj) {
        ActionContext.getContext().getValueStack().set(key, obj);// root 将一个map集合 存放在栈顶
    }

    // 目标数据存放 值栈 下方 map结构
    public void put(String key, Object obj) {
        ActionContext.getContext().getValueStack().getContext().put(key, obj);
    }

    // 下载 封装 response对象
    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    // 两个头 一个流 下载需要文件名和浏览器类型  filename :真实文件名称   path参数: 下载文件在服务器的路径
    public void download(String filename, String path) {
        HttpServletResponse response = getResponse();
        try {
            ServletContext context = ServletActionContext.getServletContext();
            response.setHeader("Content-Disposition", "attachment;filename=" + DownloadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
            response.setContentType(context.getMimeType(filename));
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(path);
            int len;
            byte[] bytes = new byte[1024 * 8];
            while ((len = in.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Pageable getPageRequest() {
        Pageable pageRequest = new PageRequest(page - 1, rows);
        return pageRequest;
    }

    public void setPageDatas(Page<T> pageDatas) {
        this.pageDatas = pageDatas;
    }

    public Map<String, Object> getObj() {
        long totalElements = pageDatas.getTotalElements();
        List<T> list = pageDatas.getContent();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("total", totalElements);
        map.put("rows", list);
        return map;
    }

}
