package com.dqm.utils;

import com.dqm.annotations.Index;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dqm on 2018/8/25.
 */
public class CodeUtil {

    //类名：字段索引：字段：字节数等Index元信息
    public static volatile Map<String, Map<Integer, FieldIndex>> classInfoMap = new ConcurrentHashMap<>();

    /**
     * 需要大端转小端
     * 将对象转换成byte[]数组
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static final <T> byte[] encode(T t) throws Exception {
        //如果存在index，或者serializeSelf为false，或者（serializeSelf为true，且存在encode()方法），否则不处理
        Class<?> cla = t.getClass();
        Map<Integer, FieldIndex> metaInfo = metaInfo(cla);
        if(null != metaInfo && metaInfo.size() > 0) {
            byte[] zero = new byte[0];
            for(int key:metaInfo.keySet()) {
                Field field = metaInfo.get(key).getField();
                Index index = metaInfo.get(key).getIndex();
                boolean inverted = index.inverted();
                boolean hex = index.hex();
                field.setAccessible(true);
                Object obj = field.get(t);
                if(obj != null) {
                    byte[] bytes = null;
                    //简单编码情况，需要考虑迭代情况，比如Object
                    if (index.dependentType() == Index.DependentType.COMMON) {//普通依赖
                        bytes = obj2ByteV2(index.specialType(), inverted, obj);
                    } else if (index.dependentType() == Index.DependentType.LENGTH) {//长度依赖【通常是Object数组，例如Input数组】
                        //获取依赖的value值
                        int depIndex = index.dependentIndex();
                        Field depField = metaInfo.get(depIndex).getField();
                        depField.setAccessible(true);
                        Object depObj = depField.get(t);

                        Class clas = obj.getClass();//获得这个对象的字节码
                        if (clas.isArray()) {//数组形式
                            int dependentObjVal = (int) depObj;//数组下标只能是int类型
                            byte[] subZero = new byte[0];
                            for (int x = 0; x < dependentObjVal; x++) {
                                Object subObj = Array.get(obj, x);

                                subZero = ByteUtil.concat(subZero, encode(subObj));
                            }

                            bytes = subZero;
                        } else if (clas.getTypeName().equalsIgnoreCase("java.lang.String")) {//String情况
                            if(hex)
                                bytes = ByteUtil.hexStringToByte((String) obj);
                            else {
                                String tmp = (String) obj;
                                char[] a = tmp.toCharArray();
                                bytes = ByteUtil.hexStringToByte(a);
                            }
                        } else {
                            //非数组没有长度依赖的情况
                        }
                    } else if (index.dependentType() == Index.DependentType.EXIST) {//存在依赖
                        //获取依赖的value值
                        int depIndex = index.dependentIndex();
                        Field depField = metaInfo.get(depIndex).getField();
                        depField.setAccessible(true);
                        long depObjVal = (long) depField.get(t);//目前只支持long类型的

                        if (depObjVal >= index.dependentVal()) {//目前只支持大于等于，用于verion判断
                            bytes = obj2ByteV2(index.specialType(), inverted, obj);
                        } else {
                            //其他情况暂时不支持
                        }
                    } else {
                        //异常情况
                    }

                    zero = ByteUtil.concat(zero, bytes);
                }
            }

            return zero;
        }

        return null;
    }

    /**
     * 判断是否为基本类型
     * @param obj
     * @return
     */
    private static boolean baseClass(Object obj) {
        return ((obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long
                || obj instanceof Float || obj instanceof Double || obj instanceof Character || obj instanceof Boolean) || (obj instanceof Class && (
                        ((Class) obj).getTypeName().equalsIgnoreCase("byte") || ((Class) obj).getTypeName().equalsIgnoreCase("short")
                || ((Class) obj).getTypeName().equalsIgnoreCase("int") || ((Class) obj).getTypeName().equalsIgnoreCase("long")
                || ((Class) obj).getTypeName().equalsIgnoreCase("float") || ((Class) obj).getTypeName().equalsIgnoreCase("double")
                || ((Class) obj).getTypeName().equalsIgnoreCase("char") || ((Class) obj).getTypeName().equalsIgnoreCase("boolean")
        )));
    }

