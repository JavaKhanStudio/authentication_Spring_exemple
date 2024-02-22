package com.mina.authentication.repository;

import com.mina.authentication.domain.LoginAttempt;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class LoginAttemptRepository {

  private static final int RECENT_COUNT = 10; // can be in the config
  private static final String INSERT = "INSERT INTO login_attempt (email, success, created_at) VALUES(?, ?, ?)";
  private static final String FIND_RECENT = "SELECT * FROM login_attempt WHERE email = ? ORDER BY created_at DESC LIMIT ?";

  private final JdbcClient jdbcClient;

  public LoginAttemptRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public void add(LoginAttempt loginAttempt) {
    long affected = jdbcClient.sql(INSERT)
        .param(1, loginAttempt.email())
        .param(2, loginAttempt.success())
        .param(3, loginAttempt.createdAt())
        .update();

    Assert.isTrue(affected == 1, "Could not add login attempt.");
  }

  public List<LoginAttempt> findRecent(String email) {
    return jdbcClient.sql(FIND_RECENT)
        .param(1, email)
        .param(2, RECENT_COUNT)
        .query(LoginAttempt.class)
        .list();
  }
}
