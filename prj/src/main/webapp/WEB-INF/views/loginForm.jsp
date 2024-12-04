
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
   	 
		$(".loginBtn").bind("click",function(){
			checkLoginForm();			
		});
		$(".signinBtn").bind("click",function(){
			checkSigninForm();			
		});
		$("[name='mid']").val("${cookie.mid.value}");
		$("[name='pwd']").val("${cookie.pwd.value}");
	
		$("[name='autoLogin']").prop("checked", ${empty cookie.mid.value?false:true} );

	}
	function checkLoginForm(){	
		var mid = $("[name='mid']").val();
		if( typeof(mid)!="string" ) { mid=""; }
		mid = $.trim(mid);
		

		var pwd = $("[name='pwd']").val();
		if( typeof(pwd)!="string" ) { pwd=""; }
		pwd = $.trim(pwd);
		if( mid=="" ){
			alert("아이디가 비어 있음! 입력 바람");
			return;
		}
		if( pwd=="" ){
			alert("암호가 비어 있음! 입력 바람");
			return;
		}
			
		ajax(
				"/loginProc.do"
				,"post"
				,$("[name='loginForm']")
				,function(idCnt){
					if( idCnt==1 ){
						location.replace("/main.do");
					}
					else{
						alert("로그인 실패! 아이디 또는 암호가 틀립니다. 재입력해 주십시요!");
					}
				}
		);
	}		
	
	function checkSigninForm(){	
		location.replace( "/signin.do" );
	}	
</script>

</head>
<body>
	<center>
<span style="cursor:pointer; font-size: 2.0em" onclick="location.replace('/main.do')">EXAM</span><br><br>
		<form name="loginForm">
			<table class="tableA11" >
				<tr>
					<th bgcolor="lightgray">아이디</th> 
					<td>
						<input type="text" name="mid">
					</td> 
				</tr> 
				<tr>
					<th bgcolor="lightgray">암 호</th> 
					<td>
						<input type="password" name="pwd">
					</td> 
				</tr> 
			</table>
			<div style="height:5px"></div>
			<input type="checkbox" name="autoLogin" value="yes" class="autoLogin">아이디/암호 자동입력<br>
			<div style="height:5px"></div>
			<input type="button" value="로그인" class="loginBtn">
			<input type="button" value="회원가입" class="signinBtn">
		</form>
	</center>
</body>
</html>














