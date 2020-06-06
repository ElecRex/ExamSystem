<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上机考试系统</title>
</head>

<body>
	<jsp:include page="admin_head.jsp"></jsp:include>
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
	<script type="text/javascript">

	$(function() {
		retHtml();
	})
	
	function stopExam(name){
		var a = confirm("确定停止当前考试?");

		if (a == true) {
			$.post("../stopExam", {'name':name}, function() {
				retHtml();
			})
			return true;
		} else {
			return false;
		}
	}
	
	function clearExam(name){
		var a = confirm("确定清楚考试资料?");

		if (a == true) {
			$.post("../clearExam", {'name':name}, function() {
				retHtml();
			})
			return true;
		} else {
			return false;
		}
		
	}
	function retHtml(){
		$.post("../getAllExams", function(data) {
			var result = "";
			for (var i = 0; i < data.length; i++) {
				result+="<tr>"
				result+="<td>"+data[i].e_name +"</td>";
				result+="<td>"+data[i].e_starttime +"</td>";
				result+="<td>"+data[i].e_teacher +"</td>";
				result+="<td>";
				if(data[i].e_examination!=null)
				result+="<span class='glyphicon glyphicon-ok'></span>";
				result+="</td>";
				result+="<td>";
				if(data[i].e_autostart==1)
				result+="<span class='glyphicon glyphicon-ok'></span>";
				result+="</td>";
				result+="<td>";
				if(data[i].e_isend==1)
				result+="<span class='glyphicon glyphicon-ok'></span>";
				result+="</td>";
				result+="<td>";
				if(data[i].e_file==1)
				result+="<span class='glyphicon glyphicon-ok'></span>";
				result+="</td>";
				result+="<td>";
				if(data[i].e_clear==1)
				result+="<span class='glyphicon glyphicon-ok'></span>";
				result+="</td>";
				result+="<td>";
				if(data[i].e_isstart==0)
				result+="<span>考试未开启</span>";
				if(data[i].e_isend==0 && data[i].e_isstart==1)
				result+="<a href='javascript:;' onclick='stopExam("+'"'+data[i].e_name+'"'+")' title='停止考试' class='btn btn-info'>停止考试</a>";
				if(data[i].e_isend==1 && data[i].e_clear==0)
                    result += "<a href='../teacherDownload?e_name=" + data[i].e_name + "'" + "title='下载考生答案' class='btn btn-info'>下载</a>&nbsp;&nbsp;<a href='javascript:;' onclick='clearExam(" + '"' + data[i].e_name + '"' + ")' title='清理考试' class='clear_btn btn btn-info'>清理考试</a>";
				result+="</td>";
				result+="</tr>";
			}
			$("#tbody").html(result);
		});
	}
	$(".clear_btn").click(function() {
		var a = confirm("确定删除?");

		if (a == true) {
			return true;
		} else {
			return false;
		}
	})
</script>
</body>
</html>