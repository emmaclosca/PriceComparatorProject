package com.pricecomparator.repository;

import com.pricecomparator.model.Product;
import com.pricecomparator.model.ProductPrice;
import com.pricecomparator.utils.FileHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ProductRepository {
    private List<Product> products;
    private List<ProductPrice> productPrices;
    private static ProductRepository instance;

    private ProductRepository() {
        System.out.println("init ProductRepository");
        products = new ArrayList<>();
        productPrices = new ArrayList<>();

        init();
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }

        return instance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getProductById(String id) {
        return products.stream().filter(product -> product.getId().equals(id)).collect(Collectors.toList()).getFirst();
    }

    public List<Product> filterProductsByIds(List<String> ids) {
        return products.stream().filter(product -> ids.contains(product.getId())).collect(Collectors.toList());
    }

    public List<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public List<ProductPrice> filterProductPricesByIdsAndDate(List<String> ids, LocalDate date) {
        return productPrices.stream().filter(productPrice -> ids.contains(productPrice.getProductId()) && productPrice.getDateOfPrice().equals(date)).collect(Collectors.toList());
    }

    private void init() {
        try {
            CSVFormat csvFormat =  CSVFormat.Builder.create().setDelimiter(';')
                    .setHeader(FileHelper.PRODUCT_HEADERS)
                    .setQuote('"')
                    .setRecordSeparator("\r\n")
                    .setIgnoreEmptyLines(true)
                    .setSkipHeaderRecord(true)
                    .build();
            for (String file : FileHelper.PRODUCT_FILE_NAMES) {
                String[] tokens = file.split("_");
                String storeName = tokens[0];
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.from(dateFormatter.parse(tokens[1]));
                Reader reader = Files.newBufferedReader(Path.of(this.getClass().getResource("/products/" + file + ".csv").toURI()));
                CSVParser csvParser = csvFormat.parse(reader);
                csvParser.getRecords()
                        .forEach(csvRecord -> {
                            products.add(Product.builder()
                                    .id(csvRecord.get(0))
                                    .name(csvRecord.get(1))
                                    .category(csvRecord.get(2))
                                    .brand(csvRecord.get(3))
                                    .quantity(Float.parseFloat(csvRecord.get(4)))
                                    .unit(csvRecord.get(5))
                                    .build());
                            productPrices.add(ProductPrice.builder()
                                    .productId(csvRecord.get(0))
                                    .price(Float.parseFloat(csvRecord.get(6)))
                                    .currency(csvRecord.get(7))
                                    .storeName(storeName)
                                    .dateOfPrice(date)
                                    .build());
                        });
            }
        } catch (Exception e) {
            System.out.println("CSV parsing error:" + e);
        }

        //products.forEach(System.out::println);
    }
}
