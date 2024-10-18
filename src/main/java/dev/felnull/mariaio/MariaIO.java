package dev.felnull.mariaio;

import dev.felnull.mariaio.SQLManager.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class MariaIO extends JavaPlugin {
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Plugin startup logic
        // データベースの設定を読み込み
        String host = getConfig().getString("database.host");
        int port = getConfig().getInt("database.port");
        String name = getConfig().getString("database.name");
        String user = getConfig().getString("database.user");
        String password = getConfig().getString("database.password");


        // データベースの初期化
        databaseManager = new DatabaseManager();
        databaseManager.init(host, port, name, user, password);

        if(tableExists("Test")) {
            saveData("Test", "1", "TESTERERE");
        }else{
            databaseManager.createTable("Test");
            saveData("Test", "1", "TESTERERE");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    public boolean tableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // テーブルが存在する場合は1以上
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 例外が発生した場合はfalseを返す
    }

    public boolean saveData(String table, String key, String value) {
        String sql = "INSERT INTO `"+ table +"` (key_column, value_column) VALUES (?, ?)";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            statement.setString(2, value);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
