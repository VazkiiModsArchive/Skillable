package codersafterdark.reskillable.base.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class LoadingPlugin implements IFMLLoadingPlugin {

    public static boolean runtimeDeobfEnabled;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"codersafterdark.reskillable.base.asm.ClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        runtimeDeobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}