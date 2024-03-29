package org.unipi;

import org.unipi.reflection.FileHandler;


public class Main {
    public static void main(String[] args) {
        //   INSTRUCTIONS OF USE
        //
        //1) PUT THE INPUT FILE/S IN THE input PACKAGE
        //2) RUN THE MAIN OF THE Main.java FOLDER(HERE)
        //3) THE OUTPUT FILES ARE GENERATED IN THE output PACKAGE AND HAVE THE SAME NAME AS THE INPUT FILES
        //4) YOU CAN RUN THE OUTPUT FILES DIRECTLY THERE IF YOU WOULD LIKE. IF YOU CHOOSE TO MOVE THEM SOMEWHERE ELSE,
        //   MAKE SURE YOU CHANGE THE PACKAGE NAME.
        //
        //  The accepted annotations can be seen in the annotations package.
        //
        //- @Field Annotation accepts a name and a type. The name is the column name in the database and the type is
        //  the data type of the column in the database. some databases have different interperations for the types so
        //  be sure to enter the correct one. For VARCHAR types, just input VARCHAR and the program will fill it
        //  to VARCHAR(20). If you want it to be higher you can delete your local database, edit the createTable method
        //  in the output file and change it to something else.
        //
        //- @Database has the name and the dbType. Acceptable DBTypes are: "H2, Derby ,SQLite" . It is not case-sensitive.
        //- @DBMethod has a type. Acceptable types of db operations are: "selectAll, deleteOne". It is not case-sensitive.

        FileHandler fh = FileHandler.getInstance();

        fh.handleInputFiles();


    }

}
