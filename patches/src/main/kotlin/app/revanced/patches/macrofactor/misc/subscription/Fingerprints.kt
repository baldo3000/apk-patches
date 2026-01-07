package app.revanced.patches.macrofactor.misc.subscription

import app.revanced.patcher.fingerprint

internal val spoofAndroidCertFingerprint = fingerprint {
    custom { methodDef, classDef ->
        methodDef.name == "getPackageCertificateHashBytes" && classDef.endsWith("Lcom/google/android/gms/common/util/AndroidUtilsLight;")
    }
}

internal val customerInfoFactoryBuildCustomerInfoFingerprint = fingerprint {
    strings("subscriber")
    custom { method, classDef ->
        classDef.endsWith("/CustomerInfoFactory;") && method.name == "buildCustomerInfo"
    }
}

internal val firebaseInstallerFingerprint = fingerprint {
    custom { method, classDef ->
        classDef.endsWith("com/google/firebase/crashlytics/internal/common/InstallerPackageNameProvider;")
                && method.name == "getInstallerPackageName"
    }
}

internal val firebaseIdManagerFingerprint = fingerprint {
    custom { method, classDef ->
        classDef.endsWith("com/google/firebase/crashlytics/internal/common/IdManager;")
                && method.name == "getInstallerPackageName"
    }
}

internal val flutterPackageInfoFingerprint = fingerprint {
    custom { method, classDef ->
        classDef.endsWith("dev/fluttercommunity/plus/packageinfo/PackageInfoPlugin;")
                && method.name == "getInstallerPackageName"
    }
}
