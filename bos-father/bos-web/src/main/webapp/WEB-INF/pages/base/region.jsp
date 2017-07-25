<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>区域设置</title>
    <!-- 导入jquery核心类库 -->
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
    <!-- 导入easyui类库 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/default.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
    <script src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath }/js/upload/jquery.ocupload-1.1.2.js" type="text/javascript"></script>

    <script type="text/javascript">
        var id_flag = false;

        function doAdd() {
            $('#addRegionWindow').window("open");
            $("#id").validatebox({validType: ''});
        }

        function doView() {
            $('#queryRegionWindow').window("open");
        }

        //工具栏
        var toolbar = [{
            id: 'button-view',
            text: '查询',
            iconCls: 'icon-search',
            handler: doView
        }, {
            id: 'button-edit',
            text: '修改',
            iconCls: 'icon-edit',
            handler: doView
        }, {
            id: 'button-add',
            text: '增加',
            iconCls: 'icon-add',
            handler: doAdd
        }, {
            id: 'button-import',
            text: '导入',
            iconCls: 'icon-redo'
        }];
        // 定义列
        var columns = [[{
            field: 'id',
            checkbox: true,
        }, {
            field: 'province',
            title: '省',
            width: 120,
            align: 'center'
        }, {
            field: 'city',
            title: '市',
            width: 120,
            align: 'center'
        }, {
            field: 'district',
            title: '区',
            width: 120,
            align: 'center'
        }, {
            field: 'postcode',
            title: '邮编',
            width: 120,
            align: 'center'
        }, {
            field: 'shortcode',
            title: '简码',
            width: 120,
            align: 'center'
        }, {
            field: 'citycode',
            title: '城市编码',
            width: 200,
            align: 'center'
        }]];

        $(function () {
            $("body").css({visibility: "visible"});

            $("#id").blur(function () {
                $.post("${pageContext.request.contextPath}/regionAction_validateRegionIdUnique",
                    {"id": this.value},
                    function (data) {
                        if (data) {
                            $("#id_span").html("<font color='green'>✔</font>");
                            id_flag = true;
                        } else {
                            $("#id_span").html("<font color='red'>✖</font>");
                            id_flag = false;
                        }
                    })
            })

            $("#save").click(function () {
                if ($("#addRegionForm").form("validate")) {
                    if (id_flag) {
                        $("#addRegionForm").submit();
                    }
                }
            })

            $("#query").click(function () {
                var jsondata = {
                    "province": $("#qprovince").val(), "city": $("#qcity").val(),
                    "district": $("#qdistrict").val(), "postcode": $("#qpostcode").val(),
                    "shortcode": $("#qshortcode").val(), "citycode": $("#qcitycode").val()
                };
                $("#grid").datagrid("load", jsondata);

                $('#queryRegionWindow').window("close");
            })

            $('#grid').datagrid({
                iconCls: 'icon-forward',
                fit: true,
                border: false,
                rownumbers: true,
                striped: true,
                pageList: [30, 50, 100],
                pagination: true,
                toolbar: toolbar,
                url: "${pageContext.request.contextPath }/regionAction_pageQuery",
                idField: 'id',
                columns: columns,
                onDblClickRow: doDblClickRow
            });

            $('#addRegionWindow').window({
                title: '添加修改区域',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 400,
                resizable: false,
                onBeforeClose: function () {
                    $("#addRegionWindow").form("clear");
                }
            });

            $('#queryRegionWindow').window({
                title: '添加修改区域',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 400,
                resizable: false,
                onBeforeClose: function () {
                    $("#addRegionWindow").form("clear");
                }
            });

            $("#button-import").upload({
                name: 'upload',
                action: '${pageContext.request.contextPath }/regionAction_importData',
                enctype: 'multipart/form-data',
                onSelect: function () {
                    this.autoSubmit = false;
                    var re = /^.+[\.xls|\.xlsx]$/;
                    if (re.test(this.filename())) {
                        this.submit();
                    } else {
                        $.messager.alert('警告', '必须上传excel表格', 'warning');
                    }
                },
                onComplete: function (data) {
                    if (data) {
                        $.messager.alert('信息', '文件上传成功', 'info');
                    } else {
                        $.messager.alert('错误', '文件上传失败', 'error')
                    }
                }

            })

        });

        function doDblClickRow(rowIndex, rowData) {
            $("#addRegionWindow").window("open");
            $("#addRegionForm").form("load", rowData);
            $("#id").unbind("blur")
            id_flag = true;
        }
    </script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
<div region="center" border="false">
    <table id="grid"></table>
</div>

<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false"
     maximizable="false" style="top:20px;left:200px">
    <div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
        <div class="datagrid-toolbar">
            <a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
        </div>
    </div>

    <div region="center" style="overflow:auto;padding:5px;" border="false">
        <form id="addRegionForm" action="${pageContext.request.contextPath }/regionAction_saveRegion" method="post">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="2">区域信息</td>
                </tr>
                <tr>
                    <td>区域编号</td>
                    <td><input type="text" name="id" id="id" class="easyui-validatebox" required="true"/>
                        <span id="id_span"></span>
                    </td>
                </tr>
                <tr>
                    <td>省</td>
                    <td><input type="text" name="province" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td>市</td>
                    <td><input type="text" name="city" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td>区</td>
                    <td><input type="text" name="district" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td>邮编</td>
                    <td><input type="text" name="postcode" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td>简码</td>
                    <td><input type="text" name="shortcode" class="easyui-validatebox" required="true"/></td>
                </tr>
                <tr>
                    <td>城市编码</td>
                    <td><input type="text" name="citycode" class="easyui-validatebox" required="true"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>


<div class="easyui-window" title="区域查询" id="queryRegionWindow" collapsible="false" minimizable="false"
     maximizable="false" style="top:20px;left:200px">

    <div region="center" style="overflow:auto;padding:5px;" border="false">
        <form id="queryRegionForm" action="${pageContext.request.contextPath }/regionAction_queryPage" method="post">
            <table class="table-edit" width="80%" align="center">
                <tr class="title">
                    <td colspan="2">区域信息</td>
                </tr>
                <tr>
                    <td>省</td>
                    <td><input type="text" id="qprovince"/></td>
                </tr>
                <tr>
                    <td>市</td>
                    <td><input type="text" id="qcity"/></td>
                </tr>
                <tr>
                    <td>区</td>
                    <td><input type="text" id="qdistrict"/></td>
                </tr>
                <tr>
                    <td>邮编</td>
                    <td><input type="text" id="qpostcode"/></td>
                </tr>
                <tr>
                    <td>简码</td>
                    <td><input type="text" id="qshortcode"/></td>
                </tr>
                <tr>
                    <td>城市编码</td>
                    <td><input type="text" id="qcitycode"/></td>
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