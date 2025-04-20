package mx.edu.utez.intenarvaez.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@Order(1)

public class TriggerInitializer implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(TriggerInitializer.class);
    private final JdbcTemplate jdbcTemplate;

    public TriggerInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        int count = 0;

        String[] triggers = {
                """
            ## DROP TRIGGER IF EXISTS after_insert_user;
            CREATE TRIGGER after_insert_user
                AFTER INSERT
                ON user
                FOR EACH ROW
            BEGIN
                INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                VALUES ('user',
                        NOW(),
                        CONCAT(
                                'INSERT INTO user (user_id, birthdate, email, first_name, last_name, password, phone, rfc, status, surname, temporal_password, last_login) VALUES (',
                                IFNULL(NEW.user_id, 0), ', ''', IFNULL(NEW.birthdate, ''), ''', ''', IFNULL(NEW.email, ''),
                                ''', ''', IFNULL(NEW.first_name, ''),
                                ''', ''', IFNULL(NEW.last_name, ''), ''', ''', IFNULL(NEW.password, ''), ''', ''',
                                IFNULL(NEW.phone, ''),
                                ''', ''', IFNULL(NEW.rfc, ''), ''', ', IFNULL(NEW.status, 0), ', ''', IFNULL(NEW.surname, ''),
                                ''', ', IFNULL(NEW.temporal_password, 0), ', ''', IFNULL(NEW.last_login, ''), ''');'
                        ),
                        CONCAT('DELETE FROM user WHERE user_id = ', IFNULL(NEW.user_id, 0), ';'));
            END;
            
            """
                ,
                """
                    ## DROP TRIGGER IF EXISTS after_delete_user;
                    
                    
                    CREATE TRIGGER after_delete_user
                        AFTER DELETE
                        ON user
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('user',
                                NOW(),
                                CONCAT('DELETE FROM user WHERE user_id = ', IFNULL(OLD.user_id, 0), ';'),
                                CONCAT(
                                        'INSERT INTO user (user_id, birthdate, email, first_name, last_name, password, phone, rfc, status, surname, temporal_password, last_login) VALUES (',
                                        IFNULL(OLD.user_id, 0), ', ''', IFNULL(OLD.birthdate, ''), ''', ''', IFNULL(OLD.email, ''),
                                        ''', ''', IFNULL(OLD.first_name, ''),
                                        ''', ''', IFNULL(OLD.last_name, ''), ''', ''', IFNULL(OLD.password, ''), ''', ''',
                                        IFNULL(OLD.phone, ''),
                                        ''', ''', IFNULL(OLD.rfc, ''), ''', ', IFNULL(OLD.status, 0), ', ''', IFNULL(OLD.surname, ''),
                                        ''', ', IFNULL(OLD.temporal_password, 0), ', ''', IFNULL(OLD.last_login, ''), ''');'
                                ));
                    END;
                    """,
                """
                    ## DROP TRIGGER IF EXISTS after_update_user;
                    CREATE TRIGGER after_update_user
                        AFTER UPDATE
                        ON user
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('user',
                                NOW(),
                                CONCAT(
                                        'UPDATE user SET birthdate = ''', IFNULL(NEW.birthdate, ''), ''', email = ''',
                                        IFNULL(NEW.email, ''),
                                        ''', first_name = ''', IFNULL(NEW.first_name, ''), ''', last_name = ''', IFNULL(NEW.last_name, ''),
                                        ''', password = ''', IFNULL(NEW.password, ''), ''', phone = ''', IFNULL(NEW.phone, ''),
                                        ''', rfc = ''', IFNULL(NEW.rfc, ''), ''', status = ', IFNULL(NEW.status, 0),
                                        ', surname = ''', IFNULL(NEW.surname, ''), ''', temporal_password = ',
                                        IFNULL(NEW.temporal_password, 0),
                                        ', last_login = ''', IFNULL(NEW.last_login, ''), ''' WHERE user_id = ', IFNULL(OLD.user_id, 0), ';'
                                ),
                                CONCAT(
                                        'UPDATE user SET birthdate = ''', IFNULL(OLD.birthdate, ''), ''', email = ''',
                                        IFNULL(OLD.email, ''),
                                        ''', first_name = ''', IFNULL(OLD.first_name, ''), ''', last_name = ''', IFNULL(OLD.last_name, ''),
                                        ''', password = ''', IFNULL(OLD.password, ''), ''', phone = ''', IFNULL(OLD.phone, ''),
                                        ''', rfc = ''', IFNULL(OLD.rfc, ''), ''', status = ', IFNULL(OLD.status, 0),
                                        ', surname = ''', IFNULL(OLD.surname, ''), ''', temporal_password = ',
                                        IFNULL(OLD.temporal_password, 0),
                                        ', last_login = ''', IFNULL(OLD.last_login, ''), ''' WHERE user_id = ', IFNULL(NEW.user_id, 0), ';'
                                ));
                    END;
                    """,

                """
                    ## DROP TRIGGER IF EXISTS after_insert_addresses;
                    CREATE TRIGGER after_insert_addresses
                        AFTER INSERT
                        ON addresses
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('addresses',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO addresses (id, city, name, number, state, status, street, uuid, zip_code, client_id) VALUES (',
                                        IFNULL(NEW.id, 0), ', ''', IFNULL(NEW.city, ''), ''', ''', IFNULL(NEW.name, ''), ''', ''',
                                        IFNULL(NEW.number, ''),
                                        ''', ''', IFNULL(NEW.state, ''), ''', ', IFNULL(NEW.status, 0), ', ''', IFNULL(NEW.street, ''),
                                        ''', ''', IFNULL(NEW.uuid, ''), ''', ', IFNULL(NEW.zip_code, 0), ', ', IFNULL(NEW.client_id, 0),
                                        ');'
                                ),
                                CONCAT('DELETE FROM addresses WHERE id = ', IFNULL(NEW.id, 0), ';'));
                    END;
                    """,
                """
                    ## DROP TRIGGER IF EXISTS after_delete_addresses;
                    CREATE TRIGGER after_delete_addresses
                        AFTER DELETE
                        ON addresses
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('addresses',
                                NOW(),
                                CONCAT('DELETE FROM addresses WHERE id = ', IFNULL(OLD.id, 0), ';'),
                                CONCAT(
                                        'INSERT INTO addresses (id, city, name, number, state, status, street, uuid, zip_code, client_id) VALUES (',
                                        IFNULL(OLD.id, 0), ', ''', IFNULL(OLD.city, ''), ''', ''', IFNULL(OLD.name, ''), ''', ''',
                                        IFNULL(OLD.number, ''),
                                        ''', ''', IFNULL(OLD.state, ''), ''', ', IFNULL(OLD.status, 0), ', ''', IFNULL(OLD.street, ''),
                                        ''', ''', IFNULL(OLD.uuid, ''), ''', ', IFNULL(OLD.zip_code, 0), ', ', IFNULL(OLD.client_id, 0),
                                        ');'
                                ));
                    END;
                    """,
                """
                    ## DROP TRIGGER IF EXISTS after_update_addresses;
                    CREATE TRIGGER after_update_addresses
                        AFTER UPDATE
                        ON addresses
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('addresses',
                                NOW(),
                                CONCAT(
                                        'UPDATE addresses SET city = ''', IFNULL(NEW.city, ''), ''', name = ''', IFNULL(NEW.name, ''),
                                        ''', number = ''', IFNULL(NEW.number, ''), ''', state = ''', IFNULL(NEW.state, ''),
                                        ''', status = ', IFNULL(NEW.status, 0), ', street = ''', IFNULL(NEW.street, ''),
                                        ''', uuid = ''', IFNULL(NEW.uuid, ''), ''', zip_code = ', IFNULL(NEW.zip_code, 0),
                                        ', client_id = ', IFNULL(NEW.client_id, 0), ' WHERE id = ', IFNULL(OLD.id, 0), ';'
                                ),
                                CONCAT(
                                        'UPDATE addresses SET city = ''', IFNULL(OLD.city, ''), ''', name = ''', IFNULL(OLD.name, ''),
                                        ''', number = ''', IFNULL(OLD.number, ''), ''', state = ''', IFNULL(OLD.state, ''),
                                        ''', status = ', IFNULL(OLD.status, 0), ', street = ''', IFNULL(OLD.street, ''),
                                        ''', uuid = ''', IFNULL(OLD.uuid, ''), ''', zip_code = ', IFNULL(OLD.zip_code, 0),
                                        ', client_id = ', IFNULL(OLD.client_id, 0), ' WHERE id = ', IFNULL(NEW.id, 0), ';'
                                ));
                    END;
                    """,
                """
                    ## DROP TRIGGER IF EXISTS after_insert_channel_categories;
                    
                    
                    CREATE TRIGGER after_insert_channel_categories
                        AFTER INSERT
                        ON channel_categories
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_categories',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO channel_categories (id, name, status, uuid) VALUES (',
                                        IFNULL(NEW.id, 0), ', ''',
                                        IFNULL(NEW.name, ''), ''', ',
                                        IFNULL(NEW.status, 0), ', ',
                                        IF(NEW.uuid IS NULL, 'NULL', CONCAT('''', NEW.uuid, '''')), ');'
                                ),
                                CONCAT('DELETE FROM channel_categories WHERE id = ', IFNULL(NEW.id, 0), ';'));
                    END;
                    """,
                """
                    ## DROP TRIGGER IF EXISTS after_delete_channel_categories;
                    
                    
                    CREATE TRIGGER after_delete_channel_categories
                        AFTER DELETE
                        ON channel_categories
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_categories',
                                NOW(),
                                CONCAT('DELETE FROM channel_categories WHERE id = ', IFNULL(OLD.id, 0), ';'),
                                CONCAT(
                                        'INSERT INTO channel_categories (id, name, status, uuid) VALUES (',
                                        IFNULL(OLD.id, 0), ', ''',
                                        IFNULL(OLD.name, ''), ''', ',
                                        IFNULL(OLD.status, 0), ', ',
                                        IF(OLD.uuid IS NULL, 'NULL', CONCAT('''', OLD.uuid, '''')), ');'
                                ));
                    END;
                    """,

                """
                    ## DROP TRIGGER IF EXISTS after_update_channel_categories;
                    CREATE TRIGGER after_update_channel_categories
                        AFTER UPDATE
                        ON channel_categories
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_categories',
                                NOW(),
                                CONCAT(
                                        'UPDATE channel_categories SET name = ''', IFNULL(NEW.name, ''),
                                        ''', status = ', IFNULL(NEW.status, 0),
                                        ', uuid = ', IF(NEW.uuid IS NULL, 'NULL', CONCAT('''', NEW.uuid, '''')),
                                        ' WHERE id = ', IFNULL(OLD.id, 0), ';'
                                ),
                                CONCAT(
                                        'UPDATE channel_categories SET name = ''', IFNULL(OLD.name, ''),
                                        ''', status = ', IFNULL(OLD.status, 0),
                                        ', uuid = ', IF(OLD.uuid IS NULL, 'NULL', CONCAT('''', OLD.uuid, '''')),
                                        ' WHERE id = ', IFNULL(NEW.id, 0), ';'
                                ));
                    END;
                    """,


                """
                    ## DROP TRIGGER IF EXISTS after_insert_channel_package_channels;
                    CREATE TRIGGER after_insert_channel_package_channels
                        AFTER INSERT
                        ON channel_package_channels
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_package_channels',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO channel_package_channels (channel_package_id, channel_id) VALUES (',
                                        IFNULL(NEW.channel_package_id, 0), ', ', IFNULL(NEW.channel_id, 0), ');'
                                ),
                                CONCAT(
                                        'DELETE FROM channel_package_channels WHERE channel_package_id = ',
                                        IFNULL(NEW.channel_package_id, 0), ' AND channel_id = ', IFNULL(NEW.channel_id, 0), ';'
                                ));
                    END;
                    """,

                """
                    ## DROP TRIGGER IF EXISTS after_delete_channel_package_channels;
                    
                    
                    CREATE TRIGGER after_delete_channel_package_channels
                        AFTER DELETE
                        ON channel_package_channels
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_package_channels',
                                NOW(),
                                CONCAT(
                                        'DELETE FROM channel_package_channels WHERE channel_package_id = ',
                                        IFNULL(OLD.channel_package_id, 0), ' AND channel_id = ', IFNULL(OLD.channel_id, 0), ';'
                                ),
                                CONCAT(
                                        'INSERT INTO channel_package_channels (channel_package_id, channel_id) VALUES (',
                                        IFNULL(OLD.channel_package_id, 0), ', ', IFNULL(OLD.channel_id, 0), ');'
                                ));
                    END;
                    """,


                """
                    ## DROP TRIGGER IF EXISTS after_update_channel_package_channels;
                    
                    
                    CREATE TRIGGER after_update_channel_package_channels
                        AFTER UPDATE
                        ON channel_package_channels
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_package_channels',
                                NOW(),
                                CONCAT(
                                        'UPDATE channel_package_channels SET channel_package_id = ', IFNULL(NEW.channel_package_id, 0),
                                        ', channel_id = ', IFNULL(NEW.channel_id, 0),
                                        ' WHERE channel_package_id = ', IFNULL(OLD.channel_package_id, 0),
                                        ' AND channel_id = ', IFNULL(OLD.channel_id, 0), ';'
                                ),
                                CONCAT(
                                        'UPDATE channel_package_channels SET channel_package_id = ', IFNULL(OLD.channel_package_id, 0),
                                        ', channel_id = ', IFNULL(OLD.channel_id, 0),
                                        ' WHERE channel_package_id = ', IFNULL(NEW.channel_package_id, 0),
                                        ' AND channel_id = ', IFNULL(NEW.channel_id, 0), ';'
                                ));
                    END;
                    """,


                """
                    ## DROP TRIGGER IF EXISTS after_insert_channel_packages;
                    
                    
                    CREATE TRIGGER after_insert_channel_packages
                        AFTER INSERT
                        ON channel_packages
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_packages',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO channel_packages (id, description, name, status, uuid) VALUES (',
                                        IFNULL(NEW.id, 0), ', ',
                                        IFNULL(CONCAT('''', NEW.description, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.status, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        ');'
                                ),
                                CONCAT('DELETE FROM channel_packages WHERE id = ', IFNULL(NEW.id, 0), ';'));
                    END;
                    """,

                """
                    ## DROP TRIGGER IF EXISTS after_delete_channel_packages;
                    
                    
                    CREATE TRIGGER after_delete_channel_packages
                        AFTER DELETE
                        ON channel_packages
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_packages',
                                NOW(),
                                CONCAT('DELETE FROM channel_packages WHERE id = ', IFNULL(OLD.id, 0), ';'),
                                CONCAT(
                                        'INSERT INTO channel_packages (id, description, name, status, uuid) VALUES (',
                                        IFNULL(OLD.id, 0), ', ',
                                        IFNULL(CONCAT('''', OLD.description, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.status, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        ');'
                                ));
                    END;
                    """,

                """
                    
                    
                    ## DROP TRIGGER IF EXISTS after_update_channel_packages;
                    
                    
                    CREATE TRIGGER after_update_channel_packages
                        AFTER UPDATE
                        ON channel_packages
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channel_packages',
                                NOW(),
                                CONCAT(
                                        'UPDATE channel_packages SET description = ',
                                        IFNULL(CONCAT('''', NEW.description, ''''), 'NULL'), ', ',
                                        'name = ', IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        'status = ', IFNULL(CONCAT('''', NEW.status, ''''), 'NULL'), ', ',
                                        'uuid = ', IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        ' WHERE id = ', IFNULL(OLD.id, 0), ';'
                                ),
                                CONCAT(
                                        'UPDATE channel_packages SET description = ',
                                        IFNULL(CONCAT('''', OLD.description, ''''), 'NULL'), ', ',
                                        'name = ', IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        'status = ', IFNULL(CONCAT('''', OLD.status, ''''), 'NULL'), ', ',
                                        'uuid = ', IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        ' WHERE id = ', IFNULL(NEW.id, 0), ';'
                                ));
                    END;
                    """,


                """
                    ## DROP TRIGGER IF EXISTS after_insert_channels;
                    
                    
                    CREATE TRIGGER after_insert_channels
                        AFTER INSERT
                        ON channels
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channels',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO channels (id, description, name, number, status, uuid, category_id) VALUES (',
                                        IFNULL(NEW.id, 0), ', ',
                                        IFNULL(CONCAT('''', NEW.description, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        IFNULL(NEW.number, 'NULL'), ', ',
                                        IFNULL(NEW.status, 'NULL'), ', ',
                                        IFNULL(NEW.uuid, 'NULL'), ', ',
                                        IFNULL(NEW.category_id, 'NULL'), ');'
                                ),
                                CONCAT('DELETE FROM channels WHERE id = ', IFNULL(NEW.id, 0), ';'));
                    END;
                    """,

                """
                    ## DROP TRIGGER IF EXISTS after_delete_channels;
                    
                    
                    CREATE TRIGGER after_delete_channels
                        AFTER DELETE
                        ON channels
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('channels',
                                NOW(),
                                CONCAT('DELETE FROM channels WHERE id = ', IFNULL(OLD.id, 0), ';'),
                                CONCAT(
                                        'INSERT INTO channels (id, description, name, number, status, uuid, category_id) VALUES (',
                                        IFNULL(OLD.id, 0), ', ',
                                        IFNULL(CONCAT('''', OLD.description, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        IFNULL(OLD.number, 'NULL'), ', ',
                                        IFNULL(OLD.status, 'NULL'), ', ',
                                        IFNULL(OLD.uuid, 'NULL'), ', ',
                                        IFNULL(OLD.category_id, 'NULL'), ');'
                                ));
                    END;
                    """,

                """
            ## DROP TRIGGER IF EXISTS after_update_channels;
            
            
            CREATE TRIGGER after_update_channels
                AFTER UPDATE
                ON channels
                FOR EACH ROW
            BEGIN
                INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                VALUES ('channels',
                        NOW(),
                        CONCAT(
                                'UPDATE channels SET ',
                                'description = ', IFNULL(CONCAT('''', NEW.description, ''''), 'NULL'), ', ',
                                'name = ', IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                'number = ', IFNULL(NEW.number, 'NULL'), ', ',
                                'status = ', IFNULL(NEW.status, 'NULL'), ', ',
                                'uuid = ', IFNULL(NEW.uuid, 'NULL'), ', ',
                                'category_id = ', IFNULL(NEW.category_id, 'NULL'),
                                ' WHERE id = ', IFNULL(OLD.id, 0), ';'
                        ),
                        CONCAT(
                                'UPDATE channels SET ',
                                'description = ', IFNULL(CONCAT('''', OLD.description, ''''), 'NULL'), ', ',
                                'name = ', IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                'number = ', IFNULL(OLD.number, 'NULL'), ', ',
                                'status = ', IFNULL(OLD.status, 'NULL'), ', ',
                                'uuid = ', IFNULL(OLD.uuid, 'NULL'), ', ',
                                'category_id = ', IFNULL(OLD.category_id, 'NULL'),
                                ' WHERE id = ', IFNULL(NEW.id, 0), ';'
                        ));
            END;
            """,


                """
                    ## DROP TRIGGER IF EXISTS after_insert_clients;
                    
                    
                    CREATE TRIGGER after_insert_clients
                        AFTER INSERT
                        ON clients
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('clients',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO clients (id, birthdate, email, last_name, name, phone, rfc, status, surname, uuid) VALUES (',
                                        IFNULL(NEW.id, 0), ', ',
                                        IFNULL(CONCAT('''', NEW.birthdate, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.email, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.last_name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.phone, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.rfc, ''''), 'NULL'), ', ',
                                        IFNULL(NEW.status, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.surname, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        ');'
                                ),
                                CONCAT('DELETE FROM clients WHERE id = ', IFNULL(NEW.id, 0), ';'));
                    END;
                    """,

                """
                    
                    
                    ## DROP TRIGGER IF EXISTS after_delete_clients;
                    
                    
                    CREATE TRIGGER after_delete_clients
                        AFTER DELETE
                        ON clients
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('clients',
                                NOW(),
                                CONCAT('DELETE FROM clients WHERE id = ', IFNULL(OLD.id, 0), ';'),
                                CONCAT(
                                        'INSERT INTO clients (id, birthdate, email, last_name, name, phone, rfc, status, surname, uuid) VALUES (',
                                        IFNULL(OLD.id, 0), ', ',
                                        IFNULL(CONCAT('''', OLD.birthdate, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.email, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.last_name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.phone, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.rfc, ''''), 'NULL'), ', ',
                                        IFNULL(OLD.status, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.surname, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        ');'
                                ));
                    END;
                    
                    """,
                """
                    ## DROP TRIGGER IF EXISTS after_update_clients;
                    
                    
                    CREATE TRIGGER after_update_clients
                        AFTER UPDATE
                        ON clients
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('clients',
                                NOW(),
                                CONCAT(
                                        'UPDATE clients SET ',
                                        'birthdate = ', IFNULL(CONCAT('''', NEW.birthdate, ''''), 'NULL'), ', ',
                                        'email = ', IFNULL(CONCAT('''', NEW.email, ''''), 'NULL'), ', ',
                                        'last_name = ', IFNULL(CONCAT('''', NEW.last_name, ''''), 'NULL'), ', ',
                                        'name = ', IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        'phone = ', IFNULL(CONCAT('''', NEW.phone, ''''), 'NULL'), ', ',
                                        'rfc = ', IFNULL(CONCAT('''', NEW.rfc, ''''), 'NULL'), ', ',
                                        'status = ', IFNULL(NEW.status, 'NULL'), ', ',
                                        'surname = ', IFNULL(CONCAT('''', NEW.surname, ''''), 'NULL'), ', ',
                                        'uuid = ', IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        ' WHERE id = ', IFNULL(OLD.id, 0), ';'
                                ),
                                CONCAT(
                                        'UPDATE clients SET ',
                                        'birthdate = ', IFNULL(CONCAT('''', OLD.birthdate, ''''), 'NULL'), ', ',
                                        'email = ', IFNULL(CONCAT('''', OLD.email, ''''), 'NULL'), ', ',
                                        'last_name = ', IFNULL(CONCAT('''', OLD.last_name, ''''), 'NULL'), ', ',
                                        'name = ', IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        'phone = ', IFNULL(CONCAT('''', OLD.phone, ''''), 'NULL'), ', ',
                                        'rfc = ', IFNULL(CONCAT('''', OLD.rfc, ''''), 'NULL'), ', ',
                                        'status = ', IFNULL(OLD.status, 'NULL'), ', ',
                                        'surname = ', IFNULL(CONCAT('''', OLD.surname, ''''), 'NULL'), ', ',
                                        'uuid = ', IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        ' WHERE id = ', IFNULL(NEW.id, 0), ';'
                                ));
                    END;
                    """,


                """
                    ## DROP TRIGGER IF EXISTS after_insert_contracts;
                    
                    
                    CREATE TRIGGER after_insert_contracts
                        AFTER INSERT
                        ON contracts
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('contracts',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO contracts (id, creation_date, status, uuid, address_id, sales_agent_id, sales_package_id) VALUES (',
                                        IFNULL(NEW.id, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.creation_date, ''''), 'NULL'), ', ',
                                        IFNULL(NEW.status, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        IFNULL(NEW.address_id, 'NULL'), ', ',
                                        IFNULL(NEW.sales_agent_id, 'NULL'), ', ',
                                        IFNULL(NEW.sales_package_id, 'NULL'), ');'
                                ),
                                CONCAT('DELETE FROM contracts WHERE id = ', IFNULL(NEW.id, 'NULL'), ';'));
                    END;
                    """,

                """
                    ## DROP TRIGGER IF EXISTS after_delete_contracts;
                    
                    
                    CREATE TRIGGER after_delete_contracts
                        AFTER DELETE
                        ON contracts
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('contracts',
                                NOW(),
                                CONCAT('DELETE FROM contracts WHERE id = ', IFNULL(OLD.id, 'NULL'), ';'),
                                CONCAT(
                                        'INSERT INTO contracts (id, creation_date, status, uuid, address_id, sales_agent_id, sales_package_id) VALUES (',
                                        IFNULL(OLD.id, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.creation_date, ''''), 'NULL'), ', ',
                                        IFNULL(OLD.status, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        IFNULL(OLD.address_id, 'NULL'), ', ',
                                        IFNULL(OLD.sales_agent_id, 'NULL'), ', ',
                                        IFNULL(OLD.sales_package_id, 'NULL'), ');'
                                ));
                    END;
                    """,


                """
                    ## DROP TRIGGER IF EXISTS after_update_contracts;
                    
                    
                    CREATE TRIGGER after_update_contracts
                        AFTER UPDATE
                        ON contracts
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('contracts',
                                NOW(),
                                CONCAT(
                                        'UPDATE contracts SET creation_date = ', IFNULL(CONCAT('''', NEW.creation_date, ''''), 'NULL'),
                                        ', status = ', IFNULL(NEW.status, 'NULL'),
                                        ', uuid = ', IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'),
                                        ', address_id = ', IFNULL(NEW.address_id, 'NULL'),
                                        ', sales_agent_id = ', IFNULL(NEW.sales_agent_id, 'NULL'),
                                        ', sales_package_id = ', IFNULL(NEW.sales_package_id, 'NULL'),
                                        ' WHERE id = ', IFNULL(OLD.id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'UPDATE contracts SET creation_date = ', IFNULL(CONCAT('''', OLD.creation_date, ''''), 'NULL'),
                                        ', status = ', IFNULL(OLD.status, 'NULL'),
                                        ', uuid = ', IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'),
                                        ', address_id = ', IFNULL(OLD.address_id, 'NULL'),
                                        ', sales_agent_id = ', IFNULL(OLD.sales_agent_id, 'NULL'),
                                        ', sales_package_id = ', IFNULL(OLD.sales_package_id, 'NULL'),
                                        ' WHERE id = ', IFNULL(NEW.id, 'NULL'), ';'
                                ));
                    END;
                    """,
                """
                 CREATE TRIGGER after_insert_logos
                 AFTER INSERT ON logos
                 FOR EACH ROW
                 BEGIN
                     INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                     VALUES (
                         'logos',
                         NOW(),
                         CONCAT(
                             'INSERT INTO logos (id, file_extension, image, uuid, channel_id) VALUES (',
                             IFNULL(NEW.id, 'NULL'), ', ',
                             IFNULL(CONCAT('\\'', NEW.file_extension, '\\''), 'NULL'), ', ',
                             'NULL, ', -- Aquí se fuerza a NULL la imagen
                             IFNULL(CONCAT('\\'', NEW.uuid, '\\''), 'NULL'), ', ',
                             IFNULL(NEW.channel_id, 'NULL'), ');'
                         ),
                         CONCAT('DELETE FROM logos WHERE id = ', IFNULL(NEW.id, 'NULL'), ';')
                     );
                 END;
                    """,


                """
                    CREATE TRIGGER after_delete_logos
                        AFTER DELETE ON logos
                        FOR EACH ROW
                        BEGIN
                            INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                            VALUES (
                                'logos',
                                NOW(),
                                CONCAT('DELETE FROM logos WHERE id = ', IFNULL(OLD.id, 'NULL'), ';'),
                                CONCAT(
                                    'INSERT INTO logos (id, file_extension, image, uuid, channel_id) VALUES (',
                                    IFNULL(OLD.id, 'NULL'), ', ',
                                    IFNULL(CONCAT('\\'', OLD.file_extension, '\\''), 'NULL'), ', ',
                                    'NULL, ', -- Aquí se fuerza a NULL la imagen
                                    IFNULL(CONCAT('\\'', OLD.uuid, '\\''), 'NULL'), ', ',
                                    IFNULL(OLD.channel_id, 'NULL'), ');'
                                )
                            );
                        END;
                    """,


                """
                    
                    CREATE TRIGGER after_update_logos
                        AFTER UPDATE
                        ON logos
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('logos',
                                NOW(),
                                CONCAT(
                                        'UPDATE logos SET file_extension = ',
                                        IFNULL(CONCAT('''', NEW.file_extension, ''''), 'NULL'),
                                        '', image = NULL, '',
                                        ', uuid = ', IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'),
                                        ', channel_id = ', IFNULL(NEW.channel_id, 'NULL'),
                                        ' WHERE id = ', IFNULL(OLD.id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'UPDATE logos SET file_extension = ',
                                        IFNULL(CONCAT('''', OLD.file_extension, ''''), 'NULL'),
                                        '', image = NULL, '',
                                        ', uuid = ', IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'),
                                        ', channel_id = ', IFNULL(OLD.channel_id, 'NULL'),
                                        ' WHERE id = ', IFNULL(NEW.id, 'NULL'), ';'
                                ));
                    END;
                    """,

                """
                    CREATE TRIGGER after_insert_roles
                        AFTER INSERT
                        ON roles
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('roles',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO roles (role_id, name, uuid) VALUES (',
                                        IFNULL(NEW.role_id, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ');'
                                ),
                                CONCAT(
                                        'DELETE FROM roles WHERE role_id = ', IFNULL(NEW.role_id, 'NULL'), ';'
                                ));
                    END;
                    
                    """,
                """
                    
                    
                    ## DROP TRIGGER IF EXISTS after_delete_roles;
                    
                    
                    CREATE TRIGGER after_delete_roles
                        AFTER DELETE
                        ON roles
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('roles',
                                NOW(),
                                CONCAT(
                                        'DELETE FROM roles WHERE role_id = ', IFNULL(OLD.role_id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'INSERT INTO roles (role_id, name, uuid) VALUES (',
                                        IFNULL(OLD.role_id, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ');'
                                ));
                    END;
                    
                    
                    """,
                """
                    
                    ## DROP TRIGGER IF EXISTS after_update_roles;
                    
                    
                    CREATE TRIGGER after_update_roles
                        AFTER UPDATE
                        ON roles
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('roles',
                                NOW(),
                                CONCAT(
                                        'UPDATE roles SET name = ',
                                        IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'),
                                        ', uuid = ',
                                        IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'),
                                        ' WHERE role_id = ', IFNULL(OLD.role_id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'UPDATE roles SET name = ',
                                        IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'),
                                        ', uuid = ',
                                        IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'),
                                        ' WHERE role_id = ', IFNULL(NEW.role_id, 'NULL'), ';'
                                ));
                    END;
                    
                    """,
                """
                    
                    ## DROP TRIGGER IF EXISTS after_insert_sales_packages;
                    
                    
                    CREATE TRIGGER after_insert_sales_packages
                        AFTER INSERT
                        ON sales_packages
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('sales_packages',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO sales_packages (id, name, speed, status, total_amount, uuid, channel_package_id) VALUES (',
                                        IFNULL(NEW.id, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.speed, ''''), 'NULL'), ', ',
                                        IFNULL(NEW.status, 'NULL'), ', ',
                                        IFNULL(NEW.total_amount, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        IFNULL(NEW.channel_package_id, 'NULL'), ');'
                                ),
                                CONCAT('DELETE FROM sales_packages WHERE id = ', IFNULL(NEW.id, 'NULL'), ';'));
                    END;
                    
                    """,
                """
                    
                    ## DROP TRIGGER IF EXISTS after_delete_sales_packages;
                    
                    
                    CREATE TRIGGER after_delete_sales_packages
                        AFTER DELETE
                        ON sales_packages
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('sales_packages',
                                NOW(),
                                CONCAT('DELETE FROM sales_packages WHERE id = ', IFNULL(OLD.id, 'NULL'), ';'),
                                CONCAT(
                                        'INSERT INTO sales_packages (id, name, speed, status, total_amount, uuid, channel_package_id) VALUES (',
                                        IFNULL(OLD.id, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.speed, ''''), 'NULL'), ', ',
                                        IFNULL(OLD.status, 'NULL'), ', ',
                                        IFNULL(OLD.total_amount, 'NULL'), ', ',
                                        IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        IFNULL(OLD.channel_package_id, 'NULL'), ');'
                                ));
                    END;
                    
                    """,
                """
                    
                    
                    ## DROP TRIGGER IF EXISTS after_update_sales_packages;
                    
                    
                    CREATE TRIGGER after_update_sales_packages
                        AFTER UPDATE
                        ON sales_packages
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('sales_packages',
                                NOW(),
                                CONCAT(
                                        'UPDATE sales_packages SET ',
                                        'name = ', IFNULL(CONCAT('''', NEW.name, ''''), 'NULL'), ', ',
                                        'speed = ', IFNULL(CONCAT('''', NEW.speed, ''''), 'NULL'), ', ',
                                        'status = ', IFNULL(NEW.status, 'NULL'), ', ',
                                        'total_amount = ', IFNULL(NEW.total_amount, 'NULL'), ', ',
                                        'uuid = ', IFNULL(CONCAT('''', NEW.uuid, ''''), 'NULL'), ', ',
                                        'channel_package_id = ', IFNULL(NEW.channel_package_id, 'NULL'),
                                        ' WHERE id = ', IFNULL(OLD.id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'UPDATE sales_packages SET ',
                                        'name = ', IFNULL(CONCAT('''', OLD.name, ''''), 'NULL'), ', ',
                                        'speed = ', IFNULL(CONCAT('''', OLD.speed, ''''), 'NULL'), ', ',
                                        'status = ', IFNULL(OLD.status, 'NULL'), ', ',
                                        'total_amount = ', IFNULL(OLD.total_amount, 'NULL'), ', ',
                                        'uuid = ', IFNULL(CONCAT('''', OLD.uuid, ''''), 'NULL'), ', ',
                                        'channel_package_id = ', IFNULL(OLD.channel_package_id, 'NULL'),
                                        ' WHERE id = ', IFNULL(NEW.id, 'NULL'), ';'
                                ));
                    END;
                    
                    
                    """,
                """
                    
                    #---Trigguers para Tabla  user_has_roles
                    ## DROP TRIGGER IF EXISTS after_insert_user_has_roles;
                    
                    
                    CREATE TRIGGER after_insert_user_has_roles
                        AFTER INSERT
                        ON user_has_roles
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('user_has_roles',
                                NOW(),
                                CONCAT(
                                        'INSERT INTO user_has_roles (role_id, user_id) VALUES (',
                                        IFNULL(NEW.role_id, 'NULL'), ', ', IFNULL(NEW.user_id, 'NULL'), ');'
                                ),
                                CONCAT(
                                        'DELETE FROM user_has_roles WHERE role_id = ', IFNULL(NEW.role_id, 'NULL'),
                                        ' AND user_id = ', IFNULL(NEW.user_id, 'NULL'), ';'
                                ));
                    END;
                    
                    """,
                """
                    
                    
                    ## DROP TRIGGER IF EXISTS after_delete_user_has_roles;
                    
                    
                    CREATE TRIGGER after_delete_user_has_roles
                        AFTER DELETE
                        ON user_has_roles
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('user_has_roles',
                                NOW(),
                                CONCAT(
                                        'DELETE FROM user_has_roles WHERE role_id = ', IFNULL(OLD.role_id, 'NULL'),
                                        ' AND user_id = ', IFNULL(OLD.user_id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'INSERT INTO user_has_roles (role_id, user_id) VALUES (',
                                        IFNULL(OLD.role_id, 'NULL'), ', ', IFNULL(OLD.user_id, 'NULL'), ');'
                                ));
                    END;
                    
                    
                    """,
                """
                    
                    ## DROP TRIGGER IF EXISTS after_update_user_has_roles;
                    
                    
                    CREATE TRIGGER after_update_user_has_roles
                        AFTER UPDATE
                        ON user_has_roles
                        FOR EACH ROW
                    BEGIN
                        INSERT INTO bitacora (tabla, fecha, executedSQL, reverseSQL)
                        VALUES ('user_has_roles',
                                NOW(),
                                CONCAT(
                                        'UPDATE user_has_roles SET role_id = ', IFNULL(NEW.role_id, 'NULL'),
                                        ', user_id = ', IFNULL(NEW.user_id, 'NULL'),
                                        ' WHERE role_id = ', IFNULL(OLD.role_id, 'NULL'),
                                        ' AND user_id = ', IFNULL(OLD.user_id, 'NULL'), ';'
                                ),
                                CONCAT(
                                        'UPDATE user_has_roles SET role_id = ', IFNULL(OLD.role_id, 'NULL'),
                                        ', user_id = ', IFNULL(OLD.user_id, 'NULL'),
                                        ' WHERE role_id = ', IFNULL(NEW.role_id, 'NULL'),
                                        ' AND user_id = ', IFNULL(NEW.user_id, 'NULL'), ';'
                                ));
                    END;
                    """

        };

        for (String trigger : triggers) {
            try {
                jdbcTemplate.execute(trigger);
                logger.info("Trigger ejecutado correctamente.");
                count += 1;
            } catch (Exception e) {
                if (e.getMessage().contains("Trigger already exists")) {
                    logger.info("El trigger ya existe, no se necesita crear de nuevo.");
                } else {
                    logger.error("Error al ejecutar el trigger: ", e);
                }
            }
        }
        logger.info("Se han ejecutado {} triggers.", count);
    }
}
