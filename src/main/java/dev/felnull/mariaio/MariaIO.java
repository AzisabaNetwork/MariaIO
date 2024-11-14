package dev.felnull.mariaio;

import dev.felnull.mariaio.SQLManager.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;


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

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
