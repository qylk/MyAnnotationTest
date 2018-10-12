package com.example.anotation.compiler;

import com.example.anotation.interfaces.Factory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {

    private TypeElement annotatedClassElement;
    private String qualifiedSuperClassName;
    private String simpleTypeName;
    private String id;

    public FactoryAnnotatedClass(TypeElement classElement) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;
        //获取id参数
        Factory annotation = classElement.getAnnotation(Factory.class);
        id = annotation.id();

        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException(
                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
                            Factory.class.getSimpleName(), classElement.getQualifiedName().toString()));
        }

        //获取type参数
        try {
            Class<?> clazz = annotation.type();
            qualifiedSuperClassName = clazz.getCanonicalName();
            simpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            /**
             annotation.type()返回Class对象，可能发生MirroredTypeException异常。
             那什么时候情况下会发生呢？
             答：因为注解处理是在编译Java源代码之前。我们需要考虑如下两种情况：
             1、这个类已经被编译，例如第三方.jar中的类，已经包含已编译的被@Factory注解.class文件，这时不会发生异常。
             2、这个类还未被编译，直接获取Class会抛出MirroredTypeException异常，单幸运的是，
             MirroredTypeException包含一个TypeMirror，它表示我们未编译类。
             因为我们已经知道它必定是一个类类型（我们已经在前面检查过），我们可以直接强制转换为DeclaredType，
             然后读取TypeElement来获取合法的名字
             */
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }

    /**
     * 获取在{@link Factory#id()}中指定的id
     * return the id
     */
    public String getId() {
        return id;
    }

    /**
     * 获取在{@link Factory#type()}指定的类型合法全名
     *
     * @return qualified name
     */
    public String getQualifiedFactoryGroupName() {
        return qualifiedSuperClassName;
    }


    /**
     * 获取在 {@link Factory#type()} 中指定的类型的简单名字
     *
     * @return qualified name
     */
    public String getSimpleFactoryGroupName() {
        return simpleTypeName;
    }

    /**
     * 获取被@Factory注解的原始元素
     */
    public TypeElement getTypeElement() {
        return annotatedClassElement;
    }
}