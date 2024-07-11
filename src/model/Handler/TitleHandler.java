package model.Handler;

public class TitleHandler {
    private static TitleHandler titleHandler = new TitleHandler();

    private TitleHandler(){

    }

    public static TitleHandler getInstance(){
        return titleHandler;
    }
}
