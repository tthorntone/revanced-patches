package app.revanced.patches.slack.intune.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.util.MethodUtil

object IntuneRemovalTeamPrefsFingerprint : MethodFingerprint(
    "V",
    customFingerprint = { methodDef ->
        methodDef.definingClass == "Lslack/model/prefs/TeamPrefs;" && MethodUtil.isConstructor(methodDef) && methodDef.parameterTypes.size > 0 && !methodDef.parameterTypes.contains("Lkotlin/jvm/internal/DefaultConstructorMarker;")
    }
)