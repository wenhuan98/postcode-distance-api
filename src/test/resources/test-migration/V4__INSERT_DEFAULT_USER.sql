-- insert default admin user with BCrypt-encoded password

INSERT INTO users (username, password)
VALUES ('test-user', '$2a$12$.vKkch9.tgwmGB4vmY/6dOkU3fK/2cYN2kjD2z2207xBu8taLMjDq');