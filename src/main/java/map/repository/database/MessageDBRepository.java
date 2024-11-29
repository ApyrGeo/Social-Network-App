package map.repository.database;

import map.domain.Entity;
import map.domain.Message;
import map.domain.ReplyMessage;
import map.domain.validators.Validator;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDBRepository extends AbstractDbRepository<Message>{

    public MessageDBRepository(String url, String username, String password, Validator validator) {
        super(url, username, password, validator);
    }

    @Override
    public Message createEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long id_from = rs.getLong("id_from");
        String message = rs.getString("message");
        LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();
        Long reply_to = rs.getLong("reply_to");

        Message m;
        if(reply_to == 0){
            m = new Message(id_from, message, data);
            m.setId(id);
        }
        else {
            Optional<Message> om = findOne(reply_to);
            if(om.isEmpty()){
                System.out.println("invalid id");
                return null;
            }
            m = new ReplyMessage(id_from, message, data, om.get());
            m.setId(id);
        }

        Long id_user_to = rs.getLong("id_user_to");
        m.addTo(id_user_to);

        return m;
    }

    @Override
    public PreparedStatement findOneStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages M JOIN messages_to MT ON M.id = MT.id_message WHERE id = ?");
        ps.setLong(1, id);
        return ps;
    }

    @Override
    public PreparedStatement findAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM messages M JOIN messages_to MT ON M.id = MT.id_message");
    }

    @Override
    public PreparedStatement saveStatement(Connection connection, Message entity) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        ps.setLong(1, id);
        return ps;
    }

    @Override
    public PreparedStatement updateStatement(Connection connection, Message entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE messages SET message = ?, data = ?, reply_to = ? WHERE id = ?");
        ps.setString(1, entity.getMessage());
        ps.setDate(2, Date.valueOf(entity.getData().toLocalDate()));

        if(entity.getClass() == ReplyMessage.class && ((ReplyMessage) entity).getInitialMessage() != null){
            ps.setLong(3, ((ReplyMessage) entity).getInitialMessage().getId());
        }
        else {
            ps.setNull(3, Types.NULL);
        }
        ps.setLong(4, entity.getId());

        return ps;
    }

    @Override
    public Optional<Message> save(Message entity) {

        try(Connection connection = DriverManager.getConnection(url, username, password))
        {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO messages(id_from, message, data, reply_to) VALUES (?, ?, ?, ?) RETURNING id");
            ps.setLong(1, entity.getFrom());
            ps.setString(2, entity.getMessage());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getData()));

            if(entity.getClass() == ReplyMessage.class){
                ps.setLong(4, ((ReplyMessage) entity).getInitialMessage().getId());
            }
            else ps.setNull(4, Types.NULL);

            ResultSet rs = ps.executeQuery();
            rs.next();
            Long id = rs.getLong("id");


            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO messages_to(id_user_to, id_message) VALUES (?, ?)");
            entity.getTo().forEach(e -> {
                try {
                    ps2.setLong(1, e);
                    ps2.setLong(2, id);
                    ps2.executeUpdate();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.ofNullable(entity);
        }
    }

//    private List<Long> findMessagesTo(Long id_msg) {
//        List<Long> to_list = new ArrayList<>();
//
//        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)){
//
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Iterable<Message> findAll() {
//        Set<Message> messages = new HashSet<>();
//        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)) {
//            PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages");
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    public Iterable<Message> findAll(Long id1, Long id2) throws SQLException {
        List<Message> messages = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(super.url, super.username, super.password)){
            PreparedStatement ps = connection.prepareStatement(
            """
                SELECT * FROM messages M 
                INNER JOIN messages_to MT ON M.id = MT.id_message 
                WHERE (id_from = ? OR id_from = ?) AND (id_user_to = ? OR id_user_to = ?)
                ORDER BY data
            """);

            ps.setLong(1, id1);
            ps.setLong(2, id2);
            ps.setLong(3, id2);
            ps.setLong(4, id1);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                messages.add(createEntity(rs));
            }
        }

        return messages;
    }
}
