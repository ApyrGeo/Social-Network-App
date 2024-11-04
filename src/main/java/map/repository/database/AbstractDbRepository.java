package map.repository.database;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractDbRepository <E extends Entity<Long>> implements Repository<Long, E> {
    private final String url;
    private final String username;
    private final String password;

    public AbstractDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    public abstract E createEntity(ResultSet rs) throws SQLException;
    public abstract PreparedStatement findOneStatement(Connection connection, Long id) throws SQLException;
    public abstract PreparedStatement findAllStatement(Connection connection) throws SQLException;
    public abstract PreparedStatement saveStatement(Connection connection, E entity) throws SQLException;
    public abstract PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException;
    public abstract PreparedStatement updateStatement(Connection connection, E entity) throws SQLException;

    @Override
    public Optional<E> findOne(Long id) {
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = findOneStatement(connection, id); //connection.prepareStatement("SELECT * FROM users WHERE id = ?");

            ResultSet rs = statement.executeQuery();
            rs.next();

            E entity = createEntity(rs);
            return Optional.of(entity);
        }
        catch (SQLException e){
            return Optional.empty();
        }
    }

    @Override
    public Iterable<E> findAll() {
        Set<E> entities = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = findAllStatement(connection); //connection.prepareStatement("SELECT * FROM friendships");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                E entity = createEntity(resultSet);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    @Override
    public Optional<E> save(E entity) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = saveStatement(connection, entity) //connection.prepareStatement("INSERT INTO friendships(id1, id2) VALUES(?,?)");
        ){

            int rez = statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<E> delete(Long id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = deleteStatement(connection, id) //connection.prepareStatement("DELETE FROM friendships WHERE id = ?");
        ){
            E e = findOne(id).orElse(null);
            if (e == null) return Optional.empty();

            int rez = statement.executeUpdate();
            return Optional.of(e);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> update(E entity) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = updateStatement(connection, entity) //connection.prepareStatement("UPDATE friendships SET id1 = ?, id2 = ? WHERE id = ?");
        ){
            int rez = statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.ofNullable(entity);
        }
    }

    public Long getLastId() {
        Long mxid = -1L;
        for(E l : findAll()){
            if(l.getId() > mxid)
                mxid = l.getId();
        }

        return mxid;
    }
}
