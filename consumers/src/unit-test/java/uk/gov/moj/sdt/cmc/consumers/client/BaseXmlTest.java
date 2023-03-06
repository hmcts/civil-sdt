package uk.gov.moj.sdt.cmc.consumers.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public abstract class BaseXmlTest {

    protected String readXmlAsString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            URL breathingSpaceURL = getClass().getClassLoader().getResource(fileName);
            Reader fileReader = new FileReader(breathingSpaceURL.getPath());
            BufferedReader bufReader = new BufferedReader(fileReader);
            String line = bufReader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = bufReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
