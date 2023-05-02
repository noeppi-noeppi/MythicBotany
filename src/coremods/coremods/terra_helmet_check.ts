import {
  ASMAPI,
  CoreMods,
  InsnList,
  InsnNode,
  JumpInsnNode,
  LabelNode,
  MethodNode,
  Opcodes,
  VarInsnNode
} from "coremods";

function initializeCoreMod(): CoreMods {
  return {
    'terra_helmet_check': {
      'target': {
        'type': 'METHOD',
        'class': 'vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem',
        'methodName': 'hasTerraArmorSet',
        'methodDesc': '(Lnet/minecraft/world/entity/player/Player;)Z'
      },
      'transformer': function(method: MethodNode) {
        const label = new LabelNode();
        const target = new InsnList();
        target.add(new VarInsnNode(Opcodes.ALOAD, 0));
        target.add(ASMAPI.buildMethodCall(
            'mythicbotany/core/TerraArmorChecker',
            'hasTerraHelmet', '(Lnet/minecraft/world/entity/player/Player;)Z',
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
