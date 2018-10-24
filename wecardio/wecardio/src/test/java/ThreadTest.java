import org.apache.http.annotation.ThreadSafe;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.locks.Lock;

/**
 * Created by tantian on 2018/1/6.
 */
@ThreadSafe
public class ThreadTest {

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        public Connection initialValue() {
            return null;
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();

    }

    public static void main(String[] args){
        System.out.println("Hello World");
    }




}
