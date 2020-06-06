<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上机考试系统</title>
</head>
<body>

	<%
		if(session.getAttribute("info")!=null){
			out.print(session.getAttribute("info"));
			session.removeAttribute("info");
		}
	%>
	<jsp:include page="teacher_index.jsp"></jsp:include>
	<div class="container">
		<div class="alert navbar-inverse"
			style="background-color: #eeeeee; margin-top: 20px">
			<strong style="margin-left: 20px; font-size: 18px">当前考试正在进行中</strong>
			<strong style="margin-left: 20px; font-size: 18px">添加单个学生</strong>
			<form class="form-inline" style="margin-left: 20px; margin-top: 5px;" onsubmit="return add()">
				<div class="form-group">
					<input type="text" class="form-control" style="width: 250px"
						placeholder="学号*" name="stu_id"> <input type="text"
						class="form-control" style="width: 250px" placeholder="姓名*"
						name="stu_name"> <input type="text" class="form-control"
						style="width: 250px" placeholder="班级*" name="stu_class"> <input
						type="submit" class="btn btn-info" value="添加" />
				</div>
			</form>
		</div>
		<table class="table table-bordered" style="margin-top: 10px">
			<tr>
				<th class="col-md-3">学号</th>
				<th class="col-md-3">姓名</th>
				<th class="col-md-3">班级</th>
			</tr>
			<tbody id="tbody"></tbody>
		</table>
	</div>
</body>
<script type="text/javascript">

	$(function() {
		retHtml();
	})
	
	function add(){
		$.post("../addStudent",$('.form-inline').serialize(),function(){
			retHtml();
		})
		alert("添加成功！");
		return false;
	}
	
	function retHtml(){
		$.post("../getStartExamInfo", function(data) {
			var result = "";
			for (var i = 0; i < data.length; i++) {
				result+="<tr>"
				result+="<td>"+data[i].stu_id +"</td>";
				result+="<td>"+data[i].stu_name +"</td>";
				result+="<td>"+data[i].stu_class +"</td>";
				result+="</tr>";
			}
			$("#tbody").html(result);
		});
	}
</script>
</html>