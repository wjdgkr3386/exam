
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%@include file="/WEB-INF/views/common.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>EXAM</title>

<script>
	$(function(){   init();  })

	function init(){
   	 //엔터를 눌렀을때 폼 제출 동작 제어
   	 $(document).on("keypress", "input[type='text']", function(event) {
            // 엔터 키의 키코드는 13
            if (event.which === 13) {
                event.preventDefault(); // 기본 제출 동작 제어
            }
        });
	}
	function submit(){
        var midVal = $("[name='mid']").val();
        var pwdVal = $("[name='pwd']").val();
		
        if((midVal.length <= 10 && midVal.length >= 6 && /^[a-zA-Z0-9]{4}$/.test(pwdVal))===false){
        	alert("아이디는 6~10자리, 비밀번호는 알파벳과 숫자로 4자리로만 입력하세요");
        	return;
        }
		var obj = $("[name='signinForm']");
		ajax(
				"/signinProc.do"
				,"post"
				,obj
				,function(checkCnt){
					if( checkCnt==0 ){
						alert("실패");
					}else if(checkCnt==1){
						alert("이미 존재하는 아이디입니다.");
					}else if(checkCnt==2){
						alert("성공");
						location.replace("/loginForm.do");
					}
				}
		);
	}
</script>

</head>
<body>
<center>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br>
회원가입<br><br>
<form name="signinForm">
	아  이  디: <input type="text" name="mid" maxlength="10"><br>
	비밀번호: <input type="password" name="pwd" maxlength="4"><br>
</form>
	<input type="button" value="제출" onclick="submit()">
</center>
</body>
</html>














