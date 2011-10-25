package com.compomics.suppl.halflife;

import com.google.common.io.Files;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This class is a
 */
public class ProteinContextRunner {
    File iInputFile = null;
    private static Logger logger = Logger.getLogger(ProteinContextRunner.class);


    public ProteinContextRunner(File aInputFile, File aOutputFile) {
        try {
            // Read the input file.
            iInputFile = aInputFile;
            List<String> lContent = Files.readLines(iInputFile, Charset.defaultCharset());

            // Create the output file.
            if (aOutputFile.exists() == false) {
                aOutputFile.createNewFile();
            }

            BufferedWriter lWriter = Files.newWriter(aOutputFile, Charset.defaultCharset());

            // First line is header.
            lWriter.write(lContent.get(0) + "\t" + "nterm" + "\t" + "natcategory");
            lWriter.newLine();
            lWriter.flush();

            // try and process all further lines.
            for (int i = 1; i < lContent.size(); i++) {
                String line = lContent.get(i);
                // remove leading and trailing quotes

                String[] parts = line.split("\t");
                // The fourth column are the UniProt accessions.
                String lAccessionPart = parts[4];

                if (lAccessionPart.length() > 0) {
                    if (lAccessionPart.charAt(0) == '\"') {
                        lAccessionPart = lAccessionPart.substring(1);
                    }

                    if (lAccessionPart.charAt(lAccessionPart.length() - 1) == '\"') {
                        lAccessionPart = lAccessionPart.substring(0, lAccessionPart.length() - 1);
                    }

                    String[] lAccessions = lAccessionPart.split(";");
                    if (lAccessions.length > 0) {
                        // Ok, we have accessions!
                        String lNterm = UniProtReader.getNterm(lAccessions, 5);
                        String newline = line + "\t" + lNterm + "\t" + NatCategoryCalculator.categorize(lNterm);
                        lWriter.write(newline);
                        lWriter.newLine();
                        if (i % 100 == 0) {
                            logger.info(new BigDecimal((i + 0.001) / lContent.size() * 100).setScale(2, RoundingMode.UP) + "%");
                            lWriter.flush();
                        }
                    }
                }

            }
            lWriter.flush();
            lWriter.close();
            // Ok! finished!

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


    public static void main(String[] args) {
        // args[0] == ~/compomics-suppl/halflife/src/main/resources/nature10098-s5.context.category.txt

        args = new String[]{"/Users/kennyhelsens/tmp/nature10098-s5.context.category.txt"};

        File lInputFile = new File(args[0]);
        File lOutputFile = new File(args[0] + ".context.category.txt");

        new ProteinContextRunner(lInputFile, lOutputFile);
    }
}
