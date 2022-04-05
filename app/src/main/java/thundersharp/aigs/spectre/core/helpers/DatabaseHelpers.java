package thundersharp.aigs.spectre.core.helpers;

public class DatabaseHelpers {

    private static DatabaseHelpers databaseHelpers = null;

    public static DatabaseHelpers getInstance(){
        if (databaseHelpers == null){
            databaseHelpers = new DatabaseHelpers();
        }
        return databaseHelpers;
    }



}
