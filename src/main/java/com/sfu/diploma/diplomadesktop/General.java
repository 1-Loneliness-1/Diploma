package com.sfu.diploma.diplomadesktop;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;

public class General {
    public static String[] parseFile() {
        String[] result = new String[6];

        try(FileReader reader = new FileReader("transfer.txt"))
        {
            StringBuilder str = new StringBuilder();
            int c;
            int i = 0;
            while (i != 6) {
                c = reader.read();
                if ((char)c == '@') {
                    result[i] = str.toString();
                    str.setLength(0);
                    i++;
                    continue;
                }
                str.append((char) c);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public static String getTitleOfSelectedDrug() {
        StringBuilder str = new StringBuilder();
        try(FileReader reader = new FileReader("transfer_selected_drug.txt"))
        {
            int c = reader.read();
            while((char)c != '@') {
                str.append((char)c);
                c = reader.read();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        return str.toString();
    }

    public static String getValueFromTextFile(String fileName) {
        StringBuilder str = new StringBuilder();
        try(FileReader reader = new FileReader(fileName))
        {
            int c = reader.read();
            while((char)c != '@') {
                str.append((char)c);
                c = reader.read();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        return str.toString();
    }

    public static String convertDateToStandardForm(Date dateInIncorrectForm) {
        return (dateInIncorrectForm.toString()).substring(8, 10) + "." + (dateInIncorrectForm.toString()).substring(5, 7) +
                "." + (dateInIncorrectForm.toString()).substring(0, 4);
    }
}
