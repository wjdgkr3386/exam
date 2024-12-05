package com.naver.erp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class Util {
	
	private static ProblemDAO problemDAO;
	
	public static Map<String,Integer> getPagingMap( int searchResultCount, int rowCnt, int selectPageNo ) {
		Map<String,Integer> pagingMap = new HashMap<String,Integer>();
		try {
			if(rowCnt<=0)						{ rowCnt=10; }					//행보기
			if(selectPageNo<=0)			{ selectPageNo=1; }			//선택페이지번호

			int pageNoCntPerPage=10;			//한번에 보여줄 페이지의 개수
			int last_pageNo=0;						//페이징전체번호
			int remainder=0;							//마지막페이지에 행의 개수
			
			if(searchResultCount==0) {
				//---------------------------------------------------------------------------------
				pagingMap.put("searchResultCount", searchResultCount);
				pagingMap.put("rowCnt", rowCnt);
				pagingMap.put("selectPageNo", selectPageNo);
				//---------------------------------------------------------------------------------
				pagingMap.put("pageNoCntPerPage", pageNoCntPerPage);
				pagingMap.put("last_pageNo", last_pageNo);
				pagingMap.put("remainder", remainder);
				//---------------------------------------------------------------------------------
				pagingMap.put("begin_rowNo", 0);
				pagingMap.put("end_rowNo", 0);
				pagingMap.put("begin_pageNo", 0);
				pagingMap.put("end_pageNo", 0);
				//---------------------------------------------------------------------------------
				pagingMap.put("serialNo_asc", 0);
				pagingMap.put("serialNo_desc", 0 );
				//---------------------------------------------------------------------------------
				return pagingMap;
				
			}
				last_pageNo = searchResultCount/rowCnt;
				remainder = searchResultCount%rowCnt;
				if(remainder>0) { last_pageNo++; }
				if(last_pageNo<selectPageNo) { selectPageNo = last_pageNo; }
				//-----------------------------------------------------
				int end_rowNo = selectPageNo * rowCnt;
				int begin_rowNo = end_rowNo-rowCnt+1;
					if( end_rowNo>searchResultCount ) { end_rowNo = searchResultCount; }
				//-----------------------------------------------------
				int begin_pageNo = (int)Math.floor(  (selectPageNo-1)/pageNoCntPerPage  )*pageNoCntPerPage + 1;
				int end_pageNo = begin_pageNo + pageNoCntPerPage - 1;
					if( end_pageNo>last_pageNo ) { end_pageNo = last_pageNo; }
				//---------------------------------------------------------------------------------
				pagingMap.put("searchResultCount", searchResultCount);
				pagingMap.put("rowCnt", rowCnt);
				pagingMap.put("selectPageNo", selectPageNo);
				//---------------------------------------------------------------------------------
				pagingMap.put("pageNoCntPerPage", pageNoCntPerPage);
				pagingMap.put("last_pageNo", last_pageNo);
				pagingMap.put("remainder", remainder);
				//---------------------------------------------------------------------------------
				pagingMap.put("begin_rowNo", begin_rowNo);
				pagingMap.put("end_rowNo", end_rowNo);
				pagingMap.put("begin_pageNo", begin_pageNo);
				pagingMap.put("end_pageNo", end_pageNo);
				//---------------------------------------------------------------------------------
				pagingMap.put("serialNo_asc", begin_rowNo);
				pagingMap.put("serialNo_desc", searchResultCount-begin_rowNo+1 );
				//---------------------------------------------------------------------------------
				return pagingMap;
				
			
		}catch(Exception ex){
			return new HashMap<String,Integer>();
		}
		
	}
	
	//파일 재입력 메소드
	public static int saveFileToDirectory(ExamDTO examDTO) {
		int access=0;
		String exam_code = examDTO.getExam_code();
        String folderPath = "C:/zzz/workspace_exam/prj/src/main/resources/static/img/" + exam_code;
		
        fileListNullDelete(examDTO);
        
        file_nameInput(examDTO);
        
        fileDelete( folderPath );
        
        List<MultipartFile> fileList = examDTO.getFile();
        if(fileList!=null && !fileList.isEmpty() && fileList.size()>0) {
        	access=fileCreate( folderPath, examDTO );
        }else {
        	access=1;
        }
        return access;
	}
	
	//리스트에 값 없는 공간 삭제
	public static void fileListNullDelete( ExamDTO examDTO ) {
		List<MultipartFile> files = examDTO.getFile();
		if( files != null && !files.isEmpty() ) {
			for (int i = files.size() - 1; i >= 0; i--) {
			    MultipartFile file = files.get(i);
			    if (file.isEmpty()) { files.remove(i); }
			}
			examDTO.setFile(files);
		}
	}

	//파일 불러와 파일이름을 DTO에 저장
	public static void file_nameInput( ExamDTO examDTO ) {
		List<MultipartFile> files = examDTO.getFile();
		List<String> file_nameList = new ArrayList<String>();
		if( files != null && !files.isEmpty() ) {
			for( MultipartFile file : files) {
				String originalfileName = file.getOriginalFilename();
				file_nameList.add(originalfileName);
			}
			examDTO.setFile_name(file_nameList);
		}
	}
	
	//경로에 있는 폴더 삭제 메소드
	public static void fileDelete(String folderPath) {
	    File folder = new File(folderPath);
	    File[] files = folder.listFiles();
	    
	    //폴더 안의 파일들 삭제
	    if( files != null  && files.length > 0 ) {
	        for( File file : files ) {
	            try {
	                if( file.isDirectory() ) {
	                    fileDelete(file.getAbsolutePath());
	                } else {
	                    file.delete();
	                }
	            } catch( Exception e ) {
	    	        System.err.println("Exception occurred at: " + e.getStackTrace()[0]);
	    	        e.printStackTrace();
	            }
	        }
	    }
	    //폴더 삭제
	    if( folder.exists() ) { folder.delete(); }
	}
	
	//지정된 경로에 파일 저장하는 메소드
	public static int fileCreate( String folderPath, ExamDTO examDTO ) {
		List<MultipartFile> fileList = examDTO.getFile();
        
		//폴더가 없으면 생성
		File folder = new File(folderPath);
		if ( !folder.exists() ) { folder.mkdirs(); }
		
		if( fileList!=null && !fileList.isEmpty() ) {
			for( MultipartFile file : fileList ) {
				String originalFileName = file.getOriginalFilename();
	            if(extensionCheck(originalFileName)==-13) { return -13; }
			}
			
			for( MultipartFile file : fileList ) {
				String originalFileName = file.getOriginalFilename();
	            //업로드된 파일을 지정된 경로에 저장
	            String filePath = folderPath + "/" + originalFileName;
	            File dest = new File( filePath );
	
	            try {
	            	file.transferTo(dest);
	            }catch(IOException e) {
	    	        System.err.println("Exception occurred at: " + e.getStackTrace()[0]);
	    	        e.printStackTrace();
	            }
			}
		}
		return 1;
	}

	public static int extensionCheck( String originalFileName) {
		
		String[] allowedExtensions = {"jpg", "jpeg", "jfif", "png"};
		String extension = "";
		
        //확장자 확인
        if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
        	extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
		boolean checkExtension = Arrays.asList(allowedExtensions).contains(extension.toLowerCase());
        if(!checkExtension) {
        	return -13;
        }
        
		return 1;
	}
	
	public static int isExtension( ExamDTO examDTO ) {
        Util.fileListNullDelete(examDTO);
        Util.file_nameInput(examDTO);
        
		List<MultipartFile> fileList = examDTO.getFile();
		if( fileList!=null && !fileList.isEmpty() ) {
			for( MultipartFile file : fileList ) {
				String originalFileName = file.getOriginalFilename();
	            if(Util.extensionCheck(originalFileName)==-13) {
	            	return -13;
	            }
			}
		}
		return 1;
	}
	
	//맵을 받아서 안에 있는 내용 중에 < , > , <br> 을 html에서 사용할 수 있게 변환하여 저장하고 반환
    public static Map<String, Object> convertAngleBracketsMap(Map<String, Object> convertMap, String keyword){
        if(keyword.equals("<br>")) {
            for (Map.Entry<String, Object> entry : convertMap.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    String sanitizedValue = value.toString()
                        .replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("\n", "<br>");
                    entry.setValue(sanitizedValue);
                }
            }
        }else if(keyword.equals("\n")) {
            for (Map.Entry<String, Object> entry : convertMap.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    String sanitizedValue = value.toString()
                        .replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("<br>", "\n");
                    entry.setValue(sanitizedValue);
                }
            }
        }
        return convertMap;
    }

  //맵리스트을 받아서 안에 있는 내용 중에 < , > , <br> 을 html에서 사용할 수 있게 변환하여 저장하고 반환
    public static List<Map<String, Object>> convertAngleBracketsMapList(List<Map<String, Object>> convertMapList, String keyword){
        if(keyword.equals("<br>")) {
            for(Map<String, Object> map : convertMapList) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        String sanitizedValue = value.toString()
                            .replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;")
                            .replaceAll("\n", "<br>");
                        entry.setValue(sanitizedValue);
                    }
                }
            }
        }else if(keyword.equals("\n")) {
            for(Map<String, Object> map : convertMapList) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        String sanitizedValue = value.toString()
                            .replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;")
                            .replaceAll("<br>", "\n");
                        entry.setValue(sanitizedValue);
                    }
                }
            } 
        }
        return convertMapList;
    }
    
    //String을 받아서 안에 있는 내용 중에 < , >을 html에서 사용할 수 있게 변환하여 저장하고 반환
    public static String convertAngleBracketsString(String convertString){
        convertString = convertString
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
        return convertString;
    }
}
