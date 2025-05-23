package com.pricecomparator.utils;

public class FileHelper {
    public static String[] PRODUCT_HEADERS = new String[] {"product_id", "product_name", "product_category", "brand", "package_quantity", "package_unit", "price", "currency"};
    public static String[] PRODUCT_FILE_NAMES = new String[] {"lidl_2025-05-01", "lidl_2025-05-08", "profi_2025-05-01", "profi_2025-05-08", "kaufland_2025-05-01"};
    public static String[] DISCOUNT_HEADERS = new String[] {"product_id", "product_name", "brand", "package_quantity","package_unit", "product_category", "from_date", "to_date", "percentage_of_discount"};
    public static String[] DISCOUNT_FILE_NAMES = new String[] {"lidl_discounts_2025-05-01"};
}
