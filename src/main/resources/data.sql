REPLACE INTO `role` VALUES (1, 'ADMIN');
REPLACE INTO `role` VALUES (2, 'USER');
REPLACE INTO `user` (`user_id`, `user_name`, `password`, `email`) VALUES (0, 'TestUser', '$2a$10$GVU0zVZX6y4f7ipYSG84rOikJoE2beDoDbkjZMu21.XKsLkC6IMqO', 'test@test.com');
REPLACE INTO `user_role` VALUES (0, 1);
REPLACE INTO `user_role` VALUES (0, 2);