SELECT * FROM artoo.user;


insert into user( u_email, u_pw, u_school, u_address, u_name, u_bank, u_account, u_dept, u_phone) values('dayoung9650@gmail.com', 'kdy', '동덕여자대학교', '서울시 은평구', '김다영', '신한은행', '110', '컴퓨터학과', '010-4758-4624');

delete from user where u_idx>0;