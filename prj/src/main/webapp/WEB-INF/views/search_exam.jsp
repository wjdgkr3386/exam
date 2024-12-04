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

     function search() {
         var searchObj = $("[name='searchForm']");
         
         var keyword = searchObj.find("[name='keyword']").val().trim();
         searchObj.find("[name='keyword']").val(keyword);
         
         ajax(
             "/search_exam.do",
             "post",
             searchObj,
             function (responseHtml) {
                 var obj = $(responseHtml);
                 // 검색 결과 및 페이지 번호 업데이트
                 $(".pageNos").html(obj.find(".pageNos").html());
                 $(".mainTable").html(obj.find("[name='searchForm'] .mainTable").html());
             }
         );
     }

     function pageNoClick(clickPageNo) {
         var formObj = $("[name='searchForm']");
         formObj.find("[name='selectPageNo']").val(clickPageNo);
         search();
     }

     function goExamUpdelForm(exam_code) {
         $("[name='examUpdelForm'] [name='exam_code']").val(exam_code);
         document.examUpdelForm.submit();
     }
     

</script>
</head>
<body>
<center>
    <form name="searchForm">
        <input type="hidden" name="selectPageNo" value="1">


<div style="float:right">
	<input type="button" value="로그아웃" onclick="location.replace('/loginForm.do')">
</div><br><br>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
        시험 제목 : 
        <input type="text" name="keyword" placeholder="제목검색">&nbsp;<input type="button" value="검색" onclick="search()">&nbsp;
        <input type="button" value="시험 만들기" onclick="location.replace('/set_exam.do')"><br>
        <div style="height:9px"></div>

        <table class="mainTable">
            <thead>
                <tr style="background-color: lightgray;">
                    <th class='problem_no'>번호</th>
                    <th class='problem_content'>제목</th>
                    <th class='updelBtn'>수정/삭제/채점</th>
                    <th class='resultStatus'>완성도</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="i" items="${requestScope.examMap.examList}" varStatus="vs">
                    <tr>
                        <td>${vs.index + 1}</td>
                        <td>${i.EXAM_TITLE}</td>
                        <td width='120px'>
                            <c:choose>
                                <c:when test="${i.IS_LAST_SAVE eq '미완성'}">
                                    <input type="button" value="수정/삭제" onclick="goExamUpdelForm('${i.EXAM_CODE}')">
                                </c:when>
                                <c:when test="${i.IS_LAST_SAVE eq '최종'}">
                                    <input type="button" value="확인" onclick="goExamUpdelForm('${i.EXAM_CODE}')">
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${i.IS_LAST_SAVE}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="pageNos">
            <span style="cursor:pointer" onClick="pageNoClick(1)">[처음]</span>
            <span style="cursor:pointer" onClick="pageNoClick(${requestScope.examMap.selectPageNo}-1)">[이전]</span>&nbsp;&nbsp;

            <c:forEach var="pageNo" begin="${requestScope.examMap.begin_pageNo}" end="${requestScope.examMap.end_pageNo}">
                <c:choose>
                    <c:when test="${requestScope.examMap.selectPageNo==pageNo}">
                        ${pageNo}
                    </c:when>
                    <c:otherwise>
                        <span style="cursor:pointer" onClick="pageNoClick(${pageNo})">[${pageNo}]</span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            &nbsp;&nbsp;
            <span style="cursor:pointer" onClick="pageNoClick(${requestScope.examMap.selectPageNo}+1)">[다음]</span>
            <span style="cursor:pointer" onClick="pageNoClick(${requestScope.examMap.last_pageNo})">[마지막]</span>
        </div>
    </form>

    <form name="examUpdelForm" action="/updel_exam.do" method="post">
        <input type="hidden" name="exam_code">
	</form>
</center>
</body>
</html>
