insert into member (name, email) values ('junyeong', 'test123@gmail.com');
insert into member (name, email) values ('heyhey123', 'abcd456@naver.com');

insert into authority (authority_name) values ('ROLE_MEMBER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into member_authority (member_id, authority_name) values (1, 'ROLE_MEMBER');
insert into member_authority (member_id, authority_name) values (1, 'ROLE_ADMIN');
insert into member_authority (member_id, authority_name) values (2, 'ROLE_MEMBER');