package com.example.anotation.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class FactoryGroupedClasses {

    private static final String SUFFIX = "Factory";
    private String qualifiedClassName;

    private Map<String, FactoryAnnotatedClass> itemsMap = new LinkedHashMap<String, FactoryAnnotatedClass>();

    public FactoryGroupedClasses(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    public void add(FactoryAnnotatedClass toInsert) throws IdAlreadyUsedException {
        FactoryAnnotatedClass existing = itemsMap.get(toInsert.getId());
        if (existing != null) {
            throw new IdAlreadyUsedException(existing);
        }
        itemsMap.put(toInsert.getId(), toInsert);
    }

    public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(qualifiedClassName);
        String factoryClassName = superClassName.getSimpleName() + SUFFIX;

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(String.class, "id").build())
                .returns(TypeName.get(superClassName.asType()));

        CodeBlock.Builder toStringCodeBuilder = CodeBlock.builder();
        toStringCodeBuilder.beginControlFlow("if (id == null)");
        toStringCodeBuilder.add(CodeBlock.of("throw new IllegalArgumentException(\"id is null!\");\n"));
        toStringCodeBuilder.endControlFlow();

        methodSpecBuilder.addCode(toStringCodeBuilder.build());

        for (FactoryAnnotatedClass item : itemsMap.values()) {
            toStringCodeBuilder = CodeBlock.builder();
            toStringCodeBuilder.beginControlFlow("if ($S.equals(id))", item.getId());
            toStringCodeBuilder.add(CodeBlock.of("return new $T();\n", item.getTypeElement()));
            toStringCodeBuilder.endControlFlow();
            methodSpecBuilder.addCode(toStringCodeBuilder.build());
        }
        methodSpecBuilder.addCode(CodeBlock.of("throw new IllegalArgumentException(\"Unknown id = \" + id);\n"));


        TypeSpec typeSpec = TypeSpec.classBuilder(factoryClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpecBuilder.build())
                .build();

        JavaFile.builder("com.example.annotation", typeSpec)
                .build().writeTo(filer);
    }
}