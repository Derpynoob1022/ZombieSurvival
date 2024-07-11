package model.Handler;

public class SettingsHandler{
    private static SettingsHandler settingsHandler = new SettingsHandler();

    private SettingsHandler(){

    }

    public static SettingsHandler getInstance(){
        return settingsHandler;
    }
}
