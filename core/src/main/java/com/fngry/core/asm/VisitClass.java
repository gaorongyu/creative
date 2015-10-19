package com.fngry.core.asm;


import org.springframework.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 *
 * 使用字节码技术获取方法参数的名称
 *
 * Created by gaorongyu on 15/7/30.
 *
 */
public class VisitClass {

    public static void main(String[] args) throws Exception {

        // 获取静态方法名称
        printParamNameOfStaticMethod();
        // 获取实例方法名称
        printParamNameOfInstanceMethod();

    }

    public static void printParamNameOfStaticMethod() throws Exception  {
        long start = System.nanoTime();
//        System.out.println(" start time : " + start);

        String[] names = getMethodParamNames(VisitClass.class.getMethod("getMethodParamNames",
                new Class[] { Method.class, String.class} ), "");
        System.out.println(" 参数名称 " + Arrays.toString(names));

        long end = System.nanoTime();
//        System.out.println(" end time : " + end);

        System.out.println(" time duration : " + (end - start) / 1000 + " um ");
    }

    public static void printParamNameOfInstanceMethod() throws Exception  {
        long start = System.nanoTime();
//        System.out.println(" start time : " + start);

        String[] names = getMethodParamNames(VisitClass.class.getMethod("whatIsMyName",
                new Class[] { String.class, String.class} ), "");
        System.out.println(" 参数名称 " + Arrays.toString(names));

        long end = System.nanoTime();
//        System.out.println(" end time : " + end);

        System.out.println(" time duration : " + (end - start) / 1000 + " um ");
    }

    /**
     *
     * 是否相同参数签名
     *
     * @param types
     * @param classes
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] classes) {

        if(types.length != classes.length) {
            return false;
        }

        for(int i = 0; i < types.length; i++) {
            if(!Type.getType(classes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * 获取方法参数名称
     *
     * @param m
     * @param namenameTest
     * @return
     */
    public static String[] getMethodParamNames(final Method m, String namenameTest) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final String n = m.getDeclaringClass().getName();
        ClassReader cr = null;
        try {
            cr = new ClassReader(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cr.accept(new ClassVisitor(Opcodes.ASM4) {
            @Override
            public MethodVisitor visitMethod(final int access,final String name, final String desc,
                    final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName())
                        || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature,
                            exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, desc,
                        signature, exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc,
                            String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start,
                                end, index);
                    }

                };
            }
        }, 0);
        return paramNames;
    }

    public void whatIsMyName(String first, String second) {
        System.out.println("first " + first + " second " + second);
    }

}
