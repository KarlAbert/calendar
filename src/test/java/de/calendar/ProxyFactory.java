package de.calendar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * Setting up a http Proxy for whole program
 * that is why the all methods are static
 *
 * unused:          because its a API. It will be used by another programmer
 * WeakerAccess:    because its a API. It will be used by another programmer
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ProxyFactory {

    /**
     * removes the Proxy from JVM
     * <p>
     * @return true if it is successful
     */
    public static boolean removeProxy(){
        try{
            System.getProperties().put("proxySet", "false");
            System.getProperties().put("proxyHost", "");
            System.getProperties().put("proxyPort", "");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //region HTTP

    /**
     * build the HTTP-Proxy
     * <p>
     *
     * @param user     is the Username
     * @param password is the User Password
     * @param host     is Host
     * @param port     is Port
     * @return true if it is successful
     */
    private static boolean buildHttp(String host, String port, String user, String password) {
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", port);
            Authenticator.setDefault(
                    new Authenticator() {
                        @Override
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    user, password.toCharArray());
                        }
                    }
            );
            System.setProperty("http.proxyUser", user);
            System.setProperty("http.proxyPassword", password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * build the HTTP-Proxy with a given config file
     * <p>
     * Throws IOException if you have Problems with the config File
     *
     * @param path path to config file. the config File should look like
     *             {
     *             http.proxyHost=
     *             http.proxyPort=
     *             http.proxyUser=
     *             http.proxyPassword=
     *             }
     * @return true if ProxySetup was successful
     * or throws a IOException if an error occurs while reading the file
     * @see #buildHttpProxy(String, String, String, String) for build without Error
     */
    public static boolean buildHttpProxy(String path) throws IOException {
        String host;
        String port;
        String user;
        String pass;
        try (InputStream inputStream = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(inputStream);

            host = prop.getProperty("http.proxyHost");
            port = prop.getProperty("http.proxyPort");
            user = prop.getProperty("http.proxyUser");
            pass = prop.getProperty("http.proxyPassword");
        }
        return buildHttp(host, port, user, pass);
    }

    /**
     * build the HTTP-Proxy with a given proxy settings
     * <p>
     * should not filled in the information within the code
     *
     * @param host     is the ProxyHost
     * @param port     is the ProxyPort
     * @param user     is the ProxyUser
     * @param password is the ProxyUserPassword
     * @return true if was successful
     * @see #buildHttpProxy(String) for solutuion with config file
     */
    public static boolean buildHttpProxy(String host, String port, String user, String password) {
        return buildHttp(host, port, user, password);
    }

    //endregion

    //region SOCKS

    /**
     * build the SOCKS-Proxy
     * <p>
     *
     * @param user     is the Username
     * @param password is the User Password
     * @param host     is Host
     * @param port     is Port
     * @return true if it is successful
     */
    private static boolean buildSocks(String host, String port, String user, String password) {
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            System.setProperty("socksProxyHost", host);
            System.setProperty("socksProxyPort", port);
            System.setProperty("java.net.socks.username", user);
            System.setProperty("java.net.socks.password", password);
            Authenticator.setDefault(
                    new Authenticator() {
                        @Override
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, password.toCharArray());
                        }
                    }
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * build the SOCKS-Proxy with a given proxy settings
     * <p>
     * should not filled in the information within the code
     *
     * @param host     is the ProxyHost
     * @param port     is the ProxyPort
     * @param user     is the ProxyUser
     * @param password is the ProxyUserPassword
     * @return true if was successful
     * @see #buildSocksProxy(String) for solutuion with config file
     */
    public static boolean buildSocksProxy(String host, String port, String user, String password) {
        return buildSocks(host, port, user, password);
    }

    /**
     * build the SOCKS-Proxy with a given config file
     * <p>
     * Throws IOException if you have Problems with the config File
     *
     * @param path path to config file. the config File should look like
     *             {
     *             socksProxyHost=
     *             socksProxyPort=
     *             java.net.socks.username=
     *             java.net.socks.password=
     *             }
     * @return true if ProxySetup was successful
     * or throws a IOException if an error occurs while reading the file
     * @see #buildHttpProxy(String, String, String, String) for build without Error
     */
    public static boolean buildSocksProxy(String path) throws IOException {
        String host;
        String port;
        String user;
        String pass;
        try (InputStream inputStream = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(inputStream);

            host = prop.getProperty("socksProxyHost");
            port = prop.getProperty("socksProxyPort");
            user = prop.getProperty("java.net.socks.username");
            pass = prop.getProperty("java.net.socks.password");
        }
        return buildSocks(host, port, user, pass);
    }

    //endregion

    //region FTP
    /**
     * build the FTP-Proxy
     * <p>
     *
     * @param user     is the Username
     * @param password is the User Password
     * @param host     is Host
     * @param port     is Port
     * @return true if it is successful
     */
    private static boolean buildFtp(String host, String port, String user, String password) {
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            System.setProperty("ftp.proxyHost", host);
            System.setProperty("ftp.proxyPort", port);
            Authenticator.setDefault(
                    new Authenticator() {
                        @Override
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    user, password.toCharArray());
                        }
                    }
            );
            System.setProperty("ftp.proxyUser", user);
            System.setProperty("ftp.proxyPassword", password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * build the FTP-Proxy with a given proxy settings
     * <p>
     * should not filled in the information within the code
     *
     * @param host     is the ProxyHost
     * @param port     is the ProxyPort
     * @param user     is the ProxyUser
     * @param password is the ProxyUserPassword
     * @return true if was successful
     * @see #buildFtpProxy(String) for solutuion with config file
     */
    public static boolean buildFtpProxy(String host, String port, String user, String password) {
        return buildFtp(host, port, user, password);
    }

    /**
     * build the FTP-Proxy with a given config file
     * <p>
     * Throws IOException if you have Problems with the config File
     *
     * @param path path to config file. the config File should look like
     *             {
     *             ftp.proxyHost=
     *             ftp.proxyPort=
     *             ftp.proxyUser=
     *             ftp.proxyPassword=
     *             }
     * @return true if ProxySetup was successful
     * or throws a IOException if an error occurs while reading the file
     * @see #buildFtpProxy(String, String, String, String) for build without Error
     */
    public static boolean buildFtpProxy(String path) throws IOException {
        String host;
        String port;
        String user;
        String pass;
        try (InputStream inputStream = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(inputStream);

            host = prop.getProperty("ftp.proxyHost");
            port = prop.getProperty("ftp.proxyPort");
            user = prop.getProperty("ftp.proxyUser");
            pass = prop.getProperty("ftp.proxyPassword");
        }
        return buildFtp(host, port, user, pass);
    }
    //endregion
}
