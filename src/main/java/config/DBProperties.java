    package config;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.util.Properties;

    public class DBProperties {
        private static Properties pro = new Properties();
        static {
            try {
                File f = new File("/db.properties");
                if (f.exists()) {
                    pro.load(new FileInputStream(f));
                } else {
                    pro.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public static String host = pro.getProperty("db.host");
        public static String port = pro.getProperty("db.port");
        public static String username = pro.getProperty("db.username");
        public static String password = pro.getProperty("db.pass");
        public static String dbname = pro.getProperty("db.name");

        public static void main(String[] args) {
            System.out.println(host);
            System.out.println(port);
            System.out.println(username);
            System.out.println(password);
            System.out.println(dbname);
        }
    }
