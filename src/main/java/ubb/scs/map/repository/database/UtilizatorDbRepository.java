package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Long, Utilizator>{
    private final String url;
    private final String username;
    private final String password;

    public UtilizatorDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Utilizator findOne(Long id) {
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM utilizator");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");

                Utilizator u = new Utilizator(first_name, last_name);
                u.setId(id);

                users.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator entity) {

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users(firs_name, last_name) VALUES(?,?)");
            ){
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());

                int rez = statement.executeUpdate();
                return null; //empty cand se schimba la Optional<>
        } catch (SQLException e) {
            return entity;
        }
    }

    @Override
    public Utilizator delete(Long id) {
        return null;
    }

    @Override
    public Utilizator update(Utilizator entity) {
        return null;
    }
}
