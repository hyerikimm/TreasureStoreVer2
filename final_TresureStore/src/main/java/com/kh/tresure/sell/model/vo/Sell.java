package com.kh.tresure.sell.model.vo;

import java.sql.Date;

import com.kh.tresure.common.Time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sell {

	private int sellNo;	
	private int userNo;	
	private int categoryNo;
	private String sellTitle;		
	private String sellContent;
	private String imgSrc;
	private int count;
	private int price;
	private String sellStatus;
	private Date createDate;
	private Date updateDate;
	private String status;
	private String heartNum;
	
	private String timeago;
	
	public String getTimeago() {
		return timeago;
	}
	public void setTimeago(Date createDate) {
		this.timeago = Time.calculateTime(createDate); // 기존의 getter, setter에서 변경된 부분
	}	
	
}
