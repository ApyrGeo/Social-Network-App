package map.repository.database;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.Prietenie;

import java.sql.*;

public class PrietenieDbRepository extends AbstractDbRepository<Prietenie>{

    public PrietenieDbRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public Prietenie createEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        Long id1 = rs.getLong(2);
        Long id2 = rs.getLong(3);

        Prietenie prietenie = new Prietenie(id1, id2);
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
        PreparedStatement ps = connection.prepareStatement("INSERT INTO friendships(id1,id2) VALUES (?,?)");
        ps.setLong(1, prietenie.getIdPrieten1());
        ps.setLong(2, prietenie.getIdPrieten2());

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
        PreparedStatement ps = connection.prepareStatement("UPDATE friendships SET id1 = ?, id2 = ? WHERE id = ?");
        ps.setLong(1, prietenie.getIdPrieten1());
        ps.setLong(2, prietenie.getIdPrieten2());
        ps.setLong(3, prietenie.getId());

        return ps;
    }
}
