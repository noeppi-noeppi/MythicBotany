import {
  ASMAPI,
  CoreMods,
  InsnList,
  InsnNode,
  JumpInsnNode,
  LabelNode,
  MethodNode,
  Opcodes
} from "coremods";

function initializeCoreMod(): CoreMods {
  return {
    'fancy_sky_check': {
      'target': {
        'type': 'METHOD',
        'class': 'vazkii.botania.mixin.client.MixinLevelRenderer',
        'methodName': 'isGogSky',
        'methodDesc': '()Z'
      },
      'transformer': function(method: MethodNode) {
        const label = new LabelNode();
        const target = new InsnList();
        target.add(ASMAPI.buildMethodCall(
          'mythicbotany/core/FancySkyChecker',
          'isFancySky', '()Z',
          ASMAPI.MethodType.STATIC
        ));
        target.add(new JumpInsnNode(Opcodes.IFEQ, label));
        target.add(new InsnNode(Opcodes.ICONST_1));
        target.add(new InsnNode(Opcodes.IRETURN));
        target.add(label);
        method.instructions.insert(target);
        return method;
      }
    }
  }
}
