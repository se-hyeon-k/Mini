package com.hk.blog.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartRequest;

import com.hk.blog.command.InsertBlogCommand;
import com.hk.blog.command.UpdateBlogCommand;
import com.hk.blog.dtos.BlogDto;
import com.hk.blog.dtos.FileBoardDto;
import com.hk.blog.mapper.BlogMapper;
import com.hk.blog.mapper.FileMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BlogService {
	
	@Autowired
	private BlogMapper blogMapper;
	@Autowired
	private FileMapper fileMapper;
	@Autowired
	private FileService fileService;
	
	//글목록 조회
	public List<BlogDto> getAllList(){
		return blogMapper.getAllList();
	}

	//글 추가, 파일업로드및 파일정보 추가
	@Transactional(rollbackFor = Exception.class)
	public void insertBoard(InsertBlogCommand insertBoardCommand
			              , MultipartRequest multipartRequest
			              , HttpServletRequest request) 
			              throws IllegalStateException, IOException {
		//command:UI -> dto:DB 데이터 옮겨담기
		BlogDto blogDto=new BlogDto();
		blogDto.setId(insertBoardCommand.getId());
		blogDto.setTitle(insertBoardCommand.getTitle());
		blogDto.setContent(insertBoardCommand.getContent());
		
		//새글을 추가할때 파라미터로 전달된 boardDto객체에 자동으로,
		//증가된 board_seq값이 저장
		blogMapper.insertBoard(blogDto);//새글 추가
		System.out.println("파일첨부여부:"
		+multipartRequest.getFiles("filename").get(0).isEmpty());
		//첨부된 파일들이 있는 경우
		if(!multipartRequest.getFiles("filename").get(0).isEmpty()) {
			//파일 저장경로 설정: 절대경로, 상대경로
			String filepath=request.getSession().getServletContext()
					       .getRealPath("upload");
			System.out.println("파일저장경로:"+filepath);
			//파일업로드 작업은 FileService쪽에서 업로드하고 업로드된 파일정보 반환
			List<FileBoardDto>uploadFileList
			      =fileService.uploadFiles(filepath, multipartRequest);
			//파일정보를 DB에 추가
			//글추가할때 board_seq 증가된 값---> file정보를 추가할때 사용
			//Testboard: board_seq PK       board_seq FK
			for (FileBoardDto fDto : uploadFileList) {
				fileMapper.insertFileBoard(
				 new FileBoardDto(0, blogDto.getBlog_seq(),//증가된 blog_seq값을 넣는다 
						             fDto.getOrigin_filename(),
						 			 fDto.getStored_filename())
				                          );
			}
		}
		
	} 
	//상세내용조회
	public BlogDto getBoard(int board_seq) {
		return blogMapper.getBoard(board_seq);
	}
	
	//수정하기
	public boolean updateBoard(UpdateBlogCommand updateBlogCommand) {
		//command:UI ---> DTO:DB 
		BlogDto dto=new BlogDto();
		dto.setBlog_seq(updateBlogCommand.getBoard_seq());
		dto.setTitle(updateBlogCommand.getTitle());
		dto.setContent(updateBlogCommand.getContent());
		return blogMapper.updateBoard(dto);
	}

	public boolean mulDel(String[] seqs) {
		return blogMapper.mulDel(seqs);
	}
}






