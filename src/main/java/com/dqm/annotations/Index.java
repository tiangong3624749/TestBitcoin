package com.dqm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dqm on 2018/8/25.
 *
 * 处理的类型只有：
 * 1、Java基本数据类型
 * 2、byte数组、char数组
 * 3、包含Index注解的Object类型
 * 4、特殊类型【VarInt、UInt32、UInt64】
 *
 * 分三种不同的场景：
 * 1、普通情况
 * 2、前面的字段表示后面字段长度的情况，使用dependent指定依赖的字段
 * 3、后面的字段是否存在，依赖前面字段的值满足一定的条件，例如version>1000
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {

    /**
     * 索引号，必须唯一
     * @return
     */
    int val();

    /**
     * 指定index的类型，用于判断VarInt、UInt32、UInt64
     * @return
     */
    SpecialType specialType() default SpecialType.NOTSPECIAL;

    /**
     * 长度是否依赖其他索引
     * -1表示不依赖，默认值
     * 否则根据依赖的索引计算出当前的字节数
     * 例如，数组依赖varint的字段作为长度值
     *
     * @return
     */
    int dependentIndex() default -1;

    DependentType dependentType() default DependentType.COMMON;

    /**
     * 依赖的值，例如version
     * 【目前暂时值支持long值>=的情况】
     * @return
     */
    long dependentVal() default -1;

    /**
     *
     * 用于数组指定长度的情况，例如char[32]或者byte[5]
     * 通常情况下不用
     * @return
     */
    int size() default 0;

    /**
     * 是否反转byte[]
     * 比如，version中的port就不需要反转
     */
    boolean inverted() default true;

    /**
     * 用于区分普通字符串和hex字符串
     * 比如hash需要用hex表示
     * 而有的字符串需要用真是字符串表示
     * @return
     */
    boolean hex() default true;

    enum  DependentType {
        COMMON,//普通情况，就是不依赖
        LENGTH,//前面字段指定后面字段长度的情况
        EXIST;//前面字段满足一定条件，后面字段才存在的情况
    }

    /**
     * 特殊类型
     */
     enum SpecialType {
        VARINT,//VarInt虽然为1-9字节，但是因为Java数组不支持long类型的下标，所以Java中只能是int类型
        UINT8,
        UINT16,
        UINT32,
        UINT64,
        NOTSPECIAL;
    }
}
