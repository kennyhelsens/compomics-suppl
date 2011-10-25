# SET PATH TO LIB
setwd("/Users/kennyhelsens/github/compomics-suppl/halflife/src/main/r/lib/")

cat('Autoloading utility functions\n')
source('utilities.R')
cat('Autoloading libraries\n')
source('load_libraries.R')
cat('Autoloading data\n')
source('load_data.R')
cat('Preprocessing data\n')
source('preprocess_data.R')

source('mycols.R')


# Set the logger's file output.
logger <- create.logger()
logfile(logger) <- '../logs/base.log'
level(logger) <- log4r:::INFO

info(logger, "booting")
