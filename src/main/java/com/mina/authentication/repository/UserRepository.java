package com.mina.authentication.repository;

import com.mina.authentication.domain.User;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class UserRepository {

  private static final String INSERT = "INSERT INTO website_user (name, email, password) VALUES(?, ?, ?)";
  private static final String FIND_BY_EMAIL = "SELECT * FROM website_user WHERE email = ?";

  private final JdbcClient jdbcClient;

  public UserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public void add(User user) {
    long affected = jdbcClient.sql(INSERT)
        .param(1, user.name())
        .param(2, user.email())
        .param(3, user.password())
        .update();

    Assert.isTrue(affected == 1, "Could not add user.");
  }

  public Optional<User> findByEmail(String email) {
    return jdbcClient.sql(FIND_BY_EMAIL)
        .param(1, email)
        .query(User.class)
        .optional();
  }
}
