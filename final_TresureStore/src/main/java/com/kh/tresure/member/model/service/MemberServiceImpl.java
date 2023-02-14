package com.kh.tresure.member.model.service;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.tresure.member.model.dao.MemberDao;
import com.kh.tresure.member.model.vo.Account;
import com.kh.tresure.member.model.vo.Member;
import com.kh.tresure.sell.controller.SellController;

@Service
public class MemberServiceImpl implements MemberService {

	private Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);	
	private MemberDao memberDao;
	private SqlSession sqlSession;
	
	// 기본생성자
	public MemberServiceImpl() {}
	
	@Autowired
	public MemberServiceImpl(MemberDao memberDao,SqlSession sqlSession) {
		this.memberDao = memberDao;
		this.sqlSession = sqlSession;
	}
	
	// 로그인 및 회원가입 하는 메소드 구현
	@Override
	@Transactional (rollbackFor=Exception.class)
	public Member loginAndMemberEnroll(Member member) {
		Member loginUser = null;
		
		// 새로 하는 사람이면 result = 0
		
		// 핸드폰번호와 이메일로 회원이 존재하는지 확인
		int existUser = memberDao.selectExistenceStatus(sqlSession, member);
		
		if(existUser == 0) {
			// 혹시 모르니 탈퇴한 사람인지 확인 (이메일 핸드폰번호) 
			int goodByeUser =  memberDao.selectUserState(sqlSession, member);
			
			if(goodByeUser == 1) {	// 탈퇴한 사람이 다시 로그인 한다면 상태변경하고 유저번호 다시
				memberDao.updateUserState(sqlSession, member);
			} else if (goodByeUser == 0){
				int insertgogo = memberDao.insertMember(sqlSession, member);
			}
		}
	
		
		if(member.getPhone() == null) {	// 카카오로 로그인
			
			// 로그인하게 객체 가져오기
			loginUser = memberDao.loginAndMemberEnrollKaKako(sqlSession, member);
			
		} else if(member.getEmail() == null){	// 본인인증했을 시
			
			// 로그인하게 객체 가져오기
			loginUser = memberDao.loginAndMemberEnrollauAuthentication(sqlSession, member);
			
		}
		
		
		return loginUser;
	}
	
	// 회원탈퇴하는 메소드
	public void deleteMember(int userNo) {
		
		// 우선 이 사람의 객체를 가져와서 저장
		Member member = memberDao.selectUser(sqlSession, userNo);
		
		// 객체는 잘 가져왔으니 현재 데베에 해당 유저 삭제
		int deleteSuccess = memberDao.deleteMember(sqlSession, userNo);
		if(deleteSuccess > 0) {
			// 삭제에 성공했으면 삭제한 유저 상태만 바꿔서 다시 넣어주기
			memberDao.insertLeaveUser(sqlSession, member);
		}
	}
	
	//계좌 추가하기
	@Override
	public int userAddAccount(Account accountInfo) {
		
		return memberDao.userAddAccount(sqlSession, accountInfo);
	}
	
	//계좌 수정하기
	@Override
	public int updateAccount(Account accountInfo) {
		
		return memberDao.updateAccount(sqlSession, accountInfo);
	}
	
	//로그인 유저 계좌 가져오기
	@Override
	public int accountNumber(Account account) {
		
		return memberDao.accountNumber(sqlSession, account);
	}
	
	
}
