package com.hfconsulting.spring6.services;

import com.hfconsulting.spring6.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
}
