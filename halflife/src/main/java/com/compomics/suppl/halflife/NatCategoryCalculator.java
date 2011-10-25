package com.compomics.suppl.halflife;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;

/**
 * This class scripts the calculation of dimer amino acid combinations.
 */
public class NatCategoryCalculator {
    private static Logger logger = Logger.getLogger(NatCategoryCalculator.class);

    private boolean isFinished = false;

    private String iDatabaseID = null;

    private HashMap<NatCategoryEnum, Double> iNatCategoryMap = new HashMap<NatCategoryEnum, Double>();

    private File iFastaDatabase = null;
    public int iNonMetProteins = 0;
    public double iDBSize;
    private String iSpeciesID;
    private String iDatabaseType;


    public NatCategoryCalculator(File aDatabase) {
        iFastaDatabase = aDatabase;
    }

    public static NatCategoryEnum categorize(String aSequence) {
        /**
         - Free: Met-Pro-, Met-X-Pro (X = Ala-, Ser-, Thr-, Val-, Gly- or Cys-
         - NatA: Met-Ser-, Met-Ala-, Met-Thr-, Met-Val-, Met-Gly-, Met-Cys-
         - NatB: Met-Asp-, Met-Glu-, Met-Asn-
         - NatC: Met-Leu-, Met-Ile-, Met-Phe-, Met-Tyr
         - Other: Met-Met-, Met-Lys-, Met-His-, Met-Arg-, Met-Trp-, Met-Gln-
         */

        // Free: Met-Pro-, Met-X-Pro (X = Ala-, Ser-, Thr-, Val-, Gly- or Cys-)
        if (aSequence.indexOf("MP") == 0) {
            return NatCategoryEnum.FREE;
        } else if (aSequence.indexOf("MAP") == 0) {
            return NatCategoryEnum.FREE;
        } else if (aSequence.indexOf("MSP") == 0) {
            return NatCategoryEnum.FREE;
        } else if (aSequence.indexOf("MTP") == 0) {
            return NatCategoryEnum.FREE;
        } else if (aSequence.indexOf("MVP") == 0) {
            return NatCategoryEnum.FREE;
        } else if (aSequence.indexOf("MGP") == 0) {
            return NatCategoryEnum.FREE;
        } else if (aSequence.indexOf("MCP") == 0) {
            return NatCategoryEnum.FREE;
        }

        // NatA: Met-Ser-, Met-Ala-, Met-Thr-, Met-Val-, Met-Gly-, Met-Cys-
        else if (aSequence.indexOf("MS") == 0) {
            return NatCategoryEnum.NATA;
        } else if (aSequence.indexOf("MA") == 0) {
            return NatCategoryEnum.NATA;
        } else if (aSequence.indexOf("MT") == 0) {
            return NatCategoryEnum.NATA;
        } else if (aSequence.indexOf("MV") == 0) {
            return NatCategoryEnum.NATA;
        } else if (aSequence.indexOf("MG") == 0) {
            return NatCategoryEnum.NATA;
        } else if (aSequence.indexOf("MC") == 0) {
            return NatCategoryEnum.NATA;
        }


        // NatB: Met-Asp-, Met-Glu-, Met-Asn-
        else if (aSequence.indexOf("MD") == 0) {
            return NatCategoryEnum.NATB;
        } else if (aSequence.indexOf("ME") == 0) {
            return NatCategoryEnum.NATB;
        } else if (aSequence.indexOf("MN") == 0) {
            return NatCategoryEnum.NATB;
        }

        // NatC: Met-Leu-, Met-Ile-, Met-Phe-, Met-Trp-
        else if (aSequence.indexOf("ML") == 0) {
            return NatCategoryEnum.NATC;
        } else if (aSequence.indexOf("MI") == 0) {
            return NatCategoryEnum.NATC;
        } else if (aSequence.indexOf("MF") == 0) {
            return NatCategoryEnum.NATC;
        } else if (aSequence.indexOf("MW") == 0) {
            return NatCategoryEnum.NATC;
        }

        // Other: Met-Met-, Met-Lys-, Met-His-, Met-Arg-, Met-Tyr-, Met-Gln
        else if (aSequence.indexOf("MM") == 0) {
            return NatCategoryEnum.OTHER;
        } else if (aSequence.indexOf("MK") == 0) {
            return NatCategoryEnum.OTHER;
        } else if (aSequence.indexOf("MH") == 0) {
            return NatCategoryEnum.OTHER;
        } else if (aSequence.indexOf("MR") == 0) {
            return NatCategoryEnum.OTHER;
        } else if (aSequence.indexOf("MY") == 0) {
            return NatCategoryEnum.OTHER;
        } else if (aSequence.indexOf("MQ") == 0) {
            return NatCategoryEnum.OTHER;
        } else {
            return null;
        }
    }


}
