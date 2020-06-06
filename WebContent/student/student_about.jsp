<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>上机考试系统</title>

</head>
<style type="text/css">
    body {
        background: #f8f6e9;
    }
</style>
<body>
<%
    if (session.getAttribute("info") != null) {
        out.print(session.getAttribute("info"));
        session.removeAttribute("info");
    }
%>
<jsp:include page="student_head.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <h3>
                <strong>试卷下载</strong>
            </h3>
            <div class="alert"
                 style="display:;background-color: gray; margin-top: 5px">
                <span style="color: white;">本次考试没有电子试卷</span>
            </div>
            <a class="btn btn-default" href="../examDownload?e_name=${student.stu_exam }"
               style="display: ">下载查看</a>
        </div>
        <div class="col-md-6">
            <h3>
                <strong>答案上传</strong>
            </h3>
            <span style="font-size: 18px">请按照考试要求将答案文件打包，再次进行上传。同名文件多次上传将会覆盖。</span>
            <form class="form-inline" name="file" role="form"
                  action="../saveAnswer" onsubmit="return saveExam()" enctype="multipart/form-data"
                  method="post" style="margin-top: 12px; font-size: 18px">
                <input type="file" id="paper" name="paper">
                <button type="submit" style="margin-top: 10px;" class="btn btn-info">上传</button>
            </form>
        </div>
    </div>
    <div class="col-md-6">
        <table id="show" class="table" style="color: black">
        </table>
    </div>
</div>
</body>
<script type="text/javascript">
    function saveExam() {
        if ($("input[name='paper']").val().length == 0) {
            alert("请选择文件");
            return false;
        } else {
            return true;
        }
    }
</script>
</html>