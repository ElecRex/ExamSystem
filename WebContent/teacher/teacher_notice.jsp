<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上机考试系统</title>
</head>
<body>
	<jsp:include page="teacher_index.jsp"></jsp:include>
	<div class="container">
		<div class="alert navbar-inverse"
			style="background-color: #eeeeee; margin-top: 20px">
			<strong style="margin-left: 20px; font-size: 18px">新增通知消息</strong>
			<form class="form-inline" role="form" action="../teacherInformation"
				method="post" style="margin-left: 20px; margin-top: 5px;">
				<div class="form-group">
					<input type="text" name="information" class="form-control"
						style="width: 250px" placeholder="通知消息内容*">
                    <input type="submit" class="btn btn-info" value="发送" />
				</div>
			</form>
		</div>

		<div class="alert navbar-inverse"
			style="background-color: #eeeeee; margin-top: 20px">
			<strong style="margin-left: 20px; font-size: 18px">已有通知消息</strong>
			<form class="form-inline" role="form"
				style="margin-left: 20px; margin-top: 5px;">
				<table class="table table-bordered" style="margin-top: 10px">
					<tr>
						<th class="col-md-12"  rowspan="1" style="text-align:center">通知内容</th>
					<%
							if(session.getAttribute("notice") != null){
							    out.print("<tr><td>"+ session.getAttribute("notice") + "</td></tr>");
							}
					%></tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>