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
    'hud_background_check': {
      'target': {
        'type': 'METHOD',
        'class': 'vazkii.botania.client.core.helper.RenderHelper',
        'methodName': 'renderHUDBox',
        'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;IIII)V'
      },
      'transformer': function(method: MethodNode) {
        const label = new LabelNode();
        const target = new InsnList();
        target.add(ASMAPI.buildMethodCall(
          'mythicbotany/core/NoHudBackground',
          'shouldRenderHudBackground', '()Z',
          ASMAPI.MethodType.STATIC
        ));
        target.add(new JumpInsnNode(Opcodes.IFNE, label));
        target.add(new InsnNode(Opcodes.RETURN));
        target.add(label);
        method.instructions.insert(target);
        return method;
      }
    }
  }
}
