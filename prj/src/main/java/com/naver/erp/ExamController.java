package com.naver.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CrossOrigin(origins = "*")
public class ExamController {
	

	@Autowired
	private ProblemDAO problemDAO;
	
	@Autowired
	private ProblemService problemService;

	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping( value="/set_exam.do" )
	public ModelAndView set_exam_Form( 	
			HttpSession session
	){
		ModelAndView mav = new ModelAndView();
		String mid = (String) session.getAttribute("mid");
		if(mid==null) { 
			mav.setViewName( "loginForm.jsp" );
			return mav;
		}
		
		mav.addObject("mid",mid);
		mav.setViewName( "set_exam.jsp");
		return mav;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(
	         value="/exam_input_Proc.do"
	   )
	@ResponseBody
	public Map<String,Object> exam_input_Proc(
		ExamDTO examDTO,
		HttpSession session
	){
		Map<String,Object> problemMap = new HashMap<String,Object>();
		String mid = (String) session.getAttribute("mid");
		examDTO.setMid(mid);
		
		int problemCnt=0;
		try {
			problemCnt = problemService.insertExam(examDTO);
		}catch(Exception e) {
	        System.out.println("Exception occurred at: " + e.getStackTrace()[0]);
	        e.printStackTrace();
		}
		problemMap.put("problemCnt", problemCnt);
		return problemMap;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping( value="/search_exam.do" )
	public ModelAndView search_exam( 	
			SearchDTO searchDTO,
			HttpSession session
	){
		ModelAndView mav = new ModelAndView();
		String mid = (String) session.getAttribute("mid");
		if(mid==null) { 
			mav.setViewName( "loginForm.jsp" );
			return mav;
		}
		
		searchDTO.setMid(mid);
		Map<String,Object> examMap = getSearchResultMap(searchDTO);
		
		mav.addObject(   "examMap" , examMap     );
		mav.setViewName( "search_exam.jsp");
		return mav;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping( value="/input_exam_code.do" )
	public ModelAndView input_exam_code( 	
			HttpSession session
	){
		ModelAndView mav = new ModelAndView();
		String mid = (String) session.getAttribute("mid");
		if(mid==null) { 
			mav.setViewName( "loginForm.jsp" );
			return mav;
		}
		mav.setViewName( "input_exam_code.jsp");
		return mav;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ	
	@RequestMapping( value="/problemCheck.do" )
	public ModelAndView problemCheck( 	
			CheckDTO checkDTO,
			HttpSession session
	){
		ModelAndView mav = new ModelAndView();
		String mid = (String) session.getAttribute("mid");
		if(mid==null) { 
			mav.setViewName( "loginForm.jsp" );
			return mav;
		}

		List<Map<String,Object>> checkList = problemDAO.getCheck(checkDTO);
		checkList = Util.convertAngleBracketsMapList(checkList, "<br>");
		String exam_title = problemDAO.getTitle(checkDTO);
		exam_title = Util.convertAngleBracketsString(exam_title);
		
		//문제의 갯수 구하는 코드
		int problemMax = problemDAO.getProblemMax(checkDTO);
		//응시자의 수
		int examineeCount  = problemDAO.getExamineeCount(checkDTO);
		mav.addObject(   "checkList" , checkList     );
		mav.addObject(   "exam_title" , exam_title     );
		mav.addObject(   "problemMax" , problemMax     );
		mav.addObject(   "examineeCount" , examineeCount     );
		mav.setViewName( "problemCheck.jsp");
		return mav;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ	
	private Map<String,Object> getSearchResultMap(SearchDTO searchDTO){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int searchResultCount = this.problemDAO.searchResultCount(searchDTO);
		int problemAllCount = this.problemDAO.problemAllCount(searchDTO);
		// 여기까지 행개수-> 0개
		Map<String,Integer> pagingMap = Util.getPagingMap( searchResultCount, searchDTO.getRowCnt(), searchDTO.getSelectPageNo() );

		searchDTO.setSelectPageNo(  		  (int)pagingMap.get("selectPageNo")  );
		searchDTO.setRowCnt( 							 (int)pagingMap.get("rowCnt") );
		searchDTO.setBegin_rowNo(   	(int)pagingMap.get("begin_rowNo")   );  // 테이블에서 검색 시 시작행 번호 저장하기
		searchDTO.setEnd_rowNo(			 (int)pagingMap.get("end_rowNo")     );  // 테이블에서 검색 시 끝행 번호 저장하기
		// 여기서부터 행개수가 기본 10개가 된다. 그래서 순서를 잘 줘야함.

		List<Map<String,Object>> examList = this.problemDAO.search(searchDTO);
		examList = Util.convertAngleBracketsMapList(examList, "<br>");
		
		resultMap.put(	"examList"						, examList);														//검색결과물
		resultMap.put(	"problemAllCount"		, problemAllCount);												//db에 저장된 모든행의 개수
		resultMap.put(	"searchResultCount"		, pagingMap.get("searchResultCount"));		//검색결과물의 개수
		resultMap.put(	"rowCnt"							, pagingMap.get("rowCnt"));							//행보기 수
		resultMap.put(	"searchDTO"  					, searchDTO);														//searchDTO 객체

		resultMap.put(	"pageNoCntPerPage"	, pagingMap.get("pageNoCntPerPage"));		//한 화면에 보여줄 페이지번호
		resultMap.put(  "begin_pageNo"				, pagingMap.get("begin_pageNo")        );		//한 화면에 보여줄 시작페이지번호
		resultMap.put(  "end_pageNo"				, pagingMap.get("end_pageNo")          );		//한 화면에 보여줄 끝페이지번호
		resultMap.put(	"selectPageNo"				, pagingMap.get("selectPageNo"));				//선택한페이지번호
		resultMap.put(	"last_pageNo"				, pagingMap.get("last_pageNo"));					//마지막 페이지번호
		resultMap.put(	"remainder"					, pagingMap.get("remainder"));						//마지막 페이지에 보여줄 행의 나머지개수
		resultMap.put(  "begin_serialNo_asc"		, pagingMap.get("begin_serialNo_asc")  );
		resultMap.put(  "begin_serialNo_desc"	, pagingMap.get("begin_serialNo_desc") );
		
		return resultMap;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@ResponseBody
	@RequestMapping(
	         value="/updel_exam.do"
	   )
	public ModelAndView updel_exam(
			String exam_code,
			HttpSession session
	){
		ModelAndView mav = new ModelAndView();
		String mid = (String) session.getAttribute("mid");
		if(mid==null) { 
			mav.setViewName( "loginForm.jsp" );
			return mav;
		}
		
		List<Map<String,Object>> listMap = problemDAO.getProblem(exam_code);
		List<Map<String,Object>> examList = new ArrayList<>();
		List<Map<String,String>> file_nameList = problemDAO.getFile_name(exam_code);
		
		for (int i = 0; i < listMap.size(); i++) {
		    Map<String,Object> copyList = new HashMap<>();
		    copyList = listMap.get(i);
		    String titleStr = (String) copyList.get("EXAM_TITLE");
		    String contStr = (String) copyList.get("PROBLEM_CONTENT");
		    String answStr = (String) copyList.get("PROBLEM_ANSWER");
		    if (titleStr != null && contStr != null && answStr != null) {
		    	titleStr = Util.convertAngleBracketsString(titleStr);
		        contStr = Util.convertAngleBracketsString(contStr);
		        answStr = Util.convertAngleBracketsString(answStr);
		        copyList.put("PROBLEM_TITLE", titleStr);
		        copyList.put("PROBLEM_CONTENT", contStr);
		        copyList.put("PROBLEM_ANSWER", answStr);
		    }
		    examList.add(copyList);
		}
		
		int examListSize = examList.size();
		int file_nameListSize = file_nameList.size();
		mav.addObject("examList",examList);
		mav.addObject("examListSize",examListSize);
		mav.addObject("file_nameList", file_nameList);
		mav.addObject("file_nameListSize", file_nameListSize);
		mav.setViewName( "updel_exam.jsp");
		return mav;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(
	         value="/exam_updel_Proc.do"
	   )
	@ResponseBody
	public Map<String,Object> exam_updel_Proc(
		ExamDTO examDTO,
		HttpSession session
	){
		String mid = (String) session.getAttribute("mid");
		examDTO.setMid(mid);
		
		Map<String,Object> problemMap = new HashMap<String,Object>();
		int updateCnt=0;
		try {
			
			List<String> contList = new ArrayList<>();
			List<String> answList = new ArrayList<>();
			for(int i=0; i<examDTO.getProblem_content().size(); i++) {
				contList.add(examDTO.getProblem_content().get(i).replaceAll("\n", "<br>"));
			}
			for(int i=0; i<examDTO.getProblem_answer().size(); i++) {
				answList.add(examDTO.getProblem_answer().get(i).replaceAll("\n", "<br>"));
			}
			examDTO.setProblem_content(contList);
			examDTO.setProblem_answer(answList);
			
			updateCnt = problemService.updateProblem(examDTO);
			
		}catch(Exception e) {
	        System.out.println("Exception occurred at: " + e.getStackTrace()[0]);
	        e.printStackTrace();
		}
		problemMap.put("updateCnt", updateCnt);
		return problemMap;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(
	         value="/checkExamProc.do"
	   )
	@ResponseBody
	public Map<String,Integer> checkExamProc(
		ExamDTO examDTO,
		HttpSession session
	){
		String mid = (String) session.getAttribute("mid");
		examDTO.setMid(mid);
		
		Map<String,Integer> map = new HashMap<String,Integer>();
		String is_last_save = problemDAO.examIs_last_saveCheck(examDTO);
		int checkCnt=0;
		if("최종".equals(is_last_save)) {
			checkCnt = problemDAO.examCodeCheck(examDTO.getExam_code());
			if(checkCnt>0) { 
				checkCnt=1;
				if(problemDAO.examMidCheck(examDTO)>0) {
					checkCnt=2;
				}
			}
		}
		map.put("checkCnt" , checkCnt);
		return map;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(
	         value="/problemSolving.do"
	   )
	@ResponseBody
	public ModelAndView problemSolving(
		String exam_code,
		HttpSession session
	){
		ModelAndView mav = new ModelAndView();
		String mid = (String) session.getAttribute("mid");
		if(mid==null) { 
			mav.setViewName( "loginForm.jsp" );
			return mav;
		}
		 
		List<Map<String,Object>> examList = problemDAO.getProblem(exam_code);
		examList = Util.convertAngleBracketsMapList(examList, "<br>");
		List<Map<String,String>> file_nameList = problemDAO.getFile_name(exam_code);

		mav.addObject("examList" , examList);
		mav.addObject("file_nameList" , file_nameList);
		mav.setViewName( "problemSolving.jsp");
		
		return mav;
	}		
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(
	         value="/exam_solving_Proc.do"
	   )
	@ResponseBody
	public Map<String,Object> exam_solving_Proc(
		ExamDTO examDTO,
		HttpSession session
	){
		String mid = (String) session.getAttribute("mid");
		examDTO.setMid(mid);
		
		List<String> answList = new ArrayList<>();
		for(int i=0; i<examDTO.getProblem_answer().size(); i++) {
			answList.add(examDTO.getProblem_answer().get(i).replaceAll("\n", "<br>"));
		}
		examDTO.setProblem_answer(answList);
		
		Map<String,Object> solvMap = new HashMap<String,Object>();
		int solvCnt=0;
		try {
			solvCnt = problemService.insertSolv(examDTO);
		}catch(Exception e) {
	        System.out.println("Exception occurred at: " + e.getStackTrace()[0]);
	        e.printStackTrace();
		}
		solvMap.put("solvCnt", solvCnt);
		return solvMap;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(
	         value="/delete_exam_Proc.do"
	   )
	@ResponseBody
	public Map<String,Object> deleteExam(
		String exam_code
	){
		Map<String,Object> deleteMap = new HashMap<String,Object>();
		try {
			if(problemService.deleteExam(exam_code)==1) {
				deleteMap.put("deleteCnt", 1);
			}else {
				deleteMap.put("deleteCnt", 0);
			}
		}catch(Exception e){
	        System.out.println("Exception occurred at: " + e.getStackTrace()[0]);
	        e.printStackTrace();
			return deleteMap;
		}
		return deleteMap;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping( value="/main.do" )
	public ModelAndView main(
			HttpSession session
	){
		ModelAndView mav = new ModelAndView( );
		if(session.getAttribute("mid")==null) {
			mav.setViewName( "loginForm.jsp");
		}else {
			mav.setViewName( "main.jsp");
		}
		return mav;
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	
	
}

