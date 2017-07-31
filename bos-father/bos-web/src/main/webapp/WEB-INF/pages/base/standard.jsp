<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>取派标准</title>
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
    $(function () {
        // 先将body隐藏，再显示，不会出现页面刷新效果
        $("body").css({visibility: "visible"});

        $("#save").click(function () {
            var flag = $("#addStandardForm").form("validate");

            if (flag) {
                $("#addStandardForm").submit();
            }
        });

        // 收派标准信息表格
        $('#grid').datagrid({
            iconCls: 'icon-forward',
            fit: true,
            border: false,
            rownumbers: true,
            striped: true,
            pageList: [10, 25, 50],
            pagination: true,
            toolbar: toolbar,
            url: "${pageContext.request.contextPath}/standardAction_pageQuery",
            idField: 'id',
            columns: columns,
            onDblClickRow: function (rowIndex, rowData) {
                $("#addStandardWindow").window("open");
                $("#addStandardForm").form("load", rowData);
            }
        });

        $('#addStandardWindow').window({
            title: '取派员操作',
            width: 600,
            modal: true,
            shadow: true,
            closed: true,
            height: 400,
            resizable: false,
            onBeforeClose: function () {
                $("#addStandardForm").form("clear");
            }
        });
    });

    //工具栏
    var toolbar = [{
        id: 'button-add',
        text: '增加',
        iconCls: 'icon-add',
        handler: function () {
            $('#addStandardWindow').window("open");
        }
    }, {
        id: 'button-edit',
        text: '修改',
        iconCls: 'icon-edit',
        handler: function (rowIndex, rowData) {
            $('#addStandardWindow').window("open");
            $("#addStandardForm").form("load", rowData);
        }
    }, {
        id: 'button-delete',
        text: '作废',
        iconCls: 'icon-cancel',
        handler: function () {
            var arr = $("#grid").datagrid("getSelections");
            if (arr == null || arr.length == 0) {
                $.messager.alert("提示", "请至少选中一行进行操作", "info")
            } else {
                var ids = new Array();
                for (var i = 0; i < arr.length; i++) {
                    ids.push(arr[i].id);
                }
                var idsString = ids.join(",");
                $.post("${pageContext.request.contextPath}/standardAction_delBatch",
                    {"ids": idsString},
                    function (data) {
                        if (data) {
                            $.messager.alert("提示", "操作成功", "info")
                            $("#grid").datagrid("clearChecked");
                            $("#grid").datagrid("reload");
                        } else {
                            $.messager.alert("提示", "操作失败", "info")
                        }
                    })
            }
        }
    }, {
        id: 'button-restore',
        text: '启用',
        iconCls: 'icon-save',
        handler: function () {
            var arr = $("#grid").datagrid("getSelections");
            if (arr == null || arr.length == 0) {
                $.messager.alert("提示", "请至少选中一行进行操作", "info")
            } else {
                var ids = new Array();
                for (var i = 0; i < arr.length; i++) {
                    ids.push(arr[i].id);
                }
                var idsString = ids.join(",");
                $.post("${pageContext.request.contextPath}/standardAction_restoreBatch",
                    {"ids": idsString},
                    function (data) {
                        if (data) {
                            $.messager.alert("提示", "操作成功", "info")
                            $("#grid").datagrid("clearChecked");
                            $("#grid").datagrid("reload");
                        } else {
                            $.messager.alert("提示", "操作失败", "info")
                        }
                    })
            }
        }
    }];

    // 定义列
    var columns = [[{
        field: 'id',
        checkbox: true
    }, {
        field: 'name',
        title: '标准名称',
        width: 120,
        align: 'center'
    }, {
        field: 'minweight',
        title: '最小重量',
        width: 120,
        align: 'center'
    }, {
        field: 'maxweight',
        title: '最大重量',
        width: 120,
        align: 'center'
    }, {
        field: 'minlength',
        title: '最小长度',
        width: 120,
        align: 'center'
    }, {
        field: 'maxlength',
        title: '最大长度',
        width: 120,
        align: 'center'
    }, {
        field: 'operator',
        title: '操作人',
        width: 120,
        align: 'center'
    }, {
        field: 'operationtime',
        title: '操作时间',
        width: 120,
        align: 'center'
    }, {
        field: 'operatordept',
        title: '操作单位',
        width: 120,
        align: 'center'
    }, {
        field: 'isDel',
        title: '是否作废',
        width: 120,
        align: 'center',
        formatter: function (value, row, index) {
            if (value == 0) {
                return "启用";
            } else {
                return "作废";
            }
        }
    },
    ]];
</script>
</head>

<body class="easyui-layout" style="visibility:hidden;">
<div region="center" border="false">
    <table id="grid"></table>
</div>

<!-- 添加标准窗体  -->
<div class="easyui-window" title="对收派员进行添加或者修改" id="addStandardWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
    <div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
        <div class="datagrid-toolbar">
            <a id="save" icon="icon-save" href="javascript:void(0)" class="easyui-linkbutton" plain="true">保存</a>
        </div>
    </div>

    <div region="center" style="overflow:auto;padding:5px;" border="false">
        <form id="addStandardForm" method="post"
              action="${pageContext.request.contextPath }/standardAction_saveStandard">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="2">收派标准信息</td>
                </tr>
                <tr>
                    <input type="hidden" name="id"/>
                    <input type="hidden" name="isDel"/>
                    <td>收派名称</td>
                    <td>
                        <input type="text" name="name" class="easyui-validatebox" data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>最小重量</td>
                    <td>
                        <input type="text" name="minweight" class="easyui-numberbox" data-options="min:1,precision:0,suffix:'KG',required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>最大重量</td>
                    <td>
                        <input type="text" name="maxweight" class="easyui-numberbox" data-options="max:500,precision:0,suffix:'KG',required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>最小长度</td>
                    <td>
                        <input type="text" name="minlength" class="easyui-numberbox" data-options="min:10,precision:0,suffix:'CM',required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>最大长度</td>
                    <td>
                        <input type="text" name="maxlength" class="easyui-numberbox" data-options="max:1000,precision:0,suffix:'CM',required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>

</html>