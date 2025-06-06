package whu.english_learn.utils;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());  // 自动转换为yyyy-MM-dd格式
    }

    @Override
    public LocalDate getNullableResult(java.sql.ResultSet rs, String columnName) throws SQLException {
        String date = rs.getString(columnName);
        return (date != null) ? LocalDate.parse(date) : null;  // 转换回LocalDate
    }

    @Override
    public LocalDate getNullableResult(java.sql.ResultSet rs, int columnIndex) throws SQLException {
        String date = rs.getString(columnIndex);
        return (date != null) ? LocalDate.parse(date) : null;
    }

    @Override
    public LocalDate getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        String date = cs.getString(columnIndex);
        return (date != null) ? LocalDate.parse(date) : null;
    }
}
