package com.hk.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hk.blog.dtos.BlogDto;


@Mapper
public interface BlogMapper {
	
	//글목록
	public List<BlogDto> getAllList();
	//글상세조회
	public BlogDto getBoard(int blog_seq);
	//글추가
	public boolean insertBoard(BlogDto dto);
	//글 수정
	public boolean updateBoard(BlogDto dto);
	//글 삭제
	public boolean mulDel(String[] seqs);
}