    private static byte[] obj2ByteV2(Index.SpecialType type, boolean inverted, Object obj) throws Exception {
        if(Index.SpecialType.UINT64.equals(type) && obj instanceof BigInteger) {//处理UInt64
            byte[] bytes = ByteUtil.invertedByteArrayV2(obj2Byte(type, obj));

            byte[] tmp = new byte[8];
            if(bytes.length <= 8) {
                for(int i=0;i<8;i++) {
                    if(i<bytes.length) {
                        tmp[i] = bytes[i];
                    }
                }
            }
            return tmp;
        } if(!baseClass(obj)) {
            return obj2Byte(type, obj);
        } else {
            byte[] tmp2 = obj2Byte(type, obj);
            if(!inverted) {
                return tmp2;
            }
            byte[] tmp = ByteUtil.invertedByteArrayV2(tmp2);
            return tmp;
        }
    }

    private static byte[] obj2Byte(Index.SpecialType type, Object obj) throws Exception {
        if(Index.SpecialType.VARINT.equals(type) && obj instanceof Integer) {//处理VarInt
            return VarInt.int2Byte((int) obj);
        } else if(Index.SpecialType.UINT8.equals(type) && obj instanceof Short) {//处理UInt8
            return UInt8.fromUnsignedInt((short) obj);
        } else if(Index.SpecialType.UINT16.equals(type) && obj instanceof Integer) {//处理UInt16
            return UInt16.fromUnsignedInt((int) obj);
        } else if(Index.SpecialType.UINT32.equals(type) && obj instanceof Long) {//处理UInt32
            return UInt32.fromUnsignedInt((long) obj);
        } else if(Index.SpecialType.UINT64.equals(type) && obj instanceof BigInteger) {//处理UInt64
            return UInt64.fromUnsignedLong((BigInteger) obj);
        } else if(Index.SpecialType.NOTSPECIAL.equals(type)) {//普通类型
            if (obj instanceof Byte) {//处理byte类型
                return new byte[]{(byte) obj};
            } else if (obj instanceof Character) {//处理char类型
                return new byte[]{(byte) obj};
            } else if (obj instanceof Short) {//处理short类型
                byte[] tmp = new byte[2];
                for (int i = 0; i < 2; i++) {
                    tmp[i] = ByteUtil.number2Byte((short)obj, i);
                }
                return tmp;
            } else if (obj instanceof Integer) {//处理int
                byte[] tmp = new byte[4];
                for (int i = 0; i < 4; i++) {
                    tmp[i] = ByteUtil.number2Byte((int)obj, i);
                }
                return tmp;
            } else if (obj instanceof Long) {//处理long
                byte[] tmp = new byte[8];
                for (int i = 0; i < 8; i++) {
                    tmp[i] = ByteUtil.number2Byte((long)obj, i);
                }
                return tmp;
            } else if (obj instanceof Float) {//处理float
                return ByteUtil.float2Byte((float) obj);
            } else if (obj instanceof Double) {//处理double
                return ByteUtil.double2Byte((double) obj);
            } else if(obj instanceof String) {//string
                return ByteUtil.hexStringToByte((String)obj);
            } else {
                Class clas = obj.getClass();//获得这个对象的字节码
                if (clas.isArray()) {//处理数组
                    byte[] subZero = new byte[0];
                    Class subClass = clas.getComponentType();//获取到元素类型
                    if(baseClass(subClass)) {//基本类型数组【暂时支持byte和char】
                        if(subClass.getTypeName().equalsIgnoreCase("byte")) {//byte
                            for (int x = 0; x < Array.getLength(obj); x++) {
                                byte subObj = Array.getByte(obj, x);

                                subZero = ByteUtil.concat(subZero, new byte[]{subObj});
                            }
                        } else if(subClass.getTypeName().equalsIgnoreCase("char")) {//char
                            for (int x = 0; x < Array.getLength(obj); x++) {
                                char subObj = Array.getChar(obj, x);

                                subZero = ByteUtil.concat(subZero, new byte[]{(byte)subObj});
                            }
                        }
                    } else {
                        for (int x = 0; x < Array.getLength(obj); x++) {
                            Object subObj = Array.get(obj, x);

                            subZero = ByteUtil.concat(subZero, encode(subObj));
                        }
                    }

                    return subZero;
                } else {//Object类型
                    return encode(obj);
                }
            }
        }

        return null;
    }

