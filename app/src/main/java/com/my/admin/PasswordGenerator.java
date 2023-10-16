package com.my.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PasswordGenerator {
    private static final String DIGITS = "0123456789";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final boolean useDigits;
    private final boolean useLower;
    private final boolean usePunctuation;
    private final boolean useUpper;


    private PasswordGenerator() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    private PasswordGenerator(PasswordGeneratorBuilder passwordGeneratorBuilder) {
        this.useLower = passwordGeneratorBuilder.useLower;
        this.useUpper = passwordGeneratorBuilder.useUpper;
        this.useDigits = passwordGeneratorBuilder.useDigits;
        this.usePunctuation = passwordGeneratorBuilder.usePunctuation;
    }

    public static class PasswordGeneratorBuilder {
        /* access modifiers changed from: private */
        public boolean useDigits = false;
        /* access modifiers changed from: private */
        public boolean useLower = false;
        /* access modifiers changed from: private */
        public boolean usePunctuation = false;
        /* access modifiers changed from: private */
        public boolean useUpper = false;

        public PasswordGeneratorBuilder useLower(boolean z) {
            this.useLower = z;
            return this;
        }

        public PasswordGeneratorBuilder useUpper(boolean z) {
            this.useUpper = z;
            return this;
        }

        public PasswordGeneratorBuilder useDigits(boolean z) {
            this.useDigits = z;
            return this;
        }

        public PasswordGeneratorBuilder usePunctuation(boolean z) {
            this.usePunctuation = z;
            return this;
        }

        public PasswordGenerator build() {
            return new PasswordGenerator(this);
        }
    }

    public String generate(int i) {
        if (i <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i);
        Random random = new Random(System.nanoTime());
        List<String> arrayList = new ArrayList<>(4);
        if (this.useLower) {
            arrayList.add(LOWER);
        }
        if (this.useUpper) {
            arrayList.add(UPPER);
        }
        if (this.useDigits) {
            arrayList.add(DIGITS);
        }
        if (this.usePunctuation) {
            arrayList.add(PUNCTUATION);
        }
        for (int i2 = 0; i2 < i; i2++) {
            String str = (String) arrayList.get(random.nextInt(arrayList.size()));
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return new String(sb);
    }
}
