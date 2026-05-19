-- 1. 예약 시간 데이터 생성
INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('13:00'),
       ('14:00'),
       ('15:00'),
       ('16:00'),
       ('17:00'),
       ('18:00'),
       ('19:00'),
       ('20:00'),
       ('21:00');

-- 2. 테마 데이터 생성
INSERT INTO theme (name, description, img_url)
VALUES ('이든의 공포 하우스', '이든이 귀신으로 나오는 공포 테마', 'https://images.example.com/themes/horror-house.jpg'),
       ('정콩이의 방탈출', '정콩이가 지키는 미스터리 방탈출', 'https://images.example.com/themes/jungkong-room.jpg'),
       ('우주 정거장 탈출', '고장 난 우주 정거장에서 귀환하는 SF 테마', 'https://images.example.com/themes/space-station.jpg');

-- 3. 회원 데이터 생성 (아이디 생성 순서대로 1번부터 PK 발급 자동 보장)
INSERT INTO member (login_id, password, name, role)
VALUES ('eden', 'password123', '이든', 'USER'),         -- id: 1
       ('roy', 'password123', '로이', 'USER'),          -- id: 2
       ('brown', 'password123', '브라운', 'USER'),      -- id: 3
       ('cony', 'password123', '코니', 'USER'),         -- id: 4
       ('sally', 'password123', '샐리', 'USER'),        -- id: 5
       ('james', 'password123', '제임스', 'USER'),      -- id: 6
       ('pobi', 'password123', '포비', 'USER'),         -- id: 7
       ('river', 'password123', '리버', 'USER'),        -- id: 8
       ('glen', 'password123', '글렌', 'USER'),         -- id: 9
       ('dobby', 'password123', '도비', 'USER'),        -- id: 10
       ('lara', 'password123', '라라', 'USER'),         -- id: 11
       ('mody', 'password123', '모디', 'USER'),         -- id: 12
       ('duck', 'password123', '오리', 'USER'),         -- id: 13
       ('lulu', 'password123', '루루', 'USER'),         -- id: 14
       ('nana', 'password123', '나나', 'USER'),         -- id: 15
       ('toto', 'password123', '토토', 'USER'),         -- id: 16
       ('vivi', 'password123', '비비', 'USER'),         -- id: 17
       ('kiki', 'password123', '키키', 'USER'),         -- id: 18
       ('haru', 'password123', '하루', 'USER'),         -- id: 19
       ('duri', 'password123', '두리', 'USER'),         -- id: 20
       ('seri', 'password123', '세리', 'USER'),         -- id: 21
       ('neo', 'password123', '네오', 'USER'),          -- id: 22
       ('mint', 'password123', '민트', 'USER'),         -- id: 23
       ('choco', 'password123', '초코', 'USER'),        -- id: 24
       ('cookie', 'password123', '쿠키', 'USER'),       -- id: 25
       ('peach', 'password123', '피치', 'USER'),        -- id: 26
       ('admin', 'admin123', '관리자', 'ADMIN');         -- id: 27 (어드민 테스트용 계정)

-- 4. 예약 데이터 생성 (name 컬럼 제거 -> member_id 외래키 번호 매핑 적용)
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, CAST(DATEADD(DAY, -7, CURRENT_DATE) AS VARCHAR), 1, 1), -- 이든
       (2, CAST(DATEADD(DAY, -6, CURRENT_DATE) AS VARCHAR), 1, 1), -- 로이
       (3, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 1, 1), -- 브라운
       (4, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 1, 1), -- 코니
       (5, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 1, 1), -- 샐리
       (6, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 1, 1), -- 제임스
       (7, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 1, 1), -- 포비
       (8, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 2, 1), -- 리버
       (9, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 2, 1), -- 글렌
       (10, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 2, 1), -- 도비
       (11, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 3, 1), -- 라라
       (12, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 3, 1), -- 모디

       (13, CAST(DATEADD(DAY, -7, CURRENT_DATE) AS VARCHAR), 2, 2), -- 오리
       (14, CAST(DATEADD(DAY, -6, CURRENT_DATE) AS VARCHAR), 3, 2), -- 루루
       (15, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 4, 2), -- 나나
       (16, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 4, 2), -- 토토
       (17, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 4, 2), -- 비비
       (18, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 4, 2), -- 키키
       (19, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 4, 2), -- 하루
       (20, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 5, 2), -- 두리

       (21, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 4, 3), -- 세리
       (22, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 5, 3), -- 네오
       (23, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 5, 3), -- 민트
       (24, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 5, 3), -- 초코
       (25, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 5, 3), -- 쿠키
       (26, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 6, 3), -- 피치

       (3, CAST(DATEADD(DAY, 1, CURRENT_DATE) AS VARCHAR), 1, 1),  -- 브라운 미래 예약
       (4, CAST(DATEADD(DAY, 1, CURRENT_DATE) AS VARCHAR), 1, 2),  -- 코니 미래 예약
       (5, CAST(DATEADD(DAY, 2, CURRENT_DATE) AS VARCHAR), 2, 1);  -- 샐리 미래 예약
