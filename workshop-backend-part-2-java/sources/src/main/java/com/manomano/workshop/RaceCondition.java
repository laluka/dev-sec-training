package com.manomano.workshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.RandomStringUtils;

public class RaceCondition
{
  private long count = 0;
  public long incAndGet() {
    this.count++;
    return this.count;
  }

  public long get() { return this.count; }

  public static Runnable getRunnable(RaceCondition rc, Integer nbCoupons, String username) {
    return () -> {
      if(nbCoupons < 5) {
        String newCouponCode = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        String query = "INSERT INTO coupon(code_coupon, username) VALUES(?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
          conn = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/workshops", "userdefault",
              "password");
          stmt = conn.prepareStatement(query);
          stmt.setString(1, newCouponCode);
          stmt.setString(2, username);
          stmt.executeUpdate();

          if(stmt != null) {
            stmt.close();
          }
        } catch (SQLException e) {
          System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
  }
}

