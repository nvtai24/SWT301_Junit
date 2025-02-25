package dao;

import dal.DBConnect;
import dal.ExceptionHandlers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Role;

public class RoleDAO extends DBConnect {

    public Role getRoleByRoleId(int roleId) {
        Role role = null;
        String query = "SELECT * FROM Roles WHERE role_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, roleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                role = new Role();
                role.setRoleId(resultSet.getInt("role_id"));
                role.setRoleName(resultSet.getString("role_name"));
                // Populate other fields if necessary
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return role;
    }
}
