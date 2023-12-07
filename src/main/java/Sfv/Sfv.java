package Sfv;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Sfv {

    public static boolean isAlpha( String str ) {
        int c;
        if(str.length() > 0) {

            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isNumeric( String str ) {

        int c;
        if(str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);
                if (!( c >= '0' && c <= '9' )) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isFileName(String str) {
        int c;
        if(str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);
                if (!( (c >= '0' && c <= '9') ||
                       (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') ||
                        (c == '_' || c != ' ' ) )) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static int getInteger(String s) {
        int choice;

        if(isNumeric(s)) {
            choice = Integer.parseInt(s);
        } else {
            choice = -1;
        }
        return choice;
    }


    public static int StepBackInArray(int CurrCh, int MaxCh){
        int i = (CurrCh + (MaxCh - 1)) % MaxCh;
        return i;
    }

    public static int StepFowardInArray(int CurrCh, int MaxCh){
        int i = (CurrCh + 1) % MaxCh;
        return i;
    }


    public static int hossz(String szoveg) { return (szoveg == null ? 0 : szoveg.length()); }

    public static String left(String s, int i) {
        i = Math.abs(i);
        return ((s.length() < i) ? s : s.substring(0, i ));
    }

    public static String right(String s, int i) {
        i = Math.abs(i);
        return ((s.length() < i) ? s : s.substring(hossz(s) - i, s.length()));
    }

    public static String mid(String s, int start, int length) {
        start = Math.abs(start);
        length = Math.abs(length);

        if(start > s.length()) return "";
        return ((start + length > s.length()) ? s.substring(start, s.length()) : s.substring(start, start + length));
    };

    public static String mid(String s, int length) {
        return mid(s, length, s.length());
    }

    public static int pos (String miben, String mit) {
        return miben.indexOf(mit);
    }

    public static String StringReplaceAll (String str, String mit, String mire) {
        StringBuilder retstr = new StringBuilder();
        int olen,i = 1;

        olen = hossz(mit);

        while(i > 0) {
            i = pos(str, mit);
            if (i > 0) {
                retstr.append( left(str, i /*- 1*/));
                retstr.append( mire );
                str = mid(str, i + olen);
            }
        }

        return retstr + str;

    }

    public static String getFileExtension(String filename) {
        String extension = "";

        int pos = filename.lastIndexOf('.');

        if (pos > 0) {
            extension = filename.substring(pos + 1).toLowerCase();
        }

        return extension;
    }

    public static ArrayList<String> listFiles(String directoryPath)  {
        Path dir = Paths.get(directoryPath);

        ArrayList<String> files = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    int last = file.toString().lastIndexOf("\\");
                    files.add(right(file.toString(), file.toString().length() - last - 1));
                } else if (Files.isDirectory(file)) {
                    // nothing
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return files;
    }
}
