package com.hk.blog.HomeController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.hk.blog.command.DelBlogCommand;
import com.hk.blog.command.InsertBlogCommand;
import com.hk.blog.command.UpdateBlogCommand;
import com.hk.blog.dtos.BlogDto;
import com.hk.blog.dtos.FileBoardDto;
import com.hk.blog.service.BlogService;
import com.hk.blog.service.FileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/board")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	@Autowired
	private FileService fileService;
	
	@GetMapping(value = "/boardList")
	public String boardList(Model model) {
		System.out.println("글목록 보기");
		
		List<BlogDto> list=blogService.getAllList();
		model.addAttribute("list", list);
		model.addAttribute("delBlogCommand", new DelBlogCommand());
		return "board/boardList";// forward 기능, "redirect:board/boardList"
	}
	
	@GetMapping(value = "/boardInsert")
	public String boardInsertForm(Model model) {
		model.addAttribute("insertBlogCommand", new InsertBlogCommand());
		return "board/boardInsertForm";
	}
	
	@PostMapping(value = "/boardInsert")
	public String boardInsert(@Validated InsertBlogCommand insertBlogCommand 
			                ,BindingResult result
			                ,MultipartRequest multipartRequest //multipart data를 처리할때 사용
							,HttpServletRequest request
			                ,Model model) throws IllegalStateException, IOException {
		if(result.hasErrors()) {
			System.out.println("글을 모두 입력하세요");
			return "board/boardInsertForm";
		}
		
		blogService.insertBoard(insertBlogCommand,multipartRequest
				                ,request);
		
		return "redirect:/board/boardList";
	}
	
	//상세보기
	@GetMapping(value = "/boardDetail")
	public String boardDetail(int board_seq, Model model) {
		BlogDto dto=blogService.getBoard(board_seq);
		
		//유효값처리용
		model.addAttribute("updateBlogCommand", new UpdateBlogCommand());
		//출력용
		model.addAttribute("dto", dto);
		System.out.println(dto);
		return "board/boardDetail";
	}
	
	//수정하기
	@PostMapping(value = "/boardUpdate")
	public String boardUpdate(
				@Validated UpdateBlogCommand updateBlogCommand
				,BindingResult result
				,Model model) {
		
		if(result.hasErrors()) {
			System.out.println("수정내용을 모두 입력하세요");
			//코드 추가--------------------------------------------
			BlogDto dto=blogService.getBoard(updateBlogCommand.getBoard_seq());
			model.addAttribute("dto", dto);
			//--------------------------------------------------
			return "board/boardDetail";
		}
		
		blogService.updateBoard(updateBlogCommand);
		
		return "redirect:/board/boardDetail?board_seq="
				+ updateBlogCommand.getBoard_seq();
	}
	
	@GetMapping(value = "/download")
	public void download(int file_seq, HttpServletRequest request
			                         , HttpServletResponse response) throws UnsupportedEncodingException {
		
		FileBoardDto fdto=fileService.getFileInfo(file_seq);//파일정보가져오기
		
		fileService.fileDownload(fdto.getOrigin_filename()
				                ,fdto.getStored_filename()
				                ,request,response);
	}
	
	@RequestMapping(value="mulDel",method = {RequestMethod.POST,RequestMethod.GET})
	public String mulDel(@Validated DelBlogCommand delBoardCommand
						 ,BindingResult result
			             , Model model) {
		if(result.hasErrors()) {
			System.out.println("최소하나 체크하기");
			List<BlogDto> list=blogService.getAllList();
			model.addAttribute("list", list);
			return "board/boardlist";
		}
		blogService.mulDel(delBoardCommand.getSeq());
		System.out.println("글삭제함");
		return "redirect:/board/boardList";
	}
}














