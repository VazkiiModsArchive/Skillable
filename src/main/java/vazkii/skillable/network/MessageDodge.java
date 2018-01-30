package vazkii.skillable.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;

public class MessageDodge extends NetworkMessage {

    @Override
    public IMessage handleMessage(MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().player;
        player.mcServer.addScheduledTask(() -> {
            player.addExhaustion(0.3F);
        });

        return null;
    }

}
