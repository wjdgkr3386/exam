<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>EXAM</title>
<script>
$(function () { init(); });

function init() {
    // 초기화 작업
}



	
}
</script>
</head>
<body>
<center>
<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
	<input type="button" value="시험 만들기"  onclick="location.replace('/search_exam.do')">
	&nbsp;&nbsp;&nbsp;
	<input type="button" value="시험 풀기"  onclick="location.replace('/input_exam_code.do')">
</center>
</body>
</html>
