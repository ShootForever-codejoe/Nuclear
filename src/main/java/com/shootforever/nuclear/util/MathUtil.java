package com.shootforever.nuclear.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

public final class MathUtil {
    public static final DecimalFormat DF_0 = new DecimalFormat("0");
    public static final DecimalFormat DF_1 = new DecimalFormat("0.0");
    public static final DecimalFormat DF_2 = new DecimalFormat("0.00");
    public static final float PI = (float) Math.PI;
    public static final float TO_RADIANS = (float) (Math.PI / 180.0);
    public static final float TO_DEGREES = 180.0F / (float) Math.PI;

    private MathUtil() {
        throw new AssertionError();
    }

    public static boolean approximatelyEquals(float a, float b) {
        return Math.abs(b - a) < 1.0E-5F;
    }

    public static boolean approximatelyEquals(double a, double b) {
        return Math.abs(b - a) < 1.0E-5F;
    }

    public static double roundToPlace(double value, int place) {
        if (place < 0) return value;
        return new BigDecimal(value).setScale(place, RoundingMode.HALF_UP).doubleValue();
    }

    public static float clamp2(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static @NotNull Double clamp(double num, double min, double max) {
        return Math.min(Math.max(num, min), max);
    }

    public static double getRandomInRange(double min, double max) {
        SecureRandom random = new SecureRandom();
        return min == max ? min : random.nextDouble() * (max - min) + min;
    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static double lerp(double old, double newVal, double amount) {
        return (1.0 - amount) * old + amount * newVal;
    }

    @Contract(pure = true)
    public static @NotNull Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double output = 1.0 / Math.sqrt((Math.PI * 2) * (double) (sigma * sigma));
        return (float) (output * Math.exp((double) (-(x * x)) / (2.0 * (double) (sigma * sigma))));
    }

    public static double roundToHalf(double d) {
        return (double) Math.round(d * 2.0) / 2.0;
    }

    public static double round(double num, double increment) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int) increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }

    public static String round(String value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.stripTrailingZeros();
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.toString();
        }
    }

    public static float getRandomFloat(float max, float min) {
        return new SecureRandom().nextFloat() * (max - min) + min;
    }

    public static int getNumberOfDecimalPlace(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return Math.max(0, bigDecimal.stripTrailingZeros().scale());
    }

    public static boolean equals(float a, float b) {
        return (double) Math.abs(a - b) < 1.0E-4;
    }
}
