package com.pricecomparator.repository;

import com.pricecomparator.model.Discount;
import com.pricecomparator.utils.DateHelper;
import com.pricecomparator.utils.FileHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DiscountRepository {
    private static DiscountRepository instance;
    private List<Discount> discounts;

    private DiscountRepository() {
        System.out.println("init DiscountRepository");
        discounts = new ArrayList<>();
        init();
    }

    public static DiscountRepository getInstance() {
        if (instance == null) {
            instance = new DiscountRepository();
        }

        return instance;
    }

    public List<Discount> getDiscountsForProductsOnDate(List<String> ids, LocalDate date) {
        return discounts.stream()
                .filter(discount -> ids.contains(discount.getProductId())
                        && (date.isEqual(discount.getFromDate()) || date.isEqual(discount.getToDate()) || (date.isAfter(discount.getFromDate())
                        && date.isBefore(discount.getToDate()))))
                .collect(Collectors.toList());
    }

    public List<Discount> getAllDiscounts() {
        return discounts;
    }

    private void init() {
        try {
            CSVFormat csvFormat =  CSVFormat.Builder.create().setDelimiter(';')
                    .setHeader(FileHelper.DISCOUNT_HEADERS)
                    .setQuote('"')
                    .setRecordSeparator("\r\n")
                    .setIgnoreEmptyLines(true)
                    .setSkipHeaderRecord(true)
                    .build();
            for (String file : FileHelper.DISCOUNT_FILE_NAMES) {
                String[] tokens = file.split("_");
                String storeName = tokens[0];
                LocalDate date = DateHelper.getDateFromString(tokens[2]);
                Reader reader = Files.newBufferedReader(Path.of(this.getClass().getResource("/discounts/" + file + ".csv").toURI()));
                CSVParser csvParser = csvFormat.parse(reader);
                csvParser.getRecords()
                        .forEach(csvRecord -> {
                            discounts.add(Discount.builder()
                                            .productId(csvRecord.get(0))
                                            .storeName(storeName)
                                            .fromDate(DateHelper.getDateFromString(csvRecord.get(6)))
                                            .toDate(DateHelper.getDateFromString(csvRecord.get(7)))
                                            .percentage(Integer.valueOf(csvRecord.get(8)))
                                    .build());
                        });
            }
        } catch (Exception e) {
            System.out.println("CSV parsing error:" + e);
        }

        //discounts.forEach(System.out::println);
    }
}
