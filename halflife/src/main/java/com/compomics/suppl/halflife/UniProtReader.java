package com.compomics.suppl.halflife;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Nov 25, 2009
 * Time: 2:45:13 PM
 * <p/>
 * This class
 */
public class UniProtReader {
    // Class specific log4j logger for PICRReader instances.
    private static Logger logger = Logger.getLogger(UniProtReader.class);
    private static HashMap<String, String> iProteinNtermMap = new HashMap<String, String>();
    private static HashMap<String, Double> iProteinMassMap = new HashMap<String, Double>();

    public static ArrayList doUniprotFasta(final String aAccession) throws IOException {

        String lUniProtFasta = "http://www.uniprot.org/uniprot/" + aAccession + ".fasta";
        URL lURL = new URL(lUniProtFasta);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        lURL.openStream()));

        String inputLine;
        ArrayList list = new ArrayList();

        while ((inputLine = in.readLine()) != null) {
            list.add(inputLine);
        }
        in.close();
        return list;
    }


    /**
     * Returns the consensus Nterminus across the specified uniprot accessions.
     * Returns "NA" if no sequence could be found, or if there is no consensus.
     *
     * @param aAccessions Return a series of Protein accessions
     * @param aLength     The length of the Nterminus
     * @return
     */
    public static String getNterm(String[] aAccessions, int aLength) throws IOException {
        HashMap<String, Integer> lNterminiMap = new HashMap<String, Integer>();
        for (String lAccession : aAccessions) {

            // First check if the accession has already been seen.
            String lNterminus = iProteinNtermMap.get(lAccession);
            if (lNterminus != null) {
                Integer lCurrentCount = lNterminiMap.get(lNterminus);
                if (lCurrentCount == null) {
                    lCurrentCount = 0;
                }
                lNterminiMap.put(lNterminus, (lCurrentCount + 1));
            } else {
                ArrayList lFastaLines = UniProtReader.doUniprotFasta(lAccession);
                if (lFastaLines.size() > 1) {
                    // Ok!
                    lNterminus = lFastaLines.get(1).toString().substring(0, aLength);
                    iProteinNtermMap.put(lAccession, lNterminus);

                    Integer lCurrentCount = lNterminiMap.get(lNterminus);
                    if (lCurrentCount == null) {
                        lCurrentCount = 0;
                    }
                    lNterminiMap.put(lNterminus, (lCurrentCount + 1));

                } else {
                    for (Object lFastaLine : lFastaLines) {
                        logger.info(lFastaLine.toString());
                    }
                }
            }

        }
        if (lNterminiMap.keySet().size() == 1) {
            // Only one Nterminus found! Hooray consensus!
            return (String) lNterminiMap.keySet().toArray()[0];
        } else {

            // See all values, and try to find an Nterminus with more then 75% consensus.
            Set<String> lNterminalSequences = lNterminiMap.keySet();
            for (String lNterminus : lNterminalSequences) {
                Integer lSum = lNterminiMap.get(lNterminus);
                if ((lSum + 0.0001) / aAccessions.length >= 0.5 && lNterminus.charAt(0) == 'M') {
                    return lNterminus;
                }
            }

            logger.info("Non concensus Nterminus:"
                    + "\t" + Joiner.on(",").join(lNterminalSequences)
                    + "\t" + Joiner.on(",").join(aAccessions));
            return "NA";

        }
    }

}