    /**
     * 存储元信息
     * @param cla
     * @param <T>
     */
    private static <T> Map<Integer, FieldIndex> metaInfo(Class<T> cla) {
        String claName = cla.getCanonicalName();
        if(!classInfoMap.containsKey(claName)) {
            Field[] fields = cla.getDeclaredFields();

            Map<Integer, FieldIndex> indexInfoMap = new HashMap<>();
            for (Field f : fields) {
                Annotation[] annotations = f.getDeclaredAnnotations();
                for (Annotation a : annotations) {
                    if (a instanceof Index) {
                        Index index = (Index) a;
                        //每个字段上只有一个index注解
                        FieldIndex fieldIndex = new FieldIndex(f, index);
                        indexInfoMap.put(index.val(), fieldIndex);
                        break;
                    }
                }
            }

            if(null != indexInfoMap && indexInfoMap.size() > 0) {
                classInfoMap.put(claName, indexInfoMap);
                return indexInfoMap;
            }
        } else {
            return classInfoMap.get(claName);
        }

        return null;
    }

    /**
     * 需要小端转大端
     * 二进制转Object
     * @param data 二进制数据
     * @param <T>
     * @return
     */
    public static final <T> T decode(byte[] data, Class<T> cla) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        return decode(byteBuffer, cla);
    }

    /**
     * 需要小端转大端
     * 二进制转Object
     * @param byteBuffer 二进制数据
     * @param <T>
     * @return
     */
    public static final <T> T decode(ByteBuffer byteBuffer, Class<T> cla) throws Exception {
        Map<Integer, FieldIndex> metaInfo = metaInfo(cla);
        if(null != metaInfo && metaInfo.size() > 0) {
            T t = cla.newInstance();
            for(int key:metaInfo.keySet()) {
                Field field = metaInfo.get(key).getField();
                Index index = metaInfo.get(key).getIndex();
                field.setAccessible(true);

                if(index.dependentType() == Index.DependentType.COMMON) {//普通依赖
                    byte2Obj(byteBuffer, t, field, index);
                } else if(index.dependentType() == Index.DependentType.LENGTH) {//长度依赖【通常是Object数组，例如Input数组】
                    //获取依赖的value值
                    int depIndex = index.dependentIndex();
                    Field depField = metaInfo.get(depIndex).getField();
                    depField.setAccessible(true);
                    int depVal = (int)depField.get(t);//因为前面已经处理过了,并且java中数据的下标只能是int类型

                    Class depClass = field.getType().getComponentType();
                    if (field.getType().isArray()) {
                        doArray(field, t, byteBuffer, depClass, depVal);
                    } else if(field.getType().getTypeName().equalsIgnoreCase("java.lang.String")) {//处理string
                        char[] subObjs = (char[])Array.newInstance(char.class, depVal);
                        for (int i = 0; i < depVal; i++) {
                            subObjs[i] = (char)byteBuffer.get();
                        }

                        if(index.hex())
                            field.set(t, ByteUtil.charsToHexString(subObjs));
                        else
                            field.set(t, new String(subObjs));
                    }
                } else if (index.dependentType() == Index.DependentType.EXIST) {//存在依赖
                    //获取依赖的value值
                    int depIndex = index.dependentIndex();
                    Field depField = metaInfo.get(depIndex).getField();
                    depField.setAccessible(true);
                    long depObjVal = (long) depField.get(t);//目前只支持int类型的

                    if(depObjVal >= index.dependentVal()) {//目前只支持大于等于，用于verion判断
                        byte2Obj(byteBuffer, t, field, index);
                    } else {
                        //其他情况暂时不支持
                    }
                } else {
                    //异常情况
                }
            }

            return t;
        }
        return null;
    }

    /**
     * 将byte[]数组转成Object
     * @param byteBuffer
     * @param obj
     * @param <T>
     */
    private static final <T> void byte2Obj(final ByteBuffer byteBuffer, final T obj, Field field, Index index) throws Exception {
        Class fieldClass = field.getType();

        if(Index.SpecialType.VARINT.equals(index.specialType()) && fieldClass.getTypeName().equalsIgnoreCase("int")) {//处理VarInt
            field.setInt(obj, VarInt.byte2Int(byteBuffer, index.inverted()));
        } else if(Index.SpecialType.UINT8.equals(index.specialType()) && fieldClass.getTypeName().equalsIgnoreCase("short")) {//处理UInt8
            field.setShort(obj, UInt8.toUnsignedInt(byteBuffer, index.inverted()));
        } else if(Index.SpecialType.UINT16.equals(index.specialType()) && fieldClass.getTypeName().equalsIgnoreCase("int")) {//处理UInt16
            field.setInt(obj, UInt16.toUnsignedInt(byteBuffer, index.inverted()));
        } else if(Index.SpecialType.UINT32.equals(index.specialType()) && fieldClass.getTypeName().equalsIgnoreCase("long")) {//处理UInt32
            field.setLong(obj, UInt32.toUnsignedInt(byteBuffer, index.inverted()));
        } else if(Index.SpecialType.UINT64.equals(index.specialType()) && fieldClass.getTypeName().equalsIgnoreCase("java.math.BigInteger")) {//处理UInt64
            field.set(obj, UInt64.toUnsignedLong(byteBuffer, index.inverted()));
        } else if(Index.SpecialType.NOTSPECIAL.equals(index.specialType())) {//普通类型
            if (fieldClass.getTypeName().equalsIgnoreCase("byte")) {//处理byte类型
                field.setByte(obj, byteBuffer.get());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("char")) {//处理char类型
                field.setChar(obj, (char)byteBuffer.get());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("short")) {//处理short类型
                field.setShort(obj, byteBuffer.getShort());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("int")) {//处理int
                field.setInt(obj, byteBuffer.getInt());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("long")) {//处理long
                field.setLong(obj, byteBuffer.getLong());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("float")) {//处理float
                field.setFloat(obj, byteBuffer.getFloat());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("double")) {//处理double
                field.setDouble(obj, byteBuffer.getDouble());
            } else if (fieldClass.getTypeName().equalsIgnoreCase("java.lang.String")) {//处理String
                boolean hex = index.hex();
                int size = index.size();

                char[] subObjs = (char[]) Array.newInstance(char.class, size);
                for (int i = 0; i < size; i++) {
                    subObjs[i] = (char) byteBuffer.get();
                }
                if(hex) {
                    field.set(obj, ByteUtil.charsToHexString(subObjs));
                } else {
                    field.set(obj, new String(subObjs));
                }
            }else {//处理数组【基础类型的数组和Object的数组不一样】
                if (fieldClass.isArray()) {
                    int size = index.size();
                    Class subClass = fieldClass.getComponentType();//获取到元素类型
                    doArray(field, obj, byteBuffer, subClass, size);
                } else {//Object类型
                    field.set(obj, decode(byteBuffer, fieldClass));
                }
            }
        }
    }

    /**
     * 处理数组情况
     * @param field
     * @param obj
     * @param byteBuffer
     * @param subClass
     * @param size
     * @param <T>
     * @throws Exception
     */
    private static <T> void doArray(Field field, final T obj, ByteBuffer byteBuffer, Class subClass, int size) throws Exception {
        if(baseClass(subClass)) {//基本类型数组【暂时支持byte和char】
            if(subClass.getTypeName().equalsIgnoreCase("byte")){//byte
                byte[] subObjs = (byte[])Array.newInstance(subClass, size);
                for (int i = 0; i < size; i++) {
                    subObjs[i] = byteBuffer.get();
                }
                field.set(obj, subObjs);
            } else if(subClass.getTypeName().equalsIgnoreCase("char")){//char
                char[] subObjs = (char[])Array.newInstance(subClass, size);
                for (int i = 0; i < size; i++) {
                    subObjs[i] = (char)byteBuffer.get();
                }
                field.set(obj, subObjs);
            }
        } else {//普通Object数组
            Object[] subObjs = (Object[])Array.newInstance(subClass, size);
            for (int i = 0; i < size; i++) {
                subObjs[i] = decode(byteBuffer, subClass);
            }
            field.set(obj, subObjs);
        }
    }

    @Getter
    private static class FieldIndex {
        FieldIndex(Field field, Index index) {
            this.field = field;
            this.index = index;
        }

        private Field field;
        private Index index;
    }
}
