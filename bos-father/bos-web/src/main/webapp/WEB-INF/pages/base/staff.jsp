<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <!-- 导入jquery核心类库 -->
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
    <!-- 导入easyui类库 -->
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/css/default.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
    <script
            src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
            type="text/javascript"></script>
    <script type="text/javascript">
        function doAdd() {
            $('#addStaffWindow').window("open");
        }

        function doView() {
            $('#queryStaffWindow').window("open");
        }

        function doDelete() {
            var arr = $("#grid").datagrid("getSelections");
            if (arr == null || arr.length == 0) {
                $.messager.alert("提示", "请至少选中一行进行操作", "info")
            } else {
                var ids = new Array();
                for (var i = 0; i < arr.length; i++) {
                    ids.push(arr[i].id);
                }
                var idsString = ids.join(",");
                $.post("${pageContext.request.contextPath}/staffAction_delStaff",
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

        function doRestore() {
            var arr = $("#grid").datagrid("getSelections");
            if (arr == null || arr.length == 0) {
                $.messager.alert("提示", "请至少选中一行进行操作", "info")
            } else {
                var ids = new Array();
                for (var i = 0; i < arr.length; i++) {
                    ids.push(arr[i].id);
                }
                var idsString = ids.join(",");
                $.post("${pageContext.request.contextPath}/staffAction_restoreBatch",
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
        //工具栏
        var toolbar = [{
            id: 'button-view',
            text: '查询',
            iconCls: 'icon-search',
            handler: doView
        }, {
            id: 'button-add',
            text: '增加',
            iconCls: 'icon-add',
            handler: doAdd
        }, {
            id: 'button-edit',
            text: '修改',
            iconCls: 'icon-edit',
            handler: function (rowIndex, rowData) {
                $('#addStaffWindow').window("open");
                $("#addStaffForm").form("load", rowData);
            }
        }, {
            id: 'button-delete',
            text: '离职',
            iconCls: 'icon-cancel',
            handler: doDelete
        }, {
            id: 'button-save',
            text: '还原',
            iconCls: 'icon-save',
            handler: doRestore
        }];
        // 定义列
        var columns = [[{
            field: 'id',
            checkbox: true,
        }, {
            field: 'name',
            title: '姓名',
            width: 120,
            align: 'center'
        }, {
            field: 'telephone',
            title: '手机号',
            width: 120,
            align: 'center'
        }, {
            field: 'haspda',
            title: '是否有PDA',
            width: 120,
            align: 'center',
            formatter: function (data, row, index) {
                if (data == "1") {
                    return "有";
                } else {
                    return "无";
                }
            }
        }, {
            field: 'standard',
            title: '取派标准',
            width: 120,
            align: 'center'
        }, {
            field: 'station',
            title: '所属单位',
            width: 200,
            align: 'center'
        }, {
            field: 'deltag',
            title: '是否在职',
            width: 120,
            align: 'center',
            formatter: function (data, row, index) {
                if (data == "0") {
                    return "在职"
                } else {
                    return "离职";
                }
            }
        }]];

        $(function () {
            // 先将body隐藏，再显示，不会出现页面刷新效果
            $("body").css({visibility: "visible"});

            $("#save").click(function () {
                var flag = $("#addStaffForm").form("validate");

                if (flag) {
                    $("#addStaffForm").submit();
                    $("#addStaffWindow").window("close");
                }
            });

            $("#query").click(function () {
                var jsondata = {
                    "name": $("#qname").val(), "telephone": $("#qtelephone").val(),
                    "station": $("#qstation").val(), "standard": $("#qstandard").combobox("getValue")
                };
                $("#grid").datagrid("load", jsondata);

                $('#queryStaffWindow').window("close");
            })

            // 取派员信息表格
            $('#grid').datagrid({
                iconCls: 'icon-forward',
                fit: true,
                border: false,
                rownumbers: true,
                striped: true,
                pageList: [30, 50, 100],
                pagination: true,
                toolbar: toolbar,
                url: "${pageContext.request.contextPath }/staffAction_pageQuery",
                idField: 'id',
                columns: columns,
                onDblClickRow: doDblClickRow
            });

            // 添加取派员窗口
            $('#addStaffWindow').window({
                title: '添加取派员',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 400,
                resizable: false,
                onBeforeClose: function () {
                    $("#addStaffForm").form("clear");
                    $("#telephone").validatebox({validType: ['telephone', 'uniquePhone']});
                }
            });

            $('#queryStaffWindow').window({
                title: '查询取派员',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 400,
                resizable: false,
                onBeforeClose: function () {
                    $("#queryStaffForm").form("clear");
                }
            });
        });

        function doDblClickRow(rowIndex, rowData) {
            $("#addStaffWindow").window("open");
            $("#addStaffForm").form("load", rowData);
            $("#telephone").validatebox({validType: 'telephone'});
        }

        $.extend($.fn.validatebox.defaults.rules, {
            telephone: {
                validator: function (value, param) {
                    var reg = /^1[3|4|5|7|8]\d{9}$/;
                    return reg.test(value);
                },
                message: '手机号必须是13,14,15,17,18开头的11位数字'
            },
            uniquePhone: {
                validator: function (value, param) {
                    var flag;
                    $.ajax({
                        url: '${pageContext.request.contextPath }/staffAction_validPhone',
                        type: 'POST',
                        timeout: 6000,
                        data: {"telephone": value},
                        async: false,
                        success: function (data, textStatus, jqXHR) {
                            if (data) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        }
                    });
                    if (flag) {
                        $("#tel").removeClass('validatebox-invalid');
                    }
                    return flag;
                },
                message: '手机号已经存在'
            }
        });

    </script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
<div region="center" border="false">
    <table id="grid"></table>
</div>
<div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" collapsible="false"
     minimizable="false" maximizable="false" style="top:20px;left:200px">
    <div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
        <div class="datagrid-toolbar">
            <a id="save" icon="icon-save" href="javascript:;" class="easyui-linkbutton" plain="true">保存</a>
        </div>
    </div>

    <div region="center" style="overflow:auto;padding:5px;" border="false">
        <form id="addStaffForm" action="${pageContext.request.contextPath }/staffAction_saveStaff" method="post">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="2">收派员信息</td>
                </tr>
                <tr>
                    <input type="hidden" name="id"/>
                    <td>姓名</td>
                    <td><input type="text" name="name" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td>手机</td>
                    <td><input type="text" id="telephone" name="telephone" class="easyui-validatebox" required="true"
                               data-options="validType:['telephone','uniquePhone']"/></td>
                </tr>
                <tr>
                    <td>单位</td>
                    <td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="checkbox" name="haspda" value="1"/>
                        是否有PDA
                    </td>
                </tr>
                <tr>
                    <td>取派标准</td>
                    <td>
                        <input type="text" name="standard" class="easyui-combobox" required="true"
                               data-options="valueField:'name',textField:'name',editable:'false',
							url:'${pageContext.request.contextPath}/standardAction_ajaxList'"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>


<div class="easyui-window" title="对收派员进行查询" id="queryStaffWindow" collapsible="false"
     minimizable="false" maximizable="false" style="top:20px;left:200px">
    <div region="center" style="overflow:auto;padding:5px;" border="false">
        <form id="queryStaffForm" action="${pageContext.request.contextPath }/staffAction_pageQuery" method="post">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="2">查询收派员</td>
                </tr>
                <tr>
                    <td>姓名</td>
                    <td><input type="text" id="qname"/></td>
                </tr>
                <tr>
                    <td>手机</td>
                    <td><input type="text" id="qtelephone"/></td>
                </tr>
                <tr>
                    <td>单位</td>
                    <td><input type="text" id="qstation"/></td>
                </tr>
                <tr>
                    <td>取派标准</td>
                    <td>
                        <input type="text" id="qstandard" name="standard" class="easyui-combobox"
                               data-options="valueField:'name',textField:'name',editable:'false',
							url:'${pageContext.request.contextPath}/standardAction_ajaxList'"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div region="south" style="height:31px;overflow:hidden;" split="false" border="false">
        <div class="datagrid-toolbar">
            <a id="query" icon="icon-search" href="javascript:;" class="easyui-linkbutton" plain="true">查询</a>
        </div>
    </div>


</div>


</body>
</html>	