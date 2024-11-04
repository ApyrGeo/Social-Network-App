package map.repository.database;

import jdk.jshell.execution.Util;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UtilizatorDbRepository extends AbstractDbRepository<Utilizator> {

    public UtilizatorDbRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public Utilizator createEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String first_name = rs.getString("first_name");
        String last_name = rs.getString("last_name");

        Utilizator utilizator = new Utilizator(first_name, last_name);
        utilizator.setId(id);
        return utilizator;
    }

    @Override
    public PreparedStatement findOneStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        ps.setLong(1, id);

        return ps;
    }

    @Override
    public PreparedStatement findAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM users");
    }

    @Override
    public PreparedStatement saveStatement(Connection connection, Utilizator entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (first_name, last_name) VALUES (?, ?)");
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());

        return ps;
    }

    @Override
    public PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        ps.setLong(1, id);

        return ps;
    }

    @Override
    public PreparedStatement updateStatement(Connection connection, Utilizator entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ? WHERE id = ?");
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
        ps.setLong(3, entity.getId());

        return ps;
    }
}
