package enumTool;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Set;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
public class EnumCodeValueProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(EnumCodeValue.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            Set<? extends Element> elems = roundEnv.getElementsAnnotatedWith(EnumCodeValue.class);
            for (Element elem : elems) {
                EnumCodeValue at = elem.getAnnotation(EnumCodeValue.class);
                CodeWithValue[] value = at.value();
                TypeElement te = (TypeElement) elem;
                String qualifiedName = te.getQualifiedName().toString();
                String simpleName = te.getSimpleName().toString();
                JavaFileObject builderFile = null;
                String clazz = simpleName.substring(1);
                String fullName;
                String pkg = "";
                if (!qualifiedName.equals(simpleName)) {
                    pkg = qualifiedName.substring(0, qualifiedName.lastIndexOf(simpleName) - 1);
                    fullName=pkg+"."+clazz;
                }else{
                    fullName=clazz;
                }
                try {
                    builderFile = processingEnv.getFiler()
                            .createSourceFile(fullName);
                    try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                        out.write(buildOutput(pkg, clazz, value));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return true;
        }
        return false;
    }

    public String buildOutput(String pkg, String clazz, CodeWithValue[] vals) {
        StringBuilder sb = new StringBuilder();
        if (pkg.trim().length() > 0) {
            sb.append("package ").append(pkg).append(";\n");
        }
        sb.append("public enum ").append(clazz).append("{\n");
        for (CodeWithValue c : vals) {
            sb.append("\t").append(c.name()).append("(").append(c.code()).append(",\"").append(c.value()).append("\"),\n");
        }
        sb.append("\t;\n");
        sb.append("\tprivate Integer code;\n");
        sb.append("\tprivate String value;\n");
        sb.append("\t").append(clazz).append("(Integer code,String value){\n");
        sb.append("\t\tthis.code=code;\n");
        sb.append("\t\tthis.value=value;\n");
        sb.append("\t}");
        sb.append("\tpublic String getValue() {\n" +
                "\t\treturn value;\n" +
                "\t}\n");
        sb.append("\tpublic Integer getCode() {\n" +
                "\t\treturn code;\n" +
                "\t}\n");
        sb.append("\tpublic static " + clazz + " getEnumFromCode(Integer code) {\n" +
                "\t\tfor (" + clazz + " e : " + clazz + ".values()) {\n" +
                "\t\t\tif (e.getCode().equals(code)) {\n" +
                "\t\t\t\treturn e;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\treturn null;\n" +
                "\t}");
        sb.append("}");
        return sb.toString();
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
