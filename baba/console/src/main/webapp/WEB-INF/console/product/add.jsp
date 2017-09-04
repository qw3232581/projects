<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-add</title>
<style type="">
.h2_ch a:hover, .h2_ch a.here {
    color: #FFF;
    font-weight: bold;
    background-position: 0px -32px;
}
.h2_ch a {
    float: left;
    height: 32px;
    margin-right: -1px;
    padding: 0px 16px;
    line-height: 32px;
    font-size: 14px;
    font-weight: normal;
    border: 1px solid #C5C5C5;
    background: url('/images/admin/bg_ch.gif') repeat-x scroll 0% 0% transparent;
}
a {
    color: #06C;
    text-decoration: none;
}
</style>
<script type="text/javascript">
    $(function(){
        var tObj;//存储上一次点击的a元素
        //获得id为tabs的元素旗下的所有a元素，并进行遍历
        $("#tabs a").each(function(){
            //如果发现a元素对象的class属性含有here，则让tObj等于当前a元素对象
            if($(this).attr("class").indexOf("here") == 0){tObj = $(this)}
            //当点击a元素对象时候
            $(this).click(function(){
                var c = $(this).attr("class");//获得当前点击的a元素对象的class属性
                //如果发现a元素的class属性含有here，则直接退出（已经点击变蓝的基础上，再次点击不进行样式切换了）
                if(c.indexOf("here") == 0){return;}
                //否则
                var ref = $(this).attr("ref");//获得点击的a元素ref属性tab名称（带#号的名称，放入jquery中直接变为tab页对象）
                var ref_t = tObj.attr("ref");//获得非点击a元素ref属性（here）tab名称（同上）
                tObj.attr("class","nor");//非点击a元素 添加class样式nor（超链接普通色）
                $(this).attr("class","here");//当前点击a元素的class样式为here（超链接蓝色）
                $(ref_t).hide();//前一次点击的a元素ref属性tab对象隐藏
                $(ref).show();//点击的a元素ref属性tab对象显示
                //将当前点击a元素对象赋予tObj，它这时就变为上一点击的了
                tObj = $(this);
                //如果点击的a元素ref属性tab名称为第3个tab （富文本编辑器的那一个）
                if(ref == '#tab_3'){
                    // 设置编辑器参数
                    var kingEditorParams = {
                        //指定上传文件参数名称
                        filePostName  : "uploadFile",
                        //指定上传文件请求的url。
                        uploadJson : '/uploadFck.do',
                        //上传类型，分别为image、flash、media、file
                        dir : "image",
                        width : '1000px',
                        height : '400px'
                    };
                    //创建富文本文本编辑器 并指向到id为productdesc的文本域（textarea）
                    KindEditor.create('#productdesc',kingEditorParams);
                    //同步KindEditor的值到textarea文本框，将编辑器的内容设置到原来的textarea控件里
                    KindEditor.sync();
                }
            });
        });
    });


    //上传图片
function uploadPic(){
	//上传图片 异步的
	var options = {
			url : "/uploadFiles.do",
			type : "post",
			dataType : "json",
			success : function(data){
				//多图片回显
				var html = '<tr>'
						 + '<td width="20%" class="pn-flabel pn-flabel-h"></td>'
						 + '<td width="80%" class="pn-fcontent">';
				for(var i=0;i<data.length;i++){
					html += '<img width="100" height="100" src="' + data[i] + '" />'
					     +  '<input type="hidden" name="imgUrl" value="' + data[i] + '"/>'
				}
				html += '<a href="javascript:;" class="pn-opt" onclick="jQuery(this).parents(\'tr\').remove()">删除</a>'
					 +  '</td>'
					 +  '</tr>';
				//回显
				$("#tab_2").append(html);
				
			}
	}
	$("#jvForm").ajaxSubmit(options);
}


</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 添加</div>
	<form class="ropt">
		<input type="submit" onclick="this.form.action='list.do';" value="返回列表" class="return-button"/>
	</form>
	<div class="clear"></div>
</div>
<h2 class="h2_ch"><span id="tabs">
<a href="javascript:void(0);" ref="#tab_1" title="基本信息" class="here">基本信息</a>
<a href="javascript:void(0);" ref="#tab_2" title="商品图片" class="nor">商品图片</a>
<a href="javascript:void(0);" ref="#tab_3" title="商品描述" class="nor">商品描述</a>
<a href="javascript:void(0);" ref="#tab_4" title="包装清单" class="nor">包装清单</a>
</span></h2>
<div class="body-box" style="float:right">

	<form id="jvForm" action="doAdd.do" method="post" enctype="multipart/form-data">
		<table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
			<tbody id="tab_1">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品类型:</td><td width="80%" class="pn-fcontent">
								<select name="typeId">
									<option value="">请选择</option>
									<option value="2">瑜珈服</option>
									<option value="3">瑜伽辅助</option>
									<option value="4">瑜伽铺巾</option>
									<option value="5">瑜伽垫</option>
									<option value="6">舞蹈鞋服</option>
									<option value="7">其它</option>
								</select>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						商品名称:</td><td width="80%" class="pn-fcontent">
						<input type="text" class="required" name="name" maxlength="100" size="100"/>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商品品牌:</td><td width="80%" class="pn-fcontent">
                    <select name="brandId">
                        <option value="0">请选择品牌</option>
                        <c:forEach items="${brands}" var="brand">
                            <option value="${brand.id}">${brand.name}</option>
                        </c:forEach>
                    </select>
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						商品毛重:</td><td width="80%" class="pn-fcontent">
						<input type="text" value="0.6" class="required" name="weight" maxlength="10"/>KG
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						颜色:</td>
							<td width="80%" class="pn-fcontent">
									<c:forEach items="${colors}" var="color">
                                        <input type="checkbox" value="${color.id}" name="colors"/>${color.name}
                                    </c:forEach>
							</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						尺码:</td><td width="80%" class="pn-fcontent">
						<input type="checkbox" name="sizes" value="S"/>S
						<input type="checkbox" name="sizes" value="M"/>M
						<input type="checkbox" name="sizes" value="L"/>L
						<input type="checkbox" name="sizes" value="XL"/>XL
						<input type="checkbox" name="sizes" value="XXL"/>XXL
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						状态:</td><td width="80%" class="pn-fcontent">
						<input type="checkbox" name="isNew" value="1"/>新品
						<input type="checkbox" name="isCommend" value="1"/>推荐
						<input type="checkbox" name="isHot" value="1"/>热卖
					</td>
				</tr>
			</tbody>
			<tbody id="tab_2" style="display: none">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>
						上传商品图片(90x150尺寸):</td>
						<td width="80%" class="pn-fcontent">
						注:该尺寸图片必须为90x150。
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h"></td>
						<td width="80%" class="pn-fcontent">
						<input type="file" onchange="uploadPic()" name="mpfs" multiple="multiple"/>
					</td>
				</tr>

			</tbody>
			<tbody id="tab_3" style="display: none">
				<tr>
					<td >
						<textarea rows="20" cols="180" id="productdesc" name="description"></textarea>
					</td>
				</tr>
			</tbody>
			<tbody id="tab_4" style="display: none">
				<tr>
					<td >
						<textarea rows="20" cols="180" id="productList" name="packageList"></textarea>
					</td>
				</tr>
			</tbody>
			<tbody>
				<tr>
					<td class="pn-fbutton" colspan="2">
						<input type="submit" class="submit" value="提交"/> &nbsp; <input type="reset" class="reset" value="重置"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
</body>
</html>