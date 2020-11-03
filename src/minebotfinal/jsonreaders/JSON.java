package minebotfinal.jsonreaders;

import java.io.*;

public class JSON {
    /**
     * Reads a whole .json file and returns a String with its content.
     *
     * @param path The path of the file.
     * @return A String with the content of the file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    static public String readJson(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String content = "", line;

        while ((line = br.readLine()) != null) {
            content = content.concat(line);
        }
        br.close();

        return content;
    }
}
