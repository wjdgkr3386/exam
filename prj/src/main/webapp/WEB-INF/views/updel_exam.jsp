<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common.jsp" %>
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
	 
	//tooltip 찾아서 이벤트 부여하기
	$('#tooltip').hover(function(){
        // 마우스를 올렸을 때 텍스트 출력
        $("#tooltip").append("<span id='tooltip_text'><br>일시저장은 파일 저장이 안됩니다. 최종저장을 할 때 파일을 선택해 주세요.</span>");
    }, function(){
        // 마우스를 떼었을 때 텍스트 삭제
        $('#tooltip_text').remove();
    });
	
	var maxBtn = 10;
	var currentBtn = $('.file_div input[type="file"]').length;
	$('#addFileBtn').click(function(){
   		if(currentBtn<maxBtn){
            var fileInput = $('<input>').attr({type:'file', name:'file'});
            $('.file_div').append(fileInput);
            currentBtn++;
   		}else{
   			alert("최대 10개까지만 추가할 수 있습니다.");
   		}
	});
}

//추가버튼
function addBtn() {
    var tableObj = $(".mainTable");

    if (isEmpty()) {
        alert("제목, 문제, 정답 중에 값이 비어있는 곳이 있습니다.");
        return;
    }

    //행추가
    tableObj.append("<tr><td></td><td></td><td></td><td></td></tr>");
    var trCnt = tableObj.find("tr:gt(0)").length;
    var newProblem = tableObj.find("tr").eq(trCnt);
    newProblem.find("td").eq(0).attr('class', 'problem_no').html("<input type='hidden' name='problem_no' value='"+trCnt+"'>"+trCnt);
    newProblem.find("td").eq(1).attr('class', 'problem_content').html("<textarea rows='5' cols='150' maxlength='1000' name='problem_content' spellcheck='false'></textarea>");
    newProblem.find("td").eq(2).attr('class', 'problem_answer').html("<textarea rows='5' cols='20' name='problem_answer' spellcheck='false'></textarea>");
    newProblem.find("td").eq(3).attr('class', 'addDel').html("<input type='button' value='추가' onclick='addBtn()'><input type='button' value='삭제' onclick='delBtn()'>");
    
}

function delBtn() {
    var tableObj = $(".mainTable");
    var trObj = tableObj.find("tr:gt(0)");
    var rowToDelete = $(event.target).closest("tr");
    if(rowToDelete.index() == 1 && trObj.length==1){ 
    	alert("첫번째 문제는 삭제할 수 없습니다.");
    	return;
    }
	if(confirm( "삭제하시겠습니까?" )===false) { return; }
    rowToDelete.remove();
    update_row_numbers();
}

function update_row_numbers() {
    var tableObj = $(".mainTable");
    var trObj = tableObj.find("tr:gt(0)");
    for (var i = 0; i < trObj.length; i++) {
        trObj.eq(i).find("td").eq(0).text(i + 1);
        trObj.eq(i).find("td").eq(0).find("[name='problem_no']").val(i + 1);
    }
}

function update(){
	var formObj = $("[name='examUpdelForm']");
	
	var title = $("[name='exam_title']");
	title.val(title.val().trim());
	if(title.val()==="") { 
		alert("제목을 입력해주세요.");
		return;
	}
	var pattern = new RegExp( /^[^<]*$/ );
	if (!pattern.test(title.val())) {
        alert("제목에 < 는 들어갈 수 없습니다.");
        return;
    }
    if (isEmpty()===true) {
        alert("제목, 문제, 정답 중에 값이 비어있는 곳이 있습니다.");
        return;
    }

	ajax(
            "/exam_updel_Proc.do"
            ,"post"
            ,formObj
            ,function(responseJson){
            	var updateCnt = responseJson["updateCnt"];
            	if(updateCnt==1){
            		alert("저장 성공");
            		location.reload();
            	}else if(updateCnt==0){
            		alert("저장 실패");
            	}else if(updateCnt==-13){
            		alert("확장자는 'jpg', 'jpeg', 'png' 만 가능합니다.");
            	}
            }
     );
}

function deleteExam(){
	if(confirm( "시험을 삭제하시겠습니까?" )===false) { return; }
	var formObj = $("[name='examUpdelForm']");
	ajax(
            "/delete_exam_Proc.do"
            ,"post"
            ,formObj
            ,function(responseJson){
            	var deleteCnt = responseJson["deleteCnt"];
            	if(deleteCnt==1){
            		alert("삭제 성공");
                	location.replace('/search_exam.do');
            	}else if(deleteCnt!=1){
            		alert("삭제 실패");
            	}
            }
     );
}

