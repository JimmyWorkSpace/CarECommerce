package cc.carce.sale.util;

import java.security.SecureRandom;

/**
 * 票券序號產生規則：
 * 系統自動產生、具備唯一性、不可預測、不可推算下一組序號。
 * 格式：英數字混合，由系統統一定義（本系統為 16 位大寫英數字）。
 */
public final class TicketCodeUtil {

    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private TicketCodeUtil() {}

    /**
     * 產生一組票券唯一碼（16 位大寫英數字，具不可預測性）
     */
    public static String generate() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
