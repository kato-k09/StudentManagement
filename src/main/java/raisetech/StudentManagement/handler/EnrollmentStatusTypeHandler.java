package raisetech.StudentManagement.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import raisetech.StudentManagement.data.EnrollmentStatus;

public class EnrollmentStatusTypeHandler extends BaseTypeHandler<EnrollmentStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, EnrollmentStatus parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.name()); // Enumが日本語名なら .name() でOK
  }

  @Override
  public EnrollmentStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return fromString(value);
  }

  @Override
  public EnrollmentStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return fromString(value);
  }

  @Override
  public EnrollmentStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    String value = cs.getString(columnIndex);
    return fromString(value);
  }

  private EnrollmentStatus fromString(String value) {
    for (EnrollmentStatus status : EnrollmentStatus.values()) {
      if (status.name().equals(value)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Unknown enum value: " + value);
  }
}