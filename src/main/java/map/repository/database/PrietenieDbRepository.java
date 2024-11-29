package map.repository.database;

import map.domain.Entity;
import map.domain.FriendshipStatus;
import map.domain.Prietenie;
import map.domain.Utilizator;
import map.domain.validators.PrietenieValidator;
import map.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrietenieDbRepository extends AbstractDbRepository<Prietenie>{

    public PrietenieDbRepository(String url, String user, String password, Validator<Prietenie> validator) {
        super(url, user, password, validator);
    }

    @Override
    public Prietenie createEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        Long id1 = rs.getLong(2);
        Long id2 = rs.getLong(3);
        LocalDateTime frFrom = rs.getDate(4).toLocalDate().atTime(0,0,0);
        String status = rs.getString(5);

        FriendshipStatus friendshipStatus = null;
        switch(status){
            case "DONE": {friendshipStatus = FriendshipStatus.DONE;break;}
            case "PENDING": {friendshipStatus = FriendshipStatus.PENDING;break;}
            case "REJECTED": {friendshipStatus = FriendshipStatus.REJECTED;break;}
        }
        Prietenie prietenie = new Prietenie(id1, id2, frFrom, friendshipStatus);
        prietenie.setId(id);
        return prietenie;
    }

    @Override
    public PreparedStatement findOneStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM friendships WHERE id = ?");
        ps.setLong(1, id);

        return ps;
    }

    @Override
    public PreparedStatement findAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM friendships");
    }

    @Override
    public PreparedStatement saveStatement(Connection connection, Prietenie prietenie) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("INSERT INTO friendships(id1,id2,friends_from,status) VALUES (?,?,?,?)");
        ps.setLong(1, prietenie.getIdPrieten1());
        ps.setLong(2, prietenie.getIdPrieten2());
        ps.setDate(3, Date.valueOf(prietenie.getFriendsFrom().toLocalDate()));
        ps.setString(4, prietenie.getStatus().toString());

        return ps;
    }

    @Override
    public PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM friendships WHERE id=?");
        ps.setLong(1, id);

        return ps;
    }

    @Override
    public PreparedStatement updateStatement(Connection connection, Prietenie prietenie) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE friendships SET id1 = ?, id2 = ?, friends_from = ?, status = ? WHERE id = ?");
        ps.setLong(1, prietenie.getIdPrieten1());
        ps.setLong(2, prietenie.getIdPrieten2());
        ps.setDate(3, Date.valueOf(prietenie.getFriendsFrom().toLocalDate()));
        ps.setString(4, prietenie.getStatus().toString());
        ps.setLong(5, prietenie.getId());


        return ps;
    }

    public Optional<Prietenie> findOne(Long id1, Long id2) {
        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships WHERE id1=? AND id2=?");
            statement.setLong(1, id1);
            statement.setLong(2, id2);

            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                Prietenie p = createEntity(rs);
                return Optional.of(p);
            }
            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.empty();
        }
    }


}
