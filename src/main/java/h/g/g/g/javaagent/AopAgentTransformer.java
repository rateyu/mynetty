package h.g.g.g.javaagent;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AopAgentTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("Transforming " + className);
        if ("Student".equals(className)) {
            try {
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(new LoaderClassPath(loader));
                CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classfileBuffer), false);
                //定义一个String类型的sex属性
                CtField param = new CtField(classPool.get("java.lang.String"), "sex", clazz);
                //设置属性为private
//            param.setModifiers(Modifier.PRIVATE);
                //将属性加到类中，并设置属性的默认值为male
                clazz.addField(param, CtField.Initializer.constant("male"));
                //为刚才的sex属性添加GET SET 方法
//            clazz.addMethod(CtNewMethod.setter("setSex", param));
//            clazz.addMethod(CtNewMethod.getter("getSex", param));
                //重写toString方法，将sex属性加入返回结果中。
                CtMethod method = clazz.getDeclaredMethod("toString");
                method.setBody("return \"Student{\" +\n" +
                        "                \"name='\" + name + '\\',' +\n" +
                        "                \"sex='\" + sex + '\\'' +\n" +
                        "                '}';");
                return clazz.toBytecode();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        /*
         TODO
         修改字节码，并返回修改后的字节码
        */
        byte[] transformed = classfileBuffer;
        return transformed;
    }
}