//공백이 있다면 true 리턴하는 메소드
function isEmpty() {
    var tableObj = $(".mainTable");
    var rows = tableObj.find("tr:gt(0)");

    for (var i = 0; i < rows.length; i++) {
        for (var j = 1; j < 3; j++) {
            if (rows.eq(i).find("td").eq(j).find("textarea").val().trim() ==="") {
                return true;
            }
        }
    }
    return false;
}

function saveFinal(){
	if(confirm( "최종저장을 하면 더이상 수정할 수 없습니다. 정말 하시겠습니까?" )){
		$("[name='is_last_save']").val("최종");
		update();
	}
}

function checkExam(){
    var examCode = "${requestScope.examList[0].EXAM_CODE}";

    // 새로운 form 엘리먼트 생성
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/problemCheck.do";

    // exam_code 값을 담는 hidden input 추가
    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "exam_code";
    input.value = examCode;

    // form에 input 추가하고 바로 body에 추가하여 submit
    form.appendChild(input);
    document.body.appendChild(form).submit();
}


</script>

</head>
<body>
<center>
<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
<c:if test="${requestScope.examList[0].IS_LAST_SAVE ne '최종'}">
	<span>
		<input type="button" value="일시저장" onclick="update()">
		<input type="button" value="최종저장" onclick="saveFinal()">
	</span>
</c:if>
	<span>
		<input type="button" value="시험 삭제하기" onclick="deleteExam()">
	</span>
<c:if test="${requestScope.examList[0].IS_LAST_SAVE eq '최종'}">
	<span>
		<input type="button" value="채점하기" onclick="checkExam()">
	</span>
</c:if>
<br><br>
<form name="examUpdelForm">


<c:if test="${requestScope.examList[0].IS_LAST_SAVE ne '최종'}">
    <input type='button' id='addFileBtn' value='파일추가'><span id="tooltip"> (?)</span><br><br>
    <div class="file_div">
    
    </div>
</c:if>
시험 제목 : <input type="text" name="exam_title" size="100" maxlength="50" value="${requestScope.examList[0].EXAM_TITLE}"
		<c:if test="${requestScope.examList[0].IS_LAST_SAVE eq '최종'}">readonly</c:if>
		>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
시험코드 : ${requestScope.examList[0].EXAM_CODE}
<br><br>
<table class="mainTable">
	<tr style="background-color : lightgray;" class="headerTr">
		<td class="problem_no">번호</td>
		<td class="problem_content">문제</td>
		<td class="problem_answer">정답</td>
		<c:if test="${requestScope.examList[0].IS_LAST_SAVE ne '최종'}">
		<td class="addDel">추가/삭제</td>
		</c:if>
	</tr>
	<c:forEach var="i" items="${requestScope.examList}" varStatus="vs">
	<tr>
		<td class="problem_no"><input type="hidden" name="problem_no" value="${i.PROBLEM_NO}" >${i.PROBLEM_NO}</td>
		
		<td class="problem_content"><textarea rows='5' cols='150' maxlength='1000' name='problem_content' spellcheck='false'
		<c:if test="${requestScope.examList[0].IS_LAST_SAVE eq '최종'}">readonly</c:if>
		>${i.PROBLEM_CONTENT}</textarea></td>
		
		<td class="problem_answer"><textarea rows='5' cols='20' name='problem_answer' spellcheck='false'
		<c:if test="${requestScope.examList[0].IS_LAST_SAVE eq '최종'}">readonly</c:if>
		>${i.PROBLEM_ANSWER}</textarea></td>
		
			<c:if test="${requestScope.examList[0].IS_LAST_SAVE ne '최종'}">
			<td class="addDel">
				<input type='button' value='추가' onclick='addBtn()'>
				<input type='button' value='삭제' onclick='delBtn()'>
			</td>
			</c:if>
	</tr>
	</c:forEach>
</table>
<input type="hidden" name="exam_code" value="${requestScope.examList[0].EXAM_CODE}">
<input type="hidden" name="is_last_save" value="${requestScope.examList[0].IS_LAST_SAVE}">
</form>
</center>
</body>
</html>