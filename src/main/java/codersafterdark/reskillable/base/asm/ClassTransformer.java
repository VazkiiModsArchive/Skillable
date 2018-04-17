package codersafterdark.reskillable.base.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class ClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.client.renderer.InventoryEffectRenderer")) {
            ClassReader reader = new ClassReader(basicClass);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);

            String funcName = "drawActivePotionEffects";
            String obfName = "f";
            String desc = "()V";

            for (MethodNode method : node.methods) {
                if ((method.name.equals(funcName) || method.name.equals(obfName)) && method.desc.equals(desc)) {
                    Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                    while (iterator.hasNext()) {
                        AbstractInsnNode itrNode = iterator.next();
                        if (itrNode.getOpcode() == Opcodes.BIPUSH) {
                            IntInsnNode intNode = (IntInsnNode) itrNode;
                            if (intNode.operand == 124) {
                                MethodInsnNode newNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "codersafterdark/reskillable/client/gui/handler/InventoryTabHandler", "getPotionOffset", "()I");
                                method.instructions.insert(intNode, newNode);
                                method.instructions.remove(intNode);
                                break;
                            }
                        }
                    }

                    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    node.accept(writer);
                    return writer.toByteArray();
                }
            }
        }

        return basicClass;
    }

}
