package com.shootforever.nuclear.util.functions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil {
    private ReflectionUtil() {
        throw new AssertionError();
    }

    public static @Nullable Field findField(@NotNull Class<?> clazz, @NotNull String @NotNull ... fieldNames) {
        if (fieldNames.length == 0) return null;

        for (Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
            for (String fieldName : fieldNames) {
                try {
                    Field f = currentClass.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    UnsafeUtil.putInt(f, Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, f.getModifiers() & -17);
                    return f;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static @Nullable Method findMethod(
            @NotNull Class<?> clazz,
            @NotNull String obfName,
            @NotNull String deobfName,
            @NotNull Class<?> @NotNull ... parameterTypes
    ) {
        for (Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
            try {
                Method m = null;
                try {
                    m = currentClass.getDeclaredMethod(obfName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                if (m == null) {
                    m = currentClass.getDeclaredMethod(deobfName, parameterTypes);
                }

                m.setAccessible(true);
                UnsafeUtil.putInt(m, Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, m.getModifiers() & -17);
                return m;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
