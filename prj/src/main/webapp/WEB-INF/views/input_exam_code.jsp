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
	 //엔터를 눌렀을때 폼 제출 동작 제어
	 $(document).on("keypress", "input[type='text']", function(event) {
        // 엔터 키의 키코드는 13
        if (event.which === 13) {
            event.preventDefault(); // 기본 제출 동작 제어
        }
    });
}

function goExam(){
	var formObj = $("[name='exam_codeForm']");
	if($("[name='exam_code']").val()==""){
		alert("시험코드를 입력해주세요.");
		return;
	}
	ajax(
            "/checkExamProc.do"
            ,"post"
            ,formObj
            ,function(responseJson){
            	var codeCnt = responseJson["checkCnt"];
            	if(codeCnt==0){
            		alert("시험코드가 잘못되었습니다. 시험코드를 다시 확인해주세요.");
            	}else if(codeCnt==1){
            		alert("성공");
            		document.exam_codeForm.submit();
            	}else if(codeCnt==2){
            		alert("이미 시험을 봤습니다. 재시험을 할 수 없습니다.")
            	}
            }
     );
	
}
</script>
</head>
<body>
<center>
<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
<form name="exam_codeForm" action="/problemSolving.do" method="post">
시험 코드 입력 : 
<input type="text" name="exam_code" placeholder="시험코드">
<input type="button" value="확인" onclick="goExam()">
</form>
</center>
</body>
</html>
