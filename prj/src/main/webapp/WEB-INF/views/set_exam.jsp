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

    function save(){
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
    	var formObj = $("[name='examInputForm']");
        if (isEmpty()===true) {
            alert("값이 비어있는 문제가 있습니다.");
            return;
        }
        
        $("[name='exam_code']").val(rCode(10));
        
    	ajax(
                "/exam_input_Proc.do"
                ,"post"
                ,formObj
                ,function(responseJson){
                	var inputCnt = responseJson["problemCnt"];
                	if(inputCnt==0){
                		alert("입력 실패");
                	}else if(inputCnt==1){
                		alert("입력 성공!");
                        document.examInputForm.submit();
                	}else if(inputCnt==2){	//만약 시험코드가 중복되었다면 다시 실행
                		save();
                	}else if(inputCnt==3){
                		alert("이미 있는 제목입니다. 다른 제목을 입력해주세요.");
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


</script>
</head>
<body>
<center>
<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
<div>
	<input type="button" value="일시저장" onclick="save()">
</div><br>
<form name="examInputForm" action="/updel_exam.do" method="post">
시험 제목 : <input type="text" name="exam_title" size="100" maxlength="50">
<br><br>
<table class="mainTable">
	<tr style="background-color : lightgray;" class="headerTr">
		<td class="problem_no">번호</td>
		<td class="problem_content">문제</td>
		<td class="problem_answer">정답</td>
		<td class="addDel"></td>
	</tr>
	<tr>
		<td class="problem_no">1</td>
		<td class="problem_content"><textarea rows='5' cols='150' maxlength='1000' name='problem_content' spellcheck="false"></textarea></td>
		<td class="problem_answer"><textarea rows='5' cols='20' name='problem_answer' spellcheck="false"></textarea></td>
		<td class="addDel"><input type='button' value='추가' onclick='addBtn()'><input type='button' value='삭제' onclick='delBtn()'></td>
	</tr>
</table>
<input type="hidden" name="exam_code">
<input type="hidden" name="mid" value="${requestScope.mid}">
<input type="hidden" name="is_last_save" value="미완성">
</form>
</center>
</body>
</html>