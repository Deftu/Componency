package com.test;

//#if FABRIC==1
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;
import xyz.deftu.multi.MultiClient;
import xyz.deftu.multi.MultiScreen;
//#else
//#if MC>=11500
//$$ import net.minecraftforge.common.MinecraftForge;
//$$ import net.minecraftforge.fml.common.Mod;
//#else
//$$ import net.minecraftforge.common.MinecraftForge;
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import net.minecraftforge.fml.common.Mod.EventHandler;
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//$$ import net.minecraftforge.client.ClientCommandHandler;
//$$ import net.minecraft.command.CommandBase;
//$$ import net.minecraft.command.ICommandSender;
//#endif
//#endif

import java.util.Timer;
import java.util.TimerTask;

//#if FABRIC==1
public class TestMod implements ClientModInitializer {
//#else
//#if MC>=11500
//$$ @Mod(value = "testmod")
//$$ public class TestMod {
//#else
//$$ @Mod(modid = "testmod", name = "Test Mod", version = "1.0")
//$$ public class TestMod {
//#endif
//#endif
    private boolean openScreen;

    //#if FABRIC==1
    public void onInitializeClient() {
        boolean openStart = System.clearProperty("componency.open") != null;

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("componency").executes(context -> {
                openScreen = true;
                return 0;
            }));
        }));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openStart) {
                openScreen = true;
            }

            tick();
        });
    }
    //#else
    //#if MC>=11500
    //$$ // TODO
    //#else
    //$$ @EventHandler
    //$$ public void init(FMLInitializationEvent event) {
    //$$     MinecraftForge.EVENT_BUS.register(this);
    //$$     ClientCommandHandler.instance.registerCommand(new CommandBase() {
    //$$         @Override
    //$$         public String getCommandName() {
    //$$             return "componency";
    //$$         }
    //$$
    //$$         @Override
    //$$         public int getRequiredPermissionLevel() {
    //$$             return -1;
    //$$         }
    //$$
    //$$         @Override
    //$$         public void processCommand(ICommandSender sender, String[] args) {
    //$$             openScreen();
    //$$         }
    //$$     });
    //$$ }
    //#endif
    //#endif

    private void tick() {
        if (openScreen) {
            openScreen = false;
            MultiClient.execute(() -> {
                MultiScreen.openScreen(new TestScreen());
            });
        }
    }

}
