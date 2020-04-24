layui.use(['element', 'table', 'form', 'upload'], () => {

    var $ = layui.jquery;
    var element = layui.element;
    var form = layui.form;
    var upload = layui.upload;

    var children = null;
    $.ajax({
        url: '/getChildren',
        async: false,
        success: function (data) {
            children = data;
        }
    })

    var curProgram = null;

    generateNav(children);
    element.render('nav');

    var active = {
        tabAdd: function (id, url, name) {
            element.tabAdd('demo', {
                id: id,
                title: name,
                content: '<iframe style="width: 100%; height: 85%; margin: 0 auto;" data-frameid="'+id+'" frameborder="0" src="src/'+url+'.html"></iframe>',
            });
        },
        tabChange: function(id) {
            //切换到指定Tab项
            element.tabChange('demo', id); //根据传入的id传入到指定的tab项
        }
    };
    $('.site-demo-active').on('click', function() {
        var dataid = $(this);
        curProgram = dataid.attr("data-url");
        var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
        $.each($(".layui-tab-title li[lay-id]"), function () {
            //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
            if ($(this).attr("lay-id") == dataid.attr("data-id")) {
                isData = true;
            }
        });
        if (isData == false) {
            //标志为false 新增一个tab项
            active.tabAdd(dataid.attr("data-id"), dataid.attr("data-url"), dataid.attr("data-title"));
        }
        //最后不管是否新增tab，最后都转到要打开的选项页面上
        active.tabChange(dataid.attr("data-id"));
    });

    $("#addProcess").on('click', function () {
        layer.open({
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['420px', '240px'], //宽高
            content: $("#upload")
        });
        var index = 0;
        upload.render({
            elem: '#multipartFile',
            url: '/upload',
            accept: 'file',
            before: function() {
                index = layer.load(1, {
                    shade: [0.1,'#fff']
                });
            },
            done: function (res) {
                if (res.code == "success") {
                    layer.close(index);
                    layer.msg("上传成功！");
                    //TODO 刷新
                    // window.location.reload();
                } else layer.msg(res.msg);
            },
            error: function () {
                layer.close(index);
                layer.msg("上传失败，请重试！");
            }
        });
    });

    $("#viewSource").on('click', function () {
        if (curProgram == null) {
            layer.msg("请先打开一个已有的程序！！！");
            return;
        }
        layer.open({
            type: 2,
            title: curProgram.replace(/_/g, "/") + ".java",
            shadeClose: true,
            shade: 0.8,
            area: ['80%', '90%'],
            content: '/srcCode/' + curProgram
        });
    });

    $("#analysis").on('click', function () {
        if (curProgram == null) {
            layer.msg("请先打开一个待分析的程序");
            return;
        }
        $("#read").empty();
        $("#read").append($('<option value="0">请选择一个读或写变迁</option>'));
        $("#write").empty();
        $("#write").append($('<option value="0">请选择一个写变迁</option>'));
        var fm = $("#fm").parent();
        form.render('select');
        getVar($("#variable"));
        layer.open({
            type: 1,
            title: "分析数据竞争",
            skin: 'layui-layer-rim', //加上边框
            area: ['520px', '400px'], //宽高
            content:fm
        });
    });

    $("#analysis2").on('click', function () {
        if (curProgram == null) {
            layer.msg("请先打开一个待分析的程序");
            return;
        }
        $("#read2").empty();
        $("#read2").append($('<option value="0">请选择一个读或写语句的行号</option>'));
        $("#write2").empty();
        $("#write2").append($('<option value="0">请选择一个写语句的行号</option>'));
        var fm = $("#fm2").parent();
        form.render('select');
        getVar($("#variable2"));
        layer.open({
            type: 1,
            title: "分析数据竞争",
            skin: 'layui-layer-rim', //加上边框
            area: ['520px', '400px'], //宽高
            content:fm
        });
    });

    $("#isConcurrent").on('click', function () {
        var variable = $("#variable").val();
        var read = $("#read").val();
        var write = $("#write").val();
        if (variable == 0) {
            layer.msg("请先选择一个变量！");
            return;
        }
        if (read == 0 || write == 0) {
            layer.msg("请先选择两个变迁！");
            return;
        }
        $.ajax({
            url: "/isConcurrent",
            data: {
                cur: curProgram,
                t1: read,
                t2: write
            },
            success: function (result) {
                if (result == true) {
                    layer.msg("这两个变迁可能并发执行，程序存在潜在的数据竞争！");
                } else {
                    layer.msg("这两个变迁不会并发执行！");
                }
            },
            error: function (result) {
                layer.msg("服务器连接出错，请重试！")
            }
        });
        return false;
    });


    $("#isConcurrent2").on('click', function () {
        var variable = $("#variable2").val();
        var read = $("#read2").val();
        var write = $("#write2").val();
        if (variable == 0) {
            layer.msg("请先选择一个变量！");
            return;
        }
        if (read == 0 || write == 0) {
            layer.msg("请先选择两个语句的行号！");
            return;
        }
        $.ajax({
            url: "/isConcurrent2",
            data: {
                cur: curProgram,
                l1: read,
                l2: write
            },
            success: function (result) {
                if (result == true) {
                    layer.msg("这两行语句可能并发执行，程序存在潜在的数据竞争！");
                } else {
                    layer.msg("这两行语句不会并发执行！");
                }
            },
            error: function (result) {
                layer.msg("服务器连接出错，请重试！")
            }
        });
        return false;
    });

    form.on('select(variable)', function (data) {
        if (data.value == 0) {
            layer.msg("请先选择一个变量");
            return;
        }
        getRead(data.value, "/getRead", $("#read"), true);
        getWrite(data.value, "/getWrite", $("#write"), true);
    });

    form.on('select(variable2)', function (data) {
        if (data.value == 0) {
            layer.msg("请先选择一个变量");
            return;
        }
        getRead(data.value, "/getReadLine", $("#read2"), false);
        getWrite(data.value, "/getWriteLine", $("#write2"), false);
    });

    function getVar(variable) {
        if (curProgram == null) return;
        variable.empty();
        variable.append($('<option value="0">请选择一个变量</option>'));
        $.ajax({
            url: "/getVar",
            dataType: "json",
            async: false,
            data: {
                cur: curProgram
            },
            success: function (result) {
                for (var i = 0; i<result.length; ++i) {
                    var option = $("<option value='" + result[i] + "'>" + result[i] + "</option>");
                    variable.append(option);
                }
                form.render('select');
            }
        });
    }

    function getRead(variable, url, read, flag) {
        read.empty();
        if (flag) read.append($('<option value="0">请选择一个读或写变迁</option>'));
        else read.append($('<option value="0">请选择一个读或写语句的行号</option>'));
        $.ajax({
            url: url,
            dataType: "json",
            data: {
                cur: curProgram,
                variable: variable
            },
            success: function (result) {
                for (var i = 0; i<result.length; ++i) {
                    var option = $("<option value='" + result[i] + "'>" + result[i] + "</option>");
                    read.append(option);
                }
                form.render('select');
            }
        });
    }
    function getWrite(variable, url, write, flag) {
        write.empty();
        if (flag) write.append($('<option value="0">请选择一个写变迁</option>'));
        else write.append($('<option value="0">请选择一个写语句的行号</option>'));
        $.ajax({
            url: url,
            dataType: "json",
            data: {
                cur: curProgram,
                variable: variable
            },
            success: function (result) {
                for (var i = 0; i<result.length; ++i) {
                    var option = $("<option value='" + result[i] + "'>" + result[i] + "</option>");
                    write.append(option);
                }
                form.render('select');
            }
        });
    }

    function generateNav(childArry) {
        var child = $("#child");
        var dl = $("<dl class='layui-nav-child'></dl>");
        for (var j in childArry) {
            var a = $("<a data-id='" + j + "' data-url='" + childArry[j].href + "' data-title='"
                + childArry[j].title + "' href='#' class='site-demo-active' data-type='tabAdd'>" + childArry[j].title + "</a>")
            var dd = $("<dd></dd>");
            dd.append(a);
            dl.append(dd);
        }
        child.append(dl);
    }
});