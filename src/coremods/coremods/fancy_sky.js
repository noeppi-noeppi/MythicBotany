"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var coremods_1 = require("coremods");
function initializeCoreMod() {
    return {
        'fancy_sky_check': {
            'target': {
                'type': 'METHOD',
                'class': 'vazkii.botania.mixin.client.LevelRendererMixin',
                'methodName': 'isGogSky',
                'methodDesc': '()Z'
            },
            'transformer': function (method) {
                var label = new coremods_1.LabelNode();
                var target = new coremods_1.InsnList();
                target.add(coremods_1.ASMAPI.buildMethodCall('mythicbotany/core/FancySkyChecker', 'isFancySky', '()Z', coremods_1.ASMAPI.MethodType.STATIC));
                target.add(new coremods_1.JumpInsnNode(coremods_1.Opcodes.IFEQ, label));
                target.add(new coremods_1.InsnNode(coremods_1.Opcodes.ICONST_1));
                target.add(new coremods_1.InsnNode(coremods_1.Opcodes.IRETURN));
                target.add(label);
                method.instructions.insert(target);
                return method;
            }
        }
    };
}
