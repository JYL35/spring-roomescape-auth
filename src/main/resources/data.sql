-- 1. 매장 데이터 생성
INSERT INTO store (name)
VALUES ('강남점'),
       ('잠실점'),
       ('홍대점');

-- 2. 예약 시간 데이터 생성
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

-- 3. 테마 데이터 생성
INSERT INTO theme (name, description, img_url, store_id)
VALUES ('이든의 공포 하우스', '이든이 귀신으로 나오는 공포 테마', 'https://images.example.com/themes/horror-house.jpg', 1),
       ('정콩이의 방탈출', '정콩이가 지키는 미스터리 방탈출', 'https://images.example.com/themes/jungkong-room.jpg', 2),
       ('우주 정거장 탈출', '고장 난 우주 정거장에서 귀환하는 SF 테마', 'https://images.example.com/themes/space-station.jpg', 3),
       ('강남 비밀 금고', '도심 한복판 지하 금고의 보안 장치를 해제하는 잠입 테마', 'https://images.example.com/themes/gangnam-vault.jpg', 1),
       ('사라진 큐레이터', '폐관 직전의 갤러리에서 사라진 큐레이터의 흔적을 추적하는 추리 테마', 'https://images.example.com/themes/missing-curator.jpg', 1),
       ('잠실 호수의 전설', '호수 아래 잠든 오래된 전설의 단서를 찾아 탈출하는 판타지 테마', 'https://images.example.com/themes/jamsil-lake.jpg', 2),
       ('스파이 작전 88', '비밀 요원의 마지막 교신을 따라 제한 시간 안에 암호를 해독하는 첩보 테마', 'https://images.example.com/themes/spy-operation-88.jpg', 2),
       ('홍대 지하 연습실', '정전된 지하 연습실에서 미완성 악보와 녹음 파일의 비밀을 밝히는 음악 테마', 'https://images.example.com/themes/hongdae-studio.jpg', 3),
       ('마지막 심야 열차', '막차가 사라진 플랫폼에서 다음 역으로 향하는 문을 여는 미스터리 테마', 'https://images.example.com/themes/midnight-train.jpg', 3);

-- 4. 회원 데이터 생성 (아이디 생성 순서대로 1번부터 PK 발급 자동 보장)
INSERT INTO member (login_id, password, name, role, store_id)
VALUES ('eden', 'password123', '이든', 'USER', 1),                  -- id: 1
       ('roy', 'password123', '로이', 'USER', 1),                   -- id: 2
       ('brown', 'password123', '브라운', 'USER', 1),               -- id: 3
       ('cony', 'password123', '코니', 'USER', 1),                  -- id: 4
       ('sally', 'password123', '샐리', 'USER', 1),                 -- id: 5
       ('james', 'password123', '제임스', 'USER', 1),               -- id: 6
       ('pobi', 'password123', '포비', 'USER', 1),                  -- id: 7
       ('river', 'password123', '리버', 'USER', 1),                 -- id: 8
       ('glen', 'password123', '글렌', 'USER', 1),                  -- id: 9
       ('dobby', 'password123', '도비', 'USER', 1),                 -- id: 10
       ('lara', 'password123', '라라', 'USER', 1),                  -- id: 11
       ('mody', 'password123', '모디', 'USER', 1),                  -- id: 12
       ('duck', 'password123', '오리', 'USER', 2),                  -- id: 13
       ('lulu', 'password123', '루루', 'USER', 2),                  -- id: 14
       ('nana', 'password123', '나나', 'USER', 2),                  -- id: 15
       ('toto', 'password123', '토토', 'USER', 2),                  -- id: 16
       ('vivi', 'password123', '비비', 'USER', 2),                  -- id: 17
       ('kiki', 'password123', '키키', 'USER', 2),                  -- id: 18
       ('haru', 'password123', '하루', 'USER', 2),                  -- id: 19
       ('duri', 'password123', '두리', 'USER', 2),                  -- id: 20
       ('seri', 'password123', '세리', 'USER', 3),                  -- id: 21
       ('neo', 'password123', '네오', 'USER', 3),                   -- id: 22
       ('mint', 'password123', '민트', 'USER', 3),                  -- id: 23
       ('choco', 'password123', '초코', 'USER', 3),                 -- id: 24
       ('cookie', 'password123', '쿠키', 'USER', 3),                -- id: 25
       ('peach', 'password123', '피치', 'USER', 3),                 -- id: 26
       ('admin', 'admin123', '관리자', 'ADMIN', 1),                 -- id: 27 (어드민 테스트용 계정)
       ('gangnam-manager', 'password123', '강남점 매니저', 'MANAGER', 1), -- id: 28
       ('jamsil-manager', 'password123', '잠실점 매니저', 'MANAGER', 2),  -- id: 29
       ('hongdae-manager', 'password123', '홍대점 매니저', 'MANAGER', 3); -- id: 30

