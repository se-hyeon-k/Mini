package com.hk.blog.dtos;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias(value = "blogDto")
public class BlogDto {
	private int blog_seq;
	private String id;
	private String title;
	private String content;
	private Date regdate;
	private String delflag;
	
	//Join용 멤버필드
	private List<FileBoardDto> fileBoardDto;
	
	public BlogDto() {
		super();
	}

	public BlogDto(int blog_seq, String id, String title, String content, Date regdate, String delflag,
			List<FileBoardDto> fileBoardDto) {
		super();
		this.blog_seq = blog_seq;
		this.id = id;
		this.title = title;
		this.content = content;
		this.regdate = regdate;
		this.delflag = delflag;
		this.fileBoardDto = fileBoardDto;
	}
	
	
}
