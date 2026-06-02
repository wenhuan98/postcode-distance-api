-- insert default admin user with BCrypt-encoded password

INSERT INTO users (username, password)
VALUES ('admin', '$2a$12$Wd9MG5yVWPpzN.eo/mM5F.Ud0YPe6xq1GET8we2yms4Fma.M/xkoy');