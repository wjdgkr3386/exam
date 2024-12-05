<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>EXAM</title>
<script>
$( function (){ init(); } );

function init(){}




function checkSearch(){
	var examineeCount = ${requestScope.examineeCount};
	if(examineeCount==0) { return; }
	 
	var formObj = $("[name='checkForm']");
	
    ajax(
            "/problemCheck.do",
            "post",
            formObj,
            function (responseHtml) {
                $(".mainTable").html($(responseHtml).find(".mainTable").html());
                $(".subTable").html($(responseHtml).find(".subTable").html());
            }
        );
}


</script>
</head>
<body>
<form name="checkForm">
<center>
<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
시험명: ${requestScope.exam_title}
<br>
응시자 ${requestScope.examineeCount}명
<br>
<select name="problem_no_search" onchange="checkSearch()">
	<c:forEach var="i" begin="1" end="${requestScope.problemMax}">
		<option value="${i}">${i}</option>
	</c:forEach>
</select> 번 문제

<br><br>
<table class="mainTable">
	<tr style="background-color: lightgray;">
		<th class="problem_no">문제 번호</td>
		<th class="problem_content">문제 내용</td>
		<th class="problem_answer">문제 정답</td>
	</tr>
	<c:forEach var="i" items="${requestScope.checkList}" varStatus="vs" end="0">
	<tr>
		<td class="problem_no">${i.PROBLEM_NO}</td>
		<td class="problem_content">${i.PROBLEM_CONTENT}</td>
		<td class="problem_answer">${i.PT_PROBLEM_ANSWER}</td>
	</tr>
	</c:forEach>
</table>
<br><br><br><br><br>
<table class="subTable">
	<tr style="background-color: lightgray;">
		<th class="nickname">닉네임</td>
		<th class="check">문제풀이</td>
	</tr>
	<c:if test="${requestScope.examineeCount ne 0}">
		<c:forEach var="i" items="${requestScope.checkList}" varStatus="vs" end="${requestScope.examineeCount-1}">
			<tr>
				<td class="nickname">${i.NICKNAME}</td>
				<td class="problem_answer">${i.PROBLEM_ANSWER}</td>
			</tr>
		</c:forEach>
	</c:if>
</table>
</center>













<input type="hidden" name="exam_code" value="${requestScope.checkList[0].EXAM_CODE}">
</form>
</body>
</html>
