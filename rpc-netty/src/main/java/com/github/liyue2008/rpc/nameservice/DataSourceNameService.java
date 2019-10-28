package com.github.liyue2008.rpc.nameservice;

import com.github.liyue2008.rpc.NameService;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class DataSourceNameService implements NameService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceNameService.class);
    private static final Map<String,String> schemes = new HashMap<>(4);
    private static Properties properties = null;
    private URI nameSerivceUri;
    public static final String STATUS_ONLINE = "01";
    public static final String STATUS_OFFLINE = "02";

    static {
        schemes.put("mysql","com.mysql.jdbc.Driver");
        schemes.put("mysql8","com.mysql.cj.jdbc.Driver");
        schemes.put("oracle","oracle.jdbc.driver.OracleDriver");
    }

    @Override
    public Collection<String> supportedSchemes() {
        return schemes.entrySet()
                .stream().map(entry->entry.getKey()).collect(Collectors.toList());
    }

    @Override
    public void connect(URI nameServiceUri) {
        String scheme = nameServiceUri.getScheme();
        if (schemes.containsKey(scheme)){
            //保存当前地址
            this.nameSerivceUri = nameServiceUri;
        }else{
            throw new RuntimeException("Unsupported scheme!");
        }
    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {


        Connection conn = getConnection();
        //存储
        try {
            //判断是否已存在
            if (isHasSerivce(serviceName,conn)){
                updateServiceStatus(serviceName,conn);
                //更新操作
                return;
            }

            String nowTime = new Date().toString();
            String id = UUID.randomUUID().toString();
            String sql = "insert into service_register(id,name,uri,status,create_time,update_time)"
                    + " values ('"+id+"','"+serviceName+"','"+uri+"','"+STATUS_ONLINE+"','"+nowTime+"','"+nowTime+"' )"; // 插入数据的sql语句

            // 创建用于执行静态sql语句的Statement对象
            Statement statement =conn.createStatement();
            statement.execute(sql);
            statement.close();
            conn.close();  //关闭数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseConnection(conn);
        }
    }

    public boolean isHasSerivce(String serviceName,Connection conn) throws SQLException {
        String sql="select * from service_register where name = '"+serviceName+"'";
        // 创建用于执行静态sql语句的Statement对象
        Statement statement =conn.createStatement();
        //执行sql语句并返还结束
        ResultSet rs=statement.executeQuery(sql);

        if (rs.next()){
            return true;
        }

        rs.close();
        statement.close();

        return false;
    }

    /**
    * 更新服务状态
    *@Author Gred
    *@Date 2019/10/28 14:27
    *@Param null
    **/
    public void updateServiceStatus(String serviceName,Connection conn) throws SQLException {
        String nowTime = new Date().toString();
        String sql = "update service_register set status= '"+STATUS_ONLINE+"' , update_time = '"+nowTime+"'" +
                "where name = '"+serviceName+"'";
        // 创建用于执行静态sql语句的Statement对象
        Statement statement =conn.createStatement();
        //执行sql语句并返还结束
        System.out.println("execute the sql is 【"+sql+"】");
        int rs =statement.executeUpdate(sql);

        if (rs > 0){
            logger.info("the service name [{}] is online ",serviceName);
            //执行通知事件
        }

        statement.close();
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        //查询
        URI uri = null;
        Connection conn = getConnection();
        //存储
        try {
            String sql="select * from service_register where name = '"+serviceName+"'";

            // 创建用于执行静态sql语句的Statement对象
            Statement statement =conn.createStatement();
            //执行sql语句并返还结束
            ResultSet rs=statement.executeQuery(sql);

            while (rs.next()){
                String status = rs.getString("status");
                if (status.equals(STATUS_ONLINE)){
                    String uripath = rs.getString("uri");
                    uri = URI.create(uripath);
                }
            }
            if (uri == null){
                throw new RuntimeException("the service was offline");
            }

            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseConnection(conn);
        }

        return uri;
    }

    //数据库连接
    public Connection getConnection() {

        Connection conn = null;//声明连接对象
        // 注册 JDBC 驱动
        String scheme = nameSerivceUri.getScheme();
        String driver = schemes.get(scheme);// 驱动程序类名
        String url = nameSerivceUri.getSchemeSpecificPart();// 防止乱码
        String username = getProperties().getProperty(scheme+".username");
        String password = getProperties().getProperty(scheme+".password");

        try {
            Class.forName(driver);// 注册(加载)驱动程序
            conn = DriverManager.getConnection(url, username, password);// 获取数据库连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    // 释放数据库连接
    public static void releaseConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties(){

        if (properties != null){
            return properties;
        }
        try {
            // 获取到classpath下的文件
            InputStream input = Class.forName(this.getClass().getName()).getResourceAsStream("/jdbcConfig.properties");
            // 获取到package下的文件
            properties = new Properties();
            properties.load(input);
            return properties;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("the jdbcConfig was not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("parse jdbcConfig is failed");
        }
    }
}
