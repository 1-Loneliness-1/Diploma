package com.sfu.diploma.diplomadesktop;

public final class JDBCManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/diploma?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow";
    private final String nameOfUserInDataBase;
    private final String passwordOfUserInDataBase;

    public JDBCManager(String nameOfUserInDataBase, String passwordOfUserInDataBase) {
        this.nameOfUserInDataBase = nameOfUserInDataBase;
        this.passwordOfUserInDataBase = passwordOfUserInDataBase;
    }

    public String getNameOfUserInDataBase() {return nameOfUserInDataBase;}

    public String getPasswordOfUserInDataBase() {return passwordOfUserInDataBase;}

    //Function for getting current url for connect to database
    public String getUrl() {return URL;}
}
