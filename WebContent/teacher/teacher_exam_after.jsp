<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>考后操作</title>
</head>
<body>
<jsp:include page="teacher_index.jsp"></jsp:include>
<%
    if (session.getAttribute("downloadTip") != null) {
        out.print(session.getAttribute("downloadTip"));
        session.removeAttribute("downloadTip");
    }
%>
<div class="container">
    <table class="table table-bordered" style="margin-top: 10px">
        <tr>
            <th class="col-md-1">考试名称</th>
            <th class="col-md-2">考试时间</th>
            <th class="col-md-1">创建人</th>
            <th class="col-md-1">上传试卷</th>
            <th class="col-md-1">自动开始</th>
            <th class="col-md-1">已结束</th>
            <th class="col-md-1">已归档</th>
            <th class="col-md-1">已清理</th>
            <th class="col-md-2"></th>
        </tr>
        <tbody id="tbody"></tbody>
    </table>
</div>
</body>
<script type="text/javascript">

    $(function () {
        retHtml();
    })

    function stopExam(name) {
        var a = confirm("确定停止当前考试?");

        if (a == true) {
            $.post("../stopExam", {'name': name}, function () {
                retHtml();
            })
            return true;
        } else {
            return false;
        }
    }

    function clearExam(name) {
        var a = confirm("确定清楚考试资料?");

        if (a == true) {
            $.post("../clearExam", {'name': name}, function () {
                retHtml();
            })
            return true;
        } else {
            return false;
        }

    }

    function retHtml() {
        $.post("../getAllExams", function (data) {
            var result = "";
            for (var i = 0; i < data.length; i++) {
                result += "<tr>"
                result += "<td>" + data[i].e_name + "</td>";
                result += "<td>" + data[i].e_starttime + "</td>";
                result += "<td>" + data[i].e_teacher + "</td>";
                result += "<td>";
                if (data[i].e_examination != null)
                    result += "<span class='glyphicon glyphicon-ok'></span>";
                result += "</td>";
                result += "<td>";
                if (data[i].e_autostart == 1)
                    result += "<span class='glyphicon glyphicon-ok'></span>";
                result += "</td>";
                result += "<td>";
                if (data[i].e_isend == 1)
                    result += "<span class='glyphicon glyphicon-ok'></span>";
                result += "</td>";
                result += "<td>";
                if (data[i].e_file == 1)
                    result += "<span class='glyphicon glyphicon-ok'></span>";
                result += "</td>";
                result += "<td>";
                if (data[i].e_clear == 1)
                    result += "<span class='glyphicon glyphicon-ok'></span>";
                result += "</td>";
                result += "<td>";
                if (data[i].e_isstart == 0)
                    result += "<span>考试未开启</span>";
                if (data[i].e_isend == 0 && data[i].e_isstart == 1)
                    result += "<a href='javascript:;' onclick='stopExam(" + '"' + data[i].e_name + '"' + ")' title='停止考试' class='btn btn-info'>停止考试</a>";
                if (data[i].e_isend == 1 && data[i].e_clear == 0)
                    result += "<a href='../teacherDownload?e_name=" + data[i].e_name + "'" + "title='下载考生答案' class='btn btn-info'>下载</a>&nbsp;&nbsp;<a href='javascript:;' onclick='clearExam(" + '"' + data[i].e_name + '"' + ")' title='清理考试' class='clear_btn btn btn-info'>清理考试</a>";
                result += "</td>";
                result += "</tr>";
            }
            $("#tbody").html(result);
        });
    }
</script>

</html>