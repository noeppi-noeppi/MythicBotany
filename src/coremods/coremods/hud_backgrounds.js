"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var coremods_1 = require("coremods");
function initializeCoreMod() {
    return {
        'hud_background_check': {
            'target': {
                'type': 'METHOD',
                'class': 'vazkii.botania.client.core.helper.RenderHelper',
                'methodName': 'renderHUDBox',
                'methodDesc': '(Lnet/minecraft/client/gui/GuiGraphics;IIII)V'
            },
            'transformer': function (method) {
                var label = new coremods_1.LabelNode();
                var target = new coremods_1.InsnList();
                target.add(coremods_1.ASMAPI.buildMethodCall('mythicbotany/core/NoHudBackground', 'shouldRenderHudBackground', '()Z', coremods_1.ASMAPI.MethodType.STATIC));
                target.add(new coremods_1.JumpInsnNode(coremods_1.Opcodes.IFNE, label));
                target.add(new coremods_1.InsnNode(coremods_1.Opcodes.RETURN));
                target.add(label);
                method.instructions.insert(target);
                return method;
            }
        }
    };
}
