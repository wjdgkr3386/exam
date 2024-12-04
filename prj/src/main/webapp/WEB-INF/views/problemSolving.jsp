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


//양 옆에 공백이 있다면 true 리턴하는 메소드
function toBlank() {
    var tableObj = $(".mainTable");
    var rows = tableObj.find("tr:gt(0)");
    
    for (var i = 0; i < rows.length; i++) {
		var tArea = rows.eq(i).find("td").eq(2).find("textarea");
		tArea.val(tArea.val().trim());
    }
    return false;
}

function checkID(){
	if($("[name='examForm']").find("[name='nickname']").val().trim()===""){
		alert("닉네임을 입력해주세요");
		return true;
	}
	return false;
}

function examSubmit(){
	var obj = $("[name='examForm']");
	 toBlank();
	 if(checkID()==true){ return; }
	 ajax(
             "/exam_solving_Proc.do"
             ,"post"
             ,obj
             ,function(responseJson){
					var solvCnt = responseJson["solvCnt"];
					if(solvCnt==0){
						alert("제출 실패");
					}else if(solvCnt==1){
						alert("제출 성공!");
           				location.replace('/main.do');
             		}else if(solvCnt==2){
					alert("이미 있는 닉네임 입니다");
       				return;
         			}
             }
      );
}


</script>
</head>
<body>
<center>
<form name="examForm">
<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
<input type="button" value="제출" onclick="examSubmit()">
<br><br>
시험 제목: <big>${requestScope.examList[0].EXAM_TITLE}</big>
<br>

<div class="file_div">
	<ul>
		<c:forEach var="i" items="${requestScope.file_nameList}">
		    <a href="/img/${requestScope.examList[0].EXAM_CODE}/${i.FILE_NAME}" download>${i.FILE_NAME}</a><br>
		</c:forEach>
	</ul>
</div>

닉네임 : <input type="text" name="nickname" placeholder="닉네임 입력">
<br><br>
<table class="mainTable">
	<tr style="background-color : lightgray;" class="headerTr">
		<td class="problem_no">번호</td>
		<td class="problem_content">문제</td>
		<td class="problem_answer">답안지</td>
	</tr>
	<c:forEach var="i" items="${requestScope.examList}" varStatus="vs">
		<tr>
			<td class="problem_no"><input type="hidden" name="problem_no">${i.PROBLEM_NO}</td>
			<td class="problem_content">${i.PROBLEM_CONTENT}</td>
			<td class="problem_answer"><textarea rows='5' cols='20' name='problem_answer' spellcheck='false'></textarea></td>
		</tr>
	</c:forEach>
</table>
	<input type="hidden" name="exam_code" value="${requestScope.examList[0].EXAM_CODE}">
</form>
</center>
</body>
</html>
