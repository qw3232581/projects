<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/default.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		$("#grid").datagrid({
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pagination : true,
            onDblClickRow: doDblClickRow,
            onAfterEdit: function (rowIndex, rowData, changes) {
                $.post("${pageContext.request.contextPath}/functionAction_updateFunction", rowData, function (data) {
                    if (data) {
                        $.messager.alert("恭喜", "修改成功", "info");
                    } else {
                        $.messager.alert("遗憾", "修改失败", "error");
                    }
                })
            },
			toolbar :
                [{
				    id : 'add',
					text : '添加权限',
					iconCls : 'icon-add',
					handler : function(){
						location.href='${pageContext.request.contextPath}/page_admin_function_add.action';
					}
				} ,{
                    id: 'button-edit',
                    text: '保存',
                    iconCls: 'icon-edit',
                    handler: doEdit
                } ,{
                    id: 'button-delete',
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: doCancel
                }
			],
			url : '${pageContext.request.contextPath}/functionAction_pageQuery',
			columns : [[
			  {
				  field : 'name',
				  title : '名称',
				  width : 200,
                  editor: {
                      type: 'validatebox',
                      options: {
                          required: true
                      }
                  }
			  },  
			  {
				  field : 'code',
				  title : '权限关键字',
				  width : 200,
                  editor: {
                      type: 'validatebox',
                      options: {
                          required: true
                      }
                  }
			  }, 
			  {
				  field : 'description',
				  title : '描述',
				  width : 200,
                  editor: {
                      type: 'validatebox',
                      options: {
                          required: true
                      }
                  }
			  },  
			]]
		});
	});

    function doDblClickRow(rowIndex, rowData) {
        editIndex = rowIndex;
        $("#grid").datagrid("beginEdit", rowIndex);
    }

    function doEdit() {
        $("#grid").datagrid("endEdit", editIndex);
    }

    function doCancel() {
        $("#grid").datagrid("cancelEdit", editIndex);
    }
</script>	
</head>
<body class="easyui-layout">
<div data-options="region:'center'">
	<table id="grid"></table>
</div>
</body>
</html>