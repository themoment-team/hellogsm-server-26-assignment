INSERT INTO tb_member (member_id, email, auth_referrer_type, name, birth, phone_number, sex, role, created_time, updated_time)
VALUES
    (1, 'user1@example.com', 'GOOGLE', '신희성', '1990-01-01', '01012345678', 'MALE', 'APPLICANT', NOW(), NOW()),
    (2, 'user2@example.com', 'GOOGLE', '방가온', '1992-02-02', '01023456789', 'FEMALE', 'APPLICANT', NOW(), NOW()),
    (3, 'user3@example.com', 'GOOGLE', '김형록', '1995-03-03', '01034567890', 'MALE', 'APPLICANT', NOW(), NOW()),
    (4, 'user4@example.com', 'GOOGLE', '전지환', '1998-04-04', '01045678901', 'FEMALE', 'APPLICANT', NOW(), NOW()),
    (5, 'user5@example.com', 'GOOGLE', '최장우', '2000-05-05', '01056789012', 'MALE', 'APPLICANT', NOW(), NOW());

INSERT INTO tb_product (product_id, name, description, price, stock_quantity)
VALUES
    (1, 'Product A', 'Description A', 10000, 50),
    (2, 'Product B', 'Description B', 20000, 30),
    (3, 'Product C', 'Description C', 15000, 40),
    (4, 'Product D', 'Description D', 25000, 20),
    (5, 'Product E', 'Description E', 30000, 10);

INSERT INTO tb_order (order_id, member_id, total_price, status, created_time, updated_time)
VALUES
    (1, 1, 30000, 'CONFIRMED', NOW(), NOW()),
    (2, 2, 45000, 'PENDING', NOW(), NOW()),
    (3, 3, 20000, 'CONFIRMED', NOW(), NOW()),
    (4, 4, 50000, 'CANCELLED', NOW(), NOW()),
    (5, 5, 15000, 'PENDING', NOW(), NOW());

INSERT INTO tb_order_item (order_item_id, order_id, product_id, quantity, price)
VALUES
    (1, 1, 1, 2, 20000),
    (2, 1, 2, 1, 10000),
    (3, 2, 3, 3, 45000),
    (4, 3, 4, 1, 20000),
    (5, 4, 5, 2, 50000);