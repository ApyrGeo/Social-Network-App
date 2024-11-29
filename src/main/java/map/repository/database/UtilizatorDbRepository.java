package map.repository.database;

import jdk.jshell.execution.Util;
import map.domain.Utilizator;
import map.domain.validators.Validator;
import map.repository.Repository;

import java.sql.*;
import java.util.*;

public class UtilizatorDbRepository extends AbstractDbRepository<Utilizator> {

    public UtilizatorDbRepository(String url, String user, String password, Validator<Utilizator> validator) {
        super(url, user, password, validator);
    }

    @Override
    public Utilizator createEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String first_name = rs.getString("first_name");
        String last_name = rs.getString("last_name");
        String uname = rs.getString("uname");
        String pass = rs.getString("password");

        Utilizator utilizator = new Utilizator(first_name, last_name, uname, pass);
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
        return connection.prepareStatement("SELECT * FROM users ORDER BY uname");
    }

    @Override
    public PreparedStatement saveStatement(Connection connection, Utilizator entity) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (first_name, last_name, uname, password) VALUES (?, ?, ?, ?)");
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
        ps.setString(3, entity.getUname());
        ps.setString(4, entity.getPassword());

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
        PreparedStatement ps = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ?, uname = ?WHERE id = ?");
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
        ps.setString(3, entity.getUname());
        ps.setLong(4, entity.getId());

        return ps;
    }

    public Optional<Utilizator> findOne(String uname) {
        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
            statement.setString(1, uname);

            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                Utilizator u = createEntity(rs);
                return Optional.of(u);
            }
            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.empty();
        }
    }

    public Optional<Utilizator> findOne(String uname, String password) {
        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE uname = ? and password = ?");
            statement.setString(1, uname);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                Utilizator u = createEntity(rs);
                return Optional.of(u);
            }
            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.empty();
        }
    }

    public List<Utilizator> findFriends(Long id_user) {
        List<Utilizator> prieteni = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)){
            PreparedStatement statement = connection.prepareStatement(
                    """
                        (SELECT U.id, U.first_name, U.last_name, U.uname, U.password FROM users U JOIN friendships F ON F.id2 = U.id WHERE F.id1 = ?
                        UNION
                        SELECT U.id, U.first_name, U.last_name, U.uname, U.password FROM users U JOIN friendships F ON F.id1 = U.id WHERE F.id2 = ?)
                        ORDER BY uname
                       """);
            statement.setLong(1, id_user);
            statement.setLong(2, id_user);

            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                Utilizator u = createEntity(rs);
                prieteni.add(u);
            }
            return prieteni;
        }
        catch (SQLException e){
            return null;
        }
    }
}