-- 5. 예약 데이터 생성 (name 컬럼 제거 -> member_id 외래키 번호 매핑 적용)
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
       (5, CAST(DATEADD(DAY, 2, CURRENT_DATE) AS VARCHAR), 2, 1),  -- 샐리 미래 예약

       (1, CAST(DATEADD(DAY, -6, CURRENT_DATE) AS VARCHAR), 4, 4), -- 이든, 강남 비밀 금고
       (2, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 5, 4), -- 로이, 강남 비밀 금고
       (3, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 6, 4), -- 브라운, 강남 비밀 금고
       (4, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 7, 4), -- 코니, 강남 비밀 금고
       (5, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 8, 5), -- 샐리, 사라진 큐레이터
       (6, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 9, 5), -- 제임스, 사라진 큐레이터
       (7, CAST(DATEADD(DAY, 1, CURRENT_DATE) AS VARCHAR), 10, 4), -- 포비, 강남 비밀 금고 미래 예약
       (8, CAST(DATEADD(DAY, 2, CURRENT_DATE) AS VARCHAR), 11, 5), -- 리버, 사라진 큐레이터 미래 예약

       (13, CAST(DATEADD(DAY, -6, CURRENT_DATE) AS VARCHAR), 6, 6), -- 오리, 잠실 호수의 전설
       (14, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 7, 6), -- 루루, 잠실 호수의 전설
       (15, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 8, 6), -- 나나, 잠실 호수의 전설
       (16, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 9, 7), -- 토토, 스파이 작전 88
       (17, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 10, 7), -- 비비, 스파이 작전 88
       (18, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 11, 7), -- 키키, 스파이 작전 88
       (19, CAST(DATEADD(DAY, 1, CURRENT_DATE) AS VARCHAR), 2, 6), -- 하루, 잠실 호수의 전설 미래 예약
       (20, CAST(DATEADD(DAY, 2, CURRENT_DATE) AS VARCHAR), 3, 7), -- 두리, 스파이 작전 88 미래 예약

       (21, CAST(DATEADD(DAY, -6, CURRENT_DATE) AS VARCHAR), 7, 8), -- 세리, 홍대 지하 연습실
       (22, CAST(DATEADD(DAY, -5, CURRENT_DATE) AS VARCHAR), 8, 8), -- 네오, 홍대 지하 연습실
       (23, CAST(DATEADD(DAY, -4, CURRENT_DATE) AS VARCHAR), 9, 8), -- 민트, 홍대 지하 연습실
       (24, CAST(DATEADD(DAY, -3, CURRENT_DATE) AS VARCHAR), 10, 9), -- 초코, 마지막 심야 열차
       (25, CAST(DATEADD(DAY, -2, CURRENT_DATE) AS VARCHAR), 11, 9), -- 쿠키, 마지막 심야 열차
       (26, CAST(DATEADD(DAY, -1, CURRENT_DATE) AS VARCHAR), 1, 9), -- 피치, 마지막 심야 열차
       (21, CAST(DATEADD(DAY, 1, CURRENT_DATE) AS VARCHAR), 3, 8), -- 세리, 홍대 지하 연습실 미래 예약
       (22, CAST(DATEADD(DAY, 2, CURRENT_DATE) AS VARCHAR), 4, 9); -- 네오, 마지막 심야 열차 미래 예약
